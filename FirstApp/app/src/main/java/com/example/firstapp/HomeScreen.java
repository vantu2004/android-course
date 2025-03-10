package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {

    private TextView textView_home;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // String name = getIntent().getStringExtra("name");

        textView_home = findViewById(R.id.textView_home);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String name = bundle.getString("name", "N/A");
            int age = bundle.getInt("age", 0);
            String address = bundle.getString("address", "N/A");
            String phoneNum = bundle.getString("phoneNum", "N/A");

            textView_home.setText(String.format("%s\n%d\n%s\n%s", name, age, address, phoneNum));
        }

    }

    public void backFirstScreen(View view) {
        Intent intent = getIntent();
        intent.putExtra("message", "From Home Screen");
        setResult(RESULT_OK, intent);
        // hủy activity và trả về resultCode, intent để bên hàm onActivityResult bắt và xử lý
        finish();
    }
}