package com.vantu.trivia.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.vantu.trivia.R;
import com.vantu.trivia.databinding.ActivitySubmitedBinding;
import com.vantu.trivia.model.Question;
import com.vantu.trivia.model.QuestionDetailSubmited;
import com.vantu.trivia.model.QuestionDone;
import com.vantu.trivia.service.QuestionDoneArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubmitedActivity extends AppCompatActivity {

    private ActivitySubmitedBinding binding;
    private List<QuestionDetailSubmited> questionDetailSubmitedList = new ArrayList<>();
    QuestionDoneArrayAdapter questionDoneArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(SubmitedActivity.this, R.layout.activity_submited);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getDataFromMainActivity();

        questionDoneArrayAdapter = new QuestionDoneArrayAdapter (SubmitedActivity.this, R.layout.layout_question, questionDetailSubmitedList);
        binding.listViewQuestion.setAdapter(questionDoneArrayAdapter);

    }

    private void getDataFromMainActivity() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int highestScore = bundle.getInt("highestScore", 0);
            int currentScore = bundle.getInt("currentScore", 0);
            String currentDateAndTime = bundle.getString("currentDateAndTime", "");
            List<Question> questions = (List<Question>) bundle.getSerializable("questions");
            Map<Integer, QuestionDone> questionDone = (Map<Integer, QuestionDone>) bundle.getSerializable("questionDone");

            setScoreUI(highestScore, currentScore, currentDateAndTime);
            assert questions != null;
            setQuestionListUI(questions, questionDone);
        }
    }

    private void setQuestionListUI(List<Question> questions, Map<Integer, QuestionDone> questionDone) {
        // Duyệt qua từng câu hỏi
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            QuestionDone qd = questionDone.get(i);

            String userChoose = "x";
            if(qd != null){
                userChoose = String.valueOf(qd.isUserChoose());
            }
            QuestionDetailSubmited qds = new QuestionDetailSubmited(q.getAnswer(), q.isAnswerTrue(), userChoose);

            questionDetailSubmitedList.add(qds);

            // Log.d("question", "setQuestionListUI: " + questionDetailSubmitedList);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setScoreUI(int highestScore, int currentScore, String currentDateAndTime) {
        binding.textViewSubHighestScore.setText("Highest Score: " + String.valueOf(highestScore));
        binding.textViewSubCurrentScore.setText("Current Score: " + String.valueOf(currentScore));
        binding.textViewSubTime.setText("Time: " + currentDateAndTime);
    }

    public void backHomeScreen(View view) {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        // hủy activity và trả về resultCode, intent để bên hàm onActivityResult bắt và xử lý
        finish();
    }
}