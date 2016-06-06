package com.bitschupfa.sw16.yaq.Utils;


import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.utils.QuizBuilder;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QuizBuilderTest {
    @Before
    public void setUp() throws Exception {
        Answer answer = new Answer("aaa", 0);

        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));

        QuizBuilder.instance().addQuestions("firstCatalog", questions);
        QuizBuilder.instance().addQuestions("secondCatalog", questions);
    }

    @After
    public void tearDown() throws Exception {
        QuizBuilder.instance().clearQuiz();
    }

    @Test
    public void testAddQuestions() {
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 2);
        addNewCatalog();
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 5);
    }

    @Test
    public void testClearQuiz() {
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 2);
        QuizBuilder.instance().clearQuiz();
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 0);
        Assert.assertFalse(QuizBuilder.instance().isCatalogUsed("firstCatalog"));
    }

    @Test
    public void testCreateNewQuiz() {
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 2);
        addNewCatalog();
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 5);
        QuizBuilder.instance().setNumberOfQuestions(3);
        Assert.assertEquals(QuizBuilder.instance().createNewQuiz().getNumberOfQuestions(), 3);
    }

    @Test
    public void testGetNumberOfQuestions() {
        Assert.assertEquals(QuizBuilder.instance().getSmallestNumberOfQuestions(), 2);
        Assert.assertEquals(QuizBuilder.instance().getNumberOfQuestions(), 10);
        QuizBuilder.instance().setNumberOfQuestions(3);
        Assert.assertEquals(QuizBuilder.instance().getSmallestNumberOfQuestions(), 2);
        Assert.assertEquals(QuizBuilder.instance().getNumberOfQuestions(), 3);
    }

    @Test
    public void testIsCatalogUsed() {
        Assert.assertTrue(QuizBuilder.instance().isCatalogUsed("firstCatalog"));
        Assert.assertFalse(QuizBuilder.instance().isCatalogUsed("anotherCatalog"));
    }

    private void addNewCatalog() {
        Answer answer = new Answer("aaa", 0);
        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));

        QuizBuilder.instance().addQuestions("thirdCatalog", questions);
    }
}
