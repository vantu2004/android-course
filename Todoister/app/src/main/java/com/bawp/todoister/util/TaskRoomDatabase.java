package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskRoomDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "todoapp_database";
    public static final int NUMBER_OF_THREADS = 4;
    // volatile đảm bảo khởi tạo và lưu INSTANCE thẳng vào main memory (RAM - lưu dữ liệu khi CT chạy) thay vì CPU cache (bộ nhớ tạm copy dữ liệu đc sử dụng thường xuyên từ RAM). Việc lưu như này đảm bảo 1 luồng thay đổi là các luồng khác đều thấy (nghĩa là luồng này tạo INSTANCE rồi thì luồng kia sẽ thấy), còn CPU cache thì ko chắc việc này vì CPU cache nhiều core <=> nhiều cache, mỗi luồng đọc chung cache hoặc khác cache => ko thống nhất tính mới nhất của dữ liệu.
    // static đảm bảo INSTANCE thuộc về class và giữ INSTANCE tồn tại suốt chương trình.
    private static volatile TaskRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TaskRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (TaskRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDatabase.class, DATABASE_NAME).addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    public static Callback sRoomDatabaseCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                // chỉ cần gọi đến phương thức abstract là Room tự trả về instance của TaskDao
                TaskDao taskDao = INSTANCE.taskDao();

                // clean all
                taskDao.deleteAllTasks();


            });
        }
    };

    // việc khai báo như này cho phép khi 1 class khác gọi đến class này và gọi vào phương thức này thì Room sẽ tự trả về 1 instance của TaskDao
    public abstract TaskDao taskDao();
}
