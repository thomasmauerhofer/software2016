package com.bitschupfa.sw16.yaq.ui;


import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.bitschupfa.sw16.yaq.activities.QuizMaker;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
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

public class QuizMakerTest extends ActivityInstrumentationTestCase2<QuizMaker> {

    private Solo solo;

    public QuizMakerTest() {
        super(QuizMaker.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFileUpload() throws IOException {
        AssetManager am = getActivity().getBaseContext().getAssets();
        InputStream inputStream = am.open("testQuestions.txt");
        File file = createFileFromInputStream(inputStream);
        getActivity().readFile(file);
        QuestionQuerier questionQuerier = new QuestionQuerier(this.getActivity());
        List<QuestionCatalog> questionCatalogList = questionQuerier.getAllQuestionCatalogs();
        QuestionCatalog testQuestionCatalog = null;
        for(QuestionCatalog questionCatalog : questionCatalogList){
            if("TestCatalog".equals(questionCatalog.getName())){
                testQuestionCatalog = questionCatalog;
            }
        }

        assertNotNull("Catalog should not be null", testQuestionCatalog);
        assertEquals("Catalog name should be equal", "TestCatalog", testQuestionCatalog.getName());
        assertEquals("Catalog difficulty should be equal", 1, testQuestionCatalog.getDifficulty());

        List<TextQuestion> textQuestionList = testQuestionCatalog.getTextQuestionList();
        for(TextQuestion textQuestion : textQuestionList){
            Log.e("TextQuestion: ", "CatalogID   : " + textQuestion.getCatalogID());
            Log.e("TextQuestion: ", "QuestionID : " + textQuestion.getQuestionID());
            Log.e("TextQuestion: ", "Question   : " + textQuestion.getQuestion());
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
    }


    private File createFileFromInputStream(InputStream inputStream) {
        try{
            System.out.println("Line 0");
            Log.e("Error", "Line 0");
            File f = new File(getActivity().getFilesDir(), "testCatalog");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;
            System.out.println("Line 1");
            Log.e("Error", "Line 1");

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
                System.out.println("Line 2");
                Log.e("Error", "Line 2");
                Log.e("Error", buffer.toString());
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
            System.err.print(e.getStackTrace());
            Log.e("Error", e.getStackTrace().toString());
            Log.e("Error", e.getMessage().toString());
        }

        return null;
    }
}
