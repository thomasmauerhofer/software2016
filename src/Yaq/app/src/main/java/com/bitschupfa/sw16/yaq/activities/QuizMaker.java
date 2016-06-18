package com.bitschupfa.sw16.yaq.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.helper.DBHelper;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.FileChooser;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class QuizMaker extends AppCompatActivity {
    int questionCatalogId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_maker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importFile(view);
            }
        });
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("ERROR","Permission is granted");
                return true;
            } else {

                Log.v("ERROR","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("ERROR","Permission is granted");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Error","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void importFile(View view) {

        if(!isStoragePermissionGranted()){
            Snackbar.make(view, "No permission for reading storage", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        new FileChooser(this).setFileListener(new FileChooser.FileSelectedListener() {
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

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private void readCatalog(BufferedReader reader) throws IOException {
        String questionCatalogName = reader.readLine();
        if (questionCatalogName == null) {
            throw new IOException();
        }

        String difficultyString = reader.readLine();
        if (!isInteger(difficultyString)) {
            throw new IOException();
        }
        int difficulty = Integer.parseInt(difficultyString);
        QuestionCatalog questionCatalog1 = new QuestionCatalog(0, difficulty, questionCatalogName, null);
        QuestionCatalogDAO questionCatalogDAO = new QuestionCatalogDAO(questionCatalog1);
        questionCatalogDAO.insertIntoDatabase(this);
        boolean catalogFound = false;

        QuestionQuerier questionQuerier = new QuestionQuerier(this);
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

        if (!isInteger(answer1ValueString) || !isInteger(answer2ValueString) ||
                !isInteger(answer3ValueString) || !isInteger(answer4ValueString)) {
            throw new IOException();
        }

        int answer1Value = Integer.parseInt(answer1ValueString);
        int answer2Value = Integer.parseInt(answer2ValueString);
        int answer3Value = Integer.parseInt(answer3ValueString);
        int answer4Value = Integer.parseInt(answer4ValueString);

        Answer answer1 = new Answer(answer1String, answer1Value);
        Answer answer2 = new Answer(answer2String, answer2Value);
        Answer answer3 = new Answer(answer3String, answer3Value);
        Answer answer4 = new Answer(answer4String, answer4Value);

        TextQuestion textQuestion = new TextQuestion(-3, question, answer1, answer2, answer3, answer4, questionCatalogId);
        TextQuestionDAO textQuestionDAO = new TextQuestionDAO(textQuestion);
        textQuestionDAO.insertIntoDatabase(this);
    }

}
