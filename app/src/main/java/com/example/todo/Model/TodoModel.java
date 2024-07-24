package com.example.todo.Model;

public class TodoModel {
    private int id, status;
    private String content;
    private int priority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }
}
