package com.bitschupfa.sw16.yaq.game;


import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.messages.ANSWERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.ENDGAMEMessage;
import com.bitschupfa.sw16.yaq.communication.messages.Message;
import com.bitschupfa.sw16.yaq.communication.messages.NEWPLAYERMessage;
import com.bitschupfa.sw16.yaq.communication.messages.QUESTIONMessage;
import com.bitschupfa.sw16.yaq.communication.messages.STARTGAMEMessage;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.utils.AnswerCollector;
import com.bitschupfa.sw16.yaq.utils.Quiz;
import com.bitschupfa.sw16.yaq.utils.ScoreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


// TODO Handle disconnect of user(Remove from all maps, and answerCollector)
public class HostGameLogic implements ClientMessageHandler{
    private static final String TAG = "HostGameLogic";
    private static HostGameLogic instance = new HostGameLogic();

    private GameAtHost gameActivity;
    private Quiz quiz;
    private Map<String, ConnectedDevice> playerDevices = new HashMap<>();
    private Map<String, PlayerProfile> playerProfiles = new HashMap<>();
    private int timeout = 10 * 1000;
    private AnswerCollector answerCollector = new AnswerCollector();
    private ScoreUtil scoreUtil = new ScoreUtil();
    private TextQuestion currentQuestion;

    public static HostGameLogic getInstance() {
        return instance;
    }

    private HostGameLogic() {
    }

    public void setGameActivity(GameAtHost gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public void askNextQuestion() {
        if(quiz.hasNext()) {
            currentQuestion = quiz.next();
            currentQuestion.shuffleAnswers();
            answerCollector.init(playerProfiles.keySet());
            sendMessageToClients(new QUESTIONMessage(currentQuestion, timeout));
        } else {
            sendMessageToClients(new ENDGAMEMessage(getSortedScoreList()));
        }
    }

    @Override
    public void registerConnectedDevice(ConnectedDevice client) {
        Log.d(TAG, "added new connected client device: " + client.getAddress());
        new Thread(client).start();
        playerDevices.put(client.getAddress(), client);
    }

    @Override
    public void registerClient(String address, PlayerProfile profile) {
        playerProfiles.put(address, profile);

        List<String> playerNames = new ArrayList<>();
        for (PlayerProfile p : playerProfiles.values()) {
            playerNames.add(p.getPlayerName());
        }
        String[] players = playerNames.toArray(new String[playerNames.size()]);

        sendMessageToClients(new NEWPLAYERMessage(players));
    }

    @Override
    public void startGame() {
        scoreUtil.init(playerDevices.keySet());
        sendMessageToClients(new STARTGAMEMessage());
    }

    @Override
    public void handleAnswer(String address, Answer answer) {
        answerCollector.addAnswerForPlayer(address, answer);
    }

    private void sendMessageToClients(Message message) {
        for(ConnectedDevice client : playerDevices.values()) {
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                Log.e(TAG, "Could not send any player message to client " + client.toString() +
                        ". " + e.getMessage());
            }
        }
    }

    public void questionFinished() {
        Answer mostCorrectAnswer = currentQuestion.getAnswers().get(0);
        for (int i = 1; i < currentQuestion.getAnswers().size(); ++i) {
            Answer tmp = currentQuestion.getAnswers().get(i);
            if (mostCorrectAnswer.getRightAnswerValue() < tmp.getRightAnswerValue()) {
                mostCorrectAnswer = tmp;
            }
        }
        for (Map.Entry<String, Answer> entry : answerCollector.getAnswers().entrySet()) {
            String address = entry.getKey();
            Answer answer = entry.getValue();

            scoreUtil.addScoreForPlayer(address, answer.getRightAnswerValue());

            if (answer.getRightAnswerValue() < 0) {
                answer = mostCorrectAnswer;
            }

            try {
                playerDevices.get(address).sendMessage(new ANSWERMessage(answer));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameActivity.enableShowNextQuestion(true);
    }

    public ArrayList<RankingItem> getSortedScoreList() {
        ArrayList<RankingItem> namesAndScores = new ArrayList<>();
        for(Entry<String, Integer> entry : scoreUtil.getSortedScoreList().entrySet()) {
            String address = entry.getKey();
            String name = playerProfiles.get(address).getPlayerName();
            Integer score = entry.getValue();

            namesAndScores.add(new RankingItem(name, score));
        }
        return namesAndScores;
    }
}
