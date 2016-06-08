package com.bitschupfa.sw16.yaq.ui;

import android.content.Context;

import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;

public class QuestionCatalogItem {
    private QuestionCatalog catalog;
    private boolean checked;

    public QuestionCatalogItem(QuestionCatalog catalog, boolean checked, Context context) {
        this.catalog = catalog;
        this.checked = checked;
    }

    public QuestionCatalog getCatalog() {
        return catalog;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
