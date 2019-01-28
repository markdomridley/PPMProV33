package uk.co.bluebrickstudios.ppmprov2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.GridViewAdapter;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Image;
import uk.co.bluebrickstudios.ppmprov2.model.Item;

public class ItemDetail extends AppCompatActivity implements OnClickListener {
    private static final String LOG;
    static final int REQUEST_SET_LOCATION = 2;
    private int _Floor_Id;
    private int _Item_Id;
    Button btnClose;
    Button btnDelete;
    Button btnSave;
    DatabaseHelper controller;
    private GridViewAdapter gridAdapter;
    private GridView gridView;
    TextView location;

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemDetail.1 */
    class C01991 implements OnItemClickListener {
        C01991() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemDetail.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemDetail.this.startActivity(intent);
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemDetail.2 */
    class C02002 implements DialogInterface.OnClickListener {

        C02002() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ItemDetail.this.controller.deleteLocalItem(ItemDetail.this._Item_Id);
            ItemDetail.this.controller.deleteLocalImage(ItemDetail.this._Item_Id);
            ItemDetail.this.setResult(-1, new Intent());
            ItemDetail.this.finish();
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemDetail.3 */
    class C02013 implements DialogInterface.OnClickListener {
        C02013() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public ItemDetail() {
        this._Item_Id = 0;
        this._Floor_Id = 0;
        this.controller = null;
    }

    static {
        LOG = MainActivity.class.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.view_item_detail);
        this.controller = DatabaseHelper.getInstance(this);
        buildView();
    }

    protected void buildView() {
        TextView client = (TextView) findViewById(R.id.client);
        TextView estate = (TextView) findViewById(R.id.estate);
        TextView building = (TextView) findViewById(R.id.building);
        TextView floor = (TextView) findViewById(R.id.floor);
        TextView description = (TextView) findViewById(R.id.detailDescription);
        TextView status = (TextView) findViewById(R.id.status);
        TextView priority = (TextView) findViewById(R.id.priority);
        TextView itemtype = (TextView) findViewById(R.id.itemtype);
        TextView defect = (TextView) findViewById(R.id.defect);
        TextView trade = (TextView) findViewById(R.id.trade);
        TextView action = (TextView) findViewById(R.id.action);
        TextView profile = (TextView) findViewById(R.id.profile);
        TextView inspection = (TextView) findViewById(R.id.inspection);
        TextView podnumber = (TextView) findViewById(R.id.podnumber);
        this.location = (TextView) findViewById(R.id.location);
        TextView added = (TextView) findViewById(R.id.itemAdded);
        TextView edited = (TextView) findViewById(R.id.itemEdited);
        this._Item_Id = 0;
        this._Item_Id = getIntent().getIntExtra("item_Id", 0);
        Item item = new Item();
        item = this.controller.getItem((long) this._Item_Id);
        this._Floor_Id = item.getFloor_id();
        client.setText(item.getFloor().getBuilding().getEstate().getClient().getName());
        estate.setText(item.getFloor().getBuilding().getEstate().getName());
        building.setText(item.getFloor().getBuilding().getName());
        floor.setText(item.getFloor().getName());
        description.setText(item.getDescription());
        status.setText(item.getStatus().getName());
        itemtype.setText(item.getItemType().getName());
        defect.setText(item.getDefect().getName());
        trade.setText(item.getTrade().getName());
        action.setText(item.getAction().getName());
        podnumber.setText(item.getPodnumber());
        if (item.getProfile() != null) {
            profile.setText(item.getProfile().getName());
        } else {
            profile.setText(BuildConfig.FLAVOR);
        }
        if (item.getInspection() != null) {
            inspection.setText(item.getInspection().getName());
        } else {
            inspection.setText(BuildConfig.FLAVOR);
        }
        this.location.setText(item.getLocation());


        try{
            switch(item.getPriority_id()) {
                case 2:
                    priority.setText("Immediate Action Required");
                    break;
                case 4:
                    priority.setText("Poor");
                    break;
                case 6:
                    priority.setText("Fair");
                    break;
                case 8:
                    priority.setText("Good");
                    break;
                case 10:
                    priority.setText("Like New");
                    break;
                default:
                    priority.setText("N/A");
            }
        }
        catch(Exception e){
            priority.setText("N/A");
        }


        added.setText(item.getCreated_at());
        edited.setText(item.getUpdated_at());
        ArrayList<Image> imageItems = this.controller.getAllImagesByItemIDFull(this._Item_Id);
        this.gridView = (GridView) findViewById(R.id.gridView);
        this.gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        this.gridView.setAdapter(this.gridAdapter);
        this.gridView.setOnItemClickListener(new C01991());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                buildView();
            }
            if (resultCode != 0) {
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close) {
            setResult(-1, new Intent());
            finish();
        }
        if (id == R.id.edit) {
            String itemId = Integer.toString(this._Item_Id);
            Intent objIndent = new Intent(getApplicationContext(), ItemEdit.class);
            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
            startActivityForResult(objIndent, 1);
        }
        if (id == R.id.delete) {

            /*
            Builder alert = new Builder(this, R.style.MyDialogTheme);
            alert.setTitle((CharSequence) "Delete entry");
            alert.setMessage((CharSequence) "Are you sure you want to delete?");
            alert.setPositiveButton(Dialog.BUTTON_POSITIVE, new C02002());
            alert.setNegativeButton("Cancel", new C02013());
            alert.show();
            */

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemDetail.this.controller.deleteLocalItem(ItemDetail.this._Item_Id);
                    ItemDetail.this.controller.deleteLocalImage(ItemDetail.this._Item_Id);
                    ItemDetail.this.setResult(-1, new Intent());
                    ItemDetail.this.finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
    }

    public void viewMap(View view) {
        String floorId = Integer.toString(this._Floor_Id);
        Intent objIndent = new Intent(getApplicationContext(), ViewMapAdvanced.class);
        objIndent.putExtra("floor_id", Integer.parseInt(floorId));
        objIndent.putExtra("location", this.location.getText().toString());
        startActivityForResult(objIndent, REQUEST_SET_LOCATION);
    }

}
