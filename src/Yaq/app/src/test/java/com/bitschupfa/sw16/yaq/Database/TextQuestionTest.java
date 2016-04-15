package com.bitschupfa.sw16.yaq.Database;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Patrik on 10.04.2016.
 */
public class TextQuestionTest {
    private final int RIGHT_ANSWER_DECREASE = 10;

    @Test
    public void testGetAnswers(){
        String question = "Test";
        int answerValue[] = new int[4];
        answerValue[0] = 0;
        answerValue[1] = 2;
        answerValue[2] = 3;
        answerValue[3] = 10;

        Answer answer1 = new Answer(question, answerValue[0]);
        Answer answer2 = new Answer(question, answerValue[1]);
        Answer answer3 = new Answer(question, answerValue[2]);
        Answer answer4 = new Answer(question, answerValue[3]);
        int difficulty = 1;
        int catalogID = 1;
        TextQuestion textQuestion = new TextQuestion(question, answer1, answer2, answer3, answer4, difficulty, catalogID);

        List<Answer> answerList = textQuestion.getAnswers();
        int i = 0;
        for(Answer answer : answerList){
            assertEquals("Answer should be answer minus RIGHT_ANSWER_DECREASE", answer.getRightAnswerValue(), answerValue[i++]-RIGHT_ANSWER_DECREASE);
        }
    }


}
