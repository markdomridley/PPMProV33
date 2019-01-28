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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.BuildingSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ClientSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.EstateSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.FCIScoreSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.FloorSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.GridViewAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.InspectionSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.PrioritySpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ProfileSpinnerAdapter;
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
import uk.co.bluebrickstudios.ppmprov2.pherialize.MixedArray;
import uk.co.bluebrickstudios.ppmprov2.pherialize.Pherialize;

public class ItemAddByProfile extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {
    private static final String LOG;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_SET_LOCATION = 2;
    private int _Item_Id;
    private Button[] actionButtons;
    private int actionNumber;
    private Spinner building;
    BuildingSpinnerAdapter buildingAdapter;

    private Button[] tradeButtons;
    private int tradeNumber;

    //private Spinner client;
    //ClientSpinnerAdapter clientAdapter;

    DatabaseHelper controller;
    private Button[] defectButtons;
    private int defectNumber;
    private int priorityID = 6;

    //private Spinner estate;
    //EstateSpinnerAdapter estateAdapter;

    private int client_id = 0;
    private int estate_id = 0;
    private Client client;
    private Estate estate;

    private TextView clientLabel;

    private Spinner floor;
    FloorSpinnerAdapter floorAdapter;
    Item item;
    ArrayList<Image> imageItems;
    private GridViewAdapter gridAdapter;
    private GridView gridView;
    private int itemTypeNumber;
    private Button[] itemtypeButtons;
    private EditText podnumber;
    ProgressDialog prgDialogImage;
    ProgressDialog prgDialogNew;

    private Spinner profile;
    ProfileSpinnerAdapter profileAdapter;
    private Spinner inspection;
    InspectionSpinnerAdapter inspectionAdapter;
    //private Spinner fciScore;
    //FCIScoreSpinnerAdapter fciScoreAdapter;

    private String selectedAction;
    private String selectedDefect;
    private String selectedItemtype;
    private String selectedTrade;
    private Profile selectedProfile;
    private Inspection selectedInspection;
    //private FCIScore selectedFCIScore;
    private EditText description;
    private EditText location;
    String mCurrentPhotoFilename;
    String mCurrentPhotoPath;
    File photoFile;
    Uri imageUri;

    private Button btn_fci_2;
    private Button btn_fci_4;
    private Button btn_fci_6;
    private Button btn_fci_8;
    private Button btn_fci_10;


    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemAdd.1 */
    class C01951 implements AdapterView.OnItemClickListener {
        C01951() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemAddByProfile.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemAddByProfile.this.startActivity(intent);
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.1 */
    class C02021 implements AdapterView.OnItemClickListener {
        C02021() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemAddByProfile.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemAddByProfile.this.startActivity(intent);
        }
    }


    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemAddByProfile.2 */
    class C01962 implements OnClickListener {
        final /* synthetic */ int val$finalI;

        C01962(int i) {
            this.val$finalI = i;
        }

