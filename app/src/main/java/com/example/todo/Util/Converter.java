package com.example.todo.Util;

import android.content.Context;

import com.example.todo.model.Priority;

public class Converter {
    public static Priority toPriority(String s){
        Priority priority;
        switch (s){
            case "High":
                priority =  Priority.HIGH;
                break;
            case "Medium":
                priority = Priority.MEDIUM;
                break;
            case "Low":
                priority = Priority.LOW;
                break;
            default:
                priority = Priority.MEDIUM;
        }
        return priority;
    }
}
