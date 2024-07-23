package com.example.todo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.todo.Model.TodoModel;
import com.example.todo.Utils.DbHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    public EditText newTaskText;
    private Button newTaskSaveButton;
    private DbHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        newTaskText = getView().findViewById(R.id.newTaskEdit);

        newTaskSaveButton = getView().findViewById(R.id.newTaskBtn);
        newTaskSaveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
        newTaskSaveButton.setEnabled(false);

        db = new DbHandler(getActivity());
        db.openDb();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null){
            isUpdate = true;
            String content = bundle.getString("content");
            newTaskText.setText(content);

            if(content.length() > 0){
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    newTaskSaveButton.setEnabled(false);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();

                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }
                else{
                    TodoModel task = new TodoModel();
                    task.setStatus(0);
                    task.setContent(text);
                    db.createTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();

        if(activity instanceof DialogCloseListner){
            ((DialogCloseListner)activity).handleDialogClose(dialog);
        }
    }
}
