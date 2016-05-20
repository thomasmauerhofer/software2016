package com.bitschupfa.sw16.yaq.Utils;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.BuildQuiz.QuestionCatalogueItem;
import com.bitschupfa.sw16.yaq.utils.CustomAdapter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by manu on 20.05.16.
 */
public class BuildQuizTest extends InstrumentationTestCase{
    private CustomAdapter mAdapter;
    private QuestionCatalogueItem item1;
    private QuestionCatalogueItem item2;

    @Before
    public void setUp() throws Exception {
        ArrayList<QuestionCatalogueItem> data = new ArrayList<QuestionCatalogueItem>();

        item1 = new QuestionCatalogueItem("question1", 0, true, 2);
        item2 = new QuestionCatalogueItem("question2", 1, false, 3);
        data.add(item1);
        data.add(item2);
        Context context = new MockContext();
        mAdapter = new CustomAdapter(context, 0, data);
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
}
