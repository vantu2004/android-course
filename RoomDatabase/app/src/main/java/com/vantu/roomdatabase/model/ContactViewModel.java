package com.vantu.roomdatabase.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vantu.roomdatabase.data.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private static ContactRepository contactRepository;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
    }

    public LiveData<List<Contact>> getAllContacts () {
        return contactRepository.getAllContacts();
    }

    public static void insert (Contact contact){
        contactRepository.insert(contact);
    }

    public LiveData<Contact> getContact (int id){
        return contactRepository.getContact(id);
    }

    public static void updateContact (Contact contact){
        contactRepository.updateContact(contact);
    }

    public static void deleteContact (Contact contact){
        contactRepository.deleteContact(contact);
    }
}
