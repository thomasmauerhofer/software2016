package com.bitschupfa.sw16.yaq.utils;

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
import com.bitschupfa.sw16.yaq.activities.BuildQuiz.QuestionCatalogueItem;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<QuestionCatalogueItem> implements Filterable {

    public ArrayList<QuestionCatalogueItem> questionCatalagoueItem;

    private ArrayList<QuestionCatalogueItem> filteredData;
    private ItemFilter mFilter = new ItemFilter();
    private Context context;
    private boolean checkBoxEasy;
    private boolean checkBoxMedium;
    private boolean checkBoxHard;

    public CustomAdapter(Context context, int textViewResourceId,
                         ArrayList<QuestionCatalogueItem> questionCatalogueList, boolean checkBoxEasy,
                         boolean checkBoxMedium, boolean checkBoxHard) {
        super(context, textViewResourceId, questionCatalogueList);
        this.questionCatalagoueItem = new ArrayList<>();
        this.questionCatalagoueItem.addAll(questionCatalogueList);
        this.filteredData = new ArrayList<>();
        this.filteredData.addAll(questionCatalogueList);
        this.context = context;
        this.checkBoxEasy = checkBoxEasy;
        this.checkBoxMedium = checkBoxMedium;
        this.checkBoxHard = checkBoxHard;
    }

    private class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    public int getCount() {
        return filteredData.size();
    }

    public QuestionCatalogueItem getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_build_quiz, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    QuestionCatalogueItem item = (QuestionCatalogueItem) cb.getTag();
                    item.setChecked(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QuestionCatalogueItem item = getItem(position);

        String diff = "";

        switch (item.getDifficulty()) {
            case 1:
                diff = "EASY";
                break;
            case 2:
                diff = "MEDIUM";
                break;
            case 3:
                diff = "HARD";
                break;
            default:
                diff = "NO DIFFICULTY";
                break;
        }
        holder.name.setText(item.getName() + " (" + String.valueOf(item.getQuestionsCounter()) + " questions)" + " - " + diff);
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setTag(item);

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

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final ArrayList<QuestionCatalogueItem> list = questionCatalagoueItem;

            int count = list.size();
            final ArrayList<QuestionCatalogueItem> nlist = new ArrayList<>(count);

            QuestionCatalogueItem filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getName().toLowerCase(Locale.getDefault()).contains(filterString)) {
                    if (checkBoxEasy == true && filterableString.getDifficulty() == 1
                            || checkBoxMedium == true && filterableString.getDifficulty() == 2
                            || checkBoxHard == true && filterableString.getDifficulty() == 3) {

                        filterList.add(filterableString);
                    }
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<QuestionCatalogueItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
