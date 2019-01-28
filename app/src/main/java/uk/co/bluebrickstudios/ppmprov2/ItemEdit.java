package uk.co.bluebrickstudios.ppmprov2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.ActionSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.BuildingSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ClientSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.DefectSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.EstateSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.FCIScoreSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.FloorSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.GridViewAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.InspectionSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ItemTypeSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.PrioritySpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ProfileSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.StatusSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.TradeSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Action;
import uk.co.bluebrickstudios.ppmprov2.model.Building;
import uk.co.bluebrickstudios.ppmprov2.model.Client;
import uk.co.bluebrickstudios.ppmprov2.model.Defect;
import uk.co.bluebrickstudios.ppmprov2.model.Estate;
import uk.co.bluebrickstudios.ppmprov2.model.FCIScore;
import uk.co.bluebrickstudios.ppmprov2.model.Floor;
import uk.co.bluebrickstudios.ppmprov2.model.Image;
import uk.co.bluebrickstudios.ppmprov2.model.Inspection;
import uk.co.bluebrickstudios.ppmprov2.model.Item;
import uk.co.bluebrickstudios.ppmprov2.model.ItemType;
import uk.co.bluebrickstudios.ppmprov2.model.Priority;
import uk.co.bluebrickstudios.ppmprov2.model.Profile;
import uk.co.bluebrickstudios.ppmprov2.model.Status;
import uk.co.bluebrickstudios.ppmprov2.model.Trade;

public class ItemEdit extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {
    private static final String LOG;
    static final int REQUEST_SET_LOCATION = 2;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int _Item_Id;
    private Spinner action;
    ActionSpinnerAdapter actionAdapter;
    private TextView added;
    Button btnClose;
    Button btnDelete;
    Button btnSave;
    private Spinner building;
    BuildingSpinnerAdapter buildingAdapter;
    private Spinner client;
    ClientSpinnerAdapter clientAdapter;
    DatabaseHelper controller;
    private Spinner defect;
    DefectSpinnerAdapter defectAdapter;
    private EditText description;
    private TextView edited;
    private Spinner estate;
    EstateSpinnerAdapter estateAdapter;
    private Spinner floor;
    FloorSpinnerAdapter floorAdapter;
    private GridViewAdapter gridAdapter;
    private GridView gridView;
    ArrayList<Image> imageItems;
    Uri imageUri;
    Item item;
    ItemTypeSpinnerAdapter itemTypeAdapter;
    private Spinner itemtype;
    private EditText location;
    String mCurrentPhotoFilename;
    String mCurrentPhotoPath;
    File photoFile;
    private EditText podnumber;
    ProgressDialog prgDialogImage;
    private Spinner profile;
    ProfileSpinnerAdapter profileAdapter;
    private Spinner inspection;
    InspectionSpinnerAdapter inspectionAdapter;
    private Spinner status;
    StatusSpinnerAdapter statusAdapter;
    private Spinner trade;
    TradeSpinnerAdapter tradeAdapter;
    private Spinner priority;
    PrioritySpinnerAdapter priorityAdapter;
    //private Spinner fciScore;
    //FCIScoreSpinnerAdapter fciScoreAdapter;

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.1 */
    class C02021 implements OnItemClickListener {
        C02021() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemEdit.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemEdit.this.startActivity(intent);
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.2 */
    class C02032 implements DialogInterface.OnClickListener {
        C02032() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ItemEdit.this.controller.deleteLocalItem(ItemEdit.this._Item_Id);
            ItemEdit.this.controller.deleteLocalImage(ItemEdit.this._Item_Id);
            ItemEdit.this.setResult(-1, new Intent());
            ItemEdit.this.finish();
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.3 */
    class C02043 implements DialogInterface.OnClickListener {
        C02043() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.5 */
    class C02055 implements OnItemClickListener {
        C02055() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemEdit.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemEdit.this.startActivity(intent);
        }
    }

    public ItemEdit() {
        this._Item_Id = 0;
        this.controller = null;
    }

