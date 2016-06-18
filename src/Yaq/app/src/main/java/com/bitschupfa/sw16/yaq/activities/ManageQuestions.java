package com.bitschupfa.sw16.yaq.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.QuestionCatalogDAO;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.ui.ManageQuestionsAdapter;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageQuestions extends YaqActivity {

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private ArrayList<QuestionCatalogItem> catalogs = new ArrayList<>();
    private ManageQuestionsAdapter dataAdapter = null;
    private EditText searchText;
    private QuestionCatalog actualQuestionCatalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleTheme();

        initSearchView();
        displayListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        this.getMenuInflater().inflate(R.menu.menu_manage_questions, menu);
        menu.findItem(R.id.edit).setVisible(true);
        menu.findItem(R.id.delete).setVisible(true);
    }

    public void displayListView() {
        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();

        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            catalogs.add(new QuestionCatalogItem(catalog, false, this));
        }

        dataAdapter = new ManageQuestionsAdapter(this, R.layout.list_manage_questions, catalogs, true, true, true);
        listView = (ListView) findViewById(R.id.ListViewManageQuestions);
        listView.setAdapter(dataAdapter);

        this.registerForContextMenu(listView);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                QuestionCatalogItem item = (QuestionCatalogItem) listView.getItemAtPosition(position);

                Intent intent = new Intent(ManageQuestions.this, ShowQuestions.class);
                intent.putExtra("QuestionCatalogue", item.getCatalog());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                QuestionCatalogItem item = (QuestionCatalogItem) listView.getItemAtPosition(pos);
                Log.d("blah","long click: " + item.getCatalog().getName());
                actualQuestionCatalog = item.getCatalog();
                openContextMenu(listView);
                return true;
            }
        });
    }

    public void showEditDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageQuestions.this);
        LayoutInflater li = LayoutInflater.from(ManageQuestions.this);
        View dialogView = li.inflate(R.layout.dialog_manage_questions, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.editText);
        RadioButton checkEasy = (RadioButton) dialogView.findViewById(R.id.checkEasyDialog);
        RadioButton checkMedium = (RadioButton) dialogView.findViewById(R.id.checkMediumDialog);
        RadioButton checkHard = (RadioButton) dialogView.findViewById(R.id.checkHardDialog);

        if(actualQuestionCatalog != null) {
            builder.setTitle("Edit Catalog");
            input.setText(actualQuestionCatalog.getName());
            checkEasy.setChecked(actualQuestionCatalog.getDifficulty() == 1);
            checkMedium.setChecked(actualQuestionCatalog.getDifficulty() == 2);
            checkHard.setChecked(actualQuestionCatalog.getDifficulty() == 3);
        }
        else {
            builder.setTitle("Add Catalog");
        }

        builder.setView(dialogView);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(actualQuestionCatalog != null) {
                    //TODO edit catalog in db
                    actualQuestionCatalog = null;
                } else {
                    //TODO add catalog in db
                    QuestionCatalog questionCatalog = new QuestionCatalog(0, 1, input.getText().toString(), null);
                    QuestionCatalogDAO newQuestionCatalog = new QuestionCatalogDAO(questionCatalog);
                    newQuestionCatalog.insertIntoDatabase(ManageQuestions.this);
                    dataAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                showEditDialog();
                return true;
            case R.id.delete:
                // TODO delete cataloge
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void initSearchView() {
        searchText = (EditText) findViewById(R.id.EditTextBuildQuizM);

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                dataAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    @SuppressWarnings("UnusedParameters")
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkEasy:
                setCheckedEasy(checked);
                break;
            case R.id.checkMedium:
                setCheckedMedium(checked);
                break;
            case R.id.checkHard:
                setCheckedHard(checked);
                break;
            default:
                break;
        }
        dataAdapter.getFilter().filter(searchText.getText());
    }

    public void setCheckedEasy(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxEasy(checked);
        }
    }

    public void setCheckedMedium(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxMedium(checked);
        }
    }

    public void setCheckedHard(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxHard(checked);
        }
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}

