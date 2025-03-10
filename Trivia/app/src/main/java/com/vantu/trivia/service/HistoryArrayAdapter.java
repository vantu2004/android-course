package com.vantu.trivia.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vantu.trivia.R;
import com.vantu.trivia.model.History;
import com.vantu.trivia.model.QuestionDetailSubmited;

import java.util.List;

public class HistoryArrayAdapter extends ArrayAdapter<History> {
    private Activity context;
    private int Idlayout;
    private List<History> historyList;

    public HistoryArrayAdapter(Activity context, int Idlayout, List<History>historyList) {
        super(context, Idlayout, historyList);
        this.context = context;
        this.Idlayout = Idlayout;
        this.historyList = historyList;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Tạo để chú Layout
        LayoutInflater myflater = context.getLayoutInflater();

        // Đặt Layout lên để tạo thành View
        convertView = myflater.inflate(Idlayout, null);

        // Lấy 1 phần tử trong mảng
        History h = historyList.get(position);
        TextView time = convertView.findViewById(R.id.textView_time);
        TextView score = convertView.findViewById(R.id.textView_score);

        time.setText("Time: " + h.getTime());
        score.setText("Score: " + String.valueOf(h.getScore()));

        return convertView;
    }
}
