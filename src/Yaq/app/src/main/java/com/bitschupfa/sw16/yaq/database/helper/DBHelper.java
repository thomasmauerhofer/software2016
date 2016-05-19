package com.bitschupfa.sw16.yaq.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;


public class DBHelper extends SQLiteOpenHelper{

    private static String DATABASE_NAME = "yaq.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper instance_ = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper instance(Context myContext) {

        if(instance_ == null) {
            instance_ = new DBHelper(myContext);
        }
        return instance_;
    }

    public SQLiteDatabase getDatabase(){
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getInsertionDatabase(){
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONCATALOG_TABLE = "CREATE TABLE QuestionCatalog ( "+
        "`qcid`             BIGINT UNSIGNED PRIMARY KEY NOT NULL,"+
        "`description` 	    VARCHAR(250) NOT NULL);";

        String CREATE_QUESTION_TABLE = "CREATE TABLE Question ( "+
        "`qid`   			BIGINT UNSIGNED PRIMARY KEY NOT NULL,"+
        "`qcid`  			BIGINT UNSIGNED NOT NULL,"+
        "`difficulty`   	INTEGER NOT NULL,"+
        "`question`  		VARCHAR(250) NOT NULL,"+
        "`answer1`  		VARCHAR(100) NOT NULL,"+
        "`answer2`  		VARCHAR(100) NOT NULL,"+
        "`answer3`  		VARCHAR(100) NOT NULL,"+
        "`answer4`  		VARCHAR(100) NOT NULL,"+
        "`rightanswer1`	    INTEGER CHECK (rightanswer1 IN (0,20)),"+
        "`rightanswer2`	    INTEGER CHECK (rightanswer2 IN (0,20)),"+
        "`rightanswer3`	    INTEGER CHECK (rightanswer3 IN (0,20)),"+
        "`rightanswer4`	    INTEGER CHECK (rightanswer4 IN (0,20)),"+
        "FOREIGN KEY (`qcid`) REFERENCES QuestionCatalog(`qcid`)"+
        ");";

        db.execSQL("DROP TABLE IF EXISTS " + "QuestionCatalog");
        db.execSQL("DROP TABLE IF EXISTS " + "Question");

        db.execSQL(CREATE_QUESTIONCATALOG_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase dataBase){
        QuestionCatalog questionCatalog1 = new QuestionCatalog(1, "Time", null);
        QuestionCatalog questionCatalog2 = new QuestionCatalog(2,"General", null);

        QuestionCatalogDAO questionCatalogDAO1 = new QuestionCatalogDAO(questionCatalog1);
        questionCatalogDAO1.insertThisAsInitialBaselineIntoDatabase(dataBase);

        QuestionCatalogDAO questionCatalogDAO2 = new QuestionCatalogDAO(questionCatalog2);
        questionCatalogDAO2.insertThisAsInitialBaselineIntoDatabase(dataBase);

        Answer answer1;
        Answer answer2;
        Answer answer3;
        Answer answer4;

        answer1 = new Answer("Tina Turner", 20);
        answer2 = new Answer("Michael Jackson", 0);
        answer3 = new Answer("Michael Jackson", 0);
        answer4 = new Answer("Michael Jackson", 0);
        TextQuestion textQuestion1 = new TextQuestion(1,"Which singer joined Mel Gibson in the movie Mad Max: Beyond The Thunderdome?", answer1, answer2, answer3, answer4, 1, 2);

        answer1 = new Answer("Pina Colada", 0);
        answer2 = new Answer("Zombie", 0);
        answer3 = new Answer("Manhatten", 0);
        answer4 = new Answer("Harvey Wallbanger", 20);
        TextQuestion textQuestion2 = new TextQuestion(2,"Vodka, Galliano and orange juice are used to make which classic cocktail?", answer1, answer2, answer3, answer4, 2, 2);


        answer1 = new Answer("1966", 0);
        answer2 = new Answer("1967", 20);
        answer3 = new Answer("1968", 0);
        answer4 = new Answer("1969", 0);
        TextQuestion textQuestion3 = new TextQuestion(3,"In which year did Foinavon win the Grand National?", answer1, answer2, answer3, answer4, 3, 2);

        answer1 = new Answer("Peter Tosh", 0);
        answer2 = new Answer("Lee Perry", 0);
        answer3 = new Answer("Bob Marley", 20);
        answer4 = new Answer("Shaggy", 0);
        TextQuestion textQuestion4 = new TextQuestion(4,"Which reggae singing star died 11th May 1981?", answer1, answer2, answer3, answer4, 1, 1);

        answer1 = new Answer("1960", 20);
        answer2 = new Answer("1969", 0);
        answer3 = new Answer("1971", 0);
        answer4 = new Answer("1988", 0);
        TextQuestion textQuestion5 = new TextQuestion(5,"In what year was Prince Andrew born?", answer1, answer2, answer3, answer4, 2, 1);

        answer1 = new Answer("1966", 0);
        answer2 = new Answer("1967", 20);
        answer3 = new Answer("1968", 0);
        answer4 = new Answer("1969", 0);
        TextQuestion textQuestion6 = new TextQuestion(6,"In what year was Prince Andrew born?", answer1, answer2, answer3, answer4, 3, 1);

        TextQuestionDAO textQuestionDAO1 = new TextQuestionDAO(textQuestion1);
        textQuestionDAO1.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO2 = new TextQuestionDAO(textQuestion2);
        textQuestionDAO2.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO3 = new TextQuestionDAO(textQuestion3);
        textQuestionDAO3.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO4 = new TextQuestionDAO(textQuestion4);
        textQuestionDAO4.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO5 = new TextQuestionDAO(textQuestion5);
        textQuestionDAO5.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO6 = new TextQuestionDAO(textQuestion6);
        textQuestionDAO6.insertThisAsInitialBaselineIntoDatabase(dataBase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + "QuestionCatalog");
        //db.execSQL("DROP TABLE IF EXISTS " + "Question");

        onCreate(db);
    }
}