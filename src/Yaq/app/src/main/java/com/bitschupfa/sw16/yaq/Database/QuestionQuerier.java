package com.bitschupfa.sw16.yaq.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik on 01.04.2016.
 */
public class QuestionQuerier {
    private Context context;
    private SQLiteDatabase database;

    public QuestionQuerier(Context context){
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
    }


    private TextQuestion fillTextQuestion(Cursor cursor){
        Log.e("Columns: ", ""+cursor.getColumnCount());
        int difficulty = cursor.getInt(2);
        String question = cursor.getString(3);
        String answer1String = cursor.getString(4);
        String answer2String = cursor.getString(5);
        String answer3String = cursor.getString(6);
        String answer4String = cursor.getString(7);
        boolean rightAnswer1 = (cursor.getInt(8) == 1) ? true : false;
        boolean rightAnswer2 = (cursor.getInt(9) == 1) ? true : false;
        boolean rightAnswer3 = (cursor.getInt(10) == 1) ? true : false;
        boolean rightAnswer4 = (cursor.getInt(11) == 1) ? true : false;

        Answer answer1 = new Answer(answer1String, rightAnswer1);
        Answer answer2 = new Answer(answer2String, rightAnswer2);
        Answer answer3 = new Answer(answer3String, rightAnswer3);
        Answer answer4 = new Answer(answer4String, rightAnswer4);

        return new TextQuestion(question,answer1,answer2,answer3,answer4, difficulty);
    }

    public List<TextQuestion> getAllQuestionsFromCatalogByDifficulty(int catalog, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM Question WHERE qcid="+catalog+" AND difficulty="+difficulty, null);
        cursor.moveToFirst();

        List<TextQuestion> textQuestionList = new ArrayList<TextQuestion>();

        Log.v("Querier Log", "TextQuestion");
        TextQuestion textQuestion = null;

        while (!cursor.isAfterLast()) {
            textQuestionList.add(fillTextQuestion(cursor));
            cursor.moveToNext();
        }
        return textQuestionList;
    }

    public List<TextQuestion> getAllQuestionsFromCatalog(int catalog){
        Cursor cursor = database.rawQuery("SELECT * FROM Question WHERE qcid="+catalog, null);
        cursor.moveToFirst();

        List<TextQuestion> textQuestionList = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            textQuestionList.add(fillTextQuestion(cursor));
            cursor.moveToNext();
        }
        return textQuestionList;
    }

    public List<QuestionCatalog> getAllQuestionCatalogsOnlyIdAndName(){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog", null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogs = new ArrayList<QuestionCatalog>();

        while (!cursor.isAfterLast()) {
            int catalogID = cursor.getInt(0);
            String name = cursor.getString(1);
            cursor.moveToNext();
            QuestionCatalog questionCatalog = new QuestionCatalog(catalogID, name, null);
            questionCatalogs.add(questionCatalog);
        }

        return questionCatalogs;
    }

    public QuestionCatalog getQuestionCatalogById(int catalogId){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog WHERE qcid=" + catalogId, null);
        cursor.moveToFirst();

        QuestionCatalog questionCatalog = null;

        int catalogID = cursor.getInt(0);
        String question = cursor.getString(1);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogID);
        questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);

        return questionCatalog;
    }

    public QuestionCatalog getQuestionCatalogByIdAndDifficulty(int catalogId, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog WHERE qcid=" + catalogId, null);
        cursor.moveToFirst();

        QuestionCatalog questionCatalog = null;

        int catalogID = cursor.getInt(0);
        String question = cursor.getString(1);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalogByDifficulty(catalogID, difficulty);
        questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);

        return questionCatalog;
    }

    public List<QuestionCatalog> getAllQuestionCatalogs(){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog", null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogList = new ArrayList<QuestionCatalog>();

        while (!cursor.isAfterLast()) {
            int catalogID = cursor.getInt(0);
            String question = cursor.getString(1);
            cursor.moveToNext();
            List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogID);
            QuestionCatalog questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);
            questionCatalogList.add(questionCatalog);
        }

        return questionCatalogList;
    }

}
