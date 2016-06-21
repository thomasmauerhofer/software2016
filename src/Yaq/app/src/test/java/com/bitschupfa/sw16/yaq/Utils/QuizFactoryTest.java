package com.bitschupfa.sw16.yaq.Utils;


import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QuizFactoryTest {
    @Before
    public void setUp() throws Exception {
        Answer answer = new Answer("aaa", 0);

        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));

        QuizFactory.instance().addQuestions("firstCatalog", questions);
        QuizFactory.instance().addQuestions("secondCatalog", questions);
    }

    @After
    public void tearDown() throws Exception {
        QuizFactory.instance().clearQuiz();
        QuizFactory.instance().setNumberOfQuestions(10);
    }

    @Test
    public void testAddQuestions() {
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 2);
        addNewCatalog();
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 5);
    }

    @Test
    public void testClearQuiz() {
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 2);
        QuizFactory.instance().clearQuiz();
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 0);
        Assert.assertFalse(QuizFactory.instance().isCatalogUsed("firstCatalog"));
    }

    @Test
    public void testCreateNewQuiz() {
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 2);
        addNewCatalog();
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 5);
        QuizFactory.instance().setNumberOfQuestions(3);
        Assert.assertEquals(QuizFactory.instance().createNewQuiz().getNumberOfQuestions(), 3);
    }

    @Test
    public void testGetNumberOfQuestions() {
        Assert.assertEquals(QuizFactory.instance().getSmallestNumberOfQuestions(), 2);
        Assert.assertEquals(QuizFactory.instance().getNumberOfQuestions(), 10);
        QuizFactory.instance().setNumberOfQuestions(3);
        Assert.assertEquals(QuizFactory.instance().getSmallestNumberOfQuestions(), 2);
        Assert.assertEquals(QuizFactory.instance().getNumberOfQuestions(), 3);
    }

    @Test
    public void testIsCatalogUsed() {
        Assert.assertTrue(QuizFactory.instance().isCatalogUsed("firstCatalog"));
        Assert.assertFalse(QuizFactory.instance().isCatalogUsed("anotherCatalog"));
    }

    private void addNewCatalog() {
        Answer answer = new Answer("aaa", 0);
        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));

        QuizFactory.instance().addQuestions("thirdCatalog", questions);
    }
}
