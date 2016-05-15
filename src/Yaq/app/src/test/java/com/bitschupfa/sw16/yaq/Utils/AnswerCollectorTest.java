package com.bitschupfa.sw16.yaq.Utils;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.utils.AnswerCollector;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnswerCollectorTest {

    private Set<String> player = new HashSet<>();
    private AnswerCollector answerCollector = new AnswerCollector();
    private Answer tmpAnswer = new Answer("tmp", 11);

    @Before
    public void setUp() throws Exception{
        player.add("Tom");
        player.add("Matthias");
        player.add("Patrice");
        player.add("Manuel");
        player.add("Johannes");
        answerCollector.init(player);
        checkInitialisation();
    }

    @Test
    public void testAddAnswer() {
        answerCollector.addAnswerForPlayer("Tom", tmpAnswer);
        Assert.assertEquals(answerCollector.getAnswerOfPlayer("Tom"), tmpAnswer);
    }

    @Test
    public void testRemovePlayer() {
        Assert.assertEquals(answerCollector.getAnswers().size(), 5);
        answerCollector.removePlayer("Tom");
        Assert.assertEquals(answerCollector.getAnswers().size(), 4);
    }

    private void checkInitialisation() {
        for(Map.Entry<String, Answer> entry : answerCollector.getAnswers().entrySet()) {
            Assert.assertEquals(entry.getValue(), AnswerCollector.noAnswer);
        }
    }
}
