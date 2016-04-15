package com.bitschupfa.sw16.yaq.Database.Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Patrik on 01.04.2016.
 */
public class TextQuestion implements Serializable{

    private String question;
    private int catalogID;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;

    private int difficulty;

    public TextQuestion(String question, Answer answer1, Answer answer2, Answer answer3, Answer answer4, int difficulty, int catalogID) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.difficulty = difficulty;
        this.catalogID = catalogID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<Answer> getShuffeledAnswers() {
        List<Answer> tmp = new ArrayList();

        tmp.add(answer1);
        tmp.add(answer2);
        tmp.add(answer3);
        tmp.add(answer4);
        Collections.shuffle(tmp);

        return tmp;
    }

    public List<Answer> getAnswers() {
        List<Answer> rightAnswers = new ArrayList<>();
        rightAnswers.add(answer1);
        rightAnswers.add(answer2);
        rightAnswers.add(answer3);
        rightAnswers.add(answer4);

        return rightAnswers;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }
}
