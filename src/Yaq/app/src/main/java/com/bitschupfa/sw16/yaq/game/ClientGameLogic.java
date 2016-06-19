package com.bitschupfa.sw16.yaq.game;


import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.Game;
import com.bitschupfa.sw16.yaq.activities.Lobby;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.Errors;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.communication.messages.ANSWERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.HELLOMessage;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.RankingItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class ClientGameLogic implements HostMessageHandler {
    private static final String TAG = "ClientGameLogic";
    private static final Answer noAnswer = new Answer("no answer", 0);
    private static final ClientGameLogic instance = new ClientGameLogic();
    private final BlockingQueue<Answer> answerQueue = new ArrayBlockingQueue<>(1);

    private ConnectedDevice hostDevice;
    private Lobby lobbyActivity;
    private Game gameActivity;
    private boolean isConnected = false;


    private ClientGameLogic() {
    }

    public static ClientGameLogic getInstance() {
        return instance;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setLobbyActivity(Lobby lobby) {
        lobbyActivity = lobby;
    }

    public void setGameActivity(Game game) {
        gameActivity = game;
    }

    public void setConnectedHostDevice(ConnectedDevice connectedDevice) throws IOException {
        hostDevice = connectedDevice;
        new Thread(hostDevice).start();
        hostDevice.sendMessage(new HELLOMessage(lobbyActivity.accessPlayerProfile()));
        isConnected = true;
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
            Log.e(TAG, "interrupted while waiting for answer: " + e.getMessage());
        }

        try {
            answer = answer == null ? noAnswer : answer;
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
    public void endGame(ArrayList<RankingItem> scoreList) {
        gameActivity.showStatisticActivity(scoreList);
    }

    @Override
    public void quit() {
        isConnected = false;
        gameActivity = null;
        lobbyActivity = null;
        answerQueue.clear();
        hostDevice = null;
    }

    @Override
    public void handleError(Errors error, String message) {
        if (error == Errors.GAME_FULL) {
            lobbyActivity.handleFullGame();
        }
    }
}
