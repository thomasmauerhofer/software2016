package com.bitschupfa.sw16.yaq.Database.Helper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bitschupfa.sw16.yaq.Database.Object.Answer;
import com.bitschupfa.sw16.yaq.Database.Object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.Database.Object.TextQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik on 01.04.2016.
 */
public class QuestionQuerier extends Activity {
    private Context context;
    private SQLiteDatabase database;

    public QuestionQuerier(Context context){
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
    }


    private TextQuestion fillTextQuestion(Cursor cursor){
        Log.e("Columns: ", ""+cursor.getColumnCount());
        int catalogID = cursor.getInt(1);
        int difficulty = cursor.getInt(2);
        String question = cursor.getString(3);
        String answer1String = cursor.getString(4);
        String answer2String = cursor.getString(5);
        String answer3String = cursor.getString(6);
        String answer4String = cursor.getString(7);
        int rightAnswer1 = cursor.getInt(8);
        int rightAnswer2 = cursor.getInt(9);
        int rightAnswer3 = cursor.getInt(10);
        int rightAnswer4 = cursor.getInt(11);

        Answer answer1 = new Answer(answer1String, rightAnswer1);
        Answer answer2 = new Answer(answer2String, rightAnswer2);
        Answer answer3 = new Answer(answer3String, rightAnswer3);
        Answer answer4 = new Answer(answer4String, rightAnswer4);

        return new TextQuestion(question,answer1,answer2,answer3,answer4, difficulty, catalogID);
    }

    public List<TextQuestion> getAllQuestionsFromCatalogByDifficulty(int catalog, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM Question WHERE qcid="+catalog+" AND difficulty="+difficulty, null);
        cursor.moveToFirst();
        List<TextQuestion> textQuestionList = new ArrayList<>();
        Log.v("Querier Log", "TextQuestion");

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

        List<QuestionCatalog> questionCatalogs = new ArrayList<>();

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

        int catalogID = cursor.getInt(0);
        String question = cursor.getString(1);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogID);
        return new QuestionCatalog(catalogID,question,textQuestionList);
    }

    public QuestionCatalog getQuestionCatalogByIdAndDifficulty(int catalogId, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog WHERE qcid=" + catalogId, null);
        cursor.moveToFirst();

        int catalogID = cursor.getInt(0);
        String question = cursor.getString(1);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalogByDifficulty(catalogID, difficulty);
        return new QuestionCatalog(catalogID,question,textQuestionList);
    }

    public List<QuestionCatalog> getAllQuestionCatalogs(){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog", null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogList = new ArrayList<>();

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
