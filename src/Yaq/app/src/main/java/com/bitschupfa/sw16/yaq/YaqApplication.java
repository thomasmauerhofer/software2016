package com.bitschupfa.sw16.yaq;

import android.app.Application;

import com.bitschupfa.sw16.yaq.database.helper.CatalogImporter;
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
    }
}
