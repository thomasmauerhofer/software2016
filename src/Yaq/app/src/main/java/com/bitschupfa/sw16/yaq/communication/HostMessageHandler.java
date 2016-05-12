package com.bitschupfa.sw16.yaq.communication;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.RankingItem;

import java.util.ArrayList;

public interface HostMessageHandler {
    void updatePlayerList(String[] playerNames);
    void startGame();
    void showNextQuestion(TextQuestion question, int timeout);
    void showCorrectAnswer(Answer answer);
    void endGame(ArrayList<RankingItem> scoreList);
    void quit();
}
