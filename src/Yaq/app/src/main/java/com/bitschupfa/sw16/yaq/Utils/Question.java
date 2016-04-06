package com.bitschupfa.sw16.yaq.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by manu on 29.03.16.
 */
public class Question implements Serializable {
    private String question;
    private String correctAnswer;
    private List<String> allAnswers = new ArrayList<>();

    public Question(){
        this.question = "";
        this.correctAnswer = "";
    }

    public Question(String question, String correctAnswer, List<String> allAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.allAnswers = allAnswers;
    }

    public Question(String question, String correctAnswer, String answer1, String answer2, String answer3, String answer4) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.allAnswers.add(answer1);
        this.allAnswers.add(answer2);
        this.allAnswers.add(answer3);
        this.allAnswers.add(answer4);

        Collections.shuffle(allAnswers);
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

    public void setAllAnswers(List<String> _allAnswers){
        this.allAnswers = _allAnswers;
    }

    public List<String> getAllAnswers(){
        return this.allAnswers;
    }
}
