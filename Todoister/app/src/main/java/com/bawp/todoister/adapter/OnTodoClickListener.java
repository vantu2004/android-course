package com.bawp.todoister.adapter;

import com.bawp.todoister.model.Task;

public interface OnTodoClickListener {
    public void onTodoClick(Task task);
    public void onTodoRadioButtonClick(Task task);
}
