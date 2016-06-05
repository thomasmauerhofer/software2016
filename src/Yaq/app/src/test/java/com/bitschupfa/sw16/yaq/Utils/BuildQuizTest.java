package com.bitschupfa.sw16.yaq.Utils;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.BuildQuiz.QuestionCatalogueItem;
import com.bitschupfa.sw16.yaq.utils.CustomAdapter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BuildQuizTest extends InstrumentationTestCase{

    private CustomAdapter mAdapter;
    private QuestionCatalogueItem item1;
    private QuestionCatalogueItem item2;
    private ListView listView;

    @Before
    public void setUp() throws Exception {
        ArrayList<QuestionCatalogueItem> data = new ArrayList<>();

        Context context = new MockContext();

        item1 = new QuestionCatalogueItem("question1", 0, 1, true, 2, context);
        item2 = new QuestionCatalogueItem("question2", 1, 2, false, 3, context);
        data.add(item1);
        data.add(item2);
        mAdapter = new CustomAdapter(context, 0, data, true, true, true);
    }

    @Test
    public void testGetItem() {
        Assert.assertEquals("question1 was expected.", item1.getName(), (mAdapter.getItem(0)).getName());
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

        Assert.assertEquals("Names doesn't match.", item2.getName(), name.getText());
        Assert.assertEquals("Checkbox value doesn't match.", item2.isChecked(), checkBox.isChecked());
    }

    @Test
    public void testSearch() {

        View view = mAdapter.getView(0, null, null);

        listView = (ListView) view.findViewById(R.id.ListViewBuildQuiz);
        Assert.assertNotNull("The list was not loaded", listView);

        mAdapter.getFilter().filter(item1.getName());

        CustomAdapter newAdapter = (CustomAdapter) listView.getAdapter();

        Assert.assertEquals("Names doesn't match.", item1.getName(), newAdapter.getItem(0).getName());
    }

    @Test
    public void testFilter() {

        View view = mAdapter.getView(0, null, null);

        listView = (ListView) view.findViewById(R.id.ListViewBuildQuiz);
        Assert.assertNotNull("The list was not loaded", listView);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkMedium);
        checkBox.setChecked(true);

        CustomAdapter newAdapter = (CustomAdapter) listView.getAdapter();

        Assert.assertEquals("Names doesn't match.", item2.getName(), newAdapter.getItem(0).getName());
    }
}
