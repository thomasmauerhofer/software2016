package com.bitschupfa.sw16.yaq.ui;


import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.activities.QuizMaker;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.robotium.solo.Solo;

import java.io.File;
import java.io.IOException;
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
        File f = new File("file:///android_asset/testQuestions.txt");
        getActivity().readFile(f);
        QuestionQuerier questionQuerier = new QuestionQuerier(this.getActivity());
        List<QuestionCatalog> questionCatalogList = questionQuerier.getAllQuestionCatalogs();
        QuestionCatalog testQuestionCatalog = null;
        for(QuestionCatalog questionCatalog : questionCatalogList){
            if("TestCatalog".equals(questionCatalog.getName())){
                testQuestionCatalog = questionCatalog;
            }
        }

        assertNotNull("Catalog should not be null", testQuestionCatalog);
        List<TextQuestion> textQuestionList = testQuestionCatalog.getTextQuestionList();
        assertEquals("Catalog should have two questions", 2, textQuestionList.size());
    }
}
