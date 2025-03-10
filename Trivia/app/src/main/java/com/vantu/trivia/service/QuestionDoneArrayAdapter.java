package com.vantu.trivia.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vantu.trivia.R;
import com.vantu.trivia.model.QuestionDetailSubmited;

import java.util.List;

public class QuestionDoneArrayAdapter extends ArrayAdapter<QuestionDetailSubmited> {
    private Activity context;
    private int Idlayout;
    private List<QuestionDetailSubmited> questionDoneList;

    public QuestionDoneArrayAdapter(Activity context, int Idlayout, List<QuestionDetailSubmited>questionDoneList) {
        super(context, Idlayout, questionDoneList);
        this.context = context;
        this.Idlayout = Idlayout;
        this.questionDoneList = questionDoneList;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Tạo để chú Layout
        LayoutInflater myflater = context.getLayoutInflater();

        // Đặt Layout lên để tạo thành View
        convertView = myflater.inflate(Idlayout, null);

        // Lấy 1 phần tử trong mảng
        QuestionDetailSubmited q = questionDoneList.get(position);
        TextView question = convertView.findViewById(R.id.textView_subQuestion);
        TextView key = convertView.findViewById(R.id.textView_subKey);
        TextView userChoose = convertView.findViewById(R.id.textView_subUserChoose);

        question.setText(q.getQuestion());
        key.setText("Correct Answer: " + String.valueOf(q.isKey()));
        userChoose.setText("Your Answer: " + q.getUserChoose());

        if(String.valueOf(q.isKey()).equals(q.getUserChoose())){
            question.setTextColor(Color.parseColor("#388E3C"));
        }else{
            question.setTextColor(Color.RED);
        }

        return convertView;
    }
}
