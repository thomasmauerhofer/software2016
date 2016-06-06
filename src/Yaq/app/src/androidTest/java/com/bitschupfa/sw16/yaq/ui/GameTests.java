package com.bitschupfa.sw16.yaq.ui;

import android.bluetooth.BluetoothAdapter;
import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.communication.ConnectedClientDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedHostDevice;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.game.ClientGameLogic;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;
import com.robotium.solo.Solo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameTests extends ActivityInstrumentationTestCase2<GameAtHost> {

    private Solo solo;
    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    public GameTests() {
        super(GameAtHost.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ClientGameLogic.getInstance().setLobbyActivity(new Host());
        //selfConnectionHack();
        initHostGameLogic();
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void testGame() {
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));
    }

    private void selfConnectionHack() {
        final int fakeHostPort = 7777;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket fakeHost = new ServerSocket(fakeHostPort);
                    Socket socket = fakeHost.accept();
                    ConnectedDevice client = new ConnectedClientDevice("localhost", socket,
                            HostGameLogic.getInstance());
                    HostGameLogic.getInstance().registerConnectedDevice(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("localhost", fakeHostPort);
                    ConnectedDevice host = new ConnectedHostDevice("localhost", socket,
                            ClientGameLogic.getInstance());
                    ClientGameLogic.getInstance().setConnectedHostDevice(host);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initHostGameLogic() {
        Answer answer1 = new Answer("correct", 10);
        Answer answer2 = new Answer("wrong1", 0);
        Answer answer3 = new Answer("wrong2", 0);
        Answer answer4 = new Answer("wrong3", 0);
        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "Question1", answer1, answer2, answer3, answer4, 1));
        QuizFactory.instance().addQuestions("text", questions);
        HostGameLogic.getInstance().setQuiz(QuizFactory.instance().createNewQuiz());
    }
}
