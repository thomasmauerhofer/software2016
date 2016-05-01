package com.bitschupfa.sw16.yaq.utils;

import android.util.Log;

import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Quiz implements Iterator<TextQuestion> {
    private List<TextQuestion> questions;
    private int currentPosition;

    public Quiz() {
        questions = new ArrayList<>();
        currentPosition = 0;
    }

    public void addQuestions(List<TextQuestion> questions_) {
        questions = questions_;
        Collections.shuffle(questions);
    }

    public void setCurrentPosition(int position_) {
        currentPosition = position_;
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

    public void setQuiz(List<TextQuestion> questions_) {
        questions = questions_;
        Collections.shuffle(questions);
        Log.d("Quiz", "setQuiz: " + questions.size());
    }
}
