package com.bitschupfa.sw16.yaq.Database;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
        int catalogID = 1;
        TextQuestion textQuestion = new TextQuestion(1, question, answer1, answer2, answer3, answer4, catalogID);

        List<Answer> answerList = textQuestion.getAnswers();
        int i = 0;
        for(Answer answer : answerList){
            assertEquals("Answer should be answer minus RIGHT_ANSWER_DECREASE", answer.getAnswerValue(), answerValue[i++]-RIGHT_ANSWER_DECREASE);
        }
    }


}
