package com.vantu.trivia.activity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.vantu.trivia.R;
import com.vantu.trivia.databinding.ActivityHistoryBinding;
import com.vantu.trivia.model.History;
import com.vantu.trivia.service.HistoryArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private HistoryArrayAdapter historyArrayAdapter;
    private List<History> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getDataFromMainActivity();

        historyArrayAdapter = new HistoryArrayAdapter (HistoryActivity.this, R.layout.layout_history, historyList);
        binding.listViewHistory.setAdapter(historyArrayAdapter);
    }

    private void getDataFromMainActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            historyList = (List<History>) bundle.getSerializable("historyList");
        }
    }

    public void backHomeScreen(View view) {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
}