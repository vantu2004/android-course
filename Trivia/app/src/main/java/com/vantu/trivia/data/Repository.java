package com.vantu.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.vantu.trivia.controller.AppController;
import com.vantu.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final List<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    // mục đích dùng interface để khi xử lý xog async thì truyền kết quả cho phương thức trong interface, lớp nào triển khai interface để viết logic có thể nhận được đối số từ đây
    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Question question = new Question();
                    question.setAnswer((String) response.getJSONArray(i).get(0));
                    question.setAnswerTrue((Boolean) response.getJSONArray(i).get(1));

                    questionArrayList.add(question);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            // check thử tham số callBack có null hay ko để xử lý logic cho kết quả async trả về
            if (callBack != null){
                callBack.proccessFinish(questionArrayList);
            }

        }, error -> Log.d("trivia", "Failed to get info"));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}
