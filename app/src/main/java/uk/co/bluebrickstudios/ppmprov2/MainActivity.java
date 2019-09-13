package uk.co.bluebrickstudios.ppmprov2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import uk.co.blubrickstudios.ppmprov2.R;
import uk.co.bluebrickstudios.ppmprov2.adapter.ItemAdapter;
import uk.co.bluebrickstudios.ppmprov2.adapter.NavMenuAdapter;
import uk.co.bluebrickstudios.ppmprov2.helper.DatabaseHelper;
import uk.co.bluebrickstudios.ppmprov2.helper.RecyclerItemClickListener;
import uk.co.bluebrickstudios.ppmprov2.model.*;
import uk.co.bluebrickstudios.ppmprov2.adapter.ItemsAdapter;


public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private NavMenuAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    public static final String PREFS_NAME = "PPMProPrefsFile";

    private static final String LOG = MainActivity.class.getName();
    private static final int TABLE_ACTION = 1;
    private static final int TABLE_BUILDING = 2;
    private static final int TABLE_CLIENT = 3;
    private static final int TABLE_DEFECT = 4;
    private static final int TABLE_ESTATE = 5;
    private static final int TABLE_FLOOR = 6;
    private static final int TABLE_IMAGE = 7;
    private static final int TABLE_ITEM = 8;
    private static final int TABLE_ITEMTYPE = 9;
    private static final int TABLE_STATUS = 10;
    private static final int TABLE_TRADE = 11;
    private static final int TABLE_USER = 12;
    private static final int TABLE_PROFILE = 13;
    private static final int TABLE_PRIORITY = 14;
    private static final int TABLE_INSPECTION = 15;
    private static final int TABLE_LOCATION = 16;
    private static final int TABLE_JOB = 17;

    private static final int MENU_SYNC = 0;
    private static final int MENU_FILTER = 1;
    private static final int MENU_ADD = 2;
    private static final int MENU_PROFILE = 3;
    private static final int MENU_SEND_ITEMS_TO_SERVER = 4;
    private static final int MENU_INSPECTION_CALENDAR = 5;
    private static final int MENU_ABOUT = 6;

    int client_id = 0;
    int estate_id = 0;
    int building_id = 0;
    int floor_id = 0;
    int priority_id = 0;
    int status_id = 0;
    int trade_id = 0;
    int completed = 0;
    int profile_id = 0;

    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA = 0x5;
    static final Integer ACCOUNTS = 0x6;
    static final Integer GPS_SETTINGS = 0x7;


    // DB Class to perform DB related operations
    //DatabaseHelper controller = new DatabaseHelper(this);

    DatabaseHelper controller = null;

    // Progress Dialog Object
    ProgressDialog prgDialog, prgDialogNew, prgDialogEdit, prgDialogImage;
    HashMap<String, String> queryValues;

    TextView item_Id;

    RecyclerView recList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = DatabaseHelper.getInstance(this);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* instantiate Recycler View */
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);

        PPMProApp app = (PPMProApp)getApplication();

        // Construct the data source
        ArrayList<Item> itemList;

        //if(app.isAdmin()){
          //  itemList = controller.getAllItems();
        //}
        //else{
            //itemList = controller.getAllItems(app.getClient_id());
            itemList = controller.getAllItems(app.getClient_id(), app.getEstate_id());
        //}

        // If users exists in SQLite DB
        if (itemList.size() != 0) {

            ItemAdapter itemAdapter = new ItemAdapter(itemList);

            itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view , int position) {
                    item_Id = (TextView) view.findViewById(R.id.itemId);
                    String itemId = item_Id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                    objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                    startActivityForResult(objIndent, 1);
                }
            });
            recList.setAdapter(itemAdapter);

        }
        else{

        }

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);


        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Remote Database and Syncing Local Database. Please wait...");
        prgDialog.setCancelable(false);

        prgDialogNew = new ProgressDialog(this);
        prgDialogNew.setMessage("Adding new items to remote database. Please wait...");
        prgDialogNew.setCancelable(false);

        prgDialogEdit = new ProgressDialog(this);
        prgDialogEdit.setMessage("Updating changes made to device items to remote server. Please wait...");
        prgDialogEdit.setCancelable(false);

        if (prgDialogImage == null) {
            prgDialogImage = new ProgressDialog(this);
            prgDialogImage.setMessage("Transferring Image from device to remote database. Please wait...");
            prgDialogImage.setCancelable(false);
        }

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Camera
                case 5:
                    break;
            }
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }



    private void addDrawerItems() {


        ArrayList<NavMenuItem> menuItems = new ArrayList<NavMenuItem>();

        NavMenuItem mu;

        mu = new NavMenuItem();
        mu.setTitle("Update Device System Tables");
        mu.setIcon(R.mipmap.ic_action_reload);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("Filter Items");
        mu.setIcon(R.mipmap.ic_action_filter);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("Add New Item");
        mu.setIcon(R.mipmap.ic_action_add);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("Add New Item Using Profile");
        mu.setIcon(R.mipmap.ic_action_add);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("Sync items with server");
        mu.setIcon(R.mipmap.ic_action_signal);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("Inspection Calendar");
        mu.setIcon(R.mipmap.ic_action_calendar_month);
        menuItems.add(mu);

        mu = new NavMenuItem();
        mu.setTitle("About PPMPro V2");
        mu.setIcon(R.mipmap.ic_action_help);
        menuItems.add(mu);

        mAdapter = new NavMenuAdapter(this, menuItems);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == MENU_SYNC) {

                    mDrawerLayout.closeDrawers();

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if(isConnected){

                        /*
                        // Just added a new item so sync
                        ArrayList<Item> newItems = controller.getNewItems();
                        for(int i = 0; i < newItems.size(); i++){
                            Item newItem = newItems.get(i);
                            syncNewItem("https://ppm-pro.com/api/v1/item", newItem);

                        }

                        // Update existing local items - once added they must be deleted from the local database
                        ArrayList<Item> existingItems = controller.getUpdatedItems();
                        for(int i = 0; i < existingItems.size(); i++){
                            Item newItem = existingItems.get(i);
                            syncExistingItem("https://ppm-pro.com/api/v1/item/" + newItem.getId(), newItem);

                        }
                        */

                        // Get existing items and update sqllite
                        syncItems(TABLE_ACTION, "https://ppm-pro.com/api/v1/action");
                        syncItems(TABLE_BUILDING, "https://ppm-pro.com/api/v1/building");
                        syncItems(TABLE_CLIENT, "https://ppm-pro.com/api/v1/client");
                        syncItems(TABLE_DEFECT, "https://ppm-pro.com/api/v1/defect");
                        syncItems(TABLE_ESTATE, "https://ppm-pro.com/api/v1/estate");
                        syncItems(TABLE_FLOOR, "https://ppm-pro.com/api/v1/floor");
                        //syncItems(TABLE_IMAGE, "https://ppm-pro.com/api/v1/image");
                        //syncItems(TABLE_ITEM, "https://ppm-pro.com/api/v1/item");
                        syncItems(TABLE_ITEMTYPE, "https://ppm-pro.com/api/v1/itemtype");
                        syncItems(TABLE_STATUS, "https://ppm-pro.com/api/v1/status");
                        syncItems(TABLE_TRADE, "https://ppm-pro.com/api/v1/trade");
                        syncItems(TABLE_USER, "https://ppm-pro.com/api/v1/user");
                        syncItems(TABLE_PROFILE, "https://ppm-pro.com/api/v1/profile");
                        syncItems(TABLE_INSPECTION, "https://ppm-pro.com/api/v1/inspection");
                        syncItems(TABLE_PRIORITY, "https://ppm-pro.com/api/v1/priority");
                        syncItems(TABLE_LOCATION, "https://ppm-pro.com/api/v1/location");
                        syncItems(TABLE_JOB, "https://ppm-pro.com/api/v1/job");

                    }
                }

                if (position == MENU_SEND_ITEMS_TO_SERVER) {

                    mDrawerLayout.closeDrawers();

                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if(isConnected){

                        // Just added a new item so sync
                        ArrayList<Item> newItems = controller.getNewItems();
                        for(int i = 0; i < newItems.size(); i++){
                            Item newItem = newItems.get(i);
                            syncNewItem("https://ppm-pro.com/api/v1/item", newItem);

                        }

                        // Update existing local items - once added they must be deleted from the local database
                        ArrayList<Item> existingItems = controller.getUpdatedItems();
                        for(int i = 0; i < existingItems.size(); i++){
                            Item newItem = existingItems.get(i);
                            syncExistingItem("https://PPM-pro.com/api/v1/item/" + newItem.getGuid() + "/edit", newItem);
                        }

                        // Update any FCI Scores on Inspections
                        ArrayList<Inspection> updatedInspections = controller.getUpdatedInspections();
                        for(int i = 0; i < updatedInspections.size(); i++){
                            Inspection newInspection = updatedInspections.get(i);
                            syncExistingInspection("https://PPM-pro.com/api/v1/inspection/" + newInspection.getId() + "/edit", newInspection);
                        }

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        PPMProApp app = (PPMProApp)getApplication();
                        int client_id = 0;
                        if(!app.isAdmin()){
                            client_id = app.getClient_id();
                            estate_id = app.getEstate_id();
                        }

                        long longdate = settings.getLong("time", 0);

                        if(longdate > 0){
                            longdate = longdate/1000;
                        }

                        Log.d(LOG, "https://ppm-pro.com/api/v1/getitems/" + longdate + "/" + client_id);

                        syncItems(TABLE_ITEM, "https://ppm-pro.com/api/v1/getitems/" + longdate + "/" + client_id);

                        // Save last updated time
                        settings.edit().putLong("time", System.currentTimeMillis()).commit();


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No network connection detected.  Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }

                if (position == MENU_FILTER) {

                    mDrawerLayout.closeDrawers();

                    Intent objIndent = new Intent(getApplicationContext(), FilterActivity.class);
                    objIndent.putExtra("client_id", client_id);
                    objIndent.putExtra("estate_id", estate_id);
                    objIndent.putExtra("building_id", building_id);
                    objIndent.putExtra("floor_id", floor_id);
                    objIndent.putExtra("status_id", status_id);
                    objIndent.putExtra("priority_id", priority_id);
                    objIndent.putExtra("trade_id", trade_id);
                    objIndent.putExtra("completed", completed);
                    startActivityForResult(objIndent, 2);

                }

                if (position == MENU_ADD) {

                    askForPermission(Manifest.permission.CAMERA, CAMERA);

                    mDrawerLayout.closeDrawers();

                    String itemId = "0";
                    Intent objIndent = new Intent(getApplicationContext(), ItemAdd.class);
                    objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                    startActivityForResult(objIndent, 4);
                }

                if (position == MENU_PROFILE) {

                    askForPermission(Manifest.permission.CAMERA, CAMERA);

                    mDrawerLayout.closeDrawers();

                    String itemId = "0";
                    Intent objIndent = new Intent(getApplicationContext(), ItemAddByProfile.class);
                    objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                    startActivityForResult(objIndent, 5);
                }

                if (position == MENU_INSPECTION_CALENDAR) {

                    mDrawerLayout.closeDrawers();

                    Intent objIndent = new Intent(getApplicationContext(), InspectionCalendarView.class);
                    startActivityForResult(objIndent, 6);

                }

                if (position == MENU_ABOUT) {

                    mDrawerLayout.closeDrawers();

                    Intent objIndent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivityForResult(objIndent, 3);

                }

            }
        });



    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("MENU");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                PPMProApp app = (PPMProApp)getApplication();
                //if(app.isAdmin()){
                  //  client_id = data.getIntExtra("client_id", 0);
                    //estate_id = data.getIntExtra("estate_id", 0);
                //}
                //else{
                    client_id = app.getClient_id();
                    estate_id = app.getEstate_id();
                //}

                building_id = data.getIntExtra("building_id", 0);
                floor_id = data.getIntExtra("floor_id", 0);
                status_id = data.getIntExtra("status_id", 0);
                priority_id = data.getIntExtra("priority_id", 0);
                trade_id = data.getIntExtra("trade_id", 0);
                profile_id = data.getIntExtra("profile_id", 0);
                completed = data.getIntExtra("completed", 0);

                // Construct the data source
                ArrayList<Item> itemList = controller.filterItems(client_id, profile_id, estate_id, building_id, floor_id, status_id, trade_id, completed, priority_id);

                // If users exists in SQLite DB
                if (itemList.size() != 0) {

                    ItemAdapter itemAdapter = new ItemAdapter(itemList);
                    itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view , int position) {
                            item_Id = (TextView) view.findViewById(R.id.itemId);
                            String itemId = item_Id.getText().toString();
                            Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                            startActivityForResult(objIndent, 1);
                        }
                    });
                    recList.setAdapter(itemAdapter);

                }
                else{
                    Toast.makeText(getApplicationContext(), "No items match your selected criteria.  Please go back to filters and try again.", Toast.LENGTH_LONG).show();


                }


            }
        }

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){


                Log.d(LOG, "onActivityResult A");

                PPMProApp app = (PPMProApp)getApplication();

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if(isConnected){


                    // Just added a new item so sync
                    ArrayList<Item> newItems = controller.getNewItems();
                    for(int i = 0; i < newItems.size(); i++){
                        Item newItem = newItems.get(i);
                        syncNewItem("https://ppm-pro.com/api/v1/item", newItem);

                    }

                    // Update existing local items - once added they must be deleted from the local database
                    ArrayList<Item> existingItems = controller.getUpdatedItems();
                    for(int i = 0; i < existingItems.size(); i++){
                        Item newItem = existingItems.get(i);
                        syncExistingItem("https://ppm-pro.com/api/v1/item/" + newItem.getId(), newItem);

                    }

                    /*
                    // Get existing items and update sqllite
                    syncItems(TABLE_IMAGE, "https://ppm-pro.com/api/v1/image");
                    syncItems(TABLE_ITEM, "https://ppm-pro.com/api/v1/item");
                    */

                }

                // Construct the data source
                ArrayList<Item> itemList;

                //if(app.isAdmin()){
                  //  itemList = controller.getAllItems();
                //}
                //else{
                    //itemList = controller.getAllItems(app.getClient_id());
                    itemList = controller.getAllItems(app.getClient_id(), app.getEstate_id());
                //}

                // Construct the data source
                //ArrayList<Item> itemList = controller.getAllItems();

                // If users exists in SQLite DB
                if (itemList.size() != 0) {

                    ItemAdapter itemAdapter = new ItemAdapter(itemList);
                    itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view , int position) {
                            item_Id = (TextView) view.findViewById(R.id.itemId);
                            String itemId = item_Id.getText().toString();
                            Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                            startActivityForResult(objIndent, 1);
                        }
                    });
                    recList.setAdapter(itemAdapter);

                }
                else{

                }

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 4) {

            if(resultCode == RESULT_OK){

                Log.d(LOG, "onActivityResult B");

                Log.d(LOG, "Adding new item");

                PPMProApp app = (PPMProApp)getApplication();

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if(isConnected){


                    // Just added a new item so sync
                    ArrayList<Item> newItems = controller.getNewItems();
                    for(int i = 0; i < newItems.size(); i++){
                        Item newItem = newItems.get(i);
                        syncNewItem("https://ppm-pro.com/api/v1/item", newItem);

                    }

                    // Update existing local items - once added they must be deleted from the local database
                    ArrayList<Item> existingItems = controller.getUpdatedItems();
                    for(int i = 0; i < existingItems.size(); i++){
                        Item newItem = existingItems.get(i);
                        syncExistingItem("https://ppm-pro.com/api/v1/item/" + newItem.getId(), newItem);

                    }

                    /*
                    // Get existing items and update sqllite
                    syncItems(TABLE_IMAGE, "https://ppm-pro.com/api/v1/image");
                    syncItems(TABLE_ITEM, "https://ppm-pro.com/api/v1/item");
                    */

                }

                ArrayList<Item> itemList;

               // if(app.isAdmin()){
                 //   itemList = controller.getAllItems();
                //}
                //else{
                    //itemList = controller.getAllItems(app.getClient_id());
                    itemList = controller.getAllItems(app.getClient_id(), app.getEstate_id());
                //}

                if (itemList.size() != 0) {

                    ItemAdapter itemAdapter = new ItemAdapter(itemList);
                    itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view , int position) {
                            item_Id = (TextView) view.findViewById(R.id.itemId);
                            String itemId = item_Id.getText().toString();
                            Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                            startActivityForResult(objIndent, 1);
                        }
                    });
                    recList.setAdapter(itemAdapter);

                }
                else{
                    Log.d(LOG, "no items returned");
                }

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 5) {

            if (resultCode == RESULT_OK) {

                Log.d(LOG, "onActivityResult C");

                PPMProApp app = (PPMProApp)getApplication();

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if(isConnected){

                    ArrayList<Item> newItems = controller.getNewItems();
                    for(int i = 0; i < newItems.size(); i++){
                        Item newItem = newItems.get(i);
                        syncNewItem("https://ppm-pro.com/api/v1/item", newItem);

                    }

                    /*
                    // Just added a new item so sync
                    // Get existing items and update sqllite
                    syncItems(TABLE_IMAGE, "https://ppm-pro.com/api/v1/image");
                    syncItems(TABLE_ITEM, "https://ppm-pro.com/api/v1/item");
                    */

                }

                ArrayList<Item> itemList;

                //if(app.isAdmin()){
                  //  itemList = controller.getAllItems();
                //}
                //else{
                    //itemList = controller.getAllItems(app.getClient_id());
                    itemList = controller.getAllItems(app.getClient_id(), app.getEstate_id());
                //}

                if (itemList.size() != 0) {

                    ItemAdapter itemAdapter = new ItemAdapter(itemList);
                    itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view , int position) {
                            item_Id = (TextView) view.findViewById(R.id.itemId);
                            String itemId = item_Id.getText().toString();
                            Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                            startActivityForResult(objIndent, 1);
                        }
                    });
                    recList.setAdapter(itemAdapter);

                }
                else{
                    Log.d(LOG, "no items returned");
                }

            }
        }

    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void syncNewImage(String url, Image image) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();

        Item item = controller.getItem(image.getItem_id());
        params.put("guid", item.getGuid());

        params.put("name", image.getName());
        params.put("filename", image.getFilename());
        params.put("thumbnail", image.getThumbnail());
        params.put("featured", image.getFeatured());
        params.put("item_id", image.getItem_id());
        params.put("device_imageid", image.getId());
        params.put("created_at", image.getCreated_at());
        params.put("updated_at", image.getUpdated_at());

        Log.d(LOG, "Filename: " + image.getLocalImageRef());

        try{
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //File photoFile = new File(storageDir, image.getLocalImageRef());
            File photoFile = new File(storageDir, image.getName());
            if(photoFile.exists()){
                params.put("imagedata", photoFile);
            }
        }
        catch(FileNotFoundException fnfe){
            // TODO: Need to handle this error
        }

        prgDialogImage.show();

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

    public void syncExistingItem(String url, Item item) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();

        params.put("id", item.getId());
        params.put("device_itemid", item.getId());
        params.put("guid", item.getGuid());
        params.put("notes", item.getNotes());
        params.put("lastaction", item.getLastaction());
        params.put("location", item.getLocation());
        params.put("inspection_type", item.getInspection_type());
        params.put("job_number", item.getJob_number());
        PPMProApp app = (PPMProApp)getApplication();
        params.put("user_id", app.getUserId());
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
        params.put("fciscore_id", Integer.toString(item.getFciscore_id()));
        params.put("podnumber", item.getPodnumber());

        prgDialogEdit.show();

        Log.d(LOG, "URL = " + url);

        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                prgDialogEdit.hide();
                try {
                    String responseString = new String(responseBody, "UTF-8");

                    Gson gson = new GsonBuilder().create();
                    try {

                        JSONObject json = new JSONObject(responseString);
                        String device_itemid = json.getString("device_itemid");
                        String remote_itemid = json.getString("remote_itemid");
                        String guid = json.getString("guid");
                        String status_id = json.getString("status_id");
                        String itemtype_id = json.getString("itemtype_id");
                        String defect_id = json.getString("defect_id");
                        String action_id = json.getString("action_id");
                        String trade_id = json.getString("trade_id");
                        //String priority_id = json.getString("priority_id");

                        // update local item
                        Item item = new Item();
                        item.setId(Integer.parseInt(device_itemid));
                        item.setStatus_id(Integer.parseInt(status_id));
                        item.setTrade_id(Integer.parseInt(trade_id));
                        item.setItemtype_id(Integer.parseInt(itemtype_id));
                        item.setAction_id(Integer.parseInt(action_id));
                        item.setDefect_id(Integer.parseInt(defect_id));
                        //item.setPriority_id(Integer.parseInt(priority_id));

                        controller.updateLocalItemWithAttributes(item);

                        /* deal with any uploaded images */
                        // Just added a new item so sync image
                        ArrayList<Image> newImages = controller.getAllImagesByItemID(Integer.parseInt(device_itemid));
                        for(int i = 0; i < newImages.size(); i++){
                            Image newImage = newImages.get(i);
                            syncNewImage("https://ppm-pro.com/api/v1/image", newImage);

                        }

                        Toast.makeText(getApplicationContext(), "Item updated on remote server", Toast.LENGTH_SHORT).show();

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

                prgDialogEdit.hide();
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

    public void syncNewItem(String url, Item item) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();

        params.put("device_itemid", item.getId());
        params.put("name", item.getName());
        params.put("description", item.getDescription());
        params.put("notes", item.getNotes());
        params.put("lastaction", item.getLastaction());
        params.put("location", item.getLocation());
        params.put("inspection_type", item.getInspection_type());
        params.put("job_number", item.getJob_number());
        PPMProApp app = (PPMProApp)getApplication();
        params.put("user_id", app.getUserId());
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
        params.put("fciscore_id", Integer.toString(item.getFciscore_id()));
        params.put("podnumber", item.getPodnumber());
        params.put("guid", item.getGuid());
        params.put("created_at", item.getCreated_at());
        params.put("updated_at", item.getUpdated_at());

        prgDialogNew.show();

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
                        String guid = json.getString("guid");

                        // update local item
                        controller.updateLocalItemFromServer(Integer.parseInt(device_itemid));

                        Toast.makeText(getApplicationContext(), "Item added to remote server", Toast.LENGTH_LONG).show();

                        /* deal with any uploaded images */
                        // Just added a new item so sync image
                        ArrayList<Image> newImages = controller.getAllImagesByItemID(Integer.parseInt(device_itemid));
                        for(int i = 0; i < newImages.size(); i++){
                            Image newImage = newImages.get(i);
                            syncNewImage("https://ppm-pro.com/api/v1/image", newImage);

                        }


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

    public void syncItems(final int tableIndex, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk","b00gan01");
        RequestParams params = new RequestParams();
        prgDialog.show();
        client.get(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    String responseString = new String(responseBody, "UTF-8");

                    switch(tableIndex){
                        case TABLE_ACTION:
                            updateAction(responseString);
                            break;
                        case TABLE_BUILDING:
                            updateBuilding(responseString);
                            break;
                        case TABLE_CLIENT:
                            updateClient(responseString);
                            break;
                        case TABLE_DEFECT:
                            updateDefect(responseString);
                            break;
                        case TABLE_ESTATE:
                            updateEstate(responseString);
                            break;
                        case TABLE_FLOOR:
                            updateFloor(responseString);
                            break;
                        case TABLE_IMAGE:
                            updateImage(responseString);
                            break;
                        case TABLE_ITEM:
                            updateItems(responseString);
                            break;
                        case TABLE_ITEMTYPE:
                            updateItemType(responseString);
                            break;
                        case TABLE_STATUS:
                            updateStatus(responseString);
                            break;
                        case TABLE_TRADE:
                            updateTrade(responseString);
                            break;
                        case TABLE_USER:
                            updateUser(responseString);
                            break;
                        case TABLE_PROFILE:
                            updateProfile(responseString);
                            break;
                        case TABLE_INSPECTION:
                            updateInspection(responseString);
                            break;
                        case TABLE_PRIORITY:
                            updatePriority(responseString);
                            break;
                        case TABLE_LOCATION:
                            updateLocation(responseString);
                            break;
                        case TABLE_JOB:
                            updateJob(responseString);
                            break;
                    }

                } catch (Exception e) {}
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String responseString = new String(responseBody, "UTF-8");

                } catch (Exception e) {
                }

                error.printStackTrace();

                prgDialog.hide();
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

    public void updateItems(String response){

        Log.d(LOG, "update items" + response);

        ArrayList<HashMap<String, String>> itemsynclist;
        itemsynclist = new ArrayList<HashMap<String, String>>();
        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("items");

            if (jitems.length() != 0){

                Log.d(LOG, "got stuff" + jitems.length());

                for(int i=0; i < jitems.length(); i++) {

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Item item = new Item();
                    item.setNotes(jsonas.getString("notes"));
                    item.setLocation(jsonas.getString("location"));
                    item.setInspection_type(jsonas.getString("inspection_type"));
                    item.setJob_number(jsonas.getString("job_number"));
                    item.setStatus_id(Integer.parseInt(jsonas.getString("status_id")));
                    item.setDefect_id(Integer.parseInt(jsonas.getString("defect_id")));
                    item.setItemtype_id(Integer.parseInt(jsonas.getString("itemtype_id")));
                    item.setDefect_id(Integer.parseInt(jsonas.getString("defect_id")));
                    item.setTrade_id(Integer.parseInt(jsonas.getString("trade_id")));
                    item.setAction_id(Integer.parseInt(jsonas.getString("action_id")));
                    item.setFloor_id(Integer.parseInt(jsonas.getString("floor_id")));
                    item.setBuilding_id(Integer.parseInt(jsonas.getString("building_id")));
                    item.setEstate_id(Integer.parseInt(jsonas.getString("estate_id")));
                    item.setClient_id(Integer.parseInt(jsonas.getString("client_id")));
                    item.setProfile_id(Integer.parseInt(jsonas.getString("profile_id")));
                    item.setInspection_id(Integer.parseInt(jsonas.getString("inspection_id")));
                    item.setFciscore_id(Integer.parseInt(jsonas.getString("fciscore_id")));
                    item.setPodnumber(jsonas.getString("podnumber"));
                    item.setGuid(jsonas.getString("guid"));
                    item.setUpdated_at(jsonas.getString("updated_at"));

                    controller.updateItemByGUID(item);
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("id", jsonas.getString("id"));
                    map.put("status", "1");
                    itemsynclist.add(map);

                }
                //reloadActivity();

                // Construct the data source
                ArrayList<Item> itemList;

                PPMProApp app = (PPMProApp)getApplication();

                //if(app.isAdmin()){
                  //  itemList = controller.getAllItems();
                //}
                //else{
                    //itemList = controller.getAllItems(app.getClient_id());
                    itemList = controller.getAllItems(app.getClient_id(), app.getEstate_id());
                //}

                // Construct the data source
                //ArrayList<Item> itemList = controller.getAllItems();

                // If users exists in SQLite DB
                if (itemList.size() != 0) {

                    ItemAdapter itemAdapter = new ItemAdapter(itemList);
                    itemAdapter.SetOnItemClickListener(new ItemAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view , int position) {
                            item_Id = (TextView) view.findViewById(R.id.itemId);
                            String itemId = item_Id.getText().toString();
                            Intent objIndent = new Intent(getApplicationContext(),ItemDetail.class);
                            objIndent.putExtra("item_Id", Integer.parseInt(itemId));
                            startActivityForResult(objIndent, 1);
                        }
                    });
                    recList.setAdapter(itemAdapter);

                }
                else{
                    Log.d(LOG, "no items returned");
                }

            }
            else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("profiles");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Profile item = new Profile();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setClient_id(Integer.parseInt(jsonas.getString("client_id")));
                    item.setEstate_id(Integer.parseInt(jsonas.getString("estate_id")));
                    item.setUser_id(Integer.parseInt(jsonas.getString("user_id")));
                    item.setName(jsonas.getString("name"));
                    item.setItemtypes(jsonas.getString("itemtypes"));
                    item.setActions(jsonas.getString("actions"));
                    item.setDefects(jsonas.getString("defects"));
                    item.setTrades(jsonas.getString("trades"));

                    Log.d(LOG, jsonas.getString("trades"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createProfile(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateInspection(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("inspections");

            if(jitems.length() != 0){

                for(int i=0; i < jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Inspection item = new Inspection();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setClient_id(Integer.parseInt(jsonas.getString("client_id")));
                    item.setEstate_id(Integer.parseInt(jsonas.getString("estate_id")));
                    item.setBuilding_id(Integer.parseInt(jsonas.getString("building_id")));
                    item.setProfile_id(Integer.parseInt(jsonas.getString("profile_id")));
                    item.setUser_id(Integer.parseInt(jsonas.getString("user_id")));
                    item.setName(jsonas.getString("name"));
                    item.setNotes(jsonas.getString("notes"));
                    item.setIntensity_id(Integer.parseInt(jsonas.getString("intensity_id")));
                    item.setCompleted(Integer.parseInt(jsonas.getString("completed")));
                    item.setFrequency_id(Integer.parseInt(jsonas.getString("frequency_id")));
                    item.setAlertaddresses(jsonas.getString("alertaddresses"));
                    item.setPriority_id(Integer.parseInt(jsonas.getString("priority_id")));
                    item.setStart_at(jsonas.getString("start_at"));
                    item.setEnd_at(jsonas.getString("end_at"));
                    item.setCompleted_at(jsonas.getString("completed_at"));

                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createInspection(item);


                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateAction(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("actions");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Action item = new Action();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createAction(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateBuilding(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("buildings");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Building item = new Building();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setEstate_id(Integer.parseInt(jsonas.getString("estate_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createBuilding(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateClient(String response) {

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("clients");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Client item = new Client();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createClient(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDefect(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("defects");

            if (jitems.length() != 0) {

                for (int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Defect item = new Defect();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createDefect(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateEstate(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("estates");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Estate item = new Estate();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setClient_id(Integer.parseInt(jsonas.getString("client_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createEstate(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateFloor(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("floors");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Floor item = new Floor();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setFloorplan(jsonas.getString("floorplan"));
                    item.setXlower(jsonas.getString("xlower"));
                    item.setXupper(jsonas.getString("xupper"));
                    item.setYlower(jsonas.getString("ylower"));
                    item.setYupper(jsonas.getString("yupper"));
                    item.setBuilding_id(Integer.parseInt(jsonas.getString("building_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createFloor(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateLocation(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("locations");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Location item = new Location();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setBuilding_id(Integer.parseInt(jsonas.getString("building_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createLocation(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateJob(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("jobs");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Job item = new Job();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setBuilding_id(Integer.parseInt(jsonas.getString("building_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createJob(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateImage(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("images");

            if (jitems.length() != 0) {

                for(int i=0;i<jitems.length(); i++) {

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Image item = new Image();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setItem_id(Integer.parseInt(jsonas.getString("item_id")));
                    item.setFilename(jsonas.getString("filename"));
                    item.setThumbnail(jsonas.getString("thumbnail"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createImage(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateItemType(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("itemtypes");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    ItemType item = new ItemType();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createItemType(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("statuses");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Status item = new Status();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createStatus(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updatePriority(String response){

        Log.d(LOG, "update priority" + response);

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("priority");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Priority item = new Priority();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createPriority(item);

                    Log.d(LOG, "created new priority");

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateTrade(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("trades");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    Trade item = new Trade();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createTrade(item);

                }
                reloadActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String response){

        Gson gson = new GsonBuilder().create();
        try {

            JSONObject json = new JSONObject(response);
            JSONArray jitems = json.getJSONArray("users");

            if(jitems.length() != 0){

                for(int i=0;i<jitems.length(); i++){

                    JSONObject jsonas = jitems.getJSONObject(i);

                    User item = new User();
                    item.setId(Integer.parseInt(jsonas.getString("id")));
                    item.setName(jsonas.getString("name"));
                    item.setEmail(jsonas.getString("email"));
                    item.setApp(jsonas.getString("app"));

                    if(jsonas.getBoolean("is_admin")){
                        item.setIsAdmin(1);
                    }
                    else {
                        item.setIsAdmin(0);
                    }
                    item.setClient_id(Integer.parseInt(jsonas.getString("client_id")));
                    item.setEstate_id(Integer.parseInt(jsonas.getString("estate_id")));
                    item.setCreated_at(jsonas.getString("created_at"));
                    item.setUpdated_at(jsonas.getString("updated_at"));
                    item.setDeleted_at(jsonas.getString("deleted_at"));

                    controller.createUser(item);

                    Log.d(LOG, item.getEmail());
                    Log.d(LOG, item.getApp());

                }
                reloadActivity();
            }
        } catch (JSONException e) {

        }
    }

    public void syncExistingInspection(String url, Inspection inspection) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("mark.ridley@redbrickstudios.co.uk", "b00gan01");
        RequestParams params = new RequestParams();

        params.put("id", inspection.getId());
        params.put("priority_id", Integer.toString(inspection.getPriority_id()));

        prgDialogEdit.show();

        Log.d(LOG, "URL = " + url);

        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                prgDialogEdit.hide();
                try {
                    String responseString = new String(responseBody, "UTF-8");

                    Gson gson = new GsonBuilder().create();
                    try {

                        JSONObject json = new JSONObject(responseString);
                        int inspection_id = Integer.parseInt(json.getString("inspection_id"));
                        int priority_id = Integer.parseInt(json.getString("priority_id"));

                        controller.updateInspection(inspection_id, 0, priority_id);

                        Toast.makeText(getApplicationContext(), "Inspection updated on remote server", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String responseString = new String(responseBody, "UTF-8");
                } catch (Exception e) {
                }

                prgDialogEdit.hide();
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

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", false);
        editor.apply();
    }



}