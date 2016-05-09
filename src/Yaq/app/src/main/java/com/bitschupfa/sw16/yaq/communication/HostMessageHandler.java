package com.bitschupfa.sw16.yaq.communication;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

public interface HostMessageHandler {
    void updatePlayerList(String[] playerNames);
    void startGame();
    void showNextQuestion(TextQuestion question, int timeout);
    void showCorrectAnswer(Answer answer);
    void endGame();
}
