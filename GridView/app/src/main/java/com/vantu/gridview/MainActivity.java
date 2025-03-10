package com.vantu.gridview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vantu.gridview.adapter.MonHocAdapter;
import com.vantu.gridview.model.MonHoc;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<MonHoc> arrayList;
    private Button button_them;
    private Button button_sua;
    private EditText editText_language;
    private int position = -1;
    private MonHocAdapter monHocAdapter;

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

        //ánh xạ
        AnhXa();

        //Tạo ArrayAdapter
        monHocAdapter = new MonHocAdapter(MainActivity.this, R.layout.cell_mon_hoc, arrayList);

        // Gán adapter cho GridView
        gridView.setAdapter(monHocAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            //i: trả về vị trí click chuột trên GridView -> i ban đầu =0
            Toast.makeText(MainActivity.this, "Language: " + arrayList.get(position), Toast.LENGTH_SHORT).show();

            editText_language.setText(arrayList.get(position).getName());
            this.position = position;
        });

        button_them.setOnClickListener(v -> {
            String language = String.valueOf(editText_language.getText());

            MonHoc monHoc = new MonHoc(language, language + "1", R.drawable.ic_launcher_foreground);

            arrayList.add(monHoc);
            monHocAdapter.notifyDataSetChanged();
        });

        button_sua.setOnClickListener(v -> {
            arrayList.get(position).setName(editText_language.getText().toString());

            monHocAdapter.notifyDataSetChanged();
        });
    }

    private void AnhXa() {
        gridView = (GridView) findViewById(R.id.gridview_language);
        editText_language = (EditText) findViewById(R.id.editText_language);
        button_them = (Button) findViewById(R.id.button_them);
        button_sua = (Button) findViewById(R.id.button_sua);

        //Thêm dữ liệu vào List
        arrayList = new ArrayList<>();
        arrayList.add((new MonHoc("Java","Java 1", R.drawable.ic_launcher_foreground)));
        arrayList.add((new MonHoc("C#","C# 1", R.drawable.ic_launcher_foreground)));
        arrayList.add((new MonHoc("PHP","PHP 1", R.drawable.ic_launcher_foreground)));
        arrayList.add((new MonHoc("Kotlin","Kotlin 1", R.drawable.ic_launcher_foreground)));
        arrayList.add((new MonHoc("Dart","Dart 1",R.drawable.ic_launcher_foreground)));
    }
}