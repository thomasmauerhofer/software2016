package com.bitschupfa.sw16.yaq.utils;

import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Quiz implements Iterator<TextQuestion> {
    private List<TextQuestion> questions = new ArrayList<>();
    private int currentPosition;

    public Quiz(List<TextQuestion> questions) {
        this.questions.addAll(questions);
        currentPosition = 0;
    }

    public int getNumberOfQuestions() {
        return questions.size();
    }

    @Override
    public TextQuestion next() {
        TextQuestion tQ = questions.get(currentPosition);
        currentPosition++;
        return tQ;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < questions.size();
    }

    @Override
    public void remove() {
    }

    public void resetQuiz() {
        currentPosition = 0;
    }

    public void clearQuiz() {
        questions.clear();
        currentPosition = 0;
    }
}
