package com.bitschupfa.sw16.yaq.database.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class CatalogImporter {
    int questionCatalogId = 0;
    private Activity activity;

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

    public void readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
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

    public void importFile() {
        if(!isStoragePermissionGranted()){
            Snackbar.make(activity.getCurrentFocus(), "No permission for reading storage", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            new FileChooser(activity).setFileListener(new FileChooser.FileSelectedListener() {
                @Override
                public void fileSelected(final File file) {
                    try {
                        readFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).showDialog();
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

}