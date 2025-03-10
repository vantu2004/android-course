package com.vantu.databinding;

import android.content.Context;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vantu.databinding.adapter.ListUserAdapter;
import com.vantu.databinding.databinding.ActivityMainBinding;
import com.vantu.databinding.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListUserAdapter.OnItemClickListener {

    private ActivityMainBinding binding;

    // việc dùng ObservableField để đảm bảo khi có thay đổi ữ liệu thì bên UI bắt được, nếu gán cứng như thường thì dữ liệu chỉ đc cập nhật 1 lần ngay lần tạo đầu tiên
    public ObservableField<String> title = new ObservableField<>();
    private ListUserAdapter listUserAdapter;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // khởi tạo dạng ObsercableField và set nội dung, sau đó set this (instance của Mainactivity) cho biến main
        title.set("vantu");
        binding.setMain(this);

        setData();

        listUserAdapter = new ListUserAdapter(users, this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(listUserAdapter);
    }
    private void setData() {
        users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFirstName("Van" + i);
            user.setLastName("Tu" + i);
            users.add(user);
        }
    }

    @Override
    public void itemClick(int position) {
        Toast.makeText(MainActivity.this, users.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}