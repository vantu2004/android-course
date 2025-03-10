package com.bawp.todoister.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bawp.todoister.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insertTask(Task task);

    @Query("DELETE FROM task_table")
    public void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    public LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE task_id == :id")
    public LiveData<Task> getTask(long id);

    @Update
    public void updateTask(Task task);

    @Delete
    public void deleteTask(Task task);
}
