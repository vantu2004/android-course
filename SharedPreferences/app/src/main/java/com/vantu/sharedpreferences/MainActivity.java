package com.vantu.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button buttonSave;
    private EditText editText_input;
    private TextView textView_output;

    // tên file SharedPreferences
    private static final String MESSAGE_ID = "messages_prefs";

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

        buttonSave = findViewById(R.id.button_save);
        editText_input = findViewById(R.id.editText_input);
        textView_output = findViewById(R.id.textView_output);

        getDataFromSharedPrefs();
    }

    public void saveInputText(View view) {
        String inputText = editText_input.getText().toString().trim();
        // Log.d("input", "saveInputText: " + inputText);

        // MESSAGE_ID dùng để định danh file SharedPreferences lưu trữ dữ liệu.
        // MODE_PRIVATE mặc định chỉ có app này mới có thể truy cập vào SharedPreferences.
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        // sharedPreferences.edit() trả về đối tượng cho phép chỉnh sửa
        // SharedPreferences.Editor là lớp cho phép thêm, sửa, xóa trong SharedPreferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String message = editText_input.getText().toString();
        editor.putString("message", message);

        // trả v true/false khi lưu kết quả
        boolean result = editor.commit();
        Log.d("result", "saveInputText: " + result);
        // ko trả về gì
        editor.apply();

        getDataFromSharedPrefs();
    }

    private void getDataFromSharedPrefs(){
        SharedPreferences shared = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);

        // tham số thứ 2 là default, nếu ko có giá trị thì trả default
        String message = shared.getString("message", "");

        textView_output.setText(message);
    }

    public void deleteData(View view) {
        SharedPreferences shared = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit(); // Mở Editor

        editor.remove("message"); // Xóa key "message"
        editor.apply(); // Hoặc editor.apply();
    }
}