package com.example.todo.adapter;

import android.view.View;

public interface TaskRowClicksListener {

    public void onEdit(View v, int position);
    public void onDelete(View v, int position);
    public void onChecked(View v ,int position, boolean isChecked);
}
