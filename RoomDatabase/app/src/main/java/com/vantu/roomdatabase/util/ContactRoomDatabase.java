package com.vantu.roomdatabase.util;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vantu.roomdatabase.data.ContactDao;
import com.vantu.roomdatabase.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// entities liệt kê tất cả entity cần quản lý bởi RoomDatabase
@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {
    // Room tự động generate instance của ContactDao
    public abstract ContactDao contactDao();
    // giới hạn số luồng ghi dữ liệu đồng thời là 4
    public static final int NUMBER_OF_THREADS = 4;

    // giữ một tham chiếu duy nhất (singleton) tới ContactRoomDatabase, volatile đảm bảo rằng các thay đổi đối với biến này sẽ được tất cả các luồng nhìn thấy ngay lập tức.
    private static volatile ContactRoomDatabase INSTANCE;

    /*
    pool/(thread pool) là tập các luồng/thread đc quản lý để phục vụ các tác vụ cụ thể
    thay vì tạo mỗi luồng cho mỗi tác vụ thì tạo ra 1 số pool chứa các luồng cố định/động (tùy cấu hình) trước để tái sử dụng nhiều lần
    trường hợp các luồng đều đang bận thì tác vụ phải xếp hàng đợi đến khi có luồng trống để đc thực hiện
    */

    // lệnh này đang tạo ra 1 pool 4 luồng
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // trả về 1 instance của ContactRoomdatabase
    public static ContactRoomDatabase getDatabase(final Context context){
        // nếu INSTANCE null thì khóa async chuyển thành sync để tạo, đảm bảo tại thời điểm khởi tạo chỉ đúng luồng này chạy
        if (INSTANCE == null){
            synchronized (ContactRoomDatabase.class){
                // kiểm tra lại lần nữa
                if (INSTANCE == null){
                    // Room tự động quét và tìm các DAO được khai báo trong lớp RoomDatabase (như ContactDao) => phương thức trừu tượng phía trên thực chất trả về 1 instance của ContactDao
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ContactRoomDatabase.class, "contact_database")
                            // thiết lập data ban đầu cho db
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // thiết lập ban đầu cho db
    public static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // dùng luồng đã tạo trc đó thay vì luồng chính để tránh chặn luồng (thao tác async)
            databaseWriteExecutor.execute(() -> {
                ContactDao contactDao = INSTANCE.contactDao();
                contactDao.deleteAll();

                Contact contact = new Contact ("student", "vantu");
                contactDao.insert(contact);

                contact = new Contact("teacher", "anhtu");
                contactDao.insert(contact);
            });
        }
    };
}
