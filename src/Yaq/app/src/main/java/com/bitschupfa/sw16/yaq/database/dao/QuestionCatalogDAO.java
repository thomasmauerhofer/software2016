package com.bitschupfa.sw16.yaq.database.dao;

import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;

/**
 * Created by Patrik on 05.05.2016.
 */
public class QuestionCatalogDAO extends DatabaseObject {
    public static String TABLE_NAME = "QuestionCatalog";
    public static final String QUESTIONCATALOG_ID = "qcid";
    public static final String QUESTIONCATALOG_DIFFICULTY = "qcdiff";
    public static final String QUESTIONCATALOG_DESCRIPTION = "description";
    public static final String[] QUESTIONCATALOG_ALL_COLUMN_NAMES = {QUESTIONCATALOG_ID, QUESTIONCATALOG_DIFFICULTY, QUESTIONCATALOG_DESCRIPTION};

    QuestionCatalog questionCatalog;

    public QuestionCatalogDAO(QuestionCatalog questionCatalog) {
        super();
        this.questionCatalog = questionCatalog;
        tableName = TABLE_NAME;
    }

    @Override
    protected void fillDatabaseContentValues(boolean initial) {
        if(initial){
            contentValues.put(QUESTIONCATALOG_ID, questionCatalog.getCatalogID());
        }

        contentValues.put(QUESTIONCATALOG_DESCRIPTION, questionCatalog.getName());
        contentValues.put(QUESTIONCATALOG_DIFFICULTY, questionCatalog.getDifficulty());
    }
}
