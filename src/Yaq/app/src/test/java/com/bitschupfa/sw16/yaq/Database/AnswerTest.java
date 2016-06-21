package com.bitschupfa.sw16.yaq.Database;

import com.bitschupfa.sw16.yaq.database.object.Answer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AnswerTest {

    @Test
    public void testRightAnswer(){
        String test = "Test";
        int testRightAnswer = 10;
        int referenceRightAnswer = 10;
        Answer answer = new Answer(test, testRightAnswer);
        assertEquals("Right Answer should be the Answer", answer.getAnswerValue(), referenceRightAnswer);
        assertEquals("Answer String should be the same", test, answer.getAnswerString());
    }
}
