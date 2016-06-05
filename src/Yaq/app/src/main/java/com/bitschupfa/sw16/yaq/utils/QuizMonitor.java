package com.bitschupfa.sw16.yaq.utils;

import android.app.Application;

/**
 * Created by manu on 09.05.16.
 */
public class QuizMonitor extends Application {

    private Quiz quiz;

    @Override
    public void onCreate() {
        super.onCreate();

        if (quiz == null) {
            quiz = new Quiz();
        }
    }

    public Quiz getQuizClass() {
        if (quiz == null) {
            quiz = new Quiz();
        }
        return quiz;
    }
}

