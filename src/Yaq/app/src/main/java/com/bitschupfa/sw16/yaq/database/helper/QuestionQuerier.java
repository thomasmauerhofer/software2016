package com.bitschupfa.sw16.yaq.database.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.List;


public class QuestionQuerier {
    private final Context context;
    private final SQLiteDatabase database;


    public QuestionQuerier(Context context) {
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
    }

    private QuestionCatalog queryQuestionCatalogTable(Cursor cursor) {
        int catalogID = cursor.getInt(cursor.getColumnIndex(QuestionCatalogDAO.QUESTIONCATALOG_ID));
        String catalogName = cursor.getString(cursor.getColumnIndex(QuestionCatalogDAO.QUESTIONCATALOG_DESCRIPTION));
        int catalogDifficulty = cursor.getInt(cursor.getColumnIndex(QuestionCatalogDAO.QUESTIONCATALOG_DIFFICULTY));

        return new QuestionCatalog(catalogID, catalogDifficulty, catalogName, null);
    }

    private TextQuestion queryTextQuestionTableValues(Cursor cursor) {
        int questionID = cursor.getInt(cursor.getColumnIndex(TextQuestionDAO.QUESTION_ID));
        int catalogID = cursor.getInt(cursor.getColumnIndex(QuestionCatalogDAO.QUESTIONCATALOG_ID));
        String question = cursor.getString(cursor.getColumnIndex(TextQuestionDAO.QUESTION_TEXT));
        String answer1String = cursor.getString(cursor.getColumnIndex(TextQuestionDAO.QUESTION_ANSWER_1));
        String answer2String = cursor.getString(cursor.getColumnIndex(TextQuestionDAO.QUESTION_ANSWER_2));
        String answer3String = cursor.getString(cursor.getColumnIndex(TextQuestionDAO.QUESTION_ANSWER_3));
        String answer4String = cursor.getString(cursor.getColumnIndex(TextQuestionDAO.QUESTION_ANSWER_4));
        int rightAnswer1 = cursor.getInt(cursor.getColumnIndex(TextQuestionDAO.QUESTION_RIGHT_ANSWER_1_VALUE));
        int rightAnswer2 = cursor.getInt(cursor.getColumnIndex(TextQuestionDAO.QUESTION_RIGHT_ANSWER_2_VALUE));
        int rightAnswer3 = cursor.getInt(cursor.getColumnIndex(TextQuestionDAO.QUESTION_RIGHT_ANSWER_3_VALUE));
        int rightAnswer4 = cursor.getInt(cursor.getColumnIndex(TextQuestionDAO.QUESTION_RIGHT_ANSWER_4_VALUE));

        Answer answer1 = new Answer(answer1String, rightAnswer1);
        Answer answer2 = new Answer(answer2String, rightAnswer2);
        Answer answer3 = new Answer(answer3String, rightAnswer3);
        Answer answer4 = new Answer(answer4String, rightAnswer4);

        return new TextQuestion(questionID, question, answer1, answer2, answer3, answer4, catalogID);
    }

     /*public List<TextQuestion> getAllQuestionsFromCatalogByDifficulty(int catalogID, int difficulty){
       String catalogIDString = "" + catalogID;
        String difficultyString = "" + difficulty;
        Cursor cursor = database.query(TextQuestionDAO.TABLE_NAME, TextQuestionDAO.QUESTION_ALL_COLUMN_NAMES,
                QuestionCatalogDAO.QUESTIONCATALOG_ID+"=? and "+TextQuestionDAO.QUESTION_DIFFICULTY+"=?",
                new String[]{catalogIDString, difficultyString}, null, null, null);
        cursor.moveToFirst();
        List<TextQuestion> textQuestionList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            textQuestionList.add(queryTextQuestionTableValues(cursor));
            cursor.moveToNext();
        }
        return textQuestionList;
    }*/

    public List<TextQuestion> getAllQuestionsFromCatalog(int catalogID) {
        String catalogIDString = "" + catalogID;
        Cursor cursor = database.query(TextQuestionDAO.TABLE_NAME, TextQuestionDAO.QUESTION_ALL_COLUMN_NAMES,
                QuestionCatalogDAO.QUESTIONCATALOG_ID + "=?",
                new String[]{catalogIDString}, null, null, null);
        cursor.moveToFirst();

        List<TextQuestion> textQuestionList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            textQuestionList.add(queryTextQuestionTableValues(cursor));
            cursor.moveToNext();
        }
        return textQuestionList;
    }

    public List<QuestionCatalog> getAllQuestionCatalogsOnlyIdAndName() {
        Cursor cursor = database.query(QuestionCatalogDAO.TABLE_NAME, QuestionCatalogDAO.QUESTIONCATALOG_ALL_COLUMN_NAMES,
                null, null, null, null, null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogs = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            QuestionCatalog questionCatalog = queryQuestionCatalogTable(cursor);
            cursor.moveToNext();
            questionCatalogs.add(questionCatalog);
        }
        cursor.close();
        return questionCatalogs;
    }


    public QuestionCatalog getQuestionCatalogById(int catalogId) {
        String catalogIDString = "" + catalogId;
        Cursor cursor = database.query(QuestionCatalogDAO.TABLE_NAME, QuestionCatalogDAO.QUESTIONCATALOG_ALL_COLUMN_NAMES,
                QuestionCatalogDAO.QUESTIONCATALOG_ID + "=?", new String[]{catalogIDString}, null, null, null);
        cursor.moveToFirst();
        cursor.moveToFirst();

        QuestionCatalog questionCatalog = queryQuestionCatalogTable(cursor);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(questionCatalog.getCatalogID());
        cursor.close();
        questionCatalog.setTextQuestionList(textQuestionList);
        return questionCatalog;
    }

    public QuestionCatalog getQuestionCatalogByIdAndDifficulty(int catalogId, int difficulty) {
        QuestionCatalog questionCatalog = getQuestionCatalogById(catalogId);
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogId);
        questionCatalog.setTextQuestionList(textQuestionList);
        return questionCatalog;
    }

    public List<QuestionCatalog> getAllQuestionCatalogs() {
        Cursor cursor = database.query(QuestionCatalogDAO.TABLE_NAME, QuestionCatalogDAO.QUESTIONCATALOG_ALL_COLUMN_NAMES,
                null, null, null, null, null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            QuestionCatalog questionCatalog = queryQuestionCatalogTable(cursor);
            cursor.moveToNext();
            List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(questionCatalog.getCatalogID());
            questionCatalog.setTextQuestionList(textQuestionList);
            questionCatalogList.add(questionCatalog);
        }
        cursor.close();
        return questionCatalogList;
    }
}
