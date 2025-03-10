package com.vantu.trivia.model;

public class QuestionDetailSubmited {
    private String question;
    private boolean key;
    private String userChoose;

    public QuestionDetailSubmited(String question, boolean key, String userChoose) {
        this.question = question;
        this.key = key;
        this.userChoose = userChoose;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getUserChoose() {
        return userChoose;
    }

    public void setUserChoose(String userChoose) {
        this.userChoose = userChoose;
    }

    @Override
    public String toString() {
        return "QuestionDetailSubmited{" +
                "question='" + question + '\'' +
                ", key=" + key +
                ", userChoose=" + userChoose +
                '}';
    }
}
