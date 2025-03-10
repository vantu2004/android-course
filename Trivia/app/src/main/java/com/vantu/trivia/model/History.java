package com.vantu.trivia.model;

import java.io.Serializable;

public class History implements Serializable {
    private String time;
    private int score;

    public History(String time, int score) {
        this.time = time;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "History{" +
                "time='" + time + '\'' +
                ", score=" + score +
                '}';
    }
}