        public void onClick(View v) {
            ItemAddByProfile.this.itemtypeButtons[this.val$finalI].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorGreen));
            ItemAddByProfile.this.itemtypeButtons[this.val$finalI].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
            ItemAddByProfile.this.selectedItemtype = ItemAddByProfile.this.itemtypeButtons[this.val$finalI].getText().toString();
            for (int j = 0; j < ItemAddByProfile.this.itemTypeNumber; j++) {
                if (ItemAddByProfile.this.itemtypeButtons[j].getId() != ItemAddByProfile.this.itemtypeButtons[this.val$finalI].getId()) {
                    ItemAddByProfile.this.itemtypeButtons[j].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorPrimary));
                    ItemAddByProfile.this.itemtypeButtons[j].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
                }
            }
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemAddByProfile.3 */
    class C01973 implements OnClickListener {
        final /* synthetic */ int val$finalI;

        C01973(int i) {
            this.val$finalI = i;
        }

        public void onClick(View v) {
            ItemAddByProfile.this.defectButtons[this.val$finalI].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorGreen));
            ItemAddByProfile.this.defectButtons[this.val$finalI].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
            ItemAddByProfile.this.selectedDefect = ItemAddByProfile.this.defectButtons[this.val$finalI].getText().toString();
            for (int j = 0; j < ItemAddByProfile.this.defectNumber; j++) {
                if (ItemAddByProfile.this.defectButtons[j].getId() != ItemAddByProfile.this.defectButtons[this.val$finalI].getId()) {
                    ItemAddByProfile.this.defectButtons[j].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorPrimary));
                    ItemAddByProfile.this.defectButtons[j].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
                }
            }
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemAddByProfile.4 */
    class C01984 implements OnClickListener {
        final /* synthetic */ int val$finalI;

        C01984(int i) {
            this.val$finalI = i;
        }

        public void onClick(View v) {
            ItemAddByProfile.this.actionButtons[this.val$finalI].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorGreen));
            ItemAddByProfile.this.actionButtons[this.val$finalI].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
            ItemAddByProfile.this.selectedAction = ItemAddByProfile.this.actionButtons[this.val$finalI].getText().toString();
            for (int j = 0; j < ItemAddByProfile.this.actionNumber; j++) {
                if (ItemAddByProfile.this.actionButtons[j].getId() != ItemAddByProfile.this.actionButtons[this.val$finalI].getId()) {
                    ItemAddByProfile.this.actionButtons[j].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorPrimary));
                    ItemAddByProfile.this.actionButtons[j].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
                }
            }
        }
    }

    class TradeListener implements OnClickListener {
        final /* synthetic */ int val$finalI;

        TradeListener(int i) {
            this.val$finalI = i;
        }

        public void onClick(View v) {
            ItemAddByProfile.this.tradeButtons[this.val$finalI].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorGreen));
            ItemAddByProfile.this.tradeButtons[this.val$finalI].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
            ItemAddByProfile.this.selectedTrade = ItemAddByProfile.this.tradeButtons[this.val$finalI].getText().toString();
            for (int j = 0; j < ItemAddByProfile.this.tradeNumber; j++) {
                if (ItemAddByProfile.this.tradeButtons[j].getId() != ItemAddByProfile.this.tradeButtons[this.val$finalI].getId()) {
                    ItemAddByProfile.this.tradeButtons[j].setBackgroundColor(ItemAddByProfile.this.getResources().getColor(R.color.colorPrimary));
                    ItemAddByProfile.this.tradeButtons[j].setTextColor(ItemAddByProfile.this.getResources().getColor(R.color.textColorPrimary));
                }
            }
        }
    }

    /* renamed from: uk.co.bluebrickstudios.ppmprov2.ItemEdit.5 */
    class C02055 implements AdapterView.OnItemClickListener {
        C02055() {
        }

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Image item = (Image) parent.getItemAtPosition(position);
            Intent intent = new Intent(ItemAddByProfile.this, ImageDetail.class);
            intent.putExtra("image_id", item.getId());
            ItemAddByProfile.this.startActivity(intent);
        }
    }

    public ItemAddByProfile() {
        this._Item_Id = 0;
        this.controller = null;
        this.itemTypeNumber = 0;
        this.defectNumber = 0;
        this.actionNumber = 0;
        this.tradeNumber = 0;
    }

    static {
        LOG = ItemAddByProfile.class.getName();
    }

    public void clickFCI_2(View view){

        this.priorityID = 2;

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.textColorPrimary));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.navigationBarColor));

    }

    public void clickFCI_4(View view){

        this.priorityID = 4;

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.textColorPrimary));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.navigationBarColor));

    }

    public void clickFCI_6(View view){

        this.priorityID = 6;

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.textColorPrimary));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.navigationBarColor));

    }

    public void clickFCI_8(View view){

        this.priorityID = 8;

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.textColorPrimary));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.navigationBarColor));

    }

    public void clickFCI_10(View view){

        this.priorityID = 10;

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.textColorPrimary));

    }

    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate(Bundle savedInstanceState)");

        super.onCreate(savedInstanceState);
        //setContentView((int) R.layout.view_item_profile_edit);
        setContentView((int) R.layout.add_item_by_profile);
        this.controller = DatabaseHelper.getInstance(this);

        this.btn_fci_2 = (Button) findViewById(R.id.btn_fci_2);
        this.btn_fci_4 = (Button) findViewById(R.id.btn_fci_4);
        this.btn_fci_6 = (Button) findViewById(R.id.btn_fci_6);
        this.btn_fci_8 = (Button) findViewById(R.id.btn_fci_8);
        this.btn_fci_10 = (Button) findViewById(R.id.btn_fci_10);

        this.btn_fci_2.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_4.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_6.setTextColor(getResources().getColor(R.color.navigationBarColor));
        this.btn_fci_8.setTextColor(getResources().getColor(R.color.textColorPrimary));
        this.btn_fci_10.setTextColor(getResources().getColor(R.color.navigationBarColor));

        //this.client = (Spinner) findViewById(R.id.client);
        //this.estate = (Spinner) findViewById(R.id.estate);
        this.clientLabel = (TextView) findViewById(R.id.labelClient);

        this.building = (Spinner) findViewById(R.id.building);
        this.floor = (Spinner) findViewById(R.id.floor);
        this.profile = (Spinner) findViewById(R.id.profile);
        this.inspection = (Spinner) findViewById(R.id.inspection);
        //this.fciScore = (Spinner) findViewById(R.id.fciScore);
        this.description = (EditText) findViewById(R.id.detailDescription);
        this.location = (EditText) findViewById(R.id.location);
        this.podnumber = (EditText) findViewById(R.id.detailPodnumber);
        this.building.setOnItemSelectedListener(this);

        //this.client.setOnItemSelectedListener(this);
        //this.estate.setOnItemSelectedListener(this);

        this.floor.setOnItemSelectedListener(this);
        this.profile.setOnItemSelectedListener(this);

        this.inspection.setOnItemSelectedListener(this);
        //this.fciScore.setOnItemSelectedListener(this);

        //this.priority = (Spinner) findViewById(R.id.priority);
        //this.priority.setOnItemSelectedListener(this);

        this._Item_Id = 0;
        this._Item_Id = getIntent().getIntExtra("item_Id", 0);
        this.item = new Item();

        PPMProApp app = (PPMProApp) getApplication();

        //populateClientSpinner(app.getClient_id());
        //populateEstateSpinner(app.getClient_id(), app.getEstate_id());

        populateInspectionSpinner(app.getClient_id(), app.getEstate_id());

        populateBuildingSpinner(app.getEstate_id());
        populateFloorSpinner(0);
        populateFCIScoreSpinner();
        populateProfileSpinner(app.getClient_id(), app.getEstate_id());

        //populatePrioritySpinner();

        ArrayList<Image> imageItems = this.controller.getAllImagesByItemID(this._Item_Id);
        this.gridView = (GridView) findViewById(R.id.gridView);
        this.gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        this.gridView.setAdapter(this.gridAdapter);
        this.gridView.setOnItemClickListener(new ItemAddByProfile.C01951());

        try {
            //this.client.setSelection(this.clientAdapter.getItemPositionByID(app.getClient_id()));
            //this.estate.setSelection(this.estateAdapter.getItemPositionByID(app.getEstate_id()));

            this.client_id = app.getClient_id();
            this.estate_id = app.getEstate_id();

            this.client = controller.getClient(this.client_id);
            this.estate = controller.getEstate(this.estate_id);

            this.clientLabel.setText(client.getName() + " > " + estate.getName());

            if(app.getBuilding_id() > 0){
                this.building.setSelection(this.buildingAdapter.getItemPositionByID(app.getBuilding_id()));
            }
            if(app.getFloor_id() > 0){
                this.floor.setSelection(this.floorAdapter.getItemPositionByID(app.getFloor_id()));
            }
            if(app.getProfile_id() > 0){
                this.profile.setSelection(this.profileAdapter.getItemPositionByID(app.getProfile_id()));
            }
            if(app.getInspection_id() > 0){
                this.inspection.setSelection(this.inspectionAdapter.getItemPositionByID(app.getInspection_id()));
            }

            Log.d(LOG, "ON START");
            Log.d(LOG, "Client ID = " + app.getClient_id());
            Log.d(LOG, "Estate ID = " + app.getEstate_id());
            Log.d(LOG, "Building ID = " + app.getBuilding_id());
            Log.d(LOG, "Floor ID = " + app.getFloor_id());
            Log.d(LOG, "Profile ID = " + app.getProfile_id());
            Log.d(LOG, "Inspection ID = " + app.getInspection_id());
            Log.d(LOG, "Status ID = " + app.getStatus_id());
            Log.d(LOG, "Itemtype ID = " + app.getItemtype_id());
            Log.d(LOG, "Defect ID = " + app.getDefect_id());
            Log.d(LOG, "Trade ID = " + app.getTrade_id());
            Log.d(LOG, "Action ID = " + app.getAction_id());

        } catch (Exception e) {
        }

    }

    protected void onStart() {

        super.onStart();
        Log.d(LOG, "onStart()");

        /*
        try {
            PPMProApp app = (PPMProApp) getApplication();
            this.client.setSelection(this.clientAdapter.getItemPositionByID(app.getClient_id()));
            this.estate.setSelection(this.estateAdapter.getItemPositionByID(app.getEstate_id()));
            this.building.setSelection(this.buildingAdapter.getItemPositionByID(app.getBuilding_id()));
            this.floor.setSelection(this.floorAdapter.getItemPositionByID(app.getFloor_id()));
            this.profile.setSelection(this.profileAdapter.getItemPositionByID(app.getProfile_id()));

            Log.d(LOG, "ON START");
            Log.d(LOG, "Client ID = " + app.getClient_id());
            Log.d(LOG, "Estate ID = " + app.getEstate_id());
            Log.d(LOG, "Building ID = " + app.getBuilding_id());
            Log.d(LOG, "Floor ID = " + app.getFloor_id());
            Log.d(LOG, "Profile ID = " + app.getProfile_id());
            Log.d(LOG, "Status ID = " + app.getStatus_id());
            Log.d(LOG, "Itemtype ID = " + app.getItemtype_id());
            Log.d(LOG, "Defect ID = " + app.getDefect_id());
            Log.d(LOG, "Trade ID = " + app.getTrade_id());
            Log.d(LOG, "Action ID = " + app.getAction_id());

        } catch (Exception e) {
        }
        */

    }

    public void onResume() {

        Log.d(LOG, "onResume()");

        super.onResume();
        if (this.prgDialogNew == null) {
            this.prgDialogNew = new ProgressDialog(this);
            this.prgDialogNew.setMessage("Transferring Image from device to remote database. Please wait...");
            this.prgDialogNew.setCancelable(false);
        }
    }

    public void onPause() {
        super.onPause();
        this.prgDialogNew.dismiss();
    }

    /*
    private void populatePrioritySpinner() {

        Log.d(LOG, "populatePrioritySpinner(int)");

        this.priorityAdapter = new PrioritySpinnerAdapter(this, android.R.layout.simple_spinner_item, this.controller.getAllPrioritys());
        this.priority.setAdapter(this.priorityAdapter);
        this.priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.priority.setSelection(this.priorityAdapter.getItemPosition(this.item.getPriority()));
        } catch (Exception e) {
        }
    }
    */

    private void populateFCIScoreSpinner() {

        List<FCIScore> scores = new ArrayList<FCIScore>();
        scores.add(new FCIScore(0, "N/A"));
        scores.add(new FCIScore(2, "I.A.R."));
        scores.add(new FCIScore(4, "Poor"));
        scores.add(new FCIScore(6, "Fair"));
        scores.add(new FCIScore(8, "Good"));
        scores.add(new FCIScore(10, "Like New"));

        /*
        this.fciScoreAdapter = new FCIScoreSpinnerAdapter(this, android.R.layout.simple_spinner_item, scores);
        this.fciScore.setAdapter(this.fciScoreAdapter);
        this.fciScoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            //this.fciScore.setSelection(this.fciScoreAdapter.getItemPosition(this.item.getPriority()));
        } catch (Exception e) {
        }
        */

    }

    private void populateBuildingSpinner(int estate_id) {

        Log.d(LOG, "populateBuildingSpinner(" + estate_id + ")" );

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
            Inspection inspection = (Inspection) this.inspection.getItemAtPosition(this.inspection.getSelectedItemPosition());

            if(inspection.getBuilding_id() > 0){
                Building building = this.controller.getBuilding(inspection.getBuilding_id());
                this.building.setSelection(this.buildingAdapter.getItemPosition(building));
            }
            else{
                this.building.setSelection(this.buildingAdapter.getItemPosition(this.item.getFloor().getBuilding()));
            }
        } catch (Exception e) {
            try {
                this.building.setSelection(this.buildingAdapter.getItemPosition(this.item.getFloor().getBuilding()));
            }catch(Exception ignored){}
        }

    }

    /*
    private void populateClientSpinner(int client_id) {

        Log.d(LOG, "populateClientSpinner(int)");

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

    private void populateEstateSpinner(int client_id, int estate_id) {

        Log.d(LOG, "populateEstateSpinner(int)");

        List<Estate> estates;
        if (client_id > 0 && estate_id > 0) {
            estates = this.controller.getAllEstates(client_id, estate_id);
        }
        else if (client_id > 0) {
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
    */

    private void populateFloorSpinner(int building_id) {
        List<Floor> floors;
        if (building_id > 0) {
            floors = this.controller.getAllFloors(building_id);
            Log.d(LOG, "populateFloorSpinner(int)");
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


        try {
            Inspection inspection = (Inspection) this.inspection.getItemAtPosition(this.inspection.getSelectedItemPosition());
            if(inspection.getProfile_id() > 0){
                Profile profile = this.controller.getProfile(inspection.getProfile_id());
                this.profile.setSelection(this.profileAdapter.getItemPosition(profile));
            }
            else{
                this.profile.setSelection(this.profileAdapter.getItemPosition(this.item.getProfile()));
            }
        } catch (Exception e) {
            try{
                this.profile.setSelection(this.profileAdapter.getItemPosition(this.item.getProfile()));
            }catch(Exception ignored){

            }
        }
    }

    private void populateInspectionSpinner(int client_id, int estate_id) {
        List<Inspection> inspections;

        if (client_id > 0) {
            Log.d(LOG, "populateInspectionSpinner client id = " + client_id + " estate id = " + estate_id);
            inspections = this.controller.getCurrentInspections(client_id, estate_id);

        } else {
            inspections = this.controller.getAllInspections();
        }

        this.inspectionAdapter = new InspectionSpinnerAdapter(this, android.R.layout.simple_spinner_item, inspections);
        this.inspection.setAdapter(this.inspectionAdapter);
        this.inspectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.inspection.setSelection(this.inspectionAdapter.getItemPosition(this.item.getInspection()));
        } catch (Exception e) {
        }
    }

    public void saveQuickItem(View view) {
        try{
            //if (this.podnumber.getText().toString().length() > 0) {
                if (this.selectedItemtype.length() > 0) {
                    if (this.selectedDefect.length() > 0) {
                        if (this.selectedAction.length() > 0) {

                            Item saveItem = new Item();
                            saveItem.setId(this._Item_Id);
                            saveItem.setDescription(this.description.getText().toString());
                            saveItem.setLocation(this.location.getText().toString());
                            saveItem.setPodnumber(this.podnumber.getText().toString());
                            /*
                            saveItem.setFloor_id(((Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition())).getId());
                            saveItem.setBuilding_id(((Building) this.building.getItemAtPosition(this.building.getSelectedItemPosition())).getId());
                            saveItem.setEstate_id(((Estate) this.estate.getItemAtPosition(this.estate.getSelectedItemPosition())).getId());
                            saveItem.setClient_id(((Client) this.client.getItemAtPosition(this.client.getSelectedItemPosition())).getId());
                            */
                            Floor saveFloor = (Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition());
                            saveItem.setFloor_id(saveFloor.getId());
                            Building saveBuilding = (Building) this.building.getItemAtPosition(this.building.getSelectedItemPosition());
                            saveItem.setBuilding_id(saveBuilding.getId());

                            saveItem.setEstate_id(this.estate_id);
                            saveItem.setClient_id(this.client_id);

                            //Priority savePriority = (Priority) this.priority.getItemAtPosition(this.priority.getSelectedItemPosition());
                            saveItem.setPriority_id(priorityID);

                            saveItem.setProfile_id(((Profile) this.profile.getItemAtPosition(this.profile.getSelectedItemPosition())).getId());
                            try{
                                saveItem.setInspection_id(((Inspection) this.inspection.getItemAtPosition(this.inspection.getSelectedItemPosition())).getId());
                            }
                            catch(Exception ignored){}
                            //saveItem.setFciscore_id(((FCIScore) this.fciScore.getItemAtPosition(this.fciScore.getSelectedItemPosition())).getId());

                            saveItem.setItemtype_id(this.controller.getItemType(this.selectedItemtype).getId());
                            Log.d(LOG, "Itemtype ID = " + saveItem.getItemtype_id());
                            saveItem.setDefect_id(this.controller.getDefect(this.selectedDefect).getId());
                            saveItem.setAction_id(this.controller.getAction(this.selectedAction).getId());
                            //saveItem.setTrade_id(37);
                            saveItem.setTrade_id(this.controller.getTrade(this.selectedTrade).getId());
                            saveItem.setStatus_id(1);
                            saveItem.setUploaded(REQUEST_SET_LOCATION);

                            String identifier = "";
                            Date dt = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                            long unixTime = System.currentTimeMillis() / 1000L;

                            identifier = identifier + this.client.getName().substring(0, 3) + "-";
                            identifier = identifier + this.estate.getName().substring(0, 3) + "-" + unixTime;

                            saveItem.setGuid(identifier.toUpperCase());

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            saveItem.setCreated_at(sdf.format(dt));
                            saveItem.setUpdated_at(sdf.format(dt));

                            long update_id = this.controller.saveItem(saveItem);

                            /* pick up any added images and update them */
                            ArrayList<Image> newImages = this.controller.getAllImagesByItemID(-1);
                            for(int i = 0; i < newImages.size(); i++){
                                Image newImage = newImages.get(i);
                                newImage.setItem_id((int)update_id);
                                this.controller.updateImageItemID(newImage.getId(), (int)update_id);
                            }

                            Toast.makeText(this, "Item Record Added", Toast.LENGTH_SHORT).show();
                            this.podnumber.setText("");
                            this.location.setText("");
                            this.description.setText("");
                            this.selectedAction = BuildConfig.FLAVOR;
                            this.selectedDefect = BuildConfig.FLAVOR;
                            this.selectedItemtype = BuildConfig.FLAVOR;
                            refreshGridView();
                            resetButtons();
                            return;
                        }
                    }
                }
            //}
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Please ensure you add a POD number and select Item Type, Defect and Action", Toast.LENGTH_SHORT).show();

        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_edit_profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(LOG, "onOptionsItemSelected(MenuItem item)");

        int id = item.getItemId();
        if (id == R.id.close) {
            setResult(0, new Intent());
            finish();
        }
        if (id == R.id.save) {
            Floor saveFloor = (Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition());
            Building saveBuilding = (Building) this.building.getItemAtPosition(this.building.getSelectedItemPosition());
            Profile saveProfile = (Profile) this.profile.getItemAtPosition(this.profile.getSelectedItemPosition());

            PPMProApp app = (PPMProApp) getApplication();
            app.setClient_id(this.client_id);
            app.setEstate_id(this.estate_id);
            app.setBuilding_id(saveBuilding.getId());
            app.setFloor_id(saveFloor.getId());
            app.setProfile_id(saveProfile.getId());
            try {
                Inspection saveInspection = (Inspection) this.inspection.getItemAtPosition(this.inspection.getSelectedItemPosition());
                app.setInspection_id(saveInspection.getId());
            }
            catch(Exception ignored){
            }
            String itemId = Integer.toString(this._Item_Id);
            setResult(-1, new Intent());
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
        return super.onOptionsItemSelected(item);
    }

    public void syncNewItem(String url, Item item) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();
        params.put("device_itemid", item.getId());
        params.put("name", item.getName());
        params.put("description", item.getDescription());
        params.put("notes", item.getNotes());
        params.put("guid", item.getGuid());
        params.put("lastaction", item.getLastaction());
        params.put("location", item.getLocation());
        params.put("user_id", ((PPMProApp) getApplication()).getUserId());
        params.put("status_id", Integer.toString(item.getStatus_id()));
        params.put("priority_id", Integer.toString(item.getPriority_id()));
        params.put("defect_id", Integer.toString(item.getDefect_id()));
        params.put("itemtype_id", Integer.toString(item.getItemtype_id()));
        params.put("trade_id", Integer.toString(item.getTrade_id()));
        params.put("action_id", Integer.toString(item.getAction_id()));
        params.put("floor_id", Integer.toString(item.getFloor_id()));
        params.put("building_id", Integer.toString(item.getBuilding_id()));
        params.put("estate_id", Integer.toString(item.getEstate_id()));
        params.put("client_id", Integer.toString(item.getClient_id()));
        params.put("profile_id", Integer.toString(item.getProfile_id()));
        params.put("inspection_id", Integer.toString(item.getInspection_id()));
        //params.put("fciscore_id", Integer.toString(item.getFciscore_id()));
        params.put("podnumber", item.getPodnumber());
        params.put("created_at", item.getCreated_at());
        params.put("updated_at", item.getUpdated_at());
        this.prgDialogNew.show();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                prgDialogNew.hide();
                try {
                    String responseString = new String(responseBody, "UTF-8");


                    Gson gson = new GsonBuilder().create();
                    try {

                        JSONObject json = new JSONObject(responseString);
                        String device_itemid = json.getString("device_itemid");
                        String remote_itemid = json.getString("remote_itemid");

                        // delete local item
                        controller.deleteLocalItem(Integer.parseInt(device_itemid));

                        Toast.makeText(getApplicationContext(), "Item added to remote server", Toast.LENGTH_SHORT).show();


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

                prgDialogNew.hide();
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

    public void onClick(View view) {
    }

    public void resetButtons() {
        int j;
        for (j = 0; j < this.itemtypeButtons.length; j++) {
            this.itemtypeButtons[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.itemtypeButtons[j].setTextColor(getResources().getColor(R.color.textColorPrimary));
        }
        for (j = 0; j < this.defectButtons.length; j++) {
            this.defectButtons[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.defectButtons[j].setTextColor(getResources().getColor(R.color.textColorPrimary));
        }
        for (j = 0; j < this.actionButtons.length; j++) {
            this.actionButtons[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.actionButtons[j].setTextColor(getResources().getColor(R.color.textColorPrimary));
        }
        for (j = 0; j < this.tradeButtons.length; j++) {
            this.tradeButtons[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.tradeButtons[j].setTextColor(getResources().getColor(R.color.textColorPrimary));
        }
    }

    public void clearButtons() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.column1);
        if (this.itemtypeButtons != null) {
            for (View removeView : this.itemtypeButtons) {
                ll.removeView(removeView);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column2);
        if (this.defectButtons != null) {
            for (View removeView2 : this.defectButtons) {
                ll.removeView(removeView2);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column3);
        if (this.actionButtons != null) {
            for (View removeView22 : this.actionButtons) {
                ll.removeView(removeView22);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column4);
        if (this.tradeButtons != null) {
            for (View removeView222 : this.tradeButtons) {
                ll.removeView(removeView222);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column12);
        if (this.itemtypeButtons != null) {
            for (View removeView : this.itemtypeButtons) {
                ll.removeView(removeView);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column22);
        if (this.defectButtons != null) {
            for (View removeView2 : this.defectButtons) {
                ll.removeView(removeView2);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column32);
        if (this.actionButtons != null) {
            for (View removeView22 : this.actionButtons) {
                ll.removeView(removeView22);
            }
        }
        ll = (LinearLayout) findViewById(R.id.column42);
        if (this.tradeButtons != null) {
            for (View removeView222 : this.tradeButtons) {
                ll.removeView(removeView222);
            }
        }
    }

    public void setButtons() {

        Log.d(LOG, "CALLED SET BUTTONS");

        int i;
        this.selectedProfile = (Profile) this.profile.getItemAtPosition(this.profile.getSelectedItemPosition());
        clearButtons();

        MixedArray itemtypes = Pherialize.unserialize(this.selectedProfile.getItemtypes()).toArray();
        this.itemTypeNumber = itemtypes.size();

        LinearLayout ll = (LinearLayout) findViewById(R.id.column1);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.topMargin = 20;
        lp.rightMargin = 20;
        ll.setOrientation(LinearLayout.VERTICAL);

        if(this.itemTypeNumber >= 2){

            int halfWay = this.itemTypeNumber/2;

            this.itemtypeButtons = new Button[this.itemTypeNumber];

            //Log.d(LOG, this.itemTypeNumber + "/ 2 = " + halfWay);

            for (i = 0; i < halfWay; i++) {
                this.itemtypeButtons[i] = new Button(this);
                this.itemtypeButtons[i].setText(itemtypes.getString(i));
                this.itemtypeButtons[i].setId(i);
                this.itemtypeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.itemtypeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.itemtypeButtons[i].setOnClickListener(new C01962(i));
                ll.addView(this.itemtypeButtons[i], lp);
            }

            ll = (LinearLayout) findViewById(R.id.column12);
            lp = new LayoutParams(-1, -1);
            lp.topMargin = 20;
            lp.rightMargin = 20;
            ll.setOrientation(LinearLayout.VERTICAL);

            for (i = halfWay; i < this.itemTypeNumber; i++) {
                this.itemtypeButtons[i] = new Button(this);
                this.itemtypeButtons[i].setText(itemtypes.getString(i));
                this.itemtypeButtons[i].setId(i);
                this.itemtypeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.itemtypeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.itemtypeButtons[i].setOnClickListener(new C01962(i));
                ll.addView(this.itemtypeButtons[i], lp);
            }

        }
        else{

            this.itemtypeButtons = new Button[this.itemTypeNumber];
            for (i = 0; i < this.itemTypeNumber; i++) {
                this.itemtypeButtons[i] = new Button(this);
                this.itemtypeButtons[i].setText(itemtypes.getString(i));
                this.itemtypeButtons[i].setId(i);
                this.itemtypeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.itemtypeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.itemtypeButtons[i].setOnClickListener(new C01962(i));
                ll.addView(this.itemtypeButtons[i], lp);
            }
        }

        MixedArray defects = Pherialize.unserialize(this.selectedProfile.getDefects()).toArray();
        this.defectNumber = defects.size();

        ll = (LinearLayout) findViewById(R.id.column2);
        lp = new LayoutParams(-1, -1);
        lp.topMargin = 20;
        lp.rightMargin = 20;
        ll.setOrientation(LinearLayout.VERTICAL);

        if(this.defectNumber >= 2) {

            int halfWay = this.defectNumber / 2;

            this.defectButtons = new Button[this.defectNumber];


            for (i = 0; i < halfWay; i++) {
                this.defectButtons[i] = new Button(this);
                this.defectButtons[i].setText(defects.getString(i));
                this.defectButtons[i].setId(i);
                this.defectButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.defectButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.defectButtons[i].setOnClickListener(new C01973(i));
                ll.addView(this.defectButtons[i], lp);
            }

            ll = (LinearLayout) findViewById(R.id.column22);
            lp = new LayoutParams(-1, -1);
            lp.topMargin = 20;
            lp.rightMargin = 20;
            ll.setOrientation(LinearLayout.VERTICAL);

            for (i = halfWay; i < this.defectNumber; i++) {
                this.defectButtons[i] = new Button(this);
                this.defectButtons[i].setText(defects.getString(i));
                this.defectButtons[i].setId(i);
                this.defectButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.defectButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.defectButtons[i].setOnClickListener(new C01973(i));
                ll.addView(this.defectButtons[i], lp);
            }


        }
        else{

            this.defectButtons = new Button[this.defectNumber];

            for (i = 0; i < this.defectNumber; i++) {
                this.defectButtons[i] = new Button(this);
                this.defectButtons[i].setText(defects.getString(i));
                this.defectButtons[i].setId(i);
                this.defectButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.defectButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.defectButtons[i].setOnClickListener(new C01973(i));
                ll.addView(this.defectButtons[i], lp);
            }
        }

        MixedArray actions = Pherialize.unserialize(this.selectedProfile.getActions()).toArray();
        this.actionNumber = actions.size();

        ll = (LinearLayout) findViewById(R.id.column3);
        lp = new LayoutParams(-1, -1);
        lp.topMargin = 20;
        lp.rightMargin = 20;
        ll.setOrientation(LinearLayout.VERTICAL);

        if(this.actionNumber >= 2) {

            int halfWay = this.actionNumber / 2;

            this.actionButtons = new Button[this.actionNumber];

            for (i = 0; i < halfWay; i++) {
                this.actionButtons[i] = new Button(this);
                this.actionButtons[i].setText(actions.getString(i));
                this.actionButtons[i].setId(i);
                this.actionButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.actionButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.actionButtons[i].setOnClickListener(new C01984(i));
                ll.addView(this.actionButtons[i], lp);
            }

            ll = (LinearLayout) findViewById(R.id.column32);
            lp = new LayoutParams(-1, -1);
            lp.topMargin = 20;
            lp.rightMargin = 20;
            ll.setOrientation(LinearLayout.VERTICAL);

            for (i = halfWay; i < this.actionNumber; i++) {
                this.actionButtons[i] = new Button(this);
                this.actionButtons[i].setText(actions.getString(i));
                this.actionButtons[i].setId(i);
                this.actionButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.actionButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.actionButtons[i].setOnClickListener(new C01984(i));
                ll.addView(this.actionButtons[i], lp);
            }
        }
        else {

            this.actionButtons = new Button[this.actionNumber];

            for (i = 0; i < this.actionNumber; i++) {
                this.actionButtons[i] = new Button(this);
                this.actionButtons[i].setText(actions.getString(i));
                this.actionButtons[i].setId(i);
                this.actionButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                this.actionButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.actionButtons[i].setOnClickListener(new C01984(i));
                ll.addView(this.actionButtons[i], lp);
            }
        }


        MixedArray trades = null;

        Log.d(LOG, "trades" + this.selectedProfile.getTrades());

        if(this.selectedProfile.getTrades() != null && this.selectedProfile.getTrades().length() > 0 && !this.selectedProfile.getTrades().equals("null")){

            trades = Pherialize.unserialize(this.selectedProfile.getTrades()).toArray();

            Log.d(LOG, "HAS TRADES: Number of trades " + trades.size());

            this.tradeNumber = trades.size();

            ll = (LinearLayout) findViewById(R.id.column4);
            lp = new LayoutParams(-1, -1);
            lp.topMargin = 20;
            lp.rightMargin = 20;
            ll.setOrientation(LinearLayout.VERTICAL);

            if(this.tradeNumber >= 2) {

                int halfWay = this.tradeNumber / 2;

                this.tradeButtons = new Button[this.tradeNumber];

                for (i = 0; i < halfWay; i++) {
                    this.tradeButtons[i] = new Button(this);
                    this.tradeButtons[i].setText(trades.getString(i));
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                }

                ll = (LinearLayout) findViewById(R.id.column42);
                lp = new LayoutParams(-1, -1);
                lp.topMargin = 20;
                lp.rightMargin = 20;
                ll.setOrientation(LinearLayout.VERTICAL);

                for (i = halfWay; i < this.tradeNumber; i++) {
                    this.tradeButtons[i] = new Button(this);
                    this.tradeButtons[i].setText(trades.getString(i));
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                }
            }
            else {

                this.tradeButtons = new Button[this.tradeNumber];

                for (i = 0; i < this.tradeNumber; i++) {
                    this.tradeButtons[i] = new Button(this);
                    this.tradeButtons[i].setText(trades.getString(i));
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                }
            }

        }
        else{
            List<Trade> allTrades = this.controller.getAllTrades();

            Log.d(LOG, "NO TRADES: Number of trades " + allTrades.size());

            this.tradeNumber = allTrades.size();

            ll = (LinearLayout) findViewById(R.id.column4);
            lp = new LayoutParams(-1, -1);
            lp.topMargin = 20;
            lp.rightMargin = 20;
            ll.setOrientation(LinearLayout.VERTICAL);

            if(this.tradeNumber >= 2) {

                int halfWay = this.tradeNumber / 2;

                this.tradeButtons = new Button[this.tradeNumber];

                for (i = 0; i < halfWay; i++) {
                    this.tradeButtons[i] = new Button(this);
                    Trade trade = allTrades.get(i);
                    this.tradeButtons[i].setText(trade.getName());
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                    Log.d(LOG, "FIRST HALF: Number of trades (" + i + ") " + trade.getName());
                }

                ll = (LinearLayout) findViewById(R.id.column42);
                lp = new LayoutParams(-1, -1);
                lp.topMargin = 20;
                lp.rightMargin = 20;
                ll.setOrientation(LinearLayout.VERTICAL);

                for (i = halfWay; i < this.tradeNumber; i++) {
                    this.tradeButtons[i] = new Button(this);
                    Trade trade = allTrades.get(i);
                    this.tradeButtons[i].setText(trade.getName());
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                    Log.d(LOG, "SECOND HALF: Number of trades (" + i + ") " + trade.getName());
                }
            }
            else {

                this.tradeButtons = new Button[this.tradeNumber];

                for (i = 0; i < this.tradeNumber; i++) {
                    this.tradeButtons[i] = new Button(this);
                    Trade trade = allTrades.get(i);
                    this.tradeButtons[i].setText(trade.getName());
                    this.tradeButtons[i].setId(i);
                    this.tradeButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.tradeButtons[i].setTextColor(getResources().getColor(R.color.textColorPrimary));
                    this.tradeButtons[i].setOnClickListener(new TradeListener(i));
                    ll.addView(this.tradeButtons[i], lp);
                }
            }

        }



    }

    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

        Log.d(LOG, "onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)");

        PPMProApp app = (PPMProApp) getApplication();

        if (parentView == findViewById(R.id.profile) && parentView.getCount() > 0) {
            setButtons();
        }
        if (parentView == findViewById(R.id.client)) {
            populateProfileSpinner(app.getClient_id(), app.getEstate_id());
            populateBuildingSpinner(app.getEstate_id());
            populateFloorSpinner(0);
        } else if (parentView == findViewById(R.id.estate)) {
            populateBuildingSpinner(((Estate) parentView.getItemAtPosition(position)).getId());
            populateFloorSpinner(0);
        } else if (parentView == findViewById(R.id.building)) {
            populateFloorSpinner(((Building) parentView.getItemAtPosition(position)).getId());
        } else if (parentView == findViewById(R.id.inspection)) {
            populateProfileSpinner(app.getClient_id(), app.getEstate_id());
            populateBuildingSpinner(app.getEstate_id());
            populateFloorSpinner(0);

        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void dispatchTakePictureIntent()  throws IOException{
        Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            this.photoFile = null;
            try {
                this.photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occured taking picture", Toast.LENGTH_SHORT).show();
            }
            if (this.photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ItemAddByProfile.this, "uk.co.bluebrickstudios.ppmprov2.provider", createImageFile());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        File image = File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        this.mCurrentPhotoFilename = image.getName();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(LOG, "onActivityResult(int requestCode, int resultCode, Intent data)");

        if (requestCode == REQUEST_SET_LOCATION && resultCode == -1) {
            String locationData = data.getStringExtra("location");
            this.location.setText(locationData);
            Toast.makeText(this, "Location added to item", Toast.LENGTH_SHORT).show();

            /* save floorplan image */
            if(!data.getStringExtra("floorplanimage").equals("NOIMAGE")){
                long update_id = -1;
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Image image = new Image();
                image.setName(data.getStringExtra("floorplanimage"));
                image.setFilename(data.getStringExtra("floorplanimage"));
                image.setThumbnail("thumb_" + data.getStringExtra("floorplanimage"));
                image.setFeatured(0);
                image.setItem_id((int)update_id);
                image.setCreated_at(sdf.format(dt));
                image.setUpdated_at(sdf.format(dt));
                image.setUploaded(REQUEST_SET_LOCATION);
                image.setLocalImageRef(data.getStringExtra("floorplanimage_path"));
                image.setId((int) this.controller.saveImage(image));
                Toast.makeText(this, "Floorplan image saved locally", Toast.LENGTH_SHORT).show();
                refreshGridView();
            }
        }
        if (requestCode != REQUEST_TAKE_PHOTO) {
            return;
        }
        if (resultCode == -1) {

            Date dt = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            long update_id = -1;

            Image image = new Image();
            image.setName(this.mCurrentPhotoFilename);
            image.setFilename(this.mCurrentPhotoFilename);
            image.setThumbnail("thumb_" + this.mCurrentPhotoFilename);
            image.setFeatured(0);
            image.setItem_id((int) update_id);
            image.setCreated_at(simpleDateFormat.format(dt));
            image.setUpdated_at(simpleDateFormat.format(dt));
            image.setUploaded(REQUEST_SET_LOCATION);
            image.setLocalImageRef(this.mCurrentPhotoPath);
            image.setId((int) this.controller.saveImage(image));
            Toast.makeText(this, "Picture taken and saved locally", Toast.LENGTH_SHORT).show();
            String itemId = Integer.toString((int) update_id);

            refreshGridView();

        } else if (resultCode == 0) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
    }



    public void refreshGridView() {
        ArrayList<Image> imageItems = this.controller.getAllImagesByItemIDFull(this._Item_Id);
        this.gridView = (GridView) findViewById(R.id.gridView);
        this.gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, imageItems);
        this.gridView.setAdapter(this.gridAdapter);
        this.gridView.setOnItemClickListener(new C02021());
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
