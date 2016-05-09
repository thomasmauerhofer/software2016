package com.bitschupfa.sw16.yaq.game;


import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.Game;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.communication.messages.ANSWERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.HELLOMessage;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientGameLogic implements HostMessageHandler {
    private static final String TAG = "ClientGameLogic";
    private static final ClientGameLogic instance = new ClientGameLogic();
    private final BlockingQueue<Answer> answerQueue = new ArrayBlockingQueue<>(1);

    private ConnectedDevice hostDevice;
    private Join lobbyActivity;
    private Game gameActivity;


    private ClientGameLogic() {
    }

    public static ClientGameLogic getInstance() {
        return instance;
    }

    public void setLobbyActivity(Join lobby) {
        lobbyActivity = lobby;
    }

    public void setGameActivity(Game game) {
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
    public void showNextQuestion(TextQuestion question, int timeout) {
        gameActivity.showQuestion(question, timeout);

        Answer answer = null;
        try {
            answer = answerQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        // TODO: maybe send a special message object if no answer was selected (i.e. answer is null)
        try {
            hostDevice.sendMessage(new ANSWERMessage(answer));
        } catch (IOException e) {
            Log.e(TAG, "Could not send answer message to host: " + e.getMessage());
        }
    }

    public void answerQuestion(Answer answer) {
        answerQueue.add(answer);
    }

    @Override
    public void showCorrectAnswer(Answer answer) {
        gameActivity.showAnswer(answer);
    }

    @Override
    public void endGame() {
        gameActivity.showStatisticActivity();
    }
}
