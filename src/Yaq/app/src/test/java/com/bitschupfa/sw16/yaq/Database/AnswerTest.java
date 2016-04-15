package com.bitschupfa.sw16.yaq.Database;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Created by Patrik on 10.04.2016.
 */
public class AnswerTest {
    private final int RIGHT_ANSWER_DECREASE = 10;

    @Test
    public void testRightAnswer(){
        String test = "Test";
        int testRightAnswer = 10;
        int referenceRightAnswer = 10 - RIGHT_ANSWER_DECREASE;
        Answer answer = new Answer(test, testRightAnswer);
        assertEquals("Right Answer should be the Answer minus RIGHT_ANSWER_DECREASE", answer.getRightAnswerValue(), referenceRightAnswer);
        assertEquals("Answer String should be the same", test, answer.getAnswerString());
    }
}
