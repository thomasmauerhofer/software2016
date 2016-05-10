package com.bitschupfa.sw16.yaq.game;


import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.messages.NEWPLAYERMessage;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.utils.Quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostGameLogic implements ClientMessageHandler{
    private static final String TAG = "HostGameLogic";
    private static HostGameLogic instance = new HostGameLogic();

    private Host hostActivity;
    private GameAtHost gameActivity;
    private Quiz quiz;
    private List<ConnectedDevice> playerDevices = new ArrayList<>();
    private Map<String, PlayerProfile> playerProfiles = new HashMap<>();

    public static HostGameLogic getInstance() {
        return instance;
    }

    private HostGameLogic() {
    }

    public void setGameActivity(GameAtHost gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setHostActivity(Host hostActivity) {
        this.hostActivity = hostActivity;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public void askNextQuestion() {

    }

    @Override
    public void registerConnectedDevice(ConnectedDevice client) {
        new Thread(client).start();
        playerDevices.add(client);
    }

    @Override
    public void registerClient(String address, PlayerProfile profile) {
        playerProfiles.put(address, profile);

        List<String> playerNames = new ArrayList<>();
        for (PlayerProfile p : playerProfiles.values()) {
            playerNames.add(p.getPlayerName());
        }
        String[] players = playerNames.toArray(new String[playerNames.size()]);

        hostActivity.updatePlayerList(players);
        for (ConnectedDevice client : playerDevices) {
            try {
                client.sendMessage(new NEWPLAYERMessage(players));
            } catch (IOException e) {
                Log.e(TAG, "Could not send any player message to client " + client.toString() +
                        ". " + e.getMessage());
            }
        }
    }

    @Override
    public void startGame() {

    }
}
