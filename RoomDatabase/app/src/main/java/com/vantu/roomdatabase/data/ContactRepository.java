package com.vantu.roomdatabase.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vantu.roomdatabase.model.Contact;
import com.vantu.roomdatabase.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private final ContactDao contactDao;
    // private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
        // Room quản lý vòng đời của Database nên phải tạo contactDao thông qua room
        ContactRoomDatabase room = ContactRoomDatabase.getDatabase(application);
        contactDao = room.contactDao();
    }

    public LiveData<List<Contact>> getAllContacts(){
        return contactDao.getAllContacts();
    }

    // thay vì dùng contactDao đã khai báo bên trên thì dùng databaseWriteExecutor (đã tạo 4 luồng phụ bên ContactRoomDatabase) tránh luồng chính nhằm thao tác async
    public void insert (Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }

    public LiveData<Contact> getContact(int id){
        return contactDao.getContact(id);
    }

    public void updateContact(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.updateContact(contact));
    }

    public void deleteContact(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(()-> contactDao.deleteContact(contact));
    }
}
