package com.example.todo.model;

public class Task {
    private String task;
    private boolean isDone;
    private Priority priority;

    public Task(String task, boolean isDone, Priority priority) {
        this.task = task;
        this.isDone = isDone;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}

