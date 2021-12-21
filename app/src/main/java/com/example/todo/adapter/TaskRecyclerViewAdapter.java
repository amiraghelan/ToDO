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

    public void sortList(){
        Collections.sort(tasks, new TaskComparator());
        notifyDataSetChanged();
    }

    public TaskRecyclerViewAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        Collections.sort(tasks, new TaskComparator());
        this.context = context;
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
                    Task task = tasks.get(getAdapterPosition());
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.todo_dialog);

                    EditText todoEditText = dialog.findViewById(R.id.edt_todo);
                    RadioGroup radioGroup = dialog.findViewById(R.id.rg_priority);
                    todoEditText.setText(task.getTask());
                    radioGroup.check(rbIds[task.getPriority().getNumericValue()]);
                    Button addButton = dialog.findViewById(R.id.btn_submit);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String todo = todoEditText.getText().toString();
                            int id = radioGroup.getCheckedRadioButtonId();
                            RadioButton priorityRb = dialog.findViewById(id);
                            Priority priority = Converter.toPriority(priorityRb.getText().toString());
                            if (!todo.isEmpty()) {
                                task.setTask(todo);
                                task.setPriority(priority);
                                notifyItemChanged(getAdapterPosition());
                                sortList();
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasks.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            isDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasks.get(getAdapterPosition()).setDone(isDone.isChecked());
                    notifyItemChanged(getAdapterPosition());
                    sortList();
                }
            });

//            isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    tasks.get(getAdapterPosition()).setDone(isDone.isChecked());
//                    notifyItemChanged(getAdapterPosition());
//                    sortList();
//                }
//            });


        }
    }
}
