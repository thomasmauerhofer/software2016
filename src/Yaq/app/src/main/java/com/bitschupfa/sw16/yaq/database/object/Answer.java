package com.bitschupfa.sw16.yaq.database.object;

import java.io.Serializable;


public class Answer implements Serializable{
    private String answer;
    private int answerValue;
    public static final int MIN_VAL_HACK = -10;

    public Answer(String answer, int isRightAnswer) {
        this.answer = answer;
        this.answerValue = isRightAnswer + MIN_VAL_HACK;
    }

    public String getAnswerString() {
        return answer;
    }

    public void setAnswerString(String answer) {
        this.answer = answer;
    }

    public int getRightAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        this.answerValue = answerValue;
    }
}
