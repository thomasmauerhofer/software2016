package com.bitschupfa.sw16.yaq.communication;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;

public interface ClientMessageHandler {
    void askNextQuestion();
    void registerConnectedDevice(ConnectedDevice client);
    void registerClient(String address, PlayerProfile profile);
    void startGame();
    void handleAnswer(String address, Answer answer);
}