    static {
        LOG = ItemEdit.class.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.view_item_edit);
        this.controller = DatabaseHelper.getInstance(this);
        this.client = (Spinner) findViewById(R.id.client);
        this.estate = (Spinner) findViewById(R.id.estate);
        this.building = (Spinner) findViewById(R.id.building);
        this.floor = (Spinner) findViewById(R.id.floor);
        this.profile = (Spinner) findViewById(R.id.profile);
        this.inspection = (Spinner) findViewById(R.id.inspection);
        this.description = (EditText) findViewById(R.id.detailDescription);
        this.itemtype = (Spinner) findViewById(R.id.itemtype);
        this.defect = (Spinner) findViewById(R.id.defect);
        this.trade = (Spinner) findViewById(R.id.trade);
        this.action = (Spinner) findViewById(R.id.action);
        this.location = (EditText) findViewById(R.id.location);
        this.podnumber = (EditText) findViewById(R.id.podnumber);
        this.added = (TextView) findViewById(R.id.itemAdded);
        this.edited = (TextView) findViewById(R.id.itemEdited);
        this.status = (Spinner) findViewById(R.id.status);
        this.action.setOnItemSelectedListener(this);
        this.building.setOnItemSelectedListener(this);
        this.client.setOnItemSelectedListener(this);
        this.defect.setOnItemSelectedListener(this);
        this.estate.setOnItemSelectedListener(this);
        this.profile.setOnItemSelectedListener(this);
        this.inspection.setOnItemSelectedListener(this);
        this.floor.setOnItemSelectedListener(this);
        this.itemtype.setOnItemSelectedListener(this);
        this.status.setOnItemSelectedListener(this);
        this.trade.setOnItemSelectedListener(this);

        //this.fciScore = (Spinner) findViewById(R.id.fciscore);
        //this.fciScore.setOnItemSelectedListener(this);

        this.priority = (Spinner) findViewById(R.id.priority);
        this.priority.setOnItemSelectedListener(this);

        this._Item_Id = 0;
        this._Item_Id = getIntent().getIntExtra("item_Id", 0);
        this.item = new Item();
        this.item = this.controller.getItem((long) this._Item_Id);
        this.description.setText(this.item.getDescription());
        this.location.setText(this.item.getLocation());
        this.podnumber.setText(this.item.getPodnumber());
        this.added.setText(this.item.getCreated_at());
        this.edited.setText(this.item.getUpdated_at());
        populateActionSpinner();
        
        
        if (this.item.getFloor() != null) {
            populateBuildingSpinner(this.item.getFloor().getBuilding().getEstate_id());
        } else {
            populateBuildingSpinner(0);
        }
        PPMProApp app = (PPMProApp) getApplication();
        //if (app.isAdmin()) {
            //populateClientSpinner(0);
        //} else {
            populateClientSpinner(app.getClient_id());
        //}
        populateDefectSpinner();
        if (this.item.getFloor() != null) {
            populateEstateSpinner(this.item.getFloor().getBuilding().getEstate().getClient_id(), this.item.getFloor().getBuilding().getEstate().getId());
        } else {
            populateEstateSpinner(0, 0);
        }
        if (this.item.getFloor() != null) {
            populateFloorSpinner(this.item.getFloor().getBuilding_id());
        } else {
            populateFloorSpinner(0);
        }
        //if (app.isAdmin()) {
            //populateProfileSpinner(0, 0);
        //} else {
            populateProfileSpinner(app.getClient_id(), app.getEstate_id());
            populateInspectionSpinner(app.getClient_id(), app.getEstate_id());
        //}
        populateItemTypeSpinner();
        populateStatusSpinner();

        populatePrioritySpinner();
        //populateFCIScoreSpinner();
        
        populateTradeSpinner();
        ArrayList<Image> imageItems = this.controller.getAllImagesByItemIDFull(this._Item_Id);
        this.gridView = (GridView) findViewById(R.id.gridView);
        this.gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        this.gridView.setAdapter(this.gridAdapter);
        this.gridView.setOnItemClickListener(new C02021());
    }

    public void onResume() {
        super.onResume();
        if (this.prgDialogImage == null) {
            this.prgDialogImage = new ProgressDialog(this);
            this.prgDialogImage.setMessage("Transferring Image from device to remote database. Please wait...");
            this.prgDialogImage.setCancelable(false);
        }
    }

    public void onPause() {
        super.onPause();
        this.prgDialogImage.dismiss();
    }

