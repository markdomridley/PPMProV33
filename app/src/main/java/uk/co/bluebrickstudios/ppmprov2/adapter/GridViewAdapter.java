package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.BuildConfig;
import uk.co.bluebrickstudios.ppmprov2.model.Image;

public class GridViewAdapter extends ArrayAdapter {
    private static final String LOG;
    private Context context;
    private ArrayList data;
    private int layoutResourceId;

    static class ViewHolder {
        ImageView image;
        TextView imageTitle;

        ViewHolder() {
        }
    }

    static {
        LOG = GridViewAdapter.class.getName();
    }

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.data = new ArrayList();
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.galleryImageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Image item = (Image) this.data.get(position);
        holder.imageTitle.setText(BuildConfig.FLAVOR);
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    public long getItemId(int position){
        return position;
    }
}
