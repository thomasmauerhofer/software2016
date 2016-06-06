package com.bitschupfa.sw16.yaq.utils;

import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizBuilder {
    private int numberOfQuestions;
    private List<String> usedCatalogs = new ArrayList<>();
    private List<TextQuestion> allQuestions = new ArrayList<>();
    private static QuizBuilder instance = null;

    private QuizBuilder() {
        numberOfQuestions = 10;
    }

    public static QuizBuilder instance() {
        if(instance == null) {
            instance = new QuizBuilder();
        }
        return instance;
    }

    public void addQuestions(String catalogName, List< TextQuestion > questions) {
        usedCatalogs.add(catalogName);
        allQuestions.addAll(questions);
        Collections.shuffle(allQuestions);
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getSmallestNumberOfQuestions() {
        return allQuestions.size() < numberOfQuestions ? allQuestions.size() : numberOfQuestions;
    }

    public boolean isCatalogUsed(String name) {
        return usedCatalogs.contains(name);
    }

    public void clearQuiz() {
        allQuestions.clear();
        usedCatalogs.clear();
        numberOfQuestions = 10;
    }

    public Quiz createNewQuiz() {
        Collections.shuffle(allQuestions);
        return new Quiz(allQuestions.subList(0, getSmallestNumberOfQuestions()));
    }
}