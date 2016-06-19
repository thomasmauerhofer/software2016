package com.bitschupfa.sw16.yaq.Utils;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.BuildQuizAdapter;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class BuildQuizTest extends InstrumentationTestCase{

    private BuildQuizAdapter mAdapter;
    private QuestionCatalogItem item1;
    private QuestionCatalogItem item2;
    private ListView listView;

    @Before
    public void setUp() throws Exception {
        ArrayList<QuestionCatalogItem> data = new ArrayList<>();

        Answer answer = new Answer("aaa", 0);
        RealmList<TextQuestion> questions = new RealmList<>();
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));
        questions.add(new TextQuestion(42, "question", answer, answer, answer, answer, 0));

        Context context = new MockContext();
        QuestionCatalog catalog1 = new QuestionCatalog(42, 0, "question1", questions);
        QuestionCatalog catalog2 = new QuestionCatalog(42, 0, "question1", questions);

        item1 = new QuestionCatalogItem(catalog1, true, context);
        item2 = new QuestionCatalogItem(catalog2, false, context);
        data.add(item1);
        data.add(item2);
        mAdapter = new BuildQuizAdapter(context, 0, data, true, true, true);
    }

    @Test
    public void testGetItem() {
        Assert.assertEquals("question1 was expected.", item1.getCatalog().getName(), (mAdapter.getItem(0)).getCatalog().getName());
    }

    @Test
    public void testGetItemId() {
        Assert.assertEquals("wrong ID.", 0, mAdapter.getItemId(0));
    }

    @Test
    public void testGetCount() {
        Assert.assertEquals("question amount incorrect.", 2, mAdapter.getCount());
    }

    @Test
    public void testGetView() {

        View view = mAdapter.getView(0, null, null);

        Assert.assertNotNull("View is null. ", view);

        TextView name = (TextView) view.findViewById(R.id.name);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);

        Assert.assertNotNull("TextView is null. ", name);
        Assert.assertNotNull("CheckBox is null. ", checkBox);

        Assert.assertEquals("Names doesn't match.", item2.getCatalog().getName(), name.getText());
        Assert.assertEquals("Checkbox value doesn't match.", item2.isChecked(), checkBox.isChecked());
    }

    @Test
    public void testSearch() {

        View view = mAdapter.getView(0, null, null);

        listView = (ListView) view.findViewById(R.id.ListViewBuildQuiz);
        Assert.assertNotNull("The list was not loaded", listView);

        mAdapter.getFilter().filter(item1.getCatalog().getName());

        BuildQuizAdapter newAdapter = (BuildQuizAdapter) listView.getAdapter();

        Assert.assertEquals("Names doesn't match.", item1.getCatalog().getName(), newAdapter.getItem(0).getCatalog().getName());
    }

    @Test
    public void testFilter() {

        View view = mAdapter.getView(0, null, null);

        listView = (ListView) view.findViewById(R.id.ListViewBuildQuiz);
        Assert.assertNotNull("The list was not loaded", listView);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkMedium);
        checkBox.setChecked(true);

        BuildQuizAdapter newAdapter = (BuildQuizAdapter) listView.getAdapter();

        Assert.assertEquals("Names doesn't match.", item2.getCatalog().getName(), newAdapter.getItem(0).getCatalog().getName());
    }
}
