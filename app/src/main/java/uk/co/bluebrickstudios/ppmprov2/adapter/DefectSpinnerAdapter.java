package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import uk.co.bluebrickstudios.ppmprov2.model.Defect;

public class DefectSpinnerAdapter extends ArrayAdapter<Defect> {
    private Context mContext;
    private List<Defect> mValues;

    public DefectSpinnerAdapter(Context context, int textViewResourceId, List<Defect> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    public int getCount() {
        return this.mValues.size();
    }

    public Defect getItem(int position) {
        return (Defect) this.mValues.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemPosition(Defect defect) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Defect) this.mValues.get(i)).getId() == defect.getId()) {
                return i;
            }
        }
        return 0;
    }
    public int getItemPositionByID(int id) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Defect) this.mValues.get(i)).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setText(" " + ((Defect) this.mValues.get(position)).getName());
        label.setHeight(50);
        label.setTextSize(18.0f);
        label.setGravity(19);
        return label;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setTextSize(18.0f);
        label.setText(" " + ((Defect) this.mValues.get(position)).getName());
        label.setHeight(70);
        label.setGravity(19);
        return label;
    }
}
