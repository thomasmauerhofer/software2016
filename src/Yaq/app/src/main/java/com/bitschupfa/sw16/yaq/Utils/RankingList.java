package com.bitschupfa.sw16.yaq.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;

import java.util.List;

public class RankingList extends ArrayAdapter<RankingItem> {

    private Activity activity;

    private List<RankingItem> items;

    public RankingList(Activity activity, List<RankingItem> items) {
        super(activity, R.layout.statictic_rank, items);
        this.items = items;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.statictic_rank, parent, false);
        TextView rank = (TextView) rowView.findViewById(R.id.rank);
        TextView player = (TextView) rowView.findViewById(R.id.player_name);
        TextView score = (TextView) rowView.findViewById(R.id.score);


        rank.setText(String.valueOf(position + 1));
        player.setText(items.get(position).getName());
        score.setText(String.valueOf(items.get(position).getScore()));

        return rowView;
    }

    public void addItems() {
        items.add(new RankingItem("Thomas", 1000));
        items.add(new RankingItem("Patrik", 100));
        items.add(new RankingItem("Manuel", 100));
        items.add(new RankingItem("Johannes", 2));
        items.add(new RankingItem("Matthias", 1));
    }
}
