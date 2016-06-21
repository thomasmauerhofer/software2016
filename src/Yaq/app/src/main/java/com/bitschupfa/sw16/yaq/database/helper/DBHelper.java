package com.bitschupfa.sw16.yaq.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;


public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "yaq.db";
    private static final int DATABASE_VERSION = 38;
    private static DBHelper instance_ = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper instance(Context myContext) {

        if (instance_ == null) {
            instance_ = new DBHelper(myContext);
        }
        return instance_;
    }

    public SQLiteDatabase getDatabase() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getInsertionDatabase() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONCATALOG_TABLE = "CREATE TABLE QuestionCatalog ( " +
                "`qcid`             INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`qcdiff`           INTEGER NOT NULL," +
                "`description` 	    VARCHAR(250) NOT NULL);";

        String CREATE_QUESTION_TABLE = "CREATE TABLE Question ( " +
                "`qid`   			INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`qcid`  			BIGINT UNSIGNED NOT NULL," +
                "`question`  		VARCHAR(250) NOT NULL," +
                "`answer1`  		VARCHAR(100) NOT NULL," +
                "`answer2`  		VARCHAR(100) NOT NULL," +
                "`answer3`  		VARCHAR(100) NOT NULL," +
                "`answer4`  		VARCHAR(100) NOT NULL," +
                "`value1`   	    INTEGER," +
                "`value2`   	    INTEGER," +
                "`value3`	        INTEGER," +
                "`value4`	        INTEGER," +
                "FOREIGN KEY (`qcid`) REFERENCES QuestionCatalog(`qcid`)" +
                ");";

        db.execSQL("DROP TABLE IF EXISTS " + "QuestionCatalog");
        db.execSQL("DROP TABLE IF EXISTS " + "Question");

        db.execSQL(CREATE_QUESTIONCATALOG_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase dataBase) {

        QuestionCatalog questionCatalog1 = new QuestionCatalog(1, 3, "TV/Movie", null);

        QuestionCatalogDAO questionCatalogDAO1 = new QuestionCatalogDAO(questionCatalog1);
        questionCatalogDAO1.insertThisAsInitialBaselineIntoDatabase(dataBase);

        Answer answer1;
        Answer answer2;
        Answer answer3;
        Answer answer4;

        answer1 = new Answer("1", 0);
        answer2 = new Answer("5", 0);
        answer3 = new Answer("10", 0);
        answer4 = new Answer("11", 10);
        TextQuestion textQuestion1 = new TextQuestion(1, "How many oscars did the Titanic movie got?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("1", 0);
        answer2 = new Answer("2", 10);
        answer3 = new Answer("3", 0);
        answer4 = new Answer("4", 0);
        TextQuestion textQuestion2 = new TextQuestion(2, "How many Tomb Raider movies were made?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("Bastille", 0);
        answer2 = new Answer("Alcatraz", 10);
        answer3 = new Answer("Newgate", 0);
        answer4 = new Answer("Tower of London", 0);
        TextQuestion textQuestion3 = new TextQuestion(3, "What is the name of the prison in the film The Rock?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("Mushu", 10);
        answer2 = new Answer("Mishu", 0);
        answer3 = new Answer("Masha", 0);
        answer4 = new Answer("Sasha", 0);
        TextQuestion textQuestion4 = new TextQuestion(4, "What is the name of the little dragon in the animated movie Mulan?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("Rob Bowman", 10);
        answer2 = new Answer("Arnold Schwarzenegger", 0);
        answer3 = new Answer("Sergio Leone", 0);
        answer4 = new Answer("Antonio Salieri", 0);
        TextQuestion textQuestion5 = new TextQuestion(5, "Who is the director of the X-files?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("Seaman,", 10);
        answer2 = new Answer("Engineer", -10);
        answer3 = new Answer("Farmer", 0);
        answer4 = new Answer("Electrician", 0);
        TextQuestion textQuestion6 = new TextQuestion(6, "What is the profession of Popeye?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("0815", 0);
        answer2 = new Answer("742", 10);
        answer3 = new Answer("1001", 5);
        answer4 = new Answer("5", 0);
        TextQuestion textQuestion7 = new TextQuestion(7, "What is the house number of the Simpsons?", answer1, answer2, answer3, answer4, 1);

        answer1 = new Answer("Captain Picard", 0);
        answer2 = new Answer("Antonio Salieri", 10);
        answer3 = new Answer("Robert Redford", 5);
        answer4 = new Answer("Colin Farell", 0);
        TextQuestion textQuestion8 = new TextQuestion(8, "Who was Mozart s great rival in Amadeus movie?", answer1, answer2, answer3, answer4, 1);
        
        answer1 = new Answer("Robin Williams", 10);
        answer2 = new Answer("Robert Redford", 0);
        answer3 = new Answer("Rob Bowman", 0);
        answer4 = new Answer("Woody Allen", 0);
        TextQuestion textQuestion9 = new TextQuestion(9, "Who did play the role of Peter Pan in the Peter Pan movie?", answer1, answer2, answer3, answer4, 1);

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

        TextQuestionDAO textQuestionDAO7 = new TextQuestionDAO(textQuestion7);
        textQuestionDAO7.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO8 = new TextQuestionDAO(textQuestion8);
        textQuestionDAO8.insertThisAsInitialBaselineIntoDatabase(dataBase);

        TextQuestionDAO textQuestionDAO9 = new TextQuestionDAO(textQuestion9);
        textQuestionDAO9.insertThisAsInitialBaselineIntoDatabase(dataBase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + "QuestionCatalog");
        //db.execSQL("DROP TABLE IF EXISTS " + "Question");

        onCreate(db);
    }
}