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

import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

public class CatalogImporter {
    int questionCatalogId = 0;
    private Activity activity;
    private static final int READ_REQUEST_CODE = 42;

    public CatalogImporter(Activity activity) {
        this.activity = activity;
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

    public void readFile(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        readFile(br);
    }

    private void readFile(BufferedReader br) throws IOException {
        Log.e("Import","Input ging");
        try {
            readCatalog(br);

            String line = br.readLine();
            while (line != null) {
                readQuestion(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }
    }

    public void readFile(Uri uri) throws IOException {
        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        readFile(br);
    }

    public void importFile() {
        if(!isStoragePermissionGranted()){
            Snackbar.make(activity.getCurrentFocus(), "No permission for reading storage", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            performFileSearch();
        }
    }

    private void readCatalog(BufferedReader reader) throws IOException {
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

        QuestionCatalog questionCatalog1 = new QuestionCatalog(0, difficulty, questionCatalogName, null);
        QuestionCatalogDAO questionCatalogDAO = new QuestionCatalogDAO(questionCatalog1);
        questionCatalogDAO.insertIntoDatabase(activity);
        boolean catalogFound = false;

        QuestionQuerier questionQuerier = new QuestionQuerier(activity);
        List<QuestionCatalog> questionCatalogs = questionQuerier.getAllQuestionCatalogsOnlyIdAndName();
        for (QuestionCatalog questionCatalog : questionCatalogs) {
            if (questionCatalog.getName().equals(questionCatalogName)) {
                questionCatalogId = questionCatalog.getCatalogID();
                catalogFound = true;
            }
        }

        if (!catalogFound) {
            throw new IOException();
        }
    }

    private void readQuestion(String line) throws IOException {
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

        TextQuestion textQuestion = new TextQuestion(0, question, answer1, answer2, answer3, answer4, questionCatalogId);
        TextQuestionDAO textQuestionDAO = new TextQuestionDAO(textQuestion);
        textQuestionDAO.insertIntoDatabase(activity);
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