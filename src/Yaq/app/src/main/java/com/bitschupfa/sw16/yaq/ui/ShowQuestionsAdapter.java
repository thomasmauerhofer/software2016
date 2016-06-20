package com.bitschupfa.sw16.yaq.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.List;

public class ShowQuestionsAdapter extends ArrayAdapter<TextQuestion> {

    private ArrayList<TextQuestion> questionItems = new ArrayList<>();
    private ArrayList<TextQuestion> filteredData = new ArrayList<>();

    private Context context;

    public ShowQuestionsAdapter(Context context, int textViewResourceId,
                                List<TextQuestion> questionList) {
        super(context, textViewResourceId, questionList);
        this.questionItems.addAll(questionList);
        this.filteredData = new ArrayList<>();
        this.filteredData.addAll(questionList);
        this.context = context;
    }

    public List<TextQuestion> getCatalogItems() {
        return questionItems;
    }

    public int getCount() {
        return filteredData.size();
    }

    public TextQuestion getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_show_questions, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);

        TextQuestion item = getItem(position);

        name.setText(item.getQuestion());

        return convertView;
    }
}
