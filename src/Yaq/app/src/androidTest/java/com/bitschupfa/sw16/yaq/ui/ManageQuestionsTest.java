package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.EditQuestions;
import com.bitschupfa.sw16.yaq.activities.ManageQuestions;
import com.bitschupfa.sw16.yaq.activities.ShowQuestions;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.robotium.solo.Solo;

import io.realm.Realm;

public class ManageQuestionsTest extends ActivityInstrumentationTestCase2<ManageQuestions> {
    private Solo solo;
    private String catalog = "testCatalog";
    private String editCatalog = "testEditCatalog";
    private String question = "testQuestion";
    private String editQuestion = "testEditQuestion";
    private String answer = "testAnswer";

    public ManageQuestionsTest() {
        super(ManageQuestions.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());

        addCatalog();
    }

    @Override
    public void tearDown() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(QuestionCatalog.class).equalTo("name", catalog).findAll().deleteAllFromRealm();
        realm.where(TextQuestion.class).equalTo("question", question).findAll().deleteAllFromRealm();
        realm.where(Answer.class).equalTo("answer", answer).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        solo.finishOpenedActivities();
        super.tearDown();
    }

    private void addCatalog() {
        solo.clickOnActionBarItem(R.id.menu_manage);
        solo.clickOnView(getActivity().findViewById(R.id.fab));

        EditText editText = (EditText) solo.getView(R.id.editText);
        solo.enterText(editText, catalog);

        solo.clickOnButton("OK");

        assertTrue(solo.searchText(catalog));
    }

    public void testAddEditDeleteCatalog() {
        solo.clickLongOnText(catalog);
        solo.clickOnText(getActivity().getString(R.string.menu_edit));
        EditText editText1 = (EditText) solo.getView(R.id.editText);
        solo.clearEditText(editText1);
        solo.enterText(editText1, editCatalog);
        solo.clickOnButton("OK");
        assertTrue(solo.searchText(editCatalog));

        solo.clickLongOnText(editCatalog);
        solo.clickOnText(getActivity().getString(R.string.menu_delete));
        assertFalse(solo.searchText(editCatalog));
    }

    public void testAddEditDeleteQuestion() {
        solo.clickOnText(catalog);
        assertTrue("Wrong Activity!", solo.waitForActivity(ShowQuestions.class));
        solo.clickOnView(solo.getView(R.id.fab));
        assertTrue("Wrong Activity!", solo.waitForActivity(EditQuestions.class));
        EditText editText = (EditText) solo.getView(R.id.question);
        solo.enterText(editText, question);
        EditText answerText = (EditText) solo.getView(R.id.answer1);
        solo.enterText(answerText, answer);
        answerText = (EditText) solo.getView(R.id.answer2);
        solo.enterText(answerText, answer);
        answerText = (EditText) solo.getView(R.id.answer3);
        solo.enterText(answerText, answer);
        answerText = (EditText) solo.getView(R.id.answer4);
        solo.enterText(answerText, answer);
        solo.clickOnButton(getActivity().getString(R.string.submit_quiz));
        assertTrue("Wrong Activity!", solo.waitForActivity(ShowQuestions.class));
        Realm realm = Realm.getDefaultInstance();
        Log.e("test", "Size: " + realm.where(TextQuestion.class).findAll().size());
        TextQuestion tq = realm.where(TextQuestion.class).equalTo("question", question).findFirst();
        realm.close();

        assertNotNull("TextQuestion should not be null", tq);
        assertTrue("Question is not in list", solo.searchText(question));

        solo.clickLongOnText(question);
        solo.clickOnText(getActivity().getString(R.string.menu_edit));
        EditText editText1 = (EditText) solo.getView(R.id.question);
        solo.clearEditText(editText1);
        solo.enterText(editText1, editQuestion);
        solo.clickOnButton(getActivity().getString(R.string.submit_quiz));
        assertTrue("Question is not in list", solo.searchText(editQuestion));

        solo.clickLongOnText(editQuestion);
        solo.clickOnText(getActivity().getString(R.string.menu_delete));
        assertFalse("Question is not in list", solo.searchText(editQuestion));

        solo.goBack();
        solo.clickLongOnText(catalog);
        solo.clickOnText(getActivity().getString(R.string.menu_delete));

        assertTrue("Wrong Activity!", solo.waitForActivity(ManageQuestions.class));

        assertFalse("Catalog is not in list", solo.searchText(catalog));
    }
}