    /*
    private void populateFCIScoreSpinner() {

        List<FCIScore> scores = new ArrayList<FCIScore>();
        scores.add(new FCIScore(0, "N/A"));
        scores.add(new FCIScore(2, "I.A.R."));
        scores.add(new FCIScore(4, "Poor"));
        scores.add(new FCIScore(6, "Fair"));
        scores.add(new FCIScore(8, "Good"));
        scores.add(new FCIScore(10, "Like New"));


        this.fciScoreAdapter = new FCIScoreSpinnerAdapter(this, android.R.layout.simple_spinner_item, scores);
        this.fciScore.setAdapter(this.fciScoreAdapter);
        this.fciScoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            //this.fciScore.setSelection(this.fciScoreAdapter.getItemPosition(this.item.getPriority()));
        } catch (Exception e) {
        }
    }
    */

    private void populatePrioritySpinner() {

        List<Priority> scores = new ArrayList<Priority>();
        scores.add(new Priority(0, "N/A"));
        scores.add(new Priority(2, "Immediate Action Required"));
        scores.add(new Priority(4, "Poor"));
        scores.add(new Priority(6, "Fair"));
        scores.add(new Priority(8, "Good"));
        scores.add(new Priority(10, "Like New"));

        this.priorityAdapter = new PrioritySpinnerAdapter(this, android.R.layout.simple_spinner_item, scores);
        this.priority.setAdapter(this.priorityAdapter);
        this.priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.priority.setSelection(this.priorityAdapter.getItemPosition(this.item.getPriority()));
        } catch (Exception e) {
        }

    }
    
