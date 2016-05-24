package com.bitschupfa.sw16.yaq.communication;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;

public interface ClientMessageHandler {
    void askNextQuestion();
    void registerConnectedDevice(ConnectedDevice client);
    void registerClient(String id, PlayerProfile profile);
    void startGame();
    void handleAnswer(String id, Answer answer);
    void quit();
    void clientQuits(String id);
    void handleError(Errors error, String message);
}
