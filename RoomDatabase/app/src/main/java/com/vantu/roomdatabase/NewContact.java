package com.vantu.roomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.vantu.roomdatabase.model.Contact;
import com.vantu.roomdatabase.model.ContactViewModel;

public class NewContact extends AppCompatActivity {

    private final String SOURCE = "NewContact";

    // bắt buộc khai báo
    private ContactViewModel contactViewModel;
    private EditText editText_name;
    private EditText editText_occupation;
    private Button button_save;
    private Button button_update;
    private Button button_delete;
    private int contactId = 0;
    private Boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // AndroidViewModelFactory sẽ sử dụng reflection để tìm constructor phù hợp cho ContactViewModel. Vì ContactViewModel có một constructor nhận Application, nên factory sẽ tự động truyền NewContact.this.getApplication() vào constructor đó để tạo instance của ContactViewModel
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this.getApplication()).create(ContactViewModel.class);
        // contactViewModel.getAllContacts();

        editText_name = findViewById(R.id.editText_name);
        editText_occupation = findViewById(R.id.editText_occupation);
        button_save = findViewById(R.id.button_save);
        button_update = findViewById(R.id.button_update);
        button_delete = findViewById(R.id.button_delete);

        loadData();
        checkState();
    }

    private void checkState() {
        if (isEdit){
            button_save.setVisibility(View.GONE);
        }
        else{
            button_update.setVisibility(View.GONE);
            button_delete.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        if (getIntent().hasExtra(MainActivity.CONTACT_ID)){
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.getContact(contactId).observe(this, contact -> {
                if(contact != null){
                    editText_name.setText(contact.getName());
                    editText_occupation.setText(contact.getOccupation());
                }
            });
            // nếu true thì ẩn button_save, nếu false thì ẩn button_update, button_delete
            isEdit = true;
        }
    }

    public void saveContact(View view) {
        Intent replyIntend = new Intent();

        // Kiểm tra xem cả hai trường có rỗng không
        if (TextUtils.isEmpty(String.valueOf(editText_name.getText())) || TextUtils.isEmpty(String.valueOf(editText_occupation.getText()))) {
            // Toast.makeText(this, "Value is invalid!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED, replyIntend);
        } else {
            // Nếu cả hai trường không rỗng, lưu dữ liệu
            String name = String.valueOf(editText_name.getText());
            String occupation = String.valueOf(editText_occupation.getText());

//            Contact contact = new Contact(name, occupation);
//            ContactViewModel.insert(contact);

            replyIntend.putExtra("SOURCE", SOURCE);

            replyIntend.putExtra("name", name);
            replyIntend.putExtra("occupation", occupation);

            setResult(RESULT_OK, replyIntend);
        }

        finish();
    }

    private void updateOrDeleteContact(Boolean isDelete){
        int id = contactId;
        String name = editText_name.getText().toString().trim();
        String occupation = editText_occupation.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
            Snackbar.make(editText_name, "Empty", Snackbar.LENGTH_LONG).show();
        }
        else {
            Contact contact = new Contact();
            contact.setId(id);
            contact.setName(name);
            contact.setOccupation(occupation);

            if (isDelete){
                ContactViewModel.deleteContact(contact);
            }else{
                ContactViewModel.updateContact(contact);
            }

            finish();
        }
    }

    public void updateContact(View view) {
        updateOrDeleteContact(false);
    }

    public void deleteContact(View view) {
        updateOrDeleteContact(true);
    }
}