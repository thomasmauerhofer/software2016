package com.bitschupfa.sw16.yaq.game;


import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.QuestionsAsked;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.communication.messages.HELLOMessage;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;

import java.io.IOException;

public class ClientGameLogic implements HostMessageHandler {
    private static final ClientGameLogic instance = new ClientGameLogic();
    private ConnectedDevice hostDevice;
    private Join lobbyActivity;
    private QuestionsAsked gameActivity;


    private ClientGameLogic() {
    }

    public static ClientGameLogic getInstance() {
        return instance;
    }

    public void setLobbyActivity(Join lobby) {
        lobbyActivity = lobby;
    }

    public void setGameActivity(QuestionsAsked game) {
        gameActivity = game;
    }

    public void setConnectedHostDevice(ConnectedDevice connectedDevice) throws IOException {
        hostDevice = connectedDevice;
        new Thread(hostDevice).start();
        hostDevice.sendMessage(new HELLOMessage(PlayerProfileStorage
                .getInstance(lobbyActivity.getApplicationContext())
                .getPlayerProfile()));
    }

    @Override
    public void updatePlayerList(String[] playerNames) {
        lobbyActivity.updatePlayerList(playerNames);
    }

    @Override
    public void startGame() {
        lobbyActivity.openGameActivity();
    }

    @Override
    public void showNextQuestion() {

    }
}
