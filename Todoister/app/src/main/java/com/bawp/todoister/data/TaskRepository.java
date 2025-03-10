package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> tasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        this.taskDao = taskRoomDatabase.taskDao();
        this.tasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.tasks;
    }

    public void insertTask(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> this.taskDao.insertTask(task));
    }

    public void updateTask(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> this.taskDao.updateTask(task));
    }

    public LiveData<Task> getTask(long id){
        return this.taskDao.getTask(id);
    }

    public void deleteTask(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(() -> this.taskDao.deleteTask(task));
    }
}
