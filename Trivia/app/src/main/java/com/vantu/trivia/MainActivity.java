package com.vantu.trivia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.vantu.trivia.activity.HistoryActivity;
import com.vantu.trivia.activity.SubmitedActivity;
import com.vantu.trivia.data.Repository;
import com.vantu.trivia.databinding.ActivityMainBinding;
import com.vantu.trivia.model.History;
import com.vantu.trivia.model.Question;
import com.vantu.trivia.model.QuestionDone;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Question> questions = new ArrayList<>();
    private List<History> historyList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int totalCore = 0;

    int defaultColor = Color.parseColor("#2E3856");
    int correctColor = Color.parseColor("#13A326");
    int incorrectColor = Color.parseColor("#AD0000");

    /**
     * @noinspection FieldMayBeFinal
     */
    private Map<Integer, QuestionDone> questionDone = new HashMap<>();
    private static final String MESSAGE_ID = "history_prefs";
    private final String flag_homeScreen = "homeScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        old
//        setContentView(R.layout.activity_main);

//        new
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // truyền vào getQuestion 1 interface AnswerListAsyncResponse với hàm proccessFinish đã được override để xử lý kết quả async trả về
//        List<Question> questions = new Repository().getQuestions(new AnswerListAsyncResponse() {
//            @Override
//            public void proccessFinish(List<Question> questionArrayList) {
//                Log.d("trivia", "onCreate: " + questionArrayList);
//            }
//        });

