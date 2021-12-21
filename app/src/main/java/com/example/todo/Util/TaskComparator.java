package com.example.todo.Util;

import com.example.todo.model.Task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if(o1.isDone() && !o2.isDone()){
            return 1;
        }else if(!o1.isDone() && o2.isDone()){
            return -1;
        }else{
            return o2.getPriority().getNumericValue() - o1.getPriority().getNumericValue();
        }
    }
}
