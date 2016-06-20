package com.bitschupfa.sw16.yaq.database.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmList;

public class CatalogImporter {
    int questionCatalogId = 0;
    private Activity activity;
    private static final int READ_REQUEST_CODE = 42;
    private Realm realm;
    private QuestionQuerier questionQuerier;

    public CatalogImporter(Activity activity, Realm realm) {
        this.activity = activity;
        this.realm = realm;
        this.questionQuerier = new QuestionQuerier(realm);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("ERROR","Permission is granted");
                return true;
            } else {
                Log.d("ERROR","Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d("ERROR","Permission is granted");
            return true;
        }
    }

    public QuestionCatalog readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            QuestionCatalog catalog = readCatalog(br);
            RealmList<TextQuestion> questions = new RealmList<>();

            String line = br.readLine();
            int questionId = questionQuerier.getHighestQuestionId() + 1;
            while (line != null) {
                try {
                    TextQuestion question = readQuestion(line);
                    question.setQuestionID(++questionId);
                    questions.add(question);
                } catch (IOException e) {
                    Log.e(CatalogImporter.class.getCanonicalName(), "Import of question failed. Line: " + line);
                }
                line = br.readLine();
            }
            br.close();
            catalog.setTextQuestionList(questions);
            realm.beginTransaction();
            realm.copyToRealm(catalog);
            realm.commitTransaction();
            return catalog;
        } catch (Exception e) {
            br.close();
            Log.e(CatalogImporter.class.getCanonicalName(), "Import of catalog failed. Error: " + e.getMessage());
            return null;
        }
    }

    public void readFile(Uri uri) throws IOException {
        File file = new File(uri.getPath());
        readFile(file);
    }

    public void importFile() {
        if(!isStoragePermissionGranted()){
            Snackbar.make(activity.getCurrentFocus(), "No permission for reading storage", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            performFileSearch();
        }
    }

    private QuestionCatalog readCatalog(BufferedReader reader) throws IOException {
        String questionCatalogName = reader.readLine();
        if (questionCatalogName == null) {
            throw new IOException();
        }

        String difficultyString = reader.readLine();

        int difficulty;
        try {
            difficulty = Integer.parseInt(difficultyString);
        } catch (NumberFormatException e) {
            throw new IOException();
        }

        int catalogID = questionQuerier.getHighestCatalogId() + 1;

        return new QuestionCatalog(catalogID, difficulty, questionCatalogName, null);
    }

    private TextQuestion readQuestion(String line) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(line, ";");

        String question = stringTokenizer.nextToken();
        String answer1String = stringTokenizer.nextToken();
        String answer2String = stringTokenizer.nextToken();
        String answer3String = stringTokenizer.nextToken();
        String answer4String = stringTokenizer.nextToken();

        String answer1ValueString = stringTokenizer.nextToken();
        String answer2ValueString = stringTokenizer.nextToken();
        String answer3ValueString = stringTokenizer.nextToken();
        String answer4ValueString = stringTokenizer.nextToken();

        int answer1Value, answer2Value, answer3Value, answer4Value;
        try {
            answer1Value = Integer.parseInt(answer1ValueString);
            answer2Value = Integer.parseInt(answer2ValueString);
            answer3Value = Integer.parseInt(answer3ValueString);
            answer4Value = Integer.parseInt(answer4ValueString);
        } catch (NumberFormatException e) {
            throw new IOException();
        }

        Answer answer1 = new Answer(answer1String, answer1Value);
        Answer answer2 = new Answer(answer2String, answer2Value);
        Answer answer3 = new Answer(answer3String, answer3Value);
        Answer answer4 = new Answer(answer4String, answer4Value);

        return new TextQuestion(0, question, answer1, answer2, answer3, answer4, questionCatalogId);

    }

    public void performFileSearch() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text/plain");

        activity.startActivityForResult(intent, READ_REQUEST_CODE);
    }
}