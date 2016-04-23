package com.bitschupfa.sw16.yaq.utils;

import android.content.Context;

import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Quiz implements Serializable {

    private List<TextQuestion> questions;

    private int current_question;

    public Quiz() {
        questions = new ArrayList<>();
        current_question = 0;
    }

    public void addQuestion(TextQuestion question) {
        questions.add(question);
    }

    public void addQuestions(List<TextQuestion> questions) {
        this.questions.addAll(questions);
    }

    public TextQuestion getCurrentQuestion() {
        return questions.get(current_question);
    }

    public void incrementCurrentQuestionCounter() {
        current_question++;
    }

    public int getCurrentQuestionCounter() {
        return current_question;
    }

    public List<TextQuestion> getQuestions() {
        return questions;
    }

    private void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    public Quiz createTmpQuiz(Context context) {

        QuestionQuerier questionQuerier = new QuestionQuerier(context);
        questions = questionQuerier.getAllQuestionsFromCatalog(1);
        shuffleQuestions();
        return this;
    }
}
