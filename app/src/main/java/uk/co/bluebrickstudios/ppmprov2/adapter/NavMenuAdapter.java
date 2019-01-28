package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.model.NavMenuItem;

public class NavMenuAdapter extends ArrayAdapter<NavMenuItem> {
    public NavMenuAdapter(Context context, ArrayList<NavMenuItem> users) {
        super(context, 0, users);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NavMenuItem item = (NavMenuItem) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, parent, false);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        ((TextView) convertView.findViewById(R.id.menu_name)).setText(item.getTitle());
        icon.setImageResource(item.getIcon());
        return convertView;
    }
}
