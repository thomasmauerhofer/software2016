package com.bitschupfa.sw16.yaq.Database;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Patrik on 01.04.2016.
 */
public class TextQuestion implements Serializable{

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int rightAnswer;

    private int difficulty;

    public TextQuestion(String question, String answer1, String answer2, String answer3, String answer4, int rightAnswer, int difficulty) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.rightAnswer = rightAnswer;
        this.difficulty = difficulty;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getShuffeledAnswers() {
        List<String> tmp = new ArrayList();

        tmp.add(answer1);
        tmp.add(answer2);
        tmp.add(answer3);
        tmp.add(answer4);
        Collections.shuffle(tmp);

        return tmp;
    }

    public String getCorrectAnswer() {
        switch (rightAnswer) {
            case 1:
                return answer1;
            case 2:
                return answer2;
            case 3:
                return answer3;
            case 4:
                return answer4;
            default:
                return null;
        }
    }
}
