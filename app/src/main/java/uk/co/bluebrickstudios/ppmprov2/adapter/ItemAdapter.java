package uk.co.bluebrickstudios.ppmprov2.adapter;

/**
 * Created by dominicr on 03/07/2015.
 */
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.helper.CustomVolleyRequestQueue;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Item;

public class ItemAdapter extends Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private ImageLoader mImageLoader;
    OnItemClickListener mItemClickListener;
    private NetworkImageView mNetworkImageView;
    DatabaseHelper controller = null;

    final CharSequence myList[] = { "Identified", "Allocated", "Scheduled", "In Progress", "Complete", "Audited" };

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public class ItemViewHolder extends ViewHolder implements OnClickListener {

        protected TextView action_value;
        protected TextView date_value;
        protected TextView defect_value;
        protected TextView guid_value;
        protected NetworkImageView imageitem;
        protected TextView itemId;
        protected TextView itemtype_value;
        protected TextView location_breadcrumb;
        protected TextView modified_value;
        protected TextView status_value;
        protected TextView trade_value;
        protected TextView notes_value;
        protected TextView uploaded_label;
        protected TextView priorityButton;
        protected Button statusbutton;

        public ItemViewHolder(View v) {
            super(v);
            //this.imageitem = (NetworkImageView) v.findViewById(R.id.imageitem);
            this.location_breadcrumb = (TextView) v.findViewById(R.id.item_breadcrumb);
            this.status_value = (TextView) v.findViewById(R.id.status_value);
            this.itemtype_value = (TextView) v.findViewById(R.id.itemtype_value);
            this.defect_value = (TextView) v.findViewById(R.id.defect_value);
            this.trade_value = (TextView) v.findViewById(R.id.trade_value);
            this.action_value = (TextView) v.findViewById(R.id.action_value);
            this.guid_value = (TextView) v.findViewById(R.id.guid_value);
            this.notes_value = (TextView) v.findViewById(R.id.notes_value);
            this.date_value = (TextView) v.findViewById(R.id.date_value);
            this.modified_value = (TextView) v.findViewById(R.id.modified_value);
            this.itemId = (TextView) v.findViewById(R.id.itemId);
            this.uploaded_label = (TextView) v.findViewById(R.id.uploaded_label);
            this.priorityButton = (TextView) v.findViewById(R.id.prioritybutton);
            this.statusbutton = (Button) v.findViewById(R.id.statusbutton);

            v.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (ItemAdapter.this.mItemClickListener != null) {
                ItemAdapter.this.mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public ItemAdapter(List<Item> itemList) {

        this.itemList = itemList;

    }

    public int getItemCount() {
        return this.itemList.size();
    }

    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int i) {

        final Item item = (Item) this.itemList.get(i);
        controller = DatabaseHelper.getInstance(itemViewHolder.itemView.getContext());

        try {

            itemViewHolder.location_breadcrumb.setText(item.getFloor().getBuilding().getEstate().getClient().getName() + " > " + item.getFloor().getBuilding().getEstate().getName() + " > " + item.getFloor().getBuilding().getName() + " > " + item.getFloor().getName());

            itemViewHolder.itemId.setText(Integer.toString(item.getId()));
            itemViewHolder.status_value.setText(item.getStatus().getName());
            if (item.getProfile() != null) {
                itemViewHolder.trade_value.setText(item.getProfile().getName());
            } else {
                itemViewHolder.trade_value.setText("N/A");
            }
            itemViewHolder.defect_value.setText(item.getDefect().getName());
            itemViewHolder.action_value.setText(item.getAction().getName());
            itemViewHolder.guid_value.setText(item.getGuid());
            itemViewHolder.notes_value.setText(item.getDescription());
            itemViewHolder.itemtype_value.setText(item.getItemType().getName());
            itemViewHolder.date_value.setText(item.getCreated_at());
            itemViewHolder.modified_value.setText(item.getUpdated_at());

            if(item.getUploaded() == 0){
                itemViewHolder.uploaded_label.setText("UPLOADED");
            }

            itemViewHolder.statusbutton.setText(item.getStatus().getName());
            itemViewHolder.statusbutton.setBackgroundColor(0xFF06377b);
            itemViewHolder.statusbutton.setTextColor(0xFFFFFFFF);


            if(item.getPriority_id() == 2){
                itemViewHolder.priorityButton.setBackgroundColor(0xFFB51E2A);
                itemViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                itemViewHolder.priorityButton.setText("Imd. Act. Req.");
            }
            else if(item.getPriority_id() == 4){
                itemViewHolder.priorityButton.setBackgroundColor(0xFFE9724C);
                itemViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                itemViewHolder.priorityButton.setText("Poor");
            }
            else if(item.getPriority_id() == 6){
                itemViewHolder.priorityButton.setBackgroundColor(0xFFEEA236);
                itemViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                itemViewHolder.priorityButton.setText("Fair");
            }
            else if(item.getPriority_id() == 8){
                itemViewHolder.priorityButton.setBackgroundColor(0xFFFFC857);
                itemViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                itemViewHolder.priorityButton.setText("Good");
            }
            else{
                itemViewHolder.priorityButton.setBackgroundColor(0xFF5CB85C);
                itemViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                itemViewHolder.priorityButton.setText("Like New");
            }

            //
            //itemViewHolder.location_breadcrumb.setText("TESTTEST");


            final AlertDialog.Builder ad = new AlertDialog.Builder(itemViewHolder.itemView.getContext());
            ad.setTitle("Select new item status");

            ad.setSingleChoiceItems(myList, item.getStatus_id()-1,  new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    /* update item and reset label on button */

                    item.setStatus_id(arg1+1);
                    item.setUploaded(2);
                    controller.updateItem(item);

                    item.setStatus(controller.getStatus((long) item.getStatus_id()));

                    itemViewHolder.statusbutton.setText(item.getStatus().getName());

                    arg0.dismiss();

                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*
                    Toast.makeText(getApplicationContext(),
                            "You Have Cancel the Dialog box", Toast.LENGTH_LONG)
                            .show();
                    */
                }
            });


            itemViewHolder.statusbutton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ad.show();

                }
            });

        } catch (NullPointerException e) {
        }
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_view, viewGroup, false);
        this.mImageLoader = CustomVolleyRequestQueue.getInstance(viewGroup.getContext()).getImageLoader();
        return new ItemViewHolder(itemView);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
