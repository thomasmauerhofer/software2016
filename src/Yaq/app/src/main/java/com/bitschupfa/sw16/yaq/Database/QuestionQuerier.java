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
    private QuestionCatalog currentQuestionCatalog;
    private static QuestionQuerier instance_;

    //Can be initialised as Singleton or Instance
    public QuestionQuerier(Context context){
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
    }

    private QuestionQuerier(Context context, int catalogId){
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
        currentQuestionCatalog = getQuestionCatalogById(catalogId);
    }

    //If you want to switch from one difficulty to another then you have to call refresh()
    private QuestionQuerier(Context context, int catalogId, int difficulty){
        this.context = context;
        database = DBHelper.instance(context).getDatabase();
        currentQuestionCatalog = getQuestionCatalogByIdAndDifficulty(catalogId, difficulty);
    }

    public static QuestionQuerier instance(Context myContext, int catalogId) {

        if(instance_ == null) {
            instance_ = new QuestionQuerier(myContext, catalogId);
        }
        return instance_;
    }

    public static QuestionQuerier instance(Context myContext, int catalogId, int difficulty) {

        if(instance_ == null) {
            instance_ = new QuestionQuerier(myContext, catalogId, difficulty);
        }
        return instance_;
    }

    public static void refresh(){
        instance_ = null;
    }

    public List<TextQuestion> getAllQuestionsFromCatalogByDifficulty(int catalog, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM Question WHERE qcid="+catalog+" AND difficulty="+difficulty, null);
        cursor.moveToFirst();

        List<TextQuestion> textQuestionList = new ArrayList<TextQuestion>();

        Log.v("Querier Log", "TextQuestion");
        TextQuestion textQuestion = null;

        while (!cursor.isAfterLast()) {
            difficulty = cursor.getInt(2);
            String question = cursor.getString(3);
            String answer1 = cursor.getString(4);
            String answer2 = cursor.getString(5);
            String answer3 = cursor.getString(6);
            String answer4 = cursor.getString(7);
            int rightAnswer = cursor.getInt(8);
            textQuestion = new TextQuestion(question,answer1,answer2,answer3,answer4,rightAnswer,difficulty);
            cursor.moveToNext();
            textQuestionList.add(textQuestion);

            Log.v("Querier Log", "Question     : "+textQuestion.getQuestion());
            Log.v("Querier Log", "Answer1      : "+textQuestion.getAnswer1());
            Log.v("Querier Log", "Answer2      : "+textQuestion.getAnswer2());
            Log.v("Querier Log", "Answer3      : "+textQuestion.getAnswer3());
            Log.v("Querier Log", "Answer4      : "+textQuestion.getAnswer4());
            Log.v("Querier Log", "Difficulty   : "+textQuestion.getDifficulty());
            Log.v("Querier Log", "RightAnswer  : "+textQuestion.getRightAnswer());
        }
        return textQuestionList;
    }

    public List<TextQuestion> getAllQuestionsFromCatalog(int catalog){
        Cursor cursor = database.rawQuery("SELECT * FROM Question WHERE qcid="+catalog, null);
        cursor.moveToFirst();

        List<TextQuestion> textQuestionList = new ArrayList<TextQuestion>();

        Log.v("Querier Log", "TextQuestion");
        TextQuestion textQuestion = null;

        while (!cursor.isAfterLast()) {
            int difficulty = cursor.getInt(2);
            String question = cursor.getString(3);
            String answer1 = cursor.getString(4);
            String answer2 = cursor.getString(5);
            String answer3 = cursor.getString(6);
            String answer4 = cursor.getString(7);
            int rightAnswer = cursor.getInt(8);
            textQuestion = new TextQuestion(question,answer1,answer2,answer3,answer4,rightAnswer,difficulty);
            cursor.moveToNext();
            textQuestionList.add(textQuestion);

            Log.v("Querier Log", "Question     : "+textQuestion.getQuestion());
            Log.v("Querier Log", "Answer1      : "+textQuestion.getAnswer1());
            Log.v("Querier Log", "Answer2      : "+textQuestion.getAnswer2());
            Log.v("Querier Log", "Answer3      : "+textQuestion.getAnswer3());
            Log.v("Querier Log", "Answer4      : "+textQuestion.getAnswer4());
            Log.v("Querier Log", "Difficulty   : "+textQuestion.getDifficulty());
            Log.v("Querier Log", "RightAnswer  : "+textQuestion.getRightAnswer());
        }
        return textQuestionList;
    }

    public List<QuestionCatalog> getAllQuestionCatalogsOnlyIdAndName(){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog", null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogs = new ArrayList<QuestionCatalog>();

        Log.v("Querier Log", "QuestionCatalog");

        while (!cursor.isAfterLast()) {
            int catalogID = cursor.getInt(0);
            String name = cursor.getString(1);
            cursor.moveToNext();
            QuestionCatalog questionCatalog = new QuestionCatalog(catalogID, name, null);
            questionCatalogs.add(questionCatalog);
            Log.v("Querier Log", "QuestionCatalog     : ");
            Log.v("Querier Log", "CatalogID           : "+catalogID);
        }

        return questionCatalogs;
    }

    public QuestionCatalog getQuestionCatalogById(int catalogId){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog WHERE qcid=" + catalogId, null);
        cursor.moveToFirst();

        QuestionCatalog questionCatalog = null;

        Log.v("Querier Log", "QuestionCatalog");

            int catalogID = cursor.getInt(0);
            String question = cursor.getString(1);
            cursor.moveToNext();
            List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogID);
            questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);

            Log.v("Querier Log", "QuestionCatalog     : ");
            Log.v("Querier Log", "CatalogID           : " + questionCatalog.getCatalogID());
            Log.v("Querier Log", "Name                : " + questionCatalog.getName());

        return questionCatalog;
    }

    public QuestionCatalog getQuestionCatalogByIdAndDifficulty(int catalogId, int difficulty){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog WHERE qcid=" + catalogId, null);
        cursor.moveToFirst();

        QuestionCatalog questionCatalog = null;

        Log.v("Querier Log", "QuestionCatalog");

        int catalogID = cursor.getInt(0);
        String question = cursor.getString(1);
        cursor.moveToNext();
        List<TextQuestion> textQuestionList = getAllQuestionsFromCatalogByDifficulty(catalogID, difficulty);
        questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);

        Log.v("Querier Log", "QuestionCatalog     : ");
        Log.v("Querier Log", "CatalogID           : "+questionCatalog.getCatalogID());
        Log.v("Querier Log", "Name                : " + questionCatalog.getName());

        return questionCatalog;
    }

    public List<QuestionCatalog> getAllQuestionCatalogs(){
        Cursor cursor = database.rawQuery("SELECT * FROM QuestionCatalog", null);
        cursor.moveToFirst();

        List<QuestionCatalog> questionCatalogList = new ArrayList<QuestionCatalog>();

        Log.v("Querier Log", "QuestionCatalog");

        while (!cursor.isAfterLast()) {
            int catalogID = cursor.getInt(0);
            String question = cursor.getString(1);
            cursor.moveToNext();
            List<TextQuestion> textQuestionList = getAllQuestionsFromCatalog(catalogID);
            QuestionCatalog questionCatalog = new QuestionCatalog(catalogID,question,textQuestionList);
            questionCatalogList.add(questionCatalog);

            Log.v("Querier Log", "QuestionCatalog     : ");
            Log.v("Querier Log", "CatalogID           : "+questionCatalog.getCatalogID());
            Log.v("Querier Log", "Name                : "+questionCatalog.getName());
        }

        return questionCatalogList;
    }


    public QuestionCatalog getCurrentQuestionCatalog() {
        return currentQuestionCatalog;
    }

    public void setCurrentQuestionCatalog(QuestionCatalog currentQuestionCatalog) {
        this.currentQuestionCatalog = currentQuestionCatalog;
    }

}
