package com.bitschupfa.sw16.yaq.database.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextQuestion implements Serializable {

    private int questionID;
    private String question;
    private int catalogID;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;

    public TextQuestion(int questionID, String question, Answer answer1, Answer answer2, Answer answer3, Answer answer4, int catalogID) {
        this.questionID = questionID;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.catalogID = catalogID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public List<Answer> getShuffledAnswers() {
        List<Answer> tmp = new ArrayList<>();

        tmp.add(answer1);
        tmp.add(answer2);
        tmp.add(answer3);
        tmp.add(answer4);
        Collections.shuffle(tmp);

        return tmp;
    }

    public void shuffleAnswers() {
        List<Answer> tmp = new ArrayList<>();
        tmp.add(answer1);
        tmp.add(answer2);
        tmp.add(answer3);
        tmp.add(answer4);
        Collections.shuffle(tmp);
        answer1 = tmp.get(0);
        answer2 = tmp.get(1);
        answer3 = tmp.get(2);
        answer4 = tmp.get(3);
    }

    public List<Answer> getAnswers() {
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        return answers;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }
}
