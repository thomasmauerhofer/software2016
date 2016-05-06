package com.bitschupfa.sw16.yaq.communication;

public interface HostMessageHandler {
    void updatePlayerList(String[] playerNames);
    void startGame();
    void showNextQuestion();
}
