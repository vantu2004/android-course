package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private static TaskRepository taskRepository;
    private final LiveData<List<Task>> tasks;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        this.tasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.tasks;
    }

    public LiveData<Task> getTask(long id){
        return taskRepository.getTask(id);
    }

    public static void insertTask(Task task){
        taskRepository.insertTask(task);
    }

    public static void updateTask(Task task){
        taskRepository.updateTask(task);
    }

    public static  void deleteTask(Task task){
        taskRepository.deleteTask(task);
    }


}
