package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import uk.co.bluebrickstudios.ppmprov2.model.Inspection;

public class InspectionSpinnerAdapter extends ArrayAdapter<Inspection> {
    private Context mContext;
    private List<Inspection> mValues;

    public InspectionSpinnerAdapter(Context context, int textViewResourceId, List<Inspection> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    public int getCount() {
        return this.mValues.size();
    }

    public Inspection getItem(int position) {
        return (Inspection) this.mValues.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemPosition(Inspection inspection) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Inspection) this.mValues.get(i)).getId() == inspection.getId()) {
                return i;
            }
        }
        return 0;
    }

    public int getItemPositionByID(int id) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Inspection) this.mValues.get(i)).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        try {
            Inspection inspection = ((Inspection) this.mValues.get(position));
            label.setText(" " + inspection.getName() + " (" + inspection.getStart_at().substring(0,10) + ")");
        } catch (IndexOutOfBoundsException e) {
            label.setText(" ");
        }
        label.setHeight(50);
        label.setTextSize(18.0f);
        label.setGravity(19);
        return label;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Inspection inspection = ((Inspection) this.mValues.get(position));
        TextView label = new TextView(this.mContext);
        label.setTextSize(18.0f);
        label.setText(" " + inspection.getName() + " (" + inspection.getStart_at().substring(0,10) + ")");
        label.setHeight(70);
        label.setGravity(19);
        return label;
    }
}
