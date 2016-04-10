package com.bitschupfa.sw16.yaq.Database;

import java.io.Serializable;

/**
 * Created by Patrik on 10.04.2016.
 */
public class Answer implements Serializable{
    private String answer;
    private boolean isRightAnswer;

    public Answer(String answer, boolean isRightAnswer) {
        this.answer = answer;
        this.isRightAnswer = isRightAnswer;
    }

    public String getAnswerString() {
        return answer;
    }

    public void setAnswerString(String answer) {
        this.answer = answer;
    }

    public boolean isRightAnswer() {
        return isRightAnswer;
    }

    public void setIsRightAnswer(boolean isRightAnswer) {
        this.isRightAnswer = isRightAnswer;
    }
}
