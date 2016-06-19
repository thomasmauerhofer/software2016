package com.bitschupfa.sw16.yaq.database.object;

import java.io.Serializable;

import io.realm.RealmObject;


public class Answer extends RealmObject implements Serializable {
    private String answer;
    private int answerValue;

    public Answer() {
    }

    public Answer(String answer, int value) {
        this.answer = answer;
        this.answerValue = value;
    }

    public String getAnswerString() {
        return answer;
    }

    public void setAnswerString(String answer) {
        this.answer = answer;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }

    public boolean isCorrectAnswer() {
        return answerValue > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Answer) {
            Answer other = (Answer) o;
            return answer.equals(other.getAnswerString()) && answerValue == other.getAnswerValue();
        }

        return false;
    }
}