//        replace with lamda
        questions = new Repository().getQuestions(questionArrayList -> setCurrentQuestion(this.currentQuestionIndex));

        // get historyList
        getMaxScore();
    }

    @SuppressLint("DefaultLocale")
    private void setCurrentQuestion(int currentQuestionIndex) {
        binding.textViewQuestionNum.setText(String.format("Question: %d/%d", currentQuestionIndex, questions.size() - 1));

        String question = questions.get(currentQuestionIndex).getAnswer();
        binding.textViewQuestion.setText(question);
    }

    @SuppressLint("SetTextI18n")
    private boolean checkAnswer(boolean userChoose) {
        boolean isAnswerTrue = questions.get(currentQuestionIndex).isAnswerTrue();

        int messageId;
        if (isAnswerTrue == userChoose) {
            messageId = R.string.correct_answer;
            fadeOutAnimation();

            if (totalCore < questions.size()) {
                totalCore++;
            }
            binding.textViewCurrentScore.setText("Current Score: " + totalCore + " (+1)");
        } else {
            messageId = R.string.wrong_answer;
            shakeAnimation();

            if (totalCore > 0) {
                totalCore--;
            }
            binding.textViewCurrentScore.setText("Current Score: " + totalCore + " (-1)");
        }

        Snackbar.make(binding.textViewQuestion, messageId, Snackbar.LENGTH_SHORT).show();

        return messageId == R.string.correct_answer;
    }

    private void setEnableButton(boolean statusButton) {
        binding.buttonTrue.setEnabled(statusButton);
        binding.buttonFalse.setEnabled(statusButton);
    }

    private void resetButtonColors() {
        binding.buttonFalse.setBackgroundColor(defaultColor);
        binding.buttonTrue.setBackgroundColor(defaultColor);
    }

    public void nextQuestion(View view) {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size();

        QuestionDone qd = questionDone.get(currentQuestionIndex);

        if (qd != null) {
            setEnableButton(false);
            binding.textViewYourAnswer.setText(String.format("Your answer is: %s", qd.isUserChoose()));
            binding.textViewYourAnswer.setVisibility(View.VISIBLE);
        } else {
            setEnableButton(true);
            binding.textViewYourAnswer.setVisibility(View.GONE);
        }

        resetButtonColors();
        setCurrentQuestion(currentQuestionIndex);
    }

    public void prevQuestion(View view) {
        if (currentQuestionIndex == 0) {
            currentQuestionIndex = questions.size() - 1;
        } else {
            currentQuestionIndex--;
        }

        QuestionDone qd = questionDone.get(currentQuestionIndex);

        if (qd != null) {
            setEnableButton(false);
            binding.textViewYourAnswer.setText(String.format("Your answer is: %s", qd.isUserChoose()));
            binding.textViewYourAnswer.setVisibility(View.VISIBLE);
        } else {
            setEnableButton(true);
            binding.textViewYourAnswer.setVisibility(View.GONE);
        }

        resetButtonColors();
        setCurrentQuestion(currentQuestionIndex);
    }

    public void checkFalseAnswer(View view) {
//        check answer
        boolean result = checkAnswer(false);

//        set color button
        int colorCode = result ? correctColor : incorrectColor;
        view.setBackgroundColor(colorCode);
        binding.buttonTrue.setBackgroundColor(defaultColor);

//        disable button
        setEnableButton(false);

//        set trạng thái cho câu đã chọn
        QuestionDone qd = new QuestionDone(false, true);
        questionDone.put(currentQuestionIndex, qd);

        Log.d("qd", "checkFalseAnswer: " + questionDone);
    }

    public void checkTrueAnswer(View view) {
//        check answer
        boolean result = checkAnswer(true);

//        set color button
        int colorCode = result ? correctColor : incorrectColor;
        view.setBackgroundColor(colorCode);
        binding.buttonFalse.setBackgroundColor(defaultColor);

//        disable button
        setEnableButton(false);

//        set trạng thái cho câu đã chọn
        QuestionDone qd = new QuestionDone(true, true);
        questionDone.put(currentQuestionIndex, qd);

        Log.d("qd", "checkFalseAnswer: " + questionDone);
    }

    private void applyAnimation(int animationResId, int startColor) {
        // Load animation từ resource XML
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, animationResId);

        // Áp dụng animation cho textView
        binding.textViewQuestion.startAnimation(animation);

        // Thiết lập AnimationListener chung
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textViewQuestion.setTextColor(startColor);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeOutAnimation() {
        applyAnimation(R.anim.fadeout_animation, Color.parseColor("#13A326"));
    }

    private void shakeAnimation() {
        applyAnimation(R.anim.shake_animation, Color.parseColor("#AD0000"));
    }

    @SuppressLint("SetTextI18n")
    public void resetApp(View view) {
        questionDone.clear();

        currentQuestionIndex = 0;
        setCurrentQuestion(currentQuestionIndex);

        totalCore = 0;

        binding.textViewYourAnswer.setText("");
        binding.textViewCurrentScore.setText("Current Score: 0");

        resetButtonColors();
        setEnableButton(true);

        binding.buttonTrue.setVisibility(View.VISIBLE);
        binding.buttonFalse.setVisibility(View.VISIBLE);
        binding.buttonSubmit.setText(R.string.submit);
        binding.buttonSubmit.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void submitTest(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = currentDateTime.format(formatter);

        editor.putString(currentDateAndTime, String.valueOf(totalCore));
        editor.apply();

        int maxScore = getMaxScore();

        // set up UI
        binding.buttonTrue.setVisibility(View.GONE);
        binding.buttonFalse.setVisibility(View.GONE);
        binding.buttonSubmit.setText(R.string.submited);
        binding.buttonSubmit.setEnabled(false);

        switchToActivitySubmitted(maxScore, currentDateAndTime);
    }

    @SuppressLint("SetTextI18n")
    private int getMaxScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);

        historyList.clear();

        // Lấy tất cả các key từ SharedPreferences
        Map<String, ?> allEntries = sharedPreferences.getAll();

        int maxScore = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            int value = Integer.parseInt(entry.getValue().toString());

            History history = new History(key, value);
            historyList.add(history);

            if (value > maxScore) {
                maxScore = value;
            }
        }
//        Log.d("max", "Value: " + maxScore);
//        Log.d("max", "Value: " + historyList);

        binding.textViewHighestScore.setText("Highest Score: " + maxScore);

        return maxScore;
    }

    // xử lý dữ liệu trả vè từ SubmitedActivity
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            });

    public void switchToActivitySubmitted(int highestScore, String currentDateAndTime) {
        Intent intent = new Intent(MainActivity.this, SubmitedActivity.class);

        // set flag
        intent.putExtra("flag", flag_homeScreen);

        // set data
        intent.putExtra("highestScore", highestScore);
        intent.putExtra("currentScore", totalCore);
        intent.putExtra("currentDateAndTime", currentDateAndTime);
        intent.putExtra("questions", (Serializable) questions);
        intent.putExtra("questionDone", (Serializable) questionDone);

        // Khởi động Activity và nhận kết quả trả về
        activityResultLauncher.launch(intent);
    }

    public void getHistoryActivity(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);

        // set flag
        intent.putExtra("flag", flag_homeScreen);

        intent.putExtra("historyList", (Serializable) historyList);

        // Khởi động Activity và nhận kết quả trả về
        activityResultLauncher.launch(intent);
    }

}