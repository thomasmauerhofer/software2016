package com.bitschupfa.sw16.yaq.ui;


import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.MainMenu;
import com.bitschupfa.sw16.yaq.database.helper.CatalogImporter;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.robotium.solo.Solo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import io.realm.Realm;

public class CatalogImporterTest extends ActivityInstrumentationTestCase2<MainMenu> {

    private Solo solo;
    private Realm realm;

    public CatalogImporterTest() {
        super(MainMenu.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void tearDown() throws Exception {
        realm.close();
        super.tearDown();
    }

    public void testFileUpload() throws IOException {
        AssetManager am = getActivity().getBaseContext().getAssets();
        InputStream inputStream = am.open("testQuestions.txt");
        File file = createFileFromInputStream(inputStream);
        CatalogImporter importer = new CatalogImporter(getActivity(), realm);
        QuestionCatalog importedCatalog = importer.readFile(file);
        QuestionCatalog savedCatalog = realm.where(QuestionCatalog.class)
                .equalTo("catalogID", importedCatalog.getCatalogID()).findFirst();

        assertNotNull("Imported catalog should not be null", importedCatalog);
        assertNotNull("Saved catalog should not be null", savedCatalog);
        assertEquals("Catalog name should be equal", "TestCatalog", savedCatalog.getName());
        assertEquals("Catalog difficulty should be equal", 1, savedCatalog.getDifficulty());

        List<TextQuestion> textQuestionList = savedCatalog.getTextQuestionList();
        for(TextQuestion textQuestion : textQuestionList){
            Log.d("TextQuestion: ", "CatalogID   : " + textQuestion.getCatalogID());
            Log.d("TextQuestion: ", "QuestionID : " + textQuestion.getQuestionID());
            Log.d("TextQuestion: ", "Question   : " + textQuestion.getQuestion());
        }

        assertTrue("Catalog should have two questions", textQuestionList.size() >= 2 && textQuestionList.size() % 2 == 0);

        TextQuestion textQuestion1 = textQuestionList.get(0);
        TextQuestion textQuestion2 = textQuestionList.get(1);

        assertEquals("Question should be equal", "Test 1?", textQuestion1.getQuestion());
        assertEquals("Question should be equal", "Test 2?", textQuestion2.getQuestion());


        assertTrue("Question should have four answers", textQuestion1.getAnswers().size() == 4);
        Answer answer1 = textQuestion1.getAnswers().get(0);
        Answer answer2 = textQuestion1.getAnswers().get(1);
        Answer answer3 = textQuestion1.getAnswers().get(2);
        Answer answer4 = textQuestion1.getAnswers().get(3);
        assertEquals("Answer values should be equal", -10, answer1.getAnswerValue());
        assertEquals("Answer values should be equal", -8, answer2.getAnswerValue());
        assertEquals("Answer values should be equal", -7, answer3.getAnswerValue());
        assertEquals("Answer values should be equal", 10, answer4.getAnswerValue());

        assertTrue("Question should have four answers", textQuestion2.getAnswers().size() == 4);
        answer1 = textQuestion2.getAnswers().get(0);
        answer2 = textQuestion2.getAnswers().get(1);
        answer3 = textQuestion2.getAnswers().get(2);
        answer4 = textQuestion2.getAnswers().get(3);
        assertEquals("Answer values should be equal", -9, answer1.getAnswerValue());
        assertEquals("Answer values should be equal", 0, answer2.getAnswerValue());
        assertEquals("Answer values should be equal", -5, answer3.getAnswerValue());
        assertEquals("Answer values should be equal", 3, answer4.getAnswerValue());

        realm.beginTransaction();
        savedCatalog.deleteFromRealm();
        realm.commitTransaction();
    }


    private File createFileFromInputStream(InputStream inputStream) {
        try{
            File f = new File(getActivity().getFilesDir(), "testCatalog");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            Log.e("Error", e.getMessage());
        }

        return null;
    }
}