package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.todo.Util.Converter;
import com.example.todo.Util.TaskComparator;
import com.example.todo.adapter.TaskRecyclerViewAdapter;
import com.example.todo.model.Priority;
import com.example.todo.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView taskRecyclerView;
    FloatingActionButton fab;
    List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab_addTask);



        tasks.add(new Task("medium1", false, Priority.MEDIUM));
        tasks.add(new Task("medium2", true, Priority.MEDIUM));
        tasks.add(new Task("low1", false, Priority.LOW));
        tasks.add(new Task("low2", false, Priority.LOW));
        tasks.add(new Task("hugh1", false, Priority.HIGH));
        tasks.add(new Task("high2", true, Priority.HIGH));


        // setting up recyclerView for tasks
        taskRecyclerView = findViewById(R.id.rcl_tasks);
        TaskRecyclerViewAdapter taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(tasks,MainActivity.this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        taskRecyclerView.setAdapter(taskRecyclerViewAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.todo_dialog);

                EditText todoEditText = dialog.findViewById(R.id.edt_todo);
                RadioGroup radioGroup = dialog.findViewById(R.id.rg_priority);
                radioGroup.check(R.id.rb_medium);
                Button addButton = dialog.findViewById(R.id.btn_submit);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String todo = todoEditText.getText().toString();
                        int id = radioGroup.getCheckedRadioButtonId();
                        RadioButton priorityRb = dialog.findViewById(id);
                        Priority priority = Converter.toPriority(priorityRb.getText().toString());
                        if(!todo.isEmpty()){
                            Task task = new Task(todo, false, priority);
                            tasks.add(task);
                            Collections.sort(tasks, new TaskComparator());
                            taskRecyclerViewAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

    }
}