package com.bitschupfa.sw16.yaq;

import android.app.Application;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class YaqApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("yaq.realm")
                .build();
        Realm.setDefaultConfiguration(config);

        fillDemoDatabase();
    }

    public void fillDemoDatabase() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(QuestionCatalog.class).findAll().size() == 0 ) {
            Answer answer1 = new Answer("Tina Turner", 10);
            Answer answer2 = new Answer("Michael Jackson", 0);
            Answer answer3 = new Answer("Michael Jackson", 0);
            Answer answer4 = new Answer("Michael Jackson", 0);
            TextQuestion textQuestion1 = new TextQuestion(1, "Which singer joined Mel Gibson in the movie Mad Max: Beyond The Thunderdome?", answer1, answer2, answer3, answer4, 2);

            Answer answer21 = new Answer("Pina Colada", 0);
            Answer answer22 = new Answer("Zombie", 0);
            Answer answer23 = new Answer("Manhatten", 0);
            Answer answer24 = new Answer("Harvey Wallbanger", 10);
            TextQuestion textQuestion2 = new TextQuestion(2, "Vodka, Galliano and orange juice are used to make which classic cocktail?", answer21, answer22, answer23, answer24, 2);

            Answer answer31 = new Answer("1966", 0);
            Answer answer32 = new Answer("1967", 10);
            Answer answer33 = new Answer("1968", 0);
            Answer answer34 = new Answer("1969", 0);
            TextQuestion textQuestion3 = new TextQuestion(3, "In which year did Foinavon win the Grand National?", answer31, answer32, answer33, answer34, 2);

            Answer answer41 = new Answer("Peter Tosh", 0);
            Answer answer42 = new Answer("Lee Perry", 0);
            Answer answer43 = new Answer("Bob Marley", 10);
            Answer answer44 = new Answer("Shaggy", 0);
            TextQuestion textQuestion4 = new TextQuestion(4, "Which reggae singing star died 11th May 1981?", answer41, answer42, answer43, answer44, 1);

            Answer answer51 = new Answer("1960", 10);
            Answer answer52 = new Answer("1969", 0);
            Answer answer53 = new Answer("1971", 0);
            Answer answer54 = new Answer("1988", 0);
            TextQuestion textQuestion5 = new TextQuestion(5, "In what year was Prince Andrew born?", answer51, answer52, answer53, answer54, 1);

            Answer answer61 = new Answer("1966", 0);
            Answer answer62 = new Answer("1967", 10);
            Answer answer63 = new Answer("1968", 0);
            Answer answer64 = new Answer("1969", 0);
            TextQuestion textQuestion6 = new TextQuestion(6, "In what year was Prince Andrew born?", answer61, answer62, answer63, answer64, 1);

            RealmList<TextQuestion> questionList1 = new RealmList<>();
            RealmList<TextQuestion> questionList2 = new RealmList<>();

            questionList1.add(textQuestion1);
            questionList1.add(textQuestion2);
            questionList1.add(textQuestion3);

            questionList2.add(textQuestion4);
            questionList2.add(textQuestion5);
            questionList2.add(textQuestion6);

            QuestionCatalog questionCatalog1 = new QuestionCatalog(1, 1, "Time", questionList1);
            QuestionCatalog questionCatalog2 = new QuestionCatalog(2, 2, "General", questionList2);

            realm.beginTransaction();
            realm.copyToRealm(questionCatalog1);
            realm.copyToRealm(questionCatalog2);
            realm.commitTransaction();
        }
        realm.close();
    }
}
