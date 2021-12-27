package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.todo.Util.Converter;
import com.example.todo.Util.TaskComparator;
import com.example.todo.adapter.TaskRecyclerViewAdapter;
import com.example.todo.adapter.TaskRowClicksListener;
import com.example.todo.model.Priority;
import com.example.todo.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String SHAREDPREFERENCES = "ToDoApp";
    private static String TASKS = "TASKS";

    RecyclerView taskRecyclerView;
    FloatingActionButton fab;
    List<Task> tasks;
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        fab = findViewById(R.id.fab_addTask);

        // setting up recyclerView for tasks
        taskRecyclerView = findViewById(R.id.rcl_tasks);
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(tasks, MainActivity.this, new TaskRowClicksListener() {
            @Override
            public void onEdit(View v, int position) {
                Task task = tasks.get(position);
                showDialog(task.getTask(), task.isDone(), task.getPriority(), position);
            }

            @Override
            public void onDelete(View v, int position) {
                tasks.remove(position);
            }

            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                tasks.get(position).setDone(isChecked);
            }
        });
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        taskRecyclerView.setAdapter(taskRecyclerViewAdapter);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks.toArray());
        Log.d("tagg", "onPause: " + json);
        editor.putString(TASKS, json);
        editor.apply();
    }

    public void loadData() {
        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES, MODE_PRIVATE);
        String json = sp.getString(TASKS, "");
        Log.d("tagg", "loadData: " + json);
        if (!json.isEmpty()) {
            Type taskListType = new TypeToken<ArrayList<Task>>() {
            }.getType();
            tasks = gson.fromJson(json, taskListType);
        } else {
            tasks = new ArrayList<>();
        }

    }


    public void showDialog(){
        showDialog("",false,Priority.MEDIUM,-1);
    }
    public void showDialog(String mtodo, boolean misDone, Priority mpriority, int mposition) {

        int rbIds[] =  {R.id.rb_low,R.id.rb_medium, R.id.rb_high};

        Dialog dialog = new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.todo_dialog);

        EditText todoEditText = dialog.findViewById(R.id.edt_todo);
        RadioGroup radioGroup = dialog.findViewById(R.id.rg_priority);
        todoEditText.setText(mtodo);
        radioGroup.check(rbIds[mpriority.getNumericValue()]);
        Button addButton = dialog.findViewById(R.id.btn_submit);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todo = todoEditText.getText().toString();
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton priorityRb = dialog.findViewById(id);
                Priority priority = Converter.toPriority(priorityRb.getText().toString());
                if (!todo.isEmpty()) {
                    editTask(todo,false, priority, mposition);
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void editTask(String todo, boolean isdone, Priority priority, int position){
        Task task;
        if(position == -1){
            task = new Task(todo, isdone, priority);
            tasks.add(task);
        }else{
            task = tasks.get(position);
            task.setTask(todo);
            task.setDone(isdone);
            task.setPriority(priority);
        }
        Collections.sort(tasks, new TaskComparator());
        taskRecyclerViewAdapter.notifyDataSetChanged();
    }



}