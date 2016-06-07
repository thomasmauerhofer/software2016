package com.bitschupfa.sw16.yaq.utils;

import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFactory {
    private int numberOfQuestions;
    private List<String> usedCatalogs = new ArrayList<>();
    private List<TextQuestion> allQuestions = new ArrayList<>();
    private static QuizFactory instance = null;

    private QuizFactory() {
        numberOfQuestions = 10;
    }

    public static QuizFactory instance() {
        if(instance == null) {
            instance = new QuizFactory();
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
    }

    public Quiz createNewQuiz() {
        Collections.shuffle(allQuestions);
        return new Quiz(allQuestions.subList(0, getSmallestNumberOfQuestions()));
    }
}