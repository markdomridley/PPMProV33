package uk.co.bluebrickstudios.ppmprov2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.List;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.BuildingSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ClientSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.EstateSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.FloorSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.PrioritySpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.ProfileSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.StatusSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.TradeSpinnerAdapter;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.model.Building;
import uk.co.bluebrickstudios.ppmprov2.model.Client;
import uk.co.bluebrickstudios.ppmprov2.model.Estate;
import uk.co.bluebrickstudios.ppmprov2.model.Floor;
import uk.co.bluebrickstudios.ppmprov2.model.Priority;
import uk.co.bluebrickstudios.ppmprov2.model.Profile;
import uk.co.bluebrickstudios.ppmprov2.model.Status;
import uk.co.bluebrickstudios.ppmprov2.model.Trade;

public class FilterActivity extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {

    private static final String LOG;
    Button btnFilter;
    Button btnReset;
    private Spinner building;
    BuildingSpinnerAdapter buildingAdapter;
    int building_id;
    private Spinner client;
    ClientSpinnerAdapter clientAdapter;
    int client_id;
    int completed;
    DatabaseHelper controller;
    private Spinner estate;
    EstateSpinnerAdapter estateAdapter;
    int estate_id;
    private Spinner floor;
    FloorSpinnerAdapter floorAdapter;
    int floor_id;
    private Spinner profile;
    ProfileSpinnerAdapter profileAdapter;
    int profile_id;
    private Spinner status;
    StatusSpinnerAdapter statusAdapter;
    int status_id;
    private Spinner trade;
    TradeSpinnerAdapter tradeAdapter;
    int trade_id;

    private Spinner priority;
    PrioritySpinnerAdapter priorityAdapter;
    int priority_id;

    public FilterActivity() {
        this.client_id = 0;
        this.profile_id = 0;
        this.estate_id = 0;
        this.building_id = 0;
        this.floor_id = 0;
        this.status_id = 0;
        this.priority_id = 0;
        this.trade_id = 0;
        this.completed = 0;
        this.controller = null;
    }

    static {
        LOG = ItemEdit.class.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        this.controller = DatabaseHelper.getInstance(this);
        Intent data = getIntent();
        this.client_id = data.getIntExtra("client_id", 0);
        this.profile_id = data.getIntExtra("profile_id", 0);
        this.estate_id = data.getIntExtra("estate_id", 0);
        this.building_id = data.getIntExtra("building_id", 0);
        this.floor_id = data.getIntExtra("floor_id", 0);
        this.status_id = data.getIntExtra("status_id", 0);
        this.priority_id = data.getIntExtra("priority_id", 0);
        this.trade_id = data.getIntExtra("trade_id", 0);
        this.completed = data.getIntExtra("completed", 0);

        client = (Spinner) findViewById(R.id.client);
        profile = (Spinner) findViewById(R.id.profile);
        estate = (Spinner) findViewById(R.id.estate);
        building = (Spinner) findViewById(R.id.building);
        floor = (Spinner) findViewById(R.id.floor);
        trade = (Spinner) findViewById(R.id.trade);
        status = (Spinner) findViewById(R.id.status);
        priority = (Spinner) findViewById(R.id.priority);

        this.client_id = ((PPMProApp) getApplication()).getClient_id();
        this.estate_id = ((PPMProApp) getApplication()).getEstate_id();
        populateClientSpinner();
        populateProfileSpinner();
        populateEstateSpinner();
        populateBuildingSpinner();
        populateFloorSpinner();
        populateStatusSpinner();
        populatePrioritySpinner();
        populateTradeSpinner();
        this.building.setOnItemSelectedListener(this);
        this.client.setOnItemSelectedListener(this);
        this.profile.setOnItemSelectedListener(this);
        this.estate.setOnItemSelectedListener(this);
        this.floor.setOnItemSelectedListener(this);
        this.status.setOnItemSelectedListener(this);
        this.priority.setOnItemSelectedListener(this);
        this.trade.setOnItemSelectedListener(this);
    }

    public void onToggleClickedCompleted(View view) {
        if (((ToggleButton) view).isChecked()) {
            this.completed = 1;
        } else {
            this.completed = 0;
        }
    }

    public void doFilter(View view) {
        this.floor_id = ((Floor) this.floor.getItemAtPosition(this.floor.getSelectedItemPosition())).getId();
        this.building_id = ((Building) this.building.getItemAtPosition(this.building.getSelectedItemPosition())).getId();
        this.estate_id = ((Estate) this.estate.getItemAtPosition(this.estate.getSelectedItemPosition())).getId();
        this.client_id = ((Client) this.client.getItemAtPosition(this.client.getSelectedItemPosition())).getId();
        this.profile_id = ((Profile) this.profile.getItemAtPosition(this.profile.getSelectedItemPosition())).getId();
        this.trade_id = ((Trade) this.trade.getItemAtPosition(this.trade.getSelectedItemPosition())).getId();
        this.status_id = ((Status) this.status.getItemAtPosition(this.status.getSelectedItemPosition())).getId();
        this.priority_id = ((Priority) this.priority.getItemAtPosition(this.priority.getSelectedItemPosition())).getId();
        Intent objIndent = new Intent();
        objIndent.putExtra("client_id", this.client_id);
        objIndent.putExtra("profile_id", this.profile_id);
        objIndent.putExtra("estate_id", this.estate_id);
        objIndent.putExtra("building_id", this.building_id);
        objIndent.putExtra("floor_id", this.floor_id);
        objIndent.putExtra("status_id", this.status_id);
        objIndent.putExtra("priority_id", this.priority_id);
        objIndent.putExtra("trade_id", this.trade_id);
        objIndent.putExtra("completed", this.completed);
        setResult(-1, objIndent);
        finish();
    }

    public void doReset(View view) {
        this.client_id = 0;
        this.profile_id = 0;
        this.estate_id = 0;
        this.building_id = 0;
        this.floor_id = 0;
        this.status_id = 0;
        this.priority_id = 0;
        this.trade_id = 0;
        this.completed = 0;
        Intent objIndent = new Intent();
        objIndent.putExtra("client_id", String.valueOf(this.client_id));
        objIndent.putExtra("profile_id", String.valueOf(this.profile_id));
        objIndent.putExtra("estate_id", String.valueOf(this.estate_id));
        objIndent.putExtra("building_id", String.valueOf(this.building_id));
        objIndent.putExtra("floor_id", String.valueOf(this.floor_id));
        objIndent.putExtra("status_id", String.valueOf(this.status_id));
        objIndent.putExtra("priority_id", String.valueOf(this.priority_id));
        objIndent.putExtra("trade_id", String.valueOf(this.trade_id));
        objIndent.putExtra("completed", String.valueOf(this.completed));
        setResult(-1, objIndent);
        finish();
    }

    private void populateBuildingSpinner() {
        List<Building> buildings = new ArrayList<Building>();

        Building defaultBuilding = new Building();
        defaultBuilding.setId(0);
        defaultBuilding.setName("-Select Building-");
        buildings.add(defaultBuilding);

        if (estate_id > 0) {
            buildings.addAll(controller.getAllBuildings(estate_id));
        } else {
            buildings.addAll(controller.getAllBuildings());
        }

        buildingAdapter = new BuildingSpinnerAdapter(FilterActivity.this, android.R.layout.simple_spinner_item, buildings);
        building.setAdapter(buildingAdapter);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            building.setSelection(buildingAdapter.getItemPositionByID(building_id));
        }
        catch(Exception ignore){}
    }

    private void populateClientSpinner() {
        List<Client> clients = new ArrayList<Client>();

        Client defaultClient = new Client();
        defaultClient.setId(0);
        defaultClient.setName("-Select Client-");
        clients.add(defaultClient);

        if(client_id > 0) {
            Client objClient = controller.getClient(client_id);
            if(objClient.getName() != null){
                clients.add(objClient);
            }
            //clients.addAll(controller.getAllClients());
        }
        else{
            clients.addAll(controller.getAllClients());
        }

        clientAdapter = new ClientSpinnerAdapter(FilterActivity.this, android.R.layout.simple_spinner_item, clients);
        client.setAdapter(clientAdapter);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            client.setSelection(clientAdapter.getItemPositionByID(client_id));
        }
        catch(Exception ignore){}
    }

    private void populateProfileSpinner() {

        List<Profile> profiles = new ArrayList();
        Profile defaultProfile = new Profile();
        defaultProfile.setId(0);
        defaultProfile.setName("-Select Profile-");
        profiles.add(defaultProfile);
        if (this.client_id > 0) {
            profiles.addAll(this.controller.getAllProfiles(this.client_id, this.estate_id));
        } else {
            profiles.addAll(this.controller.getAllProfiles());
        }
        this.profileAdapter = new ProfileSpinnerAdapter(this, android.R.layout.simple_spinner_item, profiles);
        this.profile.setAdapter(this.profileAdapter);
        this.profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.profile.setSelection(this.profileAdapter.getItemPositionByID(this.profile_id));
        } catch (Exception e) {
        }
    }

    private void populateEstateSpinner() {
        List<Estate> estates = new ArrayList();
        Estate defaultEstate = new Estate();
        defaultEstate.setId(0);
        defaultEstate.setName("-Select Estate-");
        estates.add(defaultEstate);
        if (this.client_id > 0) {
            estates.addAll(this.controller.getAllEstates(this.client_id, this.estate_id));
        } else {
            estates.addAll(this.controller.getAllEstates());
        }
        this.estateAdapter = new EstateSpinnerAdapter(this, android.R.layout.simple_spinner_item, estates);
        this.estate.setAdapter(this.estateAdapter);
        this.estateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.estate.setSelection(this.estateAdapter.getItemPositionByID(this.estate_id));
        } catch (Exception e) {
        }
    }

    private void populateFloorSpinner() {
        List<Floor> floors = new ArrayList();
        Floor defaultFloor = new Floor();
        defaultFloor.setId(0);
        defaultFloor.setName("-Select Floor-");
        floors.add(defaultFloor);
        if (this.building_id > 0) {
            floors.addAll(this.controller.getAllFloors(this.building_id));
        } else {
            floors.addAll(this.controller.getAllFloors());
        }
        this.floorAdapter = new FloorSpinnerAdapter(this, android.R.layout.simple_spinner_item, floors);
        this.floor.setAdapter(this.floorAdapter);
        this.floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.floor.setSelection(this.floorAdapter.getItemPositionByID(this.floor_id));
        } catch (Exception e) {
        }
    }

    private void populateStatusSpinner() {
        List<Status> statuses = new ArrayList();
        Status defaultStatus = new Status();
        defaultStatus.setId(0);
        defaultStatus.setName("-Select Status-");
        statuses.add(defaultStatus);
        statuses.addAll(this.controller.getAllStatuss());
        this.statusAdapter = new StatusSpinnerAdapter(this, android.R.layout.simple_spinner_item, statuses);
        this.status.setAdapter(this.statusAdapter);
        this.statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.status.setSelection(this.statusAdapter.getItemPositionByID(this.status_id));
        } catch (Exception e) {
        }
    }

    private void populatePrioritySpinner() {

        List<Priority> priorityes = new ArrayList();
        Priority defaultPriority = new Priority();
        defaultPriority.setId(0);
        defaultPriority.setName("-Select Priority-");
        priorityes.add(defaultPriority);
        priorityes.addAll(this.controller.getAllPrioritys());
        this.priorityAdapter = new PrioritySpinnerAdapter(this, android.R.layout.simple_spinner_item, priorityes);
        this.priority.setAdapter(this.priorityAdapter);
        this.priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.priority.setSelection(this.priorityAdapter.getItemPositionByID(this.priority_id));
        } catch (Exception e) {
        }

    }

    private void populateTradeSpinner() {
        List<Trade> trades = new ArrayList();
        Trade defaultTrade = new Trade();
        defaultTrade.setId(0);
        defaultTrade.setName("-Select Trade-");
        trades.add(defaultTrade);
        trades.addAll(this.controller.getAllTrades());
        this.tradeAdapter = new TradeSpinnerAdapter(this, android.R.layout.simple_spinner_item, trades);
        this.trade.setAdapter(this.tradeAdapter);
        this.tradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            this.trade.setSelection(this.tradeAdapter.getItemPositionByID(this.trade_id));
        } catch (Exception e) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.generic_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.close) {

            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();

        }
        if (id == R.id.save) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
    }

    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        if (parentView == findViewById(R.id.client)) {
            PPMProApp app = (PPMProApp) getApplication();
            //if (app.isAdmin()) {
              //  this.client_id = ((Client) parentView.getItemAtPosition(position)).getId();
               //this.estate_id = 0;
            //}
            //else{
                this.client_id = app.getClient_id();
                this.estate_id = app.getEstate_id();
            //}
            this.profile_id = 0;
            this.building_id = 0;
            this.floor_id = 0;
            populateProfileSpinner();
            populateEstateSpinner();
            populateBuildingSpinner();
            populateFloorSpinner();
        } else if (parentView == findViewById(R.id.estate)) {
            this.estate_id = ((Estate) parentView.getItemAtPosition(position)).getId();
            this.building_id = 0;
            this.floor_id = 0;
            populateBuildingSpinner();
            populateFloorSpinner();
        } else if (parentView == findViewById(R.id.building)) {
            this.building_id = ((Building) parentView.getItemAtPosition(position)).getId();
            this.floor_id = 0;
            populateFloorSpinner();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
