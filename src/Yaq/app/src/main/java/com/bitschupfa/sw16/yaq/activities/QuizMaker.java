package com.bitschupfa.sw16.yaq.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
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
            String line = br.readLine();
            if (line == null) {
                throw new IOException();
            }
            readCatalogName(line);

            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    readQuestion(line);
                }
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
            public void fileSelected(final File file) throws IOException {
                readFile(file);
            }
        }).showDialog();
    }

    private void readCatalogName(String line) throws IOException {
        QuestionCatalog questionCatalog1 = new QuestionCatalog(0, 1, line, null);
        QuestionCatalogDAO questionCatalogDAO = new QuestionCatalogDAO(questionCatalog1);
        questionCatalogDAO.insertIntoDatabase(this);
        boolean catalogFound = false;

        QuestionQuerier questionQuerier = new QuestionQuerier(this);
        List<QuestionCatalog> questionCatalogs = questionQuerier.getAllQuestionCatalogsOnlyIdAndName();
        for (QuestionCatalog questionCatalog : questionCatalogs) {
            if (questionCatalog.getName().equals(line)) {
                questionCatalogId = questionCatalog.getCatalogID();
                catalogFound = true;
            }
        }

        if (!catalogFound) {
            throw new IOException();
        }

    }

    private void readQuestion(String line) {
        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

        String question = stringTokenizer.nextToken();
        String answer1String = stringTokenizer.nextToken();
        String answer2String = stringTokenizer.nextToken();
        String answer3String = stringTokenizer.nextToken();
        String answer4String = stringTokenizer.nextToken();

        int answer1Value = Integer.parseInt(stringTokenizer.nextToken());
        int answer2Value = Integer.parseInt(stringTokenizer.nextToken());
        int answer3Value = Integer.parseInt(stringTokenizer.nextToken());
        int answer4Value = Integer.parseInt(stringTokenizer.nextToken());

        Answer answer1 = new Answer(answer1String, answer1Value);
        Answer answer2 = new Answer(answer2String, answer2Value);
        Answer answer3 = new Answer(answer3String, answer3Value);
        Answer answer4 = new Answer(answer4String, answer4Value);

        TextQuestion textQuestion = new TextQuestion(0, question, answer1, answer2, answer3, answer4, questionCatalogId);
        TextQuestionDAO textQuestionDAO = new TextQuestionDAO(textQuestion);
        textQuestionDAO.insertIntoDatabase(this);
    }

}
