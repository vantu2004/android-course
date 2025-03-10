package com.vantu.trivia.data;

import com.vantu.trivia.model.Question;

import java.util.List;

public interface AnswerListAsyncResponse {
    void proccessFinish(List<Question> questionArrayList);
}
