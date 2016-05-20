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

import java.util.ArrayList;

/**
 * Created by manu on 20.05.16.
 */

public class CustomAdapter extends ArrayAdapter<QuestionCatalogueItem> implements Filterable {

    public ArrayList<QuestionCatalogueItem> questionCatalagoueItem;

    private ArrayList<QuestionCatalogueItem> filteredData;
    private ItemFilter mFilter = new ItemFilter();
    private Context context;

    public CustomAdapter(Context context, int textViewResourceId,
                         ArrayList<QuestionCatalogueItem> questionCatalogueList) {
        super(context, textViewResourceId, questionCatalogueList);
        this.questionCatalagoueItem = new ArrayList<>();
        this.questionCatalagoueItem.addAll(questionCatalogueList);
        this.filteredData = new ArrayList<>();
        this.filteredData.addAll(questionCatalogueList);
        this.context = context;
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
        holder.name.setText(item.getName() + " (" + item.getQuestionsCounter().toString() + " questions)");
        holder.checkBox.setChecked(item.isChecked());
        holder.checkBox.setTag(item);

        return convertView;
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
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
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
