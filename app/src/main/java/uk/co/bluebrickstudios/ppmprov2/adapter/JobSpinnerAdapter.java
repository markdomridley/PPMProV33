package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.co.bluebrickstudios.ppmprov2.model.Job;

public class JobSpinnerAdapter extends ArrayAdapter<Job> {

    private Context mContext;
    private List<Job> mValues;

    public JobSpinnerAdapter(Context context, int textViewResourceId, List<Job> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    public int getCount() {
        return this.mValues.size();
    }

    public Job getItem(int position) {
        return (Job) this.mValues.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemPosition(Job job) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Job) this.mValues.get(i)).getId() == job.getId()) {
                return i;
            }
        }
        return 0;
    }

    public int getItemPositionByID(int id) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Job) this.mValues.get(i)).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setText(" " + ((Job) this.mValues.get(position)).getName());
        label.setHeight(50);
        label.setTextSize(18.0f);
        label.setGravity(19);
        return label;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setTextSize(18.0f);
        label.setText(" " + ((Job) this.mValues.get(position)).getName());
        label.setHeight(70);
        label.setGravity(19);
        return label;
    }
}