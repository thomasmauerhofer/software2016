package com.bitschupfa.sw16.yaq.database.dao;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.List;

/**
 * Created by Patrik on 05.05.2016.
 */
public class TextQuestionDAO extends DatabaseObject {
    public static String TABLE_NAME = "Question";
    public static final String QUESTION_ID = "qid";
    public static final String QUESTION_TEXT = "question";
    public static final String QUESTION_ANSWER_1 = "answer1";
    public static final String QUESTION_ANSWER_2 = "answer2";
    public static final String QUESTION_ANSWER_3 = "answer3";
    public static final String QUESTION_ANSWER_4 = "answer4";
    public static final String QUESTION_RIGHT_ANSWER_1_VALUE = "rightanswer1";
    public static final String QUESTION_RIGHT_ANSWER_2_VALUE = "rightanswer2";
    public static final String QUESTION_RIGHT_ANSWER_3_VALUE = "rightanswer3";
    public static final String QUESTION_RIGHT_ANSWER_4_VALUE = "rightanswer4";
    public static final String[] QUESTION_ALL_COLUMN_NAMES = {QUESTION_ID, QuestionCatalogDAO.QUESTIONCATALOG_ID, QUESTION_TEXT,
            QUESTION_ANSWER_1, QUESTION_ANSWER_2, QUESTION_ANSWER_3, QUESTION_ANSWER_4, QUESTION_RIGHT_ANSWER_1_VALUE,
            QUESTION_RIGHT_ANSWER_2_VALUE, QUESTION_RIGHT_ANSWER_3_VALUE, QUESTION_RIGHT_ANSWER_4_VALUE};

    private TextQuestion textQuestion;

    public TextQuestionDAO(TextQuestion textQuestion) {
        super();
        this.textQuestion = textQuestion;
        tableName = TABLE_NAME;
    }


    @Override
    protected void fillDatabaseContentValues() {
        contentValues.put(QUESTION_ID, textQuestion.getQuestionID());
        contentValues.put(QuestionCatalogDAO.QUESTIONCATALOG_ID, textQuestion.getCatalogID());

        contentValues.put(QUESTION_TEXT, textQuestion.getQuestion());
        List<Answer> answerList = textQuestion.getAnswers();

        contentValues.put(QUESTION_ANSWER_1, answerList.get(0).getAnswerString());
        contentValues.put(QUESTION_ANSWER_2, answerList.get(1).getAnswerString());
        contentValues.put(QUESTION_ANSWER_3, answerList.get(2).getAnswerString());
        contentValues.put(QUESTION_ANSWER_4, answerList.get(3).getAnswerString());

        contentValues.put(QUESTION_RIGHT_ANSWER_1_VALUE, answerList.get(0).getRightAnswerValue() - Answer.MIN_VAL_HACK);
        contentValues.put(QUESTION_RIGHT_ANSWER_2_VALUE, answerList.get(1).getRightAnswerValue() - Answer.MIN_VAL_HACK);
        contentValues.put(QUESTION_RIGHT_ANSWER_3_VALUE, answerList.get(2).getRightAnswerValue() - Answer.MIN_VAL_HACK);
        contentValues.put(QUESTION_RIGHT_ANSWER_4_VALUE, answerList.get(3).getRightAnswerValue() - Answer.MIN_VAL_HACK);
    }
}
