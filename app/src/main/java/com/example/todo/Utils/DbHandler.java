package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE = "todoDatabase";
    private static final String TABLE = "todos";
    private static final String ID = "id";
    private static final String CONTENT = "content";
    private static final String STATUS = "status";
    private static final String TABLE_CREATE_SQL = "CREATE TABLE " + TABLE + "("
                                                    + ID + " INTEGER PRIMARY_KEY AUTOINCREMENT, "
                                                    + CONTENT + " TEXT, "
                                                    + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DbHandler(Context context){
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVresion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }

    public void openDb(){
        db = this.getWritableDatabase();
    }

    public void createTask(TodoModel task){
        ContentValues cv = new ContentValues();

        cv.put(CONTENT, task.getContent());
        cv.put(STATUS, 0);
        db.insert(TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<TodoModel> getAllTasks(){
        List<TodoModel> taskList = new ArrayList<TodoModel>();
        Cursor cursor = null;

        db.beginTransaction();

        try{
            cursor = db.query(TABLE, null, null, null, null, null, null);

            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        TodoModel task = new TodoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setContent(String.valueOf(cursor.getColumnIndex(CONTENT)));
                        task.setStatus(cursor.getColumnIndex(STATUS));
                        taskList.add(task);
                    }
                    while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();

            if(cursor != null){
                cursor.close();
            }

        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();

        cv.put(STATUS, 0);
        db.update(TABLE, cv, ID + "=?", new String[] { String.valueOf(id) });
    }

    public void updateTask(int id, String content){
        ContentValues cv = new ContentValues();

        cv.put(CONTENT, content);
        db.update(TABLE, cv, ID + "=?", new String[] { String.valueOf(id) });
    }

    public void deleteTask(int id){
        db.delete(TABLE, ID + "=?", new String[] { String.valueOf(id) });
    }

}
