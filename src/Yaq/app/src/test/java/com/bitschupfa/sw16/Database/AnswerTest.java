package com.bitschupfa.sw16.yaq.Database;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Created by Patrik on 10.04.2016.
 */
public class AnswerTest {

    @Test
    public void testSetterAndGetter(){
        String test = "test";
        Answer answer = new Answer(test, false);
        assertEquals("Test answer String getter 1", answer.getAnswerString(), test);
        assertEquals("Test right Answer getter 2", answer.isRightAnswer(), false);

        String test1 = "test1";
        answer.setAnswerString(test1);
        answer.setIsRightAnswer(true);
        assertEquals("Test answer String getter 1", answer.getAnswerString(), test1);
        assertEquals("Test right Answer getter 2", answer.isRightAnswer(), true);
    }
}
