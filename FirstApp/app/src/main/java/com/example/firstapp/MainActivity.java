package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button showNameButton;
    private EditText inputYourName;
    private TextView yourName;
    private final String flag_homeScreen = "homeScreen";

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

        showNameButton = findViewById(R.id.button);
        inputYourName = findViewById(R.id.ptt_inputName);
        yourName = findViewById(R.id.textView);

        showNameButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String name = inputYourName.getText().toString().trim();
                if (name.isEmpty()) {
                    yourName.setText(R.string.hint_plainText);
                } else {
                    yourName.setText("Hi, " + name);
                    switchScreen(name);
                }
            }
        });
    }

    // định nghĩa 1 ActivityResultLauncher và logic xử lý kết quả trả về từ activity kia bên trong
/*    có 2 cách xử lý kết quả:
            1: đặt flag trong intent,
            2: định nghĩa ActivityResultLauncher cho mỗi activity riêng biệt*/
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String flag = result.getData().getStringExtra("flag");
                    // Kiểm tra flag
                    if (flag_homeScreen.equals(flag)) {
                        String message = result.getData().getStringExtra("message");
                        if (message != null){
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    public void switchScreen(String name) {
        Intent intent = new Intent(MainActivity.this, HomeScreen.class);

        // set flag
        intent.putExtra("flag", flag_homeScreen);

        // set data
        intent.putExtra("name", name);
        intent.putExtra("age", 20);
        intent.putExtra("address", "484 Le Van Viet");
        intent.putExtra("phoneNum", "0385068953");

        // Khởi động Activity và nhận kết quả trả về
        activityResultLauncher.launch(intent);
    }
}
