package com.vantu.contactmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vantu.contactmanager.adapter.ContactAdapter;
import com.vantu.contactmanager.data.DatabaseHandler;
import com.vantu.contactmanager.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private List<Contact> contactList;
    private List<String> contactArrayList;
    private ListView listViewContact;
    private ContactAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contactList = new ArrayList<>();
        contactArrayList = new ArrayList<>();
        listViewContact = findViewById(R.id.listview_contact);

        createContactList();

        databaseHandler = new DatabaseHandler(MainActivity.this);
//        addContact();
//        updateContact();
//        deleteContact();

//        Log.d("getAllContacts", "onCreate: " + databaseHandler.getAllContacts());
//        Log.d("getContact", "onCreate: " + databaseHandler.getContact(2));
//        Log.d("rowCount", "onCreate: " + databaseHandler.getRowCount());

        getAllContacts();
    }

    private void getAllContacts(){
        contactList = databaseHandler.getAllContacts();

        for (Contact contact : contactList){
            contactArrayList.add(contact.getName());
        }
        Log.d("contactArrayList", "getAllContacts: " + contactArrayList);

        // create arrayAdapter
        // arrayAdapter là lớp chuyển các list/array/... data thành view để hiện thị lên listView/...
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactArrayList);

        arrayAdapter = new ContactAdapter(MainActivity.this, R.layout.contact_layout, contactList);
        // show data to listview
        listViewContact.setAdapter(arrayAdapter);

        // add eventlistener to listview
        listViewContact.setOnItemClickListener((adapterView, view, i, l) -> {
            // position <=> index array
            Toast.makeText(this, "position: " + i + ", id: " + l, Toast.LENGTH_SHORT).show();
        });

        listViewContact.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Toast.makeText(this, "position: " + i + ", id: " + l, Toast.LENGTH_SHORT).show();
            return true; // Phải trả về true để sự kiện long click hoạt động
        });

    }

    private void deleteContact() {
        int rowAffected = databaseHandler.deleteContact(contactList.get(0));
        Log.d("rowAffected", "updateContact: " + rowAffected);
        Log.d("getAllContacts", "onCreate: " + databaseHandler.getAllContacts());
    }

    private void updateContact() {
        Contact contact = new Contact(4, "Le Van Tu", "0972292447");
        int rowAffected = databaseHandler.updateContact(contact);
        Log.d("rowAffected", "updateContact: " + rowAffected);
        Log.d("getAllContacts", "onCreate: " + databaseHandler.getAllContacts());
    }

    private void addContact() {
        for (Contact contact : contactList){
            databaseHandler.addContact(contact);
        }
        Log.d("getAllContacts", "onCreate: " + databaseHandler.getAllContacts());
    }

    private void createContactList() {
        contactList.add(new Contact(1, "John Doe", "123-456-7890"));
        contactList.add(new Contact(2, "Jane Smith", "987-654-3210"));
        contactList.add(new Contact(3, "Alice Brown", "555-123-4567"));
        contactList.add(new Contact(4, "Bob White", "555-765-4321"));
        contactList.add(new Contact(5, "Charlie Green", "555-987-6543"));
    }
}