package com.bitschupfa.sw16.yaq.Database;

import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik on 10.04.2016.
 */
public class QuestionQuerierTest extends ActivityInstrumentationTestCase2<QuestionQuerier> {
    QuestionQuerier questionQuerier;

    public QuestionQuerierTest() {
        super(QuestionQuerier.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getInstrumentation().getContext(), "test");
        questionQuerier = new QuestionQuerier(context);
    }

    public void testAllQuestionsFromCatalog() {
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalog(1);
        testAllQuestionsFromCatalogGeneric(textQuestionList, null, null);
    }

    public void testAllQuestionsFromCatalogGeneric(List<TextQuestion> textQuestionList, Integer referenceCatalogID, Integer referenceDifficulty) {
        assertTrue("Should be more than one element", textQuestionList.size() > 0);

        for(TextQuestion textQuestion : textQuestionList){
            String question = textQuestion.getQuestion();
            int catalogID = textQuestion.getCatalogID();
            int difficulty = textQuestion.getDifficulty();
            List<Answer> answers = textQuestion.getCorrectAnswers();

            assertTrue("Question shouldn't be null", question != null);
            assertTrue("Question shouldn't be empty", question != "");
            assertTrue("Difficulty should be between 1 and 3", difficulty >= 1 && difficulty <= 3);

            if(referenceCatalogID != null){
                assertTrue("CatalogID should be same as referenceCatalogID", referenceCatalogID == catalogID);
            }

            if(referenceDifficulty != null){
                assertTrue("Difficulty should be same as referenceDifficulty", difficulty == referenceDifficulty.intValue());
            }


            for(Answer answer : answers){
                int answerValue = answer.isRightAnswer();
                assertTrue("Is right answer should be between -10 and 10", answerValue >= -10 && difficulty <= 10);
            }
        }
    }

    public void testGetQuestionCatalogGeneric(List<QuestionCatalog> questionCatalogList, boolean onlyNameAndID, Integer referenceCatalogID, Integer referenceDifficulty){
        assertTrue("Should be more than one element", questionCatalogList.size() > 0);

        if(!onlyNameAndID){
            for(QuestionCatalog questionCatalog : questionCatalogList){

                List<TextQuestion> textQuestionList = questionCatalog.getTextQuestionList();
                testAllQuestionsFromCatalogGeneric(textQuestionList, referenceCatalogID, referenceDifficulty);

                assertTrue("Name shouldn't be null", questionCatalog.getName() != null);
                assertTrue("Name shouldn't be empty", questionCatalog.getName() != "");
            }
        }
    }

    public void testGetAllQuestionCatalogsGeneric(boolean onlyNameAndID){
        List<QuestionCatalog> questionCatalogList = questionQuerier.getAllQuestionCatalogs();
        testGetQuestionCatalogGeneric(questionCatalogList, onlyNameAndID, null, null);
    }

    public void testGetAllQuestionCatalogs(){
        testGetAllQuestionCatalogsGeneric(false);
    }

    public void testGetAllQuestionCatalogsOnlyIdAndName(){
        testGetAllQuestionCatalogsGeneric(true);
    }

    public void testGetAllQuestionsFromCatalog1ByDifficulty1(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(1, 1);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(1), new Integer(1));
    }

    public void testGetAllQuestionsFromCatalog1ByDifficulty2(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(1,2);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(1), new Integer(2));
    }

    public void testGetAllQuestionsFromCatalog1ByDifficulty3(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(1,3);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(1), new Integer(3));
    }

    public void testGetAllQuestionsFromCatalog2ByDifficulty1(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(2,1);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(2), new Integer(1));
    }

    public void testGetAllQuestionsFromCatalog2ByDifficulty2(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(2,2);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(2), new Integer(2));
    }

    public void testGetAllQuestionsFromCatalog2ByDifficulty3(){
        List<TextQuestion> textQuestionList = questionQuerier.getAllQuestionsFromCatalogByDifficulty(2,3);
        testAllQuestionsFromCatalogGeneric(textQuestionList, new Integer(2), new Integer(3));

    }

    public void testGetQuestionsCatalogById1(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(1);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(1), null);
    }

    public void testGetQuestionsCatalogById2(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(2), null);
    }

    public void testGetQuestionsCatalogById1AndDifficulty1(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(2), new Integer(1));
    }

    public void testGetQuestionsCatalogById1AndDifficulty2(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(2), new Integer(2));
    }


    public void testGetQuestionsCatalogById2AndDifficulty1(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(2), new Integer(1));
    }

    public void testGetQuestionsCatalogById2AndDifficulty2(){
        QuestionCatalog questionCatalog = questionQuerier.getQuestionCatalogById(2);
        List<QuestionCatalog> questionCatalogList = new ArrayList<>();
        questionCatalogList.add(questionCatalog);
        testGetQuestionCatalogGeneric(questionCatalogList, true, new Integer(2), new Integer(2));
    }

}