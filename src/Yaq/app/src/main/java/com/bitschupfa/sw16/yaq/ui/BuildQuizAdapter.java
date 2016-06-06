package com.bitschupfa.sw16.yaq.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.utils.QuizBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuildQuizAdapter extends ArrayAdapter<QuestionCatalogItem> implements Filterable {

    private ArrayList<QuestionCatalogItem> questionCatalogItems = new ArrayList<>();
    private ArrayList<QuestionCatalogItem> filteredData = new ArrayList<>();

    private ItemFilter mFilter = new ItemFilter();
    private Context context;
    private boolean checkBoxEasy;
    private boolean checkBoxMedium;
    private boolean checkBoxHard;

    public BuildQuizAdapter(Context context, int textViewResourceId,
                            ArrayList<QuestionCatalogItem> questionCatalogueList, boolean checkBoxEasy,
                            boolean checkBoxMedium, boolean checkBoxHard) {
        super(context, textViewResourceId, questionCatalogueList);
        this.questionCatalogItems.addAll(questionCatalogueList);
        this.filteredData = new ArrayList<>();
        this.filteredData.addAll(questionCatalogueList);
        this.context = context;
        this.checkBoxEasy = checkBoxEasy;
        this.checkBoxMedium = checkBoxMedium;
        this.checkBoxHard = checkBoxHard;
    }

    public List<QuestionCatalogItem> getCatalogItems() {
        return questionCatalogItems;
    }

    public int getCount() {
        return filteredData.size();
    }

    public QuestionCatalogItem getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_build_quiz, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView numberOfQuestions = (TextView) convertView.findViewById(R.id.numberQuestions);
        TextView difficulty = (TextView) convertView.findViewById(R.id.difficulty);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        final int pos = position;
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                QuestionCatalogItem item = getItem(pos);
                item.setChecked(cb.isChecked());
            }
        });

        QuestionCatalogItem item = getItem(position);
        String diff;

        switch (item.getCatalog().getDifficulty()) {
            case 1:
                diff = getContext().getString(R.string.easy);
                break;
            case 2:
                diff = getContext().getString(R.string.medium);
                break;
            case 3:
                diff = getContext().getString(R.string.hard);
                break;
            default:
                diff = getContext().getString(R.string.noDifficulty);
                break;
        }
        name.setText(item.getCatalog().getName());
        numberOfQuestions.setText(String.valueOf(item.getCatalog().getTextQuestionList().size()) + " " + getContext().getString(R.string.questions));
        difficulty.setText(diff);

        checkBox.setChecked(QuizBuilder.instance().isCatalogUsed(item.getCatalog().getName()));
        item.setChecked(QuizBuilder.instance().isCatalogUsed(item.getCatalog().getName()));

        return convertView;
    }

    public void setCheckBoxEasy(boolean value) {
        checkBoxEasy = value;
    }

    public void setCheckBoxMedium(boolean value) {
        checkBoxMedium = value;
    }

    public void setCheckBoxHard(boolean value) {
        checkBoxHard = value;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase(Locale.getDefault());
            FilterResults results = new FilterResults();

            final ArrayList<QuestionCatalogItem> filterList = new ArrayList<>();

            for (QuestionCatalogItem filterableString : questionCatalogItems) {
                if (filterableString.getCatalog().getName().toLowerCase(Locale.getDefault()).contains(filterString)) {
                    if (checkBoxEasy == true && filterableString.getCatalog().getDifficulty() == 1
                            || checkBoxMedium == true && filterableString.getCatalog().getDifficulty() == 2
                            || checkBoxHard == true && filterableString.getCatalog().getDifficulty() == 3) {

                        filterList.add(filterableString);
                    }
                }
            }
            results.values = filterList;
            results.count = filterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<QuestionCatalogItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
