package com.vantu.recyclerviewadvance;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vantu.recyclerviewadvance.adapter.CustomAdapter;
import com.vantu.recyclerviewadvance.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvMultipleViewType;
    private List<Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvMultipleViewType = (RecyclerView) findViewById(R.id.rv_multipe_view_type);

        mData = new ArrayList<>();
        mData.add(new User("Nguyen Van Nghia", "Quan 11"));
        mData.add(R.drawable.ic_launcher_foreground);
        mData.add("Text 0");
        mData.add("Text 1");
        mData.add(new User("Nguyen Hoang Minh", "Quan 3"));
        mData.add("Text 2");
        mData.add(R.drawable.ic_launcher_foreground);
        mData.add(R.drawable.ic_launcher_foreground);
        mData.add(new User("Pham Nguyen Tam Phu", "Quan 10"));
        mData.add("Text 3");
        mData.add("Text 4");
        mData.add(new User("Tran Van Phuc", "Quan 1"));
        mData.add(R.drawable.ic_launcher_foreground);

        CustomAdapter adapter = new CustomAdapter(this, mData);
        rvMultipleViewType.setAdapter(adapter);
        rvMultipleViewType.setLayoutManager(new LinearLayoutManager(this));

    }
}