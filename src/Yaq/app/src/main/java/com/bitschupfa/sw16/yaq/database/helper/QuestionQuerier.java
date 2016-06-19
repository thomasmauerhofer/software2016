package com.bitschupfa.sw16.yaq.database.helper;

import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class QuestionQuerier {
    private final Realm realm;


    public QuestionQuerier(Realm realm) {
        this.realm = realm;
    }

    public List<TextQuestion> getAllQuestionsFromCatalog(int catalogID) {
        QuestionCatalog result = realm.where(QuestionCatalog.class)
                .equalTo("catalogID", catalogID)
                .findFirst();

        if (result != null) {
            return result.getTextQuestionList();
        } else {
            return null;
        }
    }

    public QuestionCatalog getQuestionCatalogById(int catalogId) {
        QuestionCatalog result = realm.where(QuestionCatalog.class)
                .equalTo("catalogID", catalogId)
                .findFirst();
        return result;
    }

    public List<QuestionCatalog> getAllQuestionCatalogs() {
        RealmResults<QuestionCatalog> results = realm.where(QuestionCatalog.class).findAll();
        return results;
    }
}