    private void populateActionSpinner() {
        this.actionAdapter = new ActionSpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllActions());
        this.action.setAdapter(this.actionAdapter);
        this.actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.action.setSelection(this.actionAdapter.getItemPosition(this.item.getAction()));
        } catch (Exception e) {
        }
    }

    private void populateBuildingSpinner(int estate_id) {
        List<Building> buildings;
        if (estate_id > 0) {
            buildings = this.controller.getAllBuildings(estate_id);
        } else {
            buildings = this.controller.getAllBuildings();
        }
        this.buildingAdapter = new BuildingSpinnerAdapter(this, android.R.layout.simple_spinner_item, buildings);
        this.building.setAdapter(this.buildingAdapter);
        this.buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.building.setSelection(this.buildingAdapter.getItemPosition(this.item.getFloor().getBuilding()));
        } catch (Exception e) {
        }
    }

    private void populateClientSpinner(int client_id) {
        List<Client> clients;
        if (client_id > 0) {
            clients = this.controller.getAllClients(client_id);
        } else {
            clients = this.controller.getAllClients();
        }
        this.clientAdapter = new ClientSpinnerAdapter(this, android.R.layout.simple_spinner_item, clients);
        this.client.setAdapter(this.clientAdapter);
        this.clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.client.setSelection(this.clientAdapter.getItemPosition(this.item.getFloor().getBuilding().getEstate().getClient()));
        } catch (Exception e) {
        }
    }

    private void populateProfileSpinner(int client_id, int estate_id) {
        List<Profile> profiles;
        if (client_id > 0) {
            Log.d(LOG, "populateProfileSpinner client id = " + client_id + " estate id = " + estate_id);
            profiles = this.controller.getAllProfiles(client_id, estate_id);
        } else {
            profiles = this.controller.getAllProfiles();
        }
        this.profileAdapter = new ProfileSpinnerAdapter(this, android.R.layout.simple_spinner_item, profiles);
        this.profile.setAdapter(this.profileAdapter);
        this.profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (this.item.getProfile() != null) {
            try {
                this.profile.setSelection(this.profileAdapter.getItemPosition(this.item.getProfile()));
            } catch (Exception e) {
            }
        }
    }

    private void populateInspectionSpinner(int client_id, int estate_id) {
        List<Inspection> inspections;
        if (client_id > 0) {
            Log.d(LOG, "populateInspectionSpinner client id = " + client_id + " estate id = " + estate_id);
            //inspections = this.controller.getAllInspections(client_id, estate_id);
            inspections = this.controller.getCurrentInspections(client_id, estate_id);
        } else {
            inspections = this.controller.getAllInspections();
        }
        this.inspectionAdapter = new InspectionSpinnerAdapter(this, android.R.layout.simple_spinner_item, inspections);
        this.inspection.setAdapter(this.inspectionAdapter);
        this.inspectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (this.item.getInspection() != null) {
            try {
                this.inspection.setSelection(this.inspectionAdapter.getItemPosition(this.item.getInspection()));
            } catch (Exception e) {
            }
        }
    }

    private void populateDefectSpinner() {
        this.defectAdapter = new DefectSpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllDefects());
        this.defect.setAdapter(this.defectAdapter);
        this.defectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.defect.setSelection(this.defectAdapter.getItemPosition(this.item.getDefect()));
        } catch (Exception e) {
        }
    }

    private void populateEstateSpinner(int client_id, int estate_id) {
        List<Estate> estates;
        if (client_id > 0 && estate_id > 0) {
            Log.d(LOG, "populateEstateSpinner client id = " + client_id + " estate id = " + estate_id);
            estates = this.controller.getAllEstates(client_id, estate_id);
        }
        else if (client_id > 0) {
            Log.d(LOG, "populateEstateSpinner client id = " + client_id);
            estates = this.controller.getAllEstates(client_id);
        }
        else {
            estates = this.controller.getAllEstates();
        }
        this.estateAdapter = new EstateSpinnerAdapter(this, android.R.layout.simple_spinner_item, estates);
        this.estate.setAdapter(this.estateAdapter);
        this.estateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.estate.setSelection(this.estateAdapter.getItemPosition(this.item.getFloor().getBuilding().getEstate()));
        } catch (Exception e) {
        }
    }

    private void populateFloorSpinner(int building_id) {
        List<Floor> floors;
        if (building_id > 0) {
            floors = this.controller.getAllFloors(building_id);
        } else {
            floors = this.controller.getAllFloors();
        }
        this.floorAdapter = new FloorSpinnerAdapter(this, android.R.layout.simple_spinner_item, floors);
        this.floor.setAdapter(this.floorAdapter);
        this.floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.floor.setSelection(this.floorAdapter.getItemPosition(this.item.getFloor()));
        } catch (Exception e) {
        }
    }

    private void populateItemTypeSpinner() {
        this.itemTypeAdapter = new ItemTypeSpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllItemTypes());
        this.itemtype.setAdapter(this.itemTypeAdapter);
        this.itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.itemtype.setSelection(this.itemTypeAdapter.getItemPosition(this.item.getItemType()));
        } catch (Exception e) {
        }
    }

    private void populateStatusSpinner() {
        this.statusAdapter = new StatusSpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllStatuss());
        this.status.setAdapter(this.statusAdapter);
        this.statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.status.setSelection(this.statusAdapter.getItemPosition(this.item.getStatus()));
        } catch (Exception e) {
        }
    }

    private void populateTradeSpinner() {
        this.tradeAdapter = new TradeSpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllTrades());
        this.trade.setAdapter(this.tradeAdapter);
        this.tradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.trade.setSelection(this.tradeAdapter.getItemPosition(this.item.getTrade()));
        } catch (Exception e) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.close) {
            setResult(0, new Intent());
            finish();
        }
        if (id == R.id.save) {
            Item saveItem = new Item();
            saveItem.setId(this._Item_Id);
            saveItem.setDescription(this.description.getText().toString());
            saveItem.setLocation(this.location.getText().toString());
            saveItem.setPodnumber(this.podnumber.getText().toString());
            saveItem.setFloor_id(((Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition())).getId());
            saveItem.setBuilding_id(((Building) this.building.getItemAtPosition(this.building.getSelectedItemPosition())).getId());
            saveItem.setEstate_id(((Estate) this.estate.getItemAtPosition(this.estate.getSelectedItemPosition())).getId());
            saveItem.setClient_id(((Client) this.client.getItemAtPosition(this.client.getSelectedItemPosition())).getId());
            Profile saveProfile = (Profile) this.profile.getItemAtPosition(this.profile.getSelectedItemPosition());
            Log.d(LOG, "Profile ID = " + saveProfile.getName());
            saveItem.setProfile_id(saveProfile.getId());
            try{
                Inspection saveInspection = (Inspection) this.inspection.getItemAtPosition(this.inspection.getSelectedItemPosition());
                saveItem.setInspection_id(saveInspection.getId());
            }
            catch(Exception ignored){}
            saveItem.setItemtype_id(((ItemType) this.itemtype.getItemAtPosition(this.itemtype.getSelectedItemPosition())).getId());
            saveItem.setDefect_id(((Defect) this.defect.getItemAtPosition(this.defect.getSelectedItemPosition())).getId());
            saveItem.setTrade_id(((Trade) this.trade.getItemAtPosition(this.trade.getSelectedItemPosition())).getId());
            saveItem.setAction_id(((Action) this.action.getItemAtPosition(this.action.getSelectedItemPosition())).getId());
            saveItem.setStatus_id(((Status) this.status.getItemAtPosition(this.status.getSelectedItemPosition())).getId());
            saveItem.setPriority_id(((Priority) this.priority.getItemAtPosition(this.priority.getSelectedItemPosition())).getId());
            //saveItem.setFciscore_id(((FCIScore) this.fciScore.getItemAtPosition(this.fciScore.getSelectedItemPosition())).getId());
            saveItem.setUploaded(REQUEST_TAKE_PHOTO);
            Date dt = new Date();
            saveItem.setUpdated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt));
            long update_id = this.controller.updateItem(saveItem);
            Toast.makeText(this, "Item Record Updated", Toast.LENGTH_SHORT).show();
            String itemId = Integer.toString(this._Item_Id);
            Intent objIndent = new Intent();
            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
            setResult(-1, objIndent);
            finish();
        }
        if (id == R.id.addphoto) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setMessage("No photos of people permitted\n" +
                    "No photos of pictures or drawings permitted\n" +
                    "No photos of client branding permitted\n" +
                    "No photos of PC or AV screens permitted");
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Unable to take pictures - IOException Thrown", Toast.LENGTH_LONG).show();
                }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setBackgroundColor(0xFFD9534F);
            Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setBackgroundColor(0xFF5CB85C);
            pbutton.setTextColor(0xFFFFFFFF);
            pbutton.setFocusable(true);
            pbutton.setFocusableInTouchMode(true);
            pbutton.requestFocus();


        }
        if (id == R.id.delete) {
            Builder alert = new Builder(this);
            alert.setTitle((CharSequence) "Delete entry");
            alert.setMessage((CharSequence) "Are you sure you want to delete?");
            alert.setPositiveButton("OK", (DialogInterface.OnClickListener) new C02032());
            alert.setNegativeButton("Cancel", (DialogInterface.OnClickListener) new C02043());
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            this.photoFile = null;
            try {
                this.photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "Error occured taking picture", Toast.LENGTH_SHORT).show();
            }
            if (this.photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ItemEdit.this, "uk.co.bluebrickstudios.ppmprov2.provider", createImageFile());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SET_LOCATION && resultCode == -1) {

            this.location.setText(data.getStringExtra("location"));

            /* save floorplan image */
            if(!data.getStringExtra("floorplanimage").equals("NOIMAGE")){
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Image image = new Image();
                image.setName(data.getStringExtra("floorplanimage"));
                image.setFilename(data.getStringExtra("floorplanimage"));
                image.setThumbnail("thumb_" + data.getStringExtra("floorplanimage"));
                image.setFeatured(0);
                image.setItem_id(this._Item_Id);
                image.setCreated_at(sdf.format(dt));
                image.setUpdated_at(sdf.format(dt));
                image.setUploaded(REQUEST_SET_LOCATION);
                image.setLocalImageRef(data.getStringExtra("floorplanimage_path"));
                image.setId((int) this.controller.saveImage(image));
                Toast.makeText(this, "Floorplan image saved locally", Toast.LENGTH_SHORT).show();
                refreshGridView();
            }

            Toast.makeText(this, "Location added to item", Toast.LENGTH_SHORT).show();
        }
        if (requestCode != REQUEST_TAKE_PHOTO) {
            return;
        }
        if (resultCode == -1) {
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Image image = new Image();
            image.setName(this.mCurrentPhotoFilename);
            image.setFilename(this.mCurrentPhotoFilename);
            image.setThumbnail("thumb_" + this.mCurrentPhotoFilename);
            image.setFeatured(0);
            image.setItem_id(this._Item_Id);
            image.setCreated_at(sdf.format(dt));
            image.setUpdated_at(sdf.format(dt));
            image.setUploaded(REQUEST_SET_LOCATION);
            image.setLocalImageRef(this.mCurrentPhotoPath);
            image.setId((int) this.controller.saveImage(image));
            Toast.makeText(this, "Picture taken and saved locally", Toast.LENGTH_SHORT).show();
            refreshGridView();
        } else if (resultCode == 0) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {

        File image = File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        this.mCurrentPhotoFilename = image.getName();
        return image;

    }

    public void syncNewImage(String url, Image image) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();
        params.put("name", image.getName());
        params.put("filename", image.getFilename());
        params.put("thumbnail", image.getThumbnail());
        params.put("featured", image.getFeatured());
        params.put("item_id", this._Item_Id);
        params.put("device_imageid", image.getId());
        params.put("created_at", this.item.getCreated_at());
        params.put("updated_at", this.item.getUpdated_at());
        try {
            params.put("imagedata", this.photoFile);
        } catch (FileNotFoundException e) {
        }
        this.prgDialogImage.show();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {

                    prgDialogImage.hide();

                    String responseString = new String(responseBody, "UTF-8");

                    Gson gson = new GsonBuilder().create();
                    try {

                        JSONObject json = new JSONObject(responseString);
                        String device_imageid = json.getString("device_imageid");
                        String remote_imageid = json.getString("remote_imageid");

                        controller.updateLocalImage(Integer.parseInt(remote_imageid), Integer.parseInt(device_imageid));

                        refreshGridView();

                        Toast.makeText(getApplicationContext(), "Image added to remote server", Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Need to update item to remove uploaded flag
                    //controller.resetItemUploadStatus(item);

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                try {

                    String responseString = new String(responseBody, "UTF-8");

                } catch (Exception e) {
                }

                prgDialogImage.hide();

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void refreshGridView() {
        ArrayList<Image> imageItems = this.controller.getAllImagesByItemIDFull(this._Item_Id);
        this.gridView = (GridView) findViewById(R.id.gridView);
        this.gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        this.gridView.setAdapter(this.gridAdapter);
        this.gridView.setOnItemClickListener(new C02021());
    }

    public void onClick(View view) {
    }

    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        if (parentView == findViewById(R.id.client)) {
            PPMProApp app = (PPMProApp) getApplication();
            //if (app.isAdmin()) {
            //    populateEstateSpinner(((Client) parentView.getItemAtPosition(position)).getId(), 0);
            //    populateProfileSpinner(((Client) parentView.getItemAtPosition(position)).getId(), 0);
            //}
            //else{
                populateEstateSpinner(app.getClient_id(), app.getEstate_id());
                populateProfileSpinner(app.getClient_id(), app.getEstate_id());
            //}
            populateBuildingSpinner(0);
            populateFloorSpinner(0);
        } else if (parentView == findViewById(R.id.estate)) {
            populateBuildingSpinner(((Estate) parentView.getItemAtPosition(position)).getId());
            populateFloorSpinner(0);
        } else if (parentView == findViewById(R.id.building)) {
            populateFloorSpinner(((Building) parentView.getItemAtPosition(position)).getId());
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void viewMap(View view) {
        Floor saveFloor = (Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition());
        if (saveFloor != null) {
            String floorId = Integer.toString(saveFloor.getId());
            Intent objIndent = new Intent(getApplicationContext(), ViewMapAdvanced.class);
            objIndent.putExtra("floor_id", Integer.parseInt(floorId));
            objIndent.putExtra("location", this.location.getText().toString());
            startActivityForResult(objIndent, REQUEST_SET_LOCATION);
        }
    }

    public void clickAddImage(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("No photos of people permitted\n" +
                "No photos of pictures or drawings permitted\n" +
                "No photos of client branding permitted\n" +
                "No photos of PC or AV screens permitted");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Unable to take pictures - IOException Thrown", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(0xFFD9534F);
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setBackgroundColor(0xFF5CB85C);
        pbutton.setTextColor(0xFFFFFFFF);
        pbutton.setFocusable(true);
        pbutton.setFocusableInTouchMode(true);
        pbutton.requestFocus();

    }
}
