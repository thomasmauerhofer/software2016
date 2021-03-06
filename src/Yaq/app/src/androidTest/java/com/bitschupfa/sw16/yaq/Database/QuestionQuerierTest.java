package com.bitschupfa.sw16.yaq.Database;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class QuestionQuerierTest extends AndroidTestCase {
    private QuestionQuerier questionQuerier;
    private Realm realm;

    public QuestionQuerierTest() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RealmConfiguration config = new RealmConfiguration.Builder(getContext())
                .name("yaq.realm")
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        questionQuerier = new QuestionQuerier(realm);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        realm.close();
    }

    public void testAllQuestionsFromCatalog() {
        List<QuestionCatalog> catalogs = questionQuerier.getAllQuestionCatalogs();
        Log.e("catalogs", "catalog size: " + catalogs.size());
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalog(catalogs.get(0).getCatalogID());
        testAllQuestionsFromCatalogGeneric(textQuestionList, null, null);
    }

    public void testAllQuestionsFromCatalogGeneric(List<TextQuestion> textQuestionList, Integer referenceCatalogID, Integer referenceDifficulty) {
        assertTrue("Should be more than zero elements", (textQuestionList != null && textQuestionList.size() > 0));

        for (TextQuestion textQuestion : textQuestionList) {
            String question = textQuestion.getQuestion();
            int catalogID = textQuestion.getCatalogID();
            List<Answer> answers = textQuestion.getAnswers();

            assertNotNull("Question shouldn't be null", question);
            assertTrue("Question shouldn't be empty", !question.equals(""));

            if (referenceCatalogID != null) {
                assertTrue("CatalogID should be same as referenceCatalogID", referenceCatalogID == catalogID);
            }

            for(Answer answer : answers){
                int answerValue = answer.getAnswerValue();

                assertTrue("Is right answer should be between -10 and 10", answerValue >= -10);
            }
        }
    }

    public void testGetQuestionCatalogGeneric(List<QuestionCatalog> questionCatalogList, boolean onlyNameAndID, Integer referenceCatalogID, Integer referenceDifficulty) {
        assertTrue("Should be more than zero elements", questionCatalogList.size() > 0);

        if (!onlyNameAndID) {
            for (QuestionCatalog questionCatalog : questionCatalogList) {

                List<TextQuestion> textQuestionList = questionCatalog.getTextQuestionList();
                testAllQuestionsFromCatalogGeneric(textQuestionList, referenceCatalogID, referenceDifficulty);

                assertNotNull("Name shouldn't be null", questionCatalog.getName());
                assertTrue("Name shouldn't be empty", !questionCatalog.getName().equals(""));
            }
        }
    }

    public void testGetAllQuestionCatalogsGeneric(boolean onlyNameAndID) {
        List<QuestionCatalog> questionCatalogList = questionQuerier.getAllQuestionCatalogs();
        testGetQuestionCatalogGeneric(questionCatalogList, onlyNameAndID, null, null);
    }

    public void testGetAllQuestionCatalogs() {
        testGetAllQuestionCatalogsGeneric(false);
    }

    public void testGetAllQuestionCatalogsOnlyIdAndName() {
        testGetAllQuestionCatalogsGeneric(true);
    }

    public void testGetQuestionsCatalogById1() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(1);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 1, null);
    }

    public void testGetQuestionsCatalogById2() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 2, null);
    }

    public void testGetQuestionsCatalogById1AndDifficulty1() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 2, 1);
    }

    public void testGetQuestionsCatalogById1AndDifficulty2() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 2, 2);
    }


    public void testGetQuestionsCatalogById2AndDifficulty1() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 2, 1);
    }

    public void testGetQuestionsCatalogById2AndDifficulty2() {
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, 2, 2);
    }

}
