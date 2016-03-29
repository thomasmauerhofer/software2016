package com.bitschupfa.sw16.yaq.Utils;

import java.util.ArrayList;

/**
 * Created by manu on 29.03.16.
 */
public class Question {
    private String question;
    private String correctAnswer;
    private ArrayList<String> wrongAnswers;

    public Question(){
        this.question = "";
        this.correctAnswer = "";
        this.wrongAnswers = new ArrayList<String>();
    }

    public void setQuestion(String _question){
        this.question = _question;
    }

    public String getQuestion(){
        return this.question;
    }

    public void setCorrectAnswer(String _correctAnswer){
        this.correctAnswer = _correctAnswer;
    }

    public String getCorrectAnswer(){
        return this.correctAnswer;
    }

    public void setWrongAnswers(ArrayList<String> _wrongAnswers){
        this.wrongAnswers = _wrongAnswers;
    }

    public ArrayList<String> getWrongAnswers(){
        return this.wrongAnswers;
    }
}
