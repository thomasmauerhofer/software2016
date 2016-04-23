package com.bitschupfa.sw16.yaq.ui;

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

    private final Activity activity;

    private final List<RankingItem> items;

    public RankingList(Activity activity, List<RankingItem> items) {
        super(activity, R.layout.list_statictic_rank, items);
        this.items = items;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_statictic_rank, parent, false);
            TextView rank = (TextView) convertView.findViewById(R.id.rank);
            TextView player = (TextView) convertView.findViewById(R.id.player_name);
            TextView score = (TextView) convertView.findViewById(R.id.score);


            rank.setText(String.valueOf(position + 1));
            player.setText(items.get(position).getName());
            score.setText(String.valueOf(items.get(position).getScore()));
        }
        return convertView;
    }

    public void addItems() {
        items.add(new RankingItem("Thomas", 1000));
        items.add(new RankingItem("Patrik", 100));
        items.add(new RankingItem("Manuel", 100));
        items.add(new RankingItem("Johannes", 2));
        items.add(new RankingItem("Matthias", 1));
    }
}
