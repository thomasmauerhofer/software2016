package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.EditQuestions;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.ManageQuestions;
import com.bitschupfa.sw16.yaq.activities.ShowQuestions;
import com.robotium.solo.Solo;

public class ManageQuestionsTest extends ActivityInstrumentationTestCase2<ManageQuestions> {
    private Solo solo;
    private String catalog = "testCatalog";
    private String editCatalog = "testEditCatalog";
    private String question = "testQuestion";
    private String editQuestion = "testEditQuestion";

    public ManageQuestionsTest() {
        super(ManageQuestions.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
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

        addCatalog();

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
        addCatalog();
        solo.clickOnText(catalog);
        assertTrue("Wrong Activity!", solo.waitForActivity(ShowQuestions.class));
        solo.clickOnView(getActivity().findViewById(R.id.fab));
        assertTrue("Wrong Activity!", solo.waitForActivity(EditQuestions.class));
        EditText editText = (EditText) solo.getView(R.id.question);
        solo.enterText(editText, question);
        solo.clickOnButton(getActivity().getString(R.string.submit_quiz));
        assertTrue(solo.searchText(question));

        solo.clickLongOnText(question);
        solo.clickOnText(getActivity().getString(R.string.menu_edit));
        EditText editText1 = (EditText) solo.getView(R.id.question);
        solo.clearEditText(editText1);
        solo.enterText(editText1, editQuestion);
        solo.clickOnButton(getActivity().getString(R.string.submit_quiz));
        assertTrue(solo.searchText(editQuestion));

        solo.clickLongOnText(editQuestion);
        solo.clickOnText(getActivity().getString(R.string.menu_delete));
        assertFalse(solo.searchText(editQuestion));

        solo.goBack();
        solo.clickLongOnText(catalog);
        solo.clickOnText(getActivity().getString(R.string.menu_delete));
        assertFalse(solo.searchText(catalog));
    }
}
