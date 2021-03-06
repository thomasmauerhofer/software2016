package com.bitschupfa.sw16.yaq.database.object;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QuestionCatalog extends RealmObject implements Serializable {
    @PrimaryKey
    private int catalogID;

    private int difficulty;
    private String name;
    private RealmList<TextQuestion> textQuestionList = null;

    public QuestionCatalog() {
    }

    public QuestionCatalog(int catalogID, int difficulty, String name, RealmList<TextQuestion> textQuestionList) {
        this.catalogID = catalogID;
        this.difficulty = difficulty;
        this.name = name;
        this.textQuestionList = textQuestionList;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TextQuestion> getTextQuestionList() {
        return textQuestionList;
    }

    public void setTextQuestionList(RealmList<TextQuestion> textQuestionList) {
        this.textQuestionList = textQuestionList;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
