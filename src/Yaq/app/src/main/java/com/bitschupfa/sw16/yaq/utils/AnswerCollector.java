package com.bitschupfa.sw16.yaq.utils;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnswerCollector {
    public static final Answer noAnswer = new Answer("no answer from client, yet", 42);
    private final Map<String, Answer> answers = new HashMap<>();


    public Map<String, Answer> getAnswers() {
        return answers;
    }

    public Answer getAnswerOfPlayer(String id) {
        return answers.get(id);
    }

    public void init(Set<String> playerIds) {
        answers.clear();
        for (String id : playerIds) {
            answers.put(id, noAnswer);
        }
    }

    public void addAnswerForPlayer(String id, Answer answer) {
        answers.put(id, answer);

        if (isComplete()) {
            HostGameLogic.getInstance().questionFinished();
        }
    }

    public void removePlayer(String id) {
        answers.remove(id);
        if (isComplete()) {
            HostGameLogic.getInstance().questionFinished();
        }
    }

    private boolean isComplete() {
        for (Answer answer : answers.values()) {
            if (answer == noAnswer) {
                return false;
            }
        }
        return true;
    }
}
