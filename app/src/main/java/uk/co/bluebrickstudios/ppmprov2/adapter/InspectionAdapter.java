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
import uk.co.bluebrickstudios.ppmprov2.model.Inspection;

public class InspectionAdapter extends Adapter<InspectionAdapter.InspectionViewHolder> {

    private List<Inspection> inspectionList;
    private ImageLoader mImageLoader;
    OnItemClickListener mItemClickListener;
    private NetworkImageView mNetworkImageView;
    DatabaseHelper controller = null;

    final CharSequence myList[] = { "2. Imd. Act. Req.", "4. Poor", "6. Fair", "8. Good", "10. Like New" };

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public class InspectionViewHolder extends ViewHolder implements OnClickListener {

        protected TextView name_value;
        protected TextView profile_value;
        protected TextView dates_value;
        protected TextView completed_value;
        protected TextView inspectionId;
        protected TextView inspection_breadcrumb;
        protected TextView priorityButton;
        protected Button statusbutton;

        public InspectionViewHolder(View v) {

            super(v);
            this.inspection_breadcrumb = (TextView) v.findViewById(R.id.inspection_breadcrumb);
            this.name_value = (TextView) v.findViewById(R.id.name_value);
            this.profile_value = (TextView) v.findViewById(R.id.profile_value);
            this.dates_value = (TextView) v.findViewById(R.id.dates_value);
            this.completed_value = (TextView) v.findViewById(R.id.completed_value);
            this.inspectionId = (TextView) v.findViewById(R.id.inspectionId);
            this.priorityButton = (TextView) v.findViewById(R.id.insp_prioritybutton);
            this.statusbutton = (Button) v.findViewById(R.id.insp_statusbutton);

            v.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (InspectionAdapter.this.mItemClickListener != null) {
                InspectionAdapter.this.mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public InspectionAdapter(List<Inspection> inspectionList) {

        this.inspectionList = inspectionList;

    }

    public int getItemCount() {
        return this.inspectionList.size();
    }

    public void onBindViewHolder(final InspectionViewHolder inspectionViewHolder, int i) {

        final Inspection inspection = (Inspection) this.inspectionList.get(i);
        controller = DatabaseHelper.getInstance(inspectionViewHolder.itemView.getContext());

        try {

            String location_label = "";

            if(inspection.getClient() != null){
                location_label = location_label + inspection.getClient().getName();
            }

            if(inspection.getEstate() != null){
                location_label = location_label + " > " + inspection.getEstate().getName();
            }

            if(inspection.getBuilding() != null){
                location_label = location_label + " > " + inspection.getBuilding().getName();
            }

            if(inspection.getFloor() != null){
                location_label = location_label + " > " + inspection.getFloor().getName();
            }

            inspectionViewHolder.inspection_breadcrumb.setText(inspection.getName());

            inspectionViewHolder.inspectionId.setText(Integer.toString(inspection.getId()));
            inspectionViewHolder.name_value.setText(location_label);
            if (inspection.getProfile() != null) {
                inspectionViewHolder.profile_value.setText(inspection.getProfile().getName());
            } else {
                inspectionViewHolder.profile_value.setText("N/A");
            }
            inspectionViewHolder.dates_value.setText(inspection.getStart_at() + " > " + inspection.getEnd_at());

            if(inspection.getCompleted() == 1){
                inspectionViewHolder.completed_value.setText("Completed on " + inspection.getCompleted_at());
            }
            else{
                inspectionViewHolder.completed_value.setText("NO");
            }

            inspectionViewHolder.statusbutton.setText("Set FCI");
            inspectionViewHolder.statusbutton.setBackgroundColor(0xFF06377b);
            inspectionViewHolder.statusbutton.setTextColor(0xFFFFFFFF);

            if(inspection.getPriority_id() == 2){
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFFB51E2A);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("2. Imd. Act. Req.");
            }
            else if(inspection.getPriority_id() == 4){
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFFE9724C);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("4. Poor");
            }
            else if(inspection.getPriority_id() == 6){
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFFEEA236);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("6. Fair");
            }
            else if(inspection.getPriority_id() == 8){
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFFFFC857);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("8. Good");
            }
            else if(inspection.getPriority_id() == 10){
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFF5CB85C);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("10. Like New");
            }
            else{
                inspectionViewHolder.priorityButton.setBackgroundColor(0xFF000000);
                inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                inspectionViewHolder.priorityButton.setText("NOT SET");
            }


            //
            //itemViewHolder.location_breadcrumb.setText("TESTTEST");

            final AlertDialog.Builder ad = new AlertDialog.Builder(inspectionViewHolder.itemView.getContext());
            ad.setTitle("Select new FCI Score");

            int priority_index = 0;
            if(inspection.getPriority_id() > 0){
                priority_index = (inspection.getPriority_id()/2)-1;
            }
            ad.setSingleChoiceItems(myList, priority_index,  new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    int priority_id = (arg1 * 2) + 2;
                    controller.updateInspection(inspection.getId(),2, priority_id);

                    if(priority_id == 2){
                        inspectionViewHolder.priorityButton.setBackgroundColor(0xFFB51E2A);
                        inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                        inspectionViewHolder.priorityButton.setText("2. Imd. Act. Req.");
                    }
                    else if(priority_id == 4){
                        inspectionViewHolder.priorityButton.setBackgroundColor(0xFFE9724C);
                        inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                        inspectionViewHolder.priorityButton.setText("4. Poor");
                    }
                    else if(priority_id == 6){
                        inspectionViewHolder.priorityButton.setBackgroundColor(0xFFEEA236);
                        inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                        inspectionViewHolder.priorityButton.setText("6. Fair");
                    }
                    else if(priority_id == 8){
                        inspectionViewHolder.priorityButton.setBackgroundColor(0xFFFFC857);
                        inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                        inspectionViewHolder.priorityButton.setText("8. Good");
                    }
                    else{
                        inspectionViewHolder.priorityButton.setBackgroundColor(0xFF5CB85C);
                        inspectionViewHolder.priorityButton.setTextColor(0xFFFFFFFF);
                        inspectionViewHolder.priorityButton.setText("10. Like New");
                    }

                    arg0.dismiss();

                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });


            inspectionViewHolder.statusbutton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ad.show();

                }
            });

        } catch (NullPointerException e) {
        }
    }

    public InspectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inspection_card_view, viewGroup, false);
        //this.mImageLoader = CustomVolleyRequestQueue.getInstance(viewGroup.getContext()).getImageLoader();
        return new InspectionViewHolder(itemView);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}

