package com.vantu.roomdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vantu.roomdatabase.adapter.RecyclerViewAdapter;
import com.vantu.roomdatabase.model.Contact;
import com.vantu.roomdatabase.model.ContactViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    public static final String CONTACT_ID = "contact_id";
    private ContactViewModel contactViewModel;
    public static final String FLAG_NEWCONTACT = "NewContact";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;

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

        // Sử dụng ViewModelProvider để đảm bảo tạo 1 lần và sử dụng suốt life-cycle của 1 fragment/activity, đảm bảo ViewModel được tái sử dụng và không bị mất trạng thái khi cấu hình thay đổi, đồng thời giúp tránh rò rỉ bộ nhớ và dễ dàng chia sẻ dữ liệu giữa các thành phần trong ứng dụng.
        // AndroidViewModelFactory sẽ sử dụng reflection để tìm constructor phù hợp cho ContactViewModel. Vì ContactViewModel có một constructor nhận Application, nên factory sẽ tự động truyền NewContact.this.getApplication() vào constructor đó để tạo instance của ContactViewModel
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(ContactViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        // set up recyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // getAllContacts đảm bảo dữ liệu trả về sẽ đc giám sát, cật nhật, nghĩa là dữ liệu có thay đổi thì code bên dưới sẽ thực hiện lại
        // getAllContacts trả về liveData dạng async nên nếu đưa setAdapter ra ngoài khiến việc gán dữ liệu cho recyclerView xảy ra sớm trc khi liveData có dữ liệu => rỗng
        contactViewModel.getAllContacts().observe(this, contacts -> {

            contactList = contacts;

            // set up adapter
            // tham số thứ 3 là this, this ở đây cx là 1 instance của OnContactListener nên theo tính đa hình (1 object có thể đc sử dụng như nhiều kiểu dữ liệu khác nhau) => this tự nhận diện là 1 OnContactListener
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, this);

            // contacts dưới dạng livedata nên biến đổi liên tục -> set lại recyclerViewAdapter
            recyclerView.setAdapter(recyclerViewAdapter);
        });

    }
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String SOURCE = result.getData().getStringExtra("SOURCE");
                    // Kiểm tra SOURCE
                    assert SOURCE != null;
                    if (SOURCE.equals(FLAG_NEWCONTACT)) {
                        String name = result.getData().getStringExtra("name");
                        String occupation = result.getData().getStringExtra("occupation");

                        assert name != null;
                        assert occupation != null;
                        Contact contact = new Contact(occupation, name);
                        ContactViewModel.insert(contact);
                    }
                }
            });

    public void nextActivity(View view) {
        Intent intent = new Intent(MainActivity.this, NewContact.class);
        activityResultLauncher.launch(intent);
    }

    // khi tạo 1 RecyclerViewAdapter đã truyền vào 1 OnContactListener (tham số thứ 3), trong RecyclerViewAdapter, mỗi lần render 1 ViewHolder là 1 lần gán event click cho 1 itemView đồng thời trong hàm xử lý onClick đó cũng truyền position cho OnContactClick, bản chất OnContactClick lại thuộc OnContactListener được truyền vào từ MainActivity nên khi override OnContactClick bên này thì hàm đã có sẵn position
    @Override
    public void OnContactClick(int position) {
        Log.d("Clicked", "OnContactClick: " + position);
        Contact contact = contactList.get(position);
        Log.d("contact", "OnContactClick: " + contact);

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }
}