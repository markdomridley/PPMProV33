package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import uk.co.bluebrickstudios.ppmprov2.model.Trade;

public class TradeSpinnerAdapter extends ArrayAdapter<Trade> {
    private Context mContext;
    private List<Trade> mValues;

    public TradeSpinnerAdapter(Context context, int textViewResourceId, List<Trade> objects) {
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    public int getCount() {
        return this.mValues.size();
    }

    public Trade getItem(int position) {
        return (Trade) this.mValues.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemPosition(Trade trade) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Trade) this.mValues.get(i)).getId() == trade.getId()) {
                return i;
            }
        }
        return 0;
    }

    public int getItemPositionByID(int id) {
        for (int i = 0; i < this.mValues.size(); i++) {
            if (((Trade) this.mValues.get(i)).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setText(" " + ((Trade) this.mValues.get(position)).getName());
        label.setHeight(50);
        label.setTextSize(18.0f);
        label.setGravity(19);
        return label;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.mContext);
        label.setTextSize(18.0f);
        label.setText(" " + ((Trade) this.mValues.get(position)).getName());
        label.setHeight(70);
        label.setGravity(19);
        return label;
    }
}
