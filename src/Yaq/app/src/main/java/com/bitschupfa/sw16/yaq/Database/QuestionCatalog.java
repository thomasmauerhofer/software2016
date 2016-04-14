package com.bitschupfa.sw16.yaq.Database;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Patrik on 01.04.2016.
 */
public class QuestionCatalog implements Serializable {
    private int catalogID;
    private String name;
    private List<TextQuestion> textQuestionList = null;
    private int counter;

    public QuestionCatalog(int catalogID, String name, List<TextQuestion> textQuestionList) {
        this.catalogID = catalogID;
        this.name = name;
        this.textQuestionList = textQuestionList;
        this.counter = 0;
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

    public void setTextQuestionList(List<TextQuestion> textQuestionList) {
        this.textQuestionList = textQuestionList;
    }
}
