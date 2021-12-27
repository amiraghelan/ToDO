package com.example.todo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.Util.Converter;
import com.example.todo.Util.TaskComparator;
import com.example.todo.model.Priority;
import com.example.todo.model.Task;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    List<Task> tasks;
    Context context;
    int rbIds[] =  {R.id.rb_low,R.id.rb_medium, R.id.rb_high};
    TaskRowClicksListener taskRowClicksListener;

    public void sortList(){
        Collections.sort(tasks, new TaskComparator());
        notifyDataSetChanged();
    }

    public TaskRecyclerViewAdapter(List<Task> tasks, Context context, TaskRowClicksListener taskRowClicksListener) {
        this.tasks = tasks;
        Collections.sort(tasks, new TaskComparator());
        this.context = context;
        this.taskRowClicksListener = taskRowClicksListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.task_row,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.todo.setText(task.getTask());
        holder.isDone.setChecked(task.isDone());
        holder.priority.setText(task.getPriority().name());

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;
        public  TextView priority;
        public ImageView edit;
        public CheckBox isDone;
        public  ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todo = itemView.findViewById(R.id.txt_todo);
            priority = itemView.findViewById(R.id.txt_priority);
            edit = itemView.findViewById(R.id.img_edit);
            isDone = itemView.findViewById(R.id.cb_isDone);
            delete = itemView.findViewById(R.id.img_delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskRowClicksListener.onEdit(v, getAdapterPosition());
                    sortList();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskRowClicksListener.onDelete(v, getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            isDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskRowClicksListener.onChecked(v, getAdapterPosition(), isDone.isChecked());
                    notifyItemChanged(getAdapterPosition());
                    sortList();
                }
            });


        }
    }
}
