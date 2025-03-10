package com.vantu.trivia.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class QuestionDone implements Serializable {
    private boolean userChoose;
    private boolean status;

    public QuestionDone() {
    }

    public QuestionDone(boolean userChoose, boolean status) {
        this.userChoose = userChoose;
        this.status = status;
    }

    public boolean isUserChoose() {
        return userChoose;
    }

    public void setUserChoose(boolean userChoose) {
        this.userChoose = userChoose;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "QuestionDone{" +
                "userChoose=" + userChoose +
                ", status=" + status +
                '}';
    }
}
