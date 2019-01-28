package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.co.bluebrickstudios.ppmprov2.model.FCIScore;

public class FCIScoreSpinnerAdapter extends ArrayAdapter<FCIScore> {
    private Context mContext;
    private List<FCIScore> mValues;

    public FCIScoreSpinnerAdapter(Context context, int textViewResourceId, List<FCIScore> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    public int getCount() {
        return this.mValues.size();
    }

    public FCIScore getItem(int position) {
        return (FCIScore) this.mValues.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemPosition(FCIScore fciScore) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((FCIScore) this.mValues.get(i)).getId() == fciScore.getId()) {
                return i;
            }
        }
        return 0;
    }
    public int getItemPositionByID(int id) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((FCIScore) this.mValues.get(i)).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setText(" " + ((FCIScore) this.mValues.get(position)).getName());
        label.setHeight(50);
        label.setTextSize(18.0f);
        label.setGravity(19);
        return label;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setTextSize(18.0f);
        label.setText(" " + ((FCIScore) this.mValues.get(position)).getName());
        label.setHeight(70);
        label.setGravity(19);
        return label;
    }
}
