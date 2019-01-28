package uk.co.bluebrickstudios.ppmprov2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.CustomVolleyRequestQueue;
import uk.co.bluebrickstudios.ppmprov2.model.Image;
import uk.co.bluebrickstudios.ppmprov2.model.Item;

public class ItemsAdapter extends ArrayAdapter<Item> {
    private ImageLoader mImageLoader;
    private NetworkImageView mNetworkImageView;

    public ItemsAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_item_entry, parent, false);
        }
        this.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);
        TextView itemID = (TextView) convertView.findViewById(R.id.itemId);
        TextView columnOne = (TextView) convertView.findViewById(R.id.columnOne);
        TextView columnTwo = (TextView) convertView.findViewById(R.id.columnTwo);
        Image image = item.getFeaturedImage();
        if (image != null) {
            this.mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
            String url = "http://ppmpro.bluebrickstudios.co.uk/images/defectimages/" + image.getThumbnail();
            this.mImageLoader.get(url, ImageLoader.getImageListener(this.mNetworkImageView, R.mipmap.ic_action_camera, 17301543));
            this.mNetworkImageView.setImageUrl(url, this.mImageLoader);
        }
        try {
            itemID.setText(Integer.toString(item.getId()));
            columnOne.setText(item.getName() + "\nItem: " + item.getItemType().getName() + "\nDefect: " + item.getDefect().getName() + "\nAction: " + item.getAction().getName() + "\nStatus: " + item.getStatus().getName() + "\nCreated: " + item.getCreated_at());
            columnTwo.setText("Client: " + item.getFloor().getBuilding().getEstate().getClient().getName() + "\nEstate: " + item.getFloor().getBuilding().getEstate().getName() + "\nBuilding: " + item.getFloor().getBuilding().getName() + "\nFloor: " + item.getFloor().getName() + "\nLocation: " + item.getLocation() + "\nLast Modified: " + item.getUpdated_at());
        } catch (NullPointerException e) {
        }
        return convertView;
    }
}
