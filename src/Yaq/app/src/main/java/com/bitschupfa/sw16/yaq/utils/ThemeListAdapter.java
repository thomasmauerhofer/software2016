package com.bitschupfa.sw16.yaq.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.MainMenu;
import com.bitschupfa.sw16.yaq.activities.Themes;
import com.bitschupfa.sw16.yaq.ui.ThemeChooser;

import java.util.List;

/**
 * Created by Andrej on 04.06.2016.
 */
public class ThemeListAdapter extends ArrayAdapter<String> {

    private final Themes themeActivity;
    private final ThemeChooser themeChooser;
    private final List<String> names;
    public ViewHolder holder;

    static class ViewHolder {
        public TextView themeNameTextView;
        public ImageView themeImageImageView;
        public LinearLayout themePreviewLayout;
    }

    public ThemeListAdapter(Themes themeActivity, List<String> names) {
        super(themeActivity, R.layout.theme_list_row, names);
        this.themeActivity = themeActivity;
        this.names = names;
        themeChooser = new ThemeChooser(themeActivity);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = themeActivity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.theme_list_row, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.themePreviewLayout = (LinearLayout) rowView.findViewById(R.id.themePreviewLayout);
            viewHolder.themeNameTextView = (TextView) rowView.findViewById(R.id.themePreviewTextView);
            viewHolder.themeImageImageView = (ImageView) rowView.findViewById(R.id.themePreviewImageView);
            rowView.setTag(viewHolder);
        }

        holder = (ViewHolder) rowView.getTag();
        String themeName = names.get(position);
        holder.themeNameTextView.setText(themeName);

        for (int i = 0; i < themeActivity.getThemeList().size(); i++) {
            String themePreviewImageName = themeName.toLowerCase().replace(" ", "") + "preview";
            int previewImageId = themeActivity.getResources().
                    getIdentifier(themePreviewImageName, "drawable", themeActivity.getPackageName());
            if (previewImageId != 0)
                holder.themeImageImageView.setImageResource(previewImageId);
            else {
                holder.themeImageImageView.setImageResource(R.drawable.defaultpreview);
            }
        }
        holder.themePreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case ThemeChooser.THEME_BLUE:
                        themeChooser.setTheme(ThemeChooser.THEME_BLUE);
                        holder.themePreviewLayout.setAlpha(0.5f);
                        break;
                    case ThemeChooser.THEME_GREEN:
                        themeChooser.setTheme(ThemeChooser.THEME_GREEN);
                        break;
                    case ThemeChooser.THEME_HELLOKITTY:
                        themeChooser.setTheme(ThemeChooser.THEME_HELLOKITTY);
                        break;
                    case ThemeChooser.THEME_TEAL:
                        themeChooser.setTheme(ThemeChooser.THEME_TEAL);
                        break;
                }
                holder.themePreviewLayout.setAlpha(0.5f);
                themeActivity.finishActivity(MainMenu.RESULT_FINISH);
                themeActivity.finish();
                Intent intent = new Intent(themeActivity, MainMenu.class);
                themeActivity.startActivity(intent);
            }
        });

        return rowView;
    }
}
