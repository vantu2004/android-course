package com.vantu.truecitizen;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.vantu.truecitizen.databinding.ActivityMainBinding;
import com.vantu.truecitizen.model.Question;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestion;
//    private boolean firstQuestionWarningShown = false;
//    private boolean lastQuestionWarningShown = false;

    private final Question[] questionBank = new Question[]{
            new Question(R.string.question_amendments, false),
            new Question(R.string.question_constitution, true),
            new Question(R.string.question_declaration, true),
            new Question(R.string.question_independence_rights, true),
            new Question(R.string.question_religion, true),
            new Question(R.string.question_government, false),
            new Question(R.string.question_government_feds, false),
            new Question(R.string.question_government_senators, true),
            //and add more!
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // old
        // setContentView(R.layout.activity_main);
        // new
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // auto get string from string.xml
        setQuestion(0);
    }

//    normal

//    private void setQuestion(int currentQuestion) {
//        if (currentQuestion < 0) {
//            if (!firstQuestionWarningShown) {
//                Toast.makeText(MainActivity.this, "This is the first question!", Toast.LENGTH_SHORT).show();
//                firstQuestionWarningShown = true;
//            }
//            this.currentQuestion = 0;
//        } else if (currentQuestion > questionBank.length - 1) {
//            if (!lastQuestionWarningShown) {
//                Toast.makeText(MainActivity.this, "This is the last question!", Toast.LENGTH_SHORT).show();
//                lastQuestionWarningShown = true;
//            }
//            this.currentQuestion = questionBank.length - 1;
//        } else {
//            firstQuestionWarningShown = false; // Reset khi chuyển câu hỏi thành công
//            lastQuestionWarningShown = false;  // Reset khi chuyển câu hỏi thành công
//            binding.textViewQuestion.setText(questionBank[currentQuestion].getAnswerResId());
//            this.currentQuestion = currentQuestion;
//        }
//    }
//
//    public void prevQuestion(View view) {
//        currentQuestion--;
//        setQuestion(currentQuestion);
//    }
//
//    public void nextQuestion(View view) {
//        currentQuestion++;
//        setQuestion(currentQuestion);
//    }

//    improve

    private void setQuestion(int currentQuestion) {
        binding.textViewQuestion.setText(questionBank[currentQuestion].getAnswerResId());
    }

    public void prevQuestion(View view) {
        currentQuestion = (currentQuestion - 1) % questionBank.length;
        setQuestion(Math.abs(currentQuestion));
    }

    private void checkAnswer(boolean userChoose) {
        boolean isAnswerTrue = questionBank[currentQuestion].isAnswerTrue();

        int messageId;
        if (isAnswerTrue == userChoose) {
            messageId = R.string.correct_answer;
        }
        else{
            messageId = R.string.wrong_answer;
        }

        Snackbar.make(binding.imageView, messageId, Snackbar.LENGTH_SHORT).show();
    }

    public void nextQuestion(View view) {
        currentQuestion = (currentQuestion + 1) % questionBank.length;
        setQuestion(currentQuestion);
    }

    public void checkFalseAnswer(View view) {
        checkAnswer(false);
    }

    public void checkTrueAnswer(View view) {
        checkAnswer(true);
    }

}