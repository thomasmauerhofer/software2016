package com.bitschupfa.sw16.yaq.game;


import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.Errors;
import com.bitschupfa.sw16.yaq.communication.messages.ANSWERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.ENDGAMEMessage;
import com.bitschupfa.sw16.yaq.communication.messages.ERRORMessage;
import com.bitschupfa.sw16.yaq.communication.messages.Message;
import com.bitschupfa.sw16.yaq.communication.messages.NEWPLAYERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.QUESTIONMessage;
import com.bitschupfa.sw16.yaq.communication.messages.STARTGAMEMessage;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.utils.AnswerCollector;
import com.bitschupfa.sw16.yaq.utils.CastHelper;
import com.bitschupfa.sw16.yaq.utils.Quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


// TODO Handle disconnect of user(Remove from all maps, and answerCollector)
public class HostGameLogic implements ClientMessageHandler {
    private static final String TAG = "HostGameLogic";
    private static final HostGameLogic instance = new HostGameLogic();

    private GameAtHost gameActivity;
    private Quiz quiz;
    private final PlayerList players = new PlayerList();
    private int timeout = 10 * 1000;
    private AnswerCollector answerCollector = new AnswerCollector();
    private TextQuestion currentQuestion;

    private CastHelper castHelper;

    public static HostGameLogic getInstance() {
        return instance;
    }

    private HostGameLogic() {
    }

    public void setGameActivity(GameAtHost gameActivity) {
        this.gameActivity = gameActivity;
        this.castHelper = CastHelper.getInstance(gameActivity.getApplicationContext(), CastHelper.GameState.GAME);
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.quiz.resetQuiz();
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void askNextQuestion() {
        if (quiz.hasNext()) {
            currentQuestion = quiz.next();
            currentQuestion.shuffleAnswers();
            answerCollector.init(players.getPlayerIds());
            sendMessageToClients(new QUESTIONMessage(currentQuestion, timeout));
            castHelper.setQuestionToDisplay(currentQuestion);
            castHelper.sendTextQuestion(currentQuestion);
        } else {
            ArrayList<RankingItem> scoreboard = players.getSortedScoreList();
            sendMessageToClients(new ENDGAMEMessage(scoreboard));
            castHelper.setScoreboardToDisplay(scoreboard);
            castHelper.sendScoreboard(scoreboard);
        }
    }

    @Override
    public void registerConnectedDevice(ConnectedDevice client) {
        Log.d(TAG, "added new connected client device: " + client.getAddress());
        new Thread(client).start();
        players.registerConnectedDevice(client.getAddress(), client);
    }

    @Override
    public void registerClient(String id, PlayerProfile profile) {
        try {
            players.addPlayer(id, profile);
            String[] playersNames = players.getPlayerNames().toArray(new String[players.getNumberOfPlayers()]);
            sendMessageToClients(new NEWPLAYERMessage(playersNames));
        } catch (IllegalStateException e) {
            try {
                ConnectedDevice device = players.unregisterConnectedDevice(id);
                device.sendMessage(new ERRORMessage(Errors.GAME_FULL, e.getMessage()));
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage());
            }
        }
    }

    @Override
    public void startGame() {
        players.resetScores();
        sendMessageToClients(new STARTGAMEMessage());
    }

    @Override
    public void handleAnswer(String address, Answer answer) {
        answerCollector.addAnswerForPlayer(address, answer);
    }

    @Override
    public void clientQuits(String id) {
        players.removePlayer(id);
        answerCollector.removePlayer(id);
    }

    @Override
    public void handleError(Errors error, String message) {
        Log.e(TAG, "received error message from client: " + message);
    }

    @Override
    public void quit() {
        quiz = null;
        gameActivity = null;
        players.clear();
        answerCollector = null;
        currentQuestion = null;
    }

    private void sendMessageToClients(Message message) {
        for (Player player : players.getPlayers()) {
            try {
                player.getDevice().sendMessage(message);
            } catch (IOException e) {
                Log.e(TAG, "Could not send any player message to client " + player.getDevice().toString() +
                        ". " + e.getMessage());
            }
        }
    }

    public void questionFinished() {
        Answer mostCorrectAnswer = currentQuestion.getAnswers().get(0);
        for (int i = 1; i < currentQuestion.getAnswers().size(); ++i) {
            Answer tmp = currentQuestion.getAnswers().get(i);
            if (mostCorrectAnswer.getAnswerValue() < tmp.getAnswerValue()) {
                mostCorrectAnswer = tmp;
            }
        }
        for (Map.Entry<String, Answer> entry : answerCollector.getAnswers().entrySet()) {
            String id = entry.getKey();
            Answer answer = entry.getValue();

            players.getPlayer(id).addScore(answer.getAnswerValue());

            if (!answer.isCorrectAnswer()) {
                answer = mostCorrectAnswer;
            }

            try {
                players.getPlayer(id).getDevice().sendMessage(new ANSWERMessage(answer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameActivity.enableShowNextQuestion(true);

        castHelper.sendShowCorrectAnswers();
    }
}
