package uk.co.bluebrickstudios.ppmprov2.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.co.bluebrickstudios.ppmprov2.model.Action;
import uk.co.bluebrickstudios.ppmprov2.model.Building;
import uk.co.bluebrickstudios.ppmprov2.model.Client;
import uk.co.bluebrickstudios.ppmprov2.model.Defect;
import uk.co.bluebrickstudios.ppmprov2.model.Estate;
import uk.co.bluebrickstudios.ppmprov2.model.Floor;
import uk.co.bluebrickstudios.ppmprov2.model.Image;
import uk.co.bluebrickstudios.ppmprov2.model.Inspection;
import uk.co.bluebrickstudios.ppmprov2.model.Item;
import uk.co.bluebrickstudios.ppmprov2.model.ItemType;
import uk.co.bluebrickstudios.ppmprov2.model.Priority;
import uk.co.bluebrickstudios.ppmprov2.model.Profile;
import uk.co.bluebrickstudios.ppmprov2.model.Status;
import uk.co.bluebrickstudios.ppmprov2.model.Trade;
import uk.co.bluebrickstudios.ppmprov2.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_ACTION = "CREATE TABLE actions(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_BUILDING = "CREATE TABLE buildings(id INTEGER PRIMARY KEY,name TEXT,estate_id INTEGER,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_CLIENT = "CREATE TABLE clients(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_DEFECT = "CREATE TABLE defects(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_PRIORITY = "CREATE TABLE prioritys(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_ESTATE = "CREATE TABLE estates(id INTEGER PRIMARY KEY,name TEXT,client_id INTEGER,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_FLOOR = "CREATE TABLE floors(id INTEGER PRIMARY KEY,name TEXT,building_id INTEGER,xlower TEXT,xupper TEXT,ylower TEXT,yupper TEXT,floorplan TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE images(id INTEGER PRIMARY KEY,name TEXT,filename TEXT,thumbnail TEXT,featured INTEGER,item_id INTEGER,uploaded INTEGER, local_image_ref TEXT, created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE items(id INTEGER PRIMARY KEY,name TEXT, description TEXT, notes TEXT, lastaction TEXT, location TEXT, user_id INTEGER, status_id INTEGER, defect_id INTEGER, itemtype_id INTEGER, trade_id INTEGER, action_id INTEGER, floor_id INTEGER, building_id INTEGER, estate_id INTEGER, client_id INTEGER, uploaded INTEGER, profile_id INTEGER, inspection_id INTEGER, priority_id INTEGER, fciscore_id INTEGER, podnumber TEXT, guid TEXT, created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_ITEMTYPE = "CREATE TABLE itemtypes(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_LOCALITEM = "CREATE TABLE localitems(id INTEGER PRIMARY KEY,name TEXT,description TEXT,notes TEXT,lastaction TEXT,location TEXT,user_id INTEGER,status_id INTEGER,defect_id INTEGER,itemtype_id INTEGER,trade_id INTEGER,action_id INTEGER,floor_id INTEGER,uploaded INTEGER,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_PROFILE = "CREATE TABLE profiles(id INTEGER PRIMARY KEY,user_id INTEGER,client_id INTEGER,estate_id INTEGER,name TEXT,itemtypes TEXT,defects TEXT,actions TEXT,trades TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_STATUS = "CREATE TABLE statuses(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_TRADE = "CREATE TABLE trades(id INTEGER PRIMARY KEY,name TEXT,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_USER = "CREATE TABLE users(id INTEGER PRIMARY KEY,name TEXT,email TEXT,password TEXT,app TEXT,is_admin INTEGER,client_id INTEGER,estate_id INTEGER,created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String CREATE_TABLE_INSPECTION = "CREATE TABLE inspections(id INTEGER PRIMARY KEY, user_id INTEGER, client_id INTEGER, estate_id INTEGER, building_id INTEGER, profile_id INTEGER, name TEXT, notes TEXT, intensity_id INTEGER, completed INTEGER, frequency_id INTEGER, alertaddresses TEXT, priority_id INTEGER, uploaded INTEGER, start_at DATETIME, end_at DATETIME, completed_at DATETIME, created_at DATETIME, updated_at DATETIME, deleted_at DATETIME)";
    private static final String DATABASE_NAME = "ppmpro";
    private static final int DATABASE_VERSION = 45;
    private static final String KEY_ACTIONS = "actions";
    private static final String KEY_ACTION_ID = "action_id";
    private static final String KEY_APP = "app";
    private static final String KEY_BUILDING_ID = "building_id";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PRIORITYS = "prioritys";
    private static final String KEY_PRIORITY_ID = "priority_id";
    private static final String KEY_DEFECTS = "defects";
    private static final String KEY_DEFECT_ID = "defect_id";
    private static final String KEY_DELETED_AT = "deleted_at";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ESTATE_ID = "estate_id";
    private static final String KEY_FEATURED = "featured";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_FLOORPLAN = "floorplan";
    private static final String KEY_FLOOR_ID = "floor_id";
    private static final String KEY_GUID = "guid";
    private static final String KEY_ID = "id";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_ITEMTYPES = "itemtypes";
    private static final String KEY_ITEMTYPE_ID = "itemtype_id";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_LASTACTION = "lastaction";
    private static final String KEY_LOCAL_IMAGE_REF = "local_image_ref";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_NAME = "name";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PODNUMBER = "podnumber";
    private static final String KEY_PROFILE_ID = "profile_id";
    private static final String KEY_INSPECTION_ID = "inspection_id";
    private static final String KEY_FCISCORE_ID = "fciscore_id";
    private static final String KEY_STATUS_ID = "status_id";
    private static final String KEY_THUMBNAIL = "thumbnail";
    private static final String KEY_TRADE_ID = "trade_id";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_UPLOADED = "uploaded";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_XLOWER = "xlower";
    private static final String KEY_XUPPER = "xupper";
    private static final String KEY_YLOWER = "ylower";
    private static final String KEY_YUPPER = "yupper";
    private static final String KEY_INTENSITY_ID = "intensity_id";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_FREQUENCY_ID = "frequency_id";
    private static final String KEY_ALERTADDRESSES = "alertaddresses";
    private static final String KEY_START_AT = "start_at";
    private static final String KEY_END_AT = "end_at";
    private static final String KEY_COMPLETED_AT = "completed_at";
    private static final String LOG;
    private static final String TABLE_ACTION = "actions";
    private static final String TABLE_BUILDING = "buildings";
    private static final String TABLE_CLIENT = "clients";
    private static final String TABLE_DEFECT = "defects";
    private static final String TABLE_PRIORITY = "prioritys";
    private static final String TABLE_ESTATE = "estates";
    private static final String TABLE_FLOOR = "floors";
    private static final String TABLE_IMAGE = "images";
    private static final String TABLE_ITEM = "items";
    private static final String TABLE_ITEMTYPE = "itemtypes";
    private static final String TABLE_LOCALITEM = "localitems";
    private static final String TABLE_PROFILE = "profiles";
    private static final String TABLE_INSPECTION = "inspections";
    private static final String TABLE_STATUS = "statuses";
    private static final String TABLE_TRADE = "trades";
    private static final String TABLE_USER = "users";
    private static DatabaseHelper mInstance;

    static {
        mInstance = null;
        LOG = DatabaseHelper.class.getName();
    }

    public static DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACTION);
        db.execSQL(CREATE_TABLE_BUILDING);
        db.execSQL(CREATE_TABLE_CLIENT);
        db.execSQL(CREATE_TABLE_DEFECT);
        db.execSQL(CREATE_TABLE_PRIORITY);
        db.execSQL(CREATE_TABLE_ESTATE);
        db.execSQL(CREATE_TABLE_FLOOR);
        db.execSQL(CREATE_TABLE_IMAGE);
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_LOCALITEM);
        db.execSQL(CREATE_TABLE_ITEMTYPE);
        db.execSQL(CREATE_TABLE_STATUS);
        db.execSQL(CREATE_TABLE_TRADE);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_INSPECTION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS actions");
        db.execSQL("DROP TABLE IF EXISTS buildings");
        db.execSQL("DROP TABLE IF EXISTS clients");
        db.execSQL("DROP TABLE IF EXISTS defects");
        db.execSQL("DROP TABLE IF EXISTS prioritys");
        db.execSQL("DROP TABLE IF EXISTS estates");
        db.execSQL("DROP TABLE IF EXISTS floors");
        db.execSQL("DROP TABLE IF EXISTS images");
        db.execSQL("DROP TABLE IF EXISTS localitems");
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS itemtypes");
        db.execSQL("DROP TABLE IF EXISTS statuses");
        db.execSQL("DROP TABLE IF EXISTS trades");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS profiles");
        db.execSQL("DROP TABLE IF EXISTS inspections");
        onCreate(db);
    }

    public long createProfile(Profile profile) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(profile.getId()));
        values.put(KEY_USER_ID, Integer.valueOf(profile.getUser_id()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(profile.getClient_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(profile.getEstate_id()));
        values.put(KEY_NAME, profile.getName());
        values.put(TABLE_ITEMTYPE, profile.getItemtypes());
        values.put(TABLE_DEFECT, profile.getDefects());
        values.put(TABLE_ACTION, profile.getActions());
        values.put(TABLE_TRADE, profile.getTrades());
        values.put(KEY_CREATED_AT, profile.getCreated_at());
        values.put(KEY_UPDATED_AT, profile.getUpdated_at());
        values.put(KEY_DELETED_AT, profile.getDeleted_at());
        return db.replace(TABLE_PROFILE, null, values);
    }

    public Profile getProfile(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM profiles WHERE id = " + id, null);
        Profile object = new Profile();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
            object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setItemtypes(c.getString(c.getColumnIndex(TABLE_ITEMTYPE)));
            object.setDefects(c.getString(c.getColumnIndex(TABLE_DEFECT)));
            object.setActions(c.getString(c.getColumnIndex(TABLE_ACTION)));
            object.setTrades(c.getString(c.getColumnIndex(TABLE_TRADE)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Profile> getAllProfiles() {
        List<Profile> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM profiles", null);
        if (c.moveToFirst()) {
            do {
                Profile object = new Profile();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setItemtypes(c.getString(c.getColumnIndex(TABLE_ITEMTYPE)));
                object.setDefects(c.getString(c.getColumnIndex(TABLE_DEFECT)));
                object.setActions(c.getString(c.getColumnIndex(TABLE_ACTION)));
                object.setTrades(c.getString(c.getColumnIndex(TABLE_TRADE)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Profile> getAllProfiles(int client_id) {
        List<Profile> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM profiles WHERE client_id = " + client_id, null);
        if (c.moveToFirst()) {
            do {
                Profile object = new Profile();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setItemtypes(c.getString(c.getColumnIndex(TABLE_ITEMTYPE)));
                object.setDefects(c.getString(c.getColumnIndex(TABLE_DEFECT)));
                object.setActions(c.getString(c.getColumnIndex(TABLE_ACTION)));
                object.setTrades(c.getString(c.getColumnIndex(TABLE_TRADE)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Profile> getAllProfiles(int client_id, int estate_id) {
        List<Profile> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM profiles WHERE client_id = " + client_id + " AND estate_id = " + estate_id, null);
        if (c.moveToFirst()) {
            do {
                Profile object = new Profile();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setItemtypes(c.getString(c.getColumnIndex(TABLE_ITEMTYPE)));
                object.setDefects(c.getString(c.getColumnIndex(TABLE_DEFECT)));
                object.setActions(c.getString(c.getColumnIndex(TABLE_ACTION)));
                object.setTrades(c.getString(c.getColumnIndex(TABLE_TRADE)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createInspection(Inspection inspection) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(inspection.getId()));
        values.put(KEY_USER_ID, Integer.valueOf(inspection.getUser_id()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(inspection.getClient_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(inspection.getEstate_id()));
        values.put(KEY_BUILDING_ID, Integer.valueOf(inspection.getBuilding_id()));
        values.put(KEY_PROFILE_ID, Integer.valueOf(inspection.getProfile_id()));
        values.put(KEY_NAME, inspection.getName());
        values.put(KEY_NOTES, inspection.getNotes());
        values.put(KEY_INTENSITY_ID, inspection.getIntensity_id());
        values.put(KEY_COMPLETED, inspection.getCompleted());
        values.put(KEY_FREQUENCY_ID, inspection.getFrequency_id());
        values.put(KEY_ALERTADDRESSES, inspection.getAlertaddresses());
        values.put(KEY_PRIORITY_ID, Integer.valueOf(inspection.getPriority_id()));
        values.put(KEY_START_AT, inspection.getStart_at());
        values.put(KEY_END_AT, inspection.getEnd_at());
        values.put(KEY_COMPLETED_AT, inspection.getCompleted_at());
        values.put(KEY_CREATED_AT, inspection.getCreated_at());
        values.put(KEY_UPDATED_AT, inspection.getUpdated_at());
        values.put(KEY_DELETED_AT, inspection.getDeleted_at());
        return db.replace(TABLE_INSPECTION, null, values);
    }

    public long updateInspection(int inspection_id, int uploaded, int priority_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADED, uploaded);
        values.put(KEY_PRIORITY_ID, priority_id);
        return (long) db.update(TABLE_INSPECTION, values, "id = " + inspection_id, null);
    }

    public Inspection getInspection(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM inspections WHERE id = " + id, null);
        Inspection object = new Inspection();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
            object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
            object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
            object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
            object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
            object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
            object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
            object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
            object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
            object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
            object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
            object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Inspection> getAllInspections() {
        List<Inspection> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM inspections", null);
        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Inspection> getAllInspections(int client_id) {
        List<Inspection> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM inspections WHERE client_id = " + client_id, null);
        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Inspection> getAllInspections(int client_id, int estate_id) {
        List<Inspection> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id, null);
        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Inspection> getCurrentInspections(int client_id, int estate_id) {
        List<Inspection> objects = new ArrayList();
        //Cursor c = getReadableDatabase().rawQuery("SELECT * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id + " AND datetime(start_at) > datetime('now','-4 weeks') AND date(start_at) < date('now','+4 weeks')", null);
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id + " AND date(start_at) > date('now', '-7 days') AND date(start_at) < date('now','+7 days')", null);
        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));

                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Inspection> getInspectionsByDay(int client_id, int estate_id, String current_date) {
        ArrayList<Inspection> objects = new ArrayList();
        //Cursor c = getReadableDatabase().rawQuery("SELECT * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id + " AND datetime(start_at) > datetime('now','-4 weeks') AND date(start_at) < date('now','+4 weeks')", null);
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id + " AND date(start_at) <= date('" + current_date + "') AND date(end_at) >= date('" + current_date + "')", null);

        Log.d(LOG, "SELECT * FROM inspections WHERE client_id = " + client_id + " AND estate_id = " + estate_id + " AND date(start_at) <= date('" + current_date + "') AND date(end_at) >= date('" + current_date + "')");

        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));

                Client client = getClient((long) object.getClient_id());
                if (client != null) {
                    object.setClient(client);
                }

                Estate estate = getEstate((long) object.getEstate_id());
                if (estate != null) {
                    object.setEstate(estate);
                }

                try{
                    Building building = getBuilding((long) object.getBuilding_id());
                    if (building != null) {
                        object.setBuilding(building);
                    }
                }
                catch(Exception e){}

                try{
                    Floor floor = getFloor((long) object.getFloor_id());
                    if (floor != null) {
                        object.setFloor(floor);
                    }
                }
                catch(Exception e){}

                Profile profile = getProfile((long) object.getProfile_id());
                if (profile != null) {
                    object.setProfile(profile);
                }

                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Inspection> getUpdatedInspections() {
        ArrayList<Inspection> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM inspections WHERE uploaded = 2", null);
        if (c.moveToFirst()) {
            do {
                Inspection object = new Inspection();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setIntensity_id(c.getInt(c.getColumnIndex(KEY_INTENSITY_ID)));
                object.setCompleted(c.getInt(c.getColumnIndex(KEY_COMPLETED)));
                object.setFrequency_id(c.getInt(c.getColumnIndex(KEY_FREQUENCY_ID)));
                object.setAlertaddresses(c.getString(c.getColumnIndex(KEY_ALERTADDRESSES)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setStart_at(c.getString(c.getColumnIndex(KEY_START_AT)));
                object.setEnd_at(c.getString(c.getColumnIndex(KEY_END_AT)));
                object.setCompleted_at(c.getString(c.getColumnIndex(KEY_COMPLETED_AT)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));

                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createAction(Action action) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(action.getId()));
        values.put(KEY_NAME, action.getName());
        values.put(KEY_CREATED_AT, action.getCreated_at());
        values.put(KEY_UPDATED_AT, action.getUpdated_at());
        values.put(KEY_DELETED_AT, action.getDeleted_at());
        return db.replace(TABLE_ACTION, null, values);
    }

    public Action getAction(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM actions WHERE id = " + id, null);
        Action object = new Action();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public Action getAction(String name) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM actions WHERE name = '" + name + "'", null);
        Action object = new Action();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Action> getAllActions() {
        List<Action> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM actions ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                Action object = new Action();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createBuilding(Building building) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(building.getId()));
        values.put(KEY_NAME, building.getName());
        values.put(KEY_ESTATE_ID, Integer.valueOf(building.getEstate_id()));
        values.put(KEY_CREATED_AT, building.getCreated_at());
        values.put(KEY_UPDATED_AT, building.getUpdated_at());
        values.put(KEY_DELETED_AT, building.getDeleted_at());
        return db.replace(TABLE_BUILDING, null, values);
    }

    public Building getBuilding(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM buildings WHERE id = " + id, null);
        Building object = new Building();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
        }
        c.close();
        return object;
    }

    public List<Building> getAllBuildings() {
        List<Building> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM buildings", null);
        if (c.moveToFirst()) {
            do {
                Building object = new Building();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Building> getAllBuildings(int estate_id) {
        List<Building> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM buildings WHERE estate_id = " + estate_id, null);
        if (c.moveToFirst()) {
            do {
                Building object = new Building();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createClient(Client client) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(client.getId()));
        values.put(KEY_NAME, client.getName());
        values.put(KEY_CREATED_AT, client.getCreated_at());
        values.put(KEY_UPDATED_AT, client.getUpdated_at());
        values.put(KEY_DELETED_AT, client.getDeleted_at());
        return db.replace(TABLE_CLIENT, null, values);
    }

    public Client getClient(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM clients WHERE id = " + id, null);
        Client object = new Client();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Client> getAllClients() {
        List<Client> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM clients", null);
        if (c.moveToFirst()) {
            do {
                Client object = new Client();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Client> getAllClients(int client_id) {
        List<Client> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM clients WHERE id = " + client_id, null);
        if (c.moveToFirst()) {
            do {
                Client object = new Client();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createDefect(Defect defect) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(defect.getId()));
        values.put(KEY_NAME, defect.getName());
        values.put(KEY_CREATED_AT, defect.getCreated_at());
        values.put(KEY_UPDATED_AT, defect.getUpdated_at());
        values.put(KEY_DELETED_AT, defect.getDeleted_at());
        return db.replace(TABLE_DEFECT, null, values);
    }

    public Defect getDefect(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM defects WHERE id = " + id, null);
        Defect object = new Defect();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public Defect getDefect(String name) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM defects WHERE name = '" + name + "'", null);
        Defect object = new Defect();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Defect> getAllDefects() {
        List<Defect> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM defects ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                Defect object = new Defect();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }
    
    
    /* PRIORITY */
    public long createPriority(Priority priority) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(priority.getId()));
        values.put(KEY_NAME, priority.getName());
        values.put(KEY_CREATED_AT, priority.getCreated_at());
        values.put(KEY_UPDATED_AT, priority.getUpdated_at());
        values.put(KEY_DELETED_AT, priority.getDeleted_at());
        return db.replace(TABLE_PRIORITY, null, values);
    }

    public Priority getPriority(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM prioritys WHERE id = " + id, null);
        Priority object = new Priority();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public Priority getPriority(String name) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM prioritys WHERE name = '" + name + "'", null);
        Priority object = new Priority();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Priority> getAllPrioritys() {
        List<Priority> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM prioritys", null);
        if (c.moveToFirst()) {
            do {
                Priority object = new Priority();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }
    
    

    public long createEstate(Estate estate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(estate.getId()));
        values.put(KEY_NAME, estate.getName());
        values.put(KEY_CLIENT_ID, Integer.valueOf(estate.getClient_id()));
        values.put(KEY_CREATED_AT, estate.getCreated_at());
        values.put(KEY_UPDATED_AT, estate.getUpdated_at());
        values.put(KEY_DELETED_AT, estate.getDeleted_at());
        return db.replace(TABLE_ESTATE, null, values);
    }

    public Estate getEstate(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM estates WHERE id = " + id, null);
        Estate object = new Estate();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Estate> getAllEstates() {
        List<Estate> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM estates", null);
        if (c.moveToFirst()) {
            do {
                Estate object = new Estate();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Estate> getAllEstates(int client_id) {
        List<Estate> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM estates WHERE client_id = " + client_id, null);
        if (c.moveToFirst()) {
            do {
                Estate object = new Estate();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Estate> getAllEstates(int client_id, int estate_id) {
        List<Estate> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM estates WHERE client_id = " + client_id + " AND id = " + estate_id, null);
        if (c.moveToFirst()) {
            do {
                Estate object = new Estate();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createFloor(Floor floor) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(floor.getId()));
        values.put(KEY_NAME, floor.getName());
        values.put(KEY_XLOWER, floor.getXlower());
        values.put(KEY_XUPPER, floor.getXupper());
        values.put(KEY_YLOWER, floor.getYlower());
        values.put(KEY_YUPPER, floor.getYupper());
        values.put(KEY_FLOORPLAN, floor.getFloorplan());
        values.put(KEY_BUILDING_ID, Integer.valueOf(floor.getBuilding_id()));
        values.put(KEY_CREATED_AT, floor.getCreated_at());
        values.put(KEY_UPDATED_AT, floor.getUpdated_at());
        values.put(KEY_DELETED_AT, floor.getDeleted_at());
        return db.replace(TABLE_FLOOR, null, values);
    }

    public Floor getFloor(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM floors WHERE id = " + id, null);
        Floor object = new Floor();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setXlower(c.getString(c.getColumnIndex(KEY_XLOWER)));
            object.setXupper(c.getString(c.getColumnIndex(KEY_XUPPER)));
            object.setYlower(c.getString(c.getColumnIndex(KEY_YLOWER)));
            object.setYupper(c.getString(c.getColumnIndex(KEY_YUPPER)));
            object.setFloorplan(c.getString(c.getColumnIndex(KEY_FLOORPLAN)));
            object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Floor> getAllFloors() {
        List<Floor> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM floors", null);
        if (c.moveToFirst()) {
            do {
                Floor object = new Floor();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setXlower(c.getString(c.getColumnIndex(KEY_XLOWER)));
                object.setXupper(c.getString(c.getColumnIndex(KEY_XUPPER)));
                object.setYlower(c.getString(c.getColumnIndex(KEY_YLOWER)));
                object.setYupper(c.getString(c.getColumnIndex(KEY_YUPPER)));
                object.setFloorplan(c.getString(c.getColumnIndex(KEY_FLOORPLAN)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public List<Floor> getAllFloors(int building_id) {
        List<Floor> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM floors WHERE building_id = " + building_id, null);
        if (c.moveToFirst()) {
            do {
                Floor object = new Floor();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setXlower(c.getString(c.getColumnIndex(KEY_XLOWER)));
                object.setXupper(c.getString(c.getColumnIndex(KEY_XUPPER)));
                object.setYlower(c.getString(c.getColumnIndex(KEY_YLOWER)));
                object.setYupper(c.getString(c.getColumnIndex(KEY_YUPPER)));
                object.setFloorplan(c.getString(c.getColumnIndex(KEY_FLOORPLAN)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createImage(Image image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(image.getId()));
        values.put(KEY_NAME, image.getName());
        values.put(KEY_FILENAME, image.getFilename());
        values.put(KEY_THUMBNAIL, image.getThumbnail());
        values.put(KEY_FEATURED, Integer.valueOf(image.getFeatured()));
        values.put(KEY_ITEM_ID, Integer.valueOf(image.getItem_id()));
        return db.replace(TABLE_IMAGE, null, values);
    }

    public long saveImage(Image image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.getName());
        values.put(KEY_FILENAME, image.getFilename());
        values.put(KEY_THUMBNAIL, image.getThumbnail());
        values.put(KEY_FEATURED, Integer.valueOf(image.getFeatured()));
        values.put(KEY_ITEM_ID, Integer.valueOf(image.getItem_id()));
        values.put(KEY_CREATED_AT, image.getCreated_at());
        values.put(KEY_UPDATED_AT, image.getUpdated_at());
        values.put(KEY_DELETED_AT, image.getDeleted_at());
        values.put(KEY_LOCAL_IMAGE_REF, image.getLocalImageRef());
        values.put(KEY_UPLOADED, Integer.valueOf(image.getUploaded()));
        return db.insert(TABLE_IMAGE, null, values);
    }

    public long updateLocalImage(int remoteImageID, int localImageID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADED, Integer.valueOf(0));
        return (long) db.update(TABLE_IMAGE, values, "id = " + localImageID, null);
    }

    public long updateImageItemID(int image_id, int item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ID, item_id);
        return (long) db.update(TABLE_IMAGE, values, "id = " + image_id, null);
    }

    public Image getImage(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE id = " + id, null);
        Image object = new Image();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
            object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
            object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
            object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
            object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
            c.close();
        }
        return object;
    }

    public Image getFeaturedImage(long item_id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE item_id = " + item_id, null);
        if (c == null || c.getCount() <= 0) {
            return null;
        }
        c.moveToFirst();
        Image object = new Image();
        object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
        object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
        object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
        object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
        object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
        object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
        object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
        object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
        c.close();
        return object;
    }

    public ArrayList<Image> getAllImages() {
        ArrayList<Image> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images", null);
        if (c.moveToFirst()) {
            do {
                Image object = new Image();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
                object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
                object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Image> getAllImagesByItemID(int itemID) {
        ArrayList<Image> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE item_id = " + itemID, null);
        if (c.moveToFirst()) {
            do {
                Image object = new Image();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
                object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
                object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Image> getNewImagesByItemID(int itemID) {
        ArrayList<Image> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE item_id = " + itemID + " AND " + KEY_UPLOADED + " = 2", null);
        if (c.moveToFirst()) {
            do {
                Image object = new Image();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
                object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
                object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Image> getAllImagesByItemIDFull(int itemID) {
        ArrayList<Image> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE item_id = " + itemID, null);
        if (c.moveToFirst()) {
            do {
                Image object = new Image();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
                object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
                object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                try {
                    if (object.getLocalImageRef() != null) {
                        Log.d(LOG, "LOCAL: " + object.getLocalImageRef());
                        File photoFile = null;
                        photoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), object.getName());
                        if (photoFile.exists()) {
                            Log.d(LOG, "File Exits: " + photoFile.getAbsolutePath());
                            Bitmap myBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoFile.getPath()), 100, 100);
                            //Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            if (myBitmap != null) {
                                object.setImage(myBitmap);
                            }
                        }

                    }
                }catch(Exception ignore){};
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Image> getNewImages() {
        ArrayList<Image> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM images WHERE uploaded = 2", null);
        if (c.moveToFirst()) {
            do {
                Image object = new Image();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                object.setThumbnail(c.getString(c.getColumnIndex(KEY_THUMBNAIL)));
                object.setFeatured(c.getInt(c.getColumnIndex(KEY_FEATURED)));
                object.setItem_id(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setLocalImageRef(c.getString(c.getColumnIndex(KEY_LOCAL_IMAGE_REF)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(item.getId()));
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_NOTES, item.getNotes());
        values.put(KEY_LASTACTION, item.getLastaction());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_USER_ID, Integer.valueOf(item.getUser_id()));
        values.put(KEY_STATUS_ID, Integer.valueOf(item.getStatus_id()));
        values.put(KEY_DEFECT_ID, Integer.valueOf(item.getDefect_id()));
        values.put(KEY_PRIORITY_ID, Integer.valueOf(item.getPriority_id()));
        values.put(KEY_ITEMTYPE_ID, Integer.valueOf(item.getItemtype_id()));
        values.put(KEY_TRADE_ID, Integer.valueOf(item.getTrade_id()));
        values.put(KEY_ACTION_ID, Integer.valueOf(item.getAction_id()));
        values.put(KEY_FLOOR_ID, Integer.valueOf(item.getFloor_id()));
        values.put(KEY_BUILDING_ID, Integer.valueOf(item.getBuilding_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(item.getEstate_id()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(item.getClient_id()));
        values.put(KEY_PROFILE_ID, Integer.valueOf(item.getProfile_id()));
        values.put(KEY_INSPECTION_ID, Integer.valueOf(item.getInspection_id()));
        values.put(KEY_FCISCORE_ID, Integer.valueOf(item.getFciscore_id()));
        values.put(KEY_PODNUMBER, item.getPodnumber());
        values.put(KEY_GUID, item.getGuid());
        values.put(KEY_CREATED_AT, item.getCreated_at());
        values.put(KEY_UPDATED_AT, item.getUpdated_at());
        values.put(KEY_DELETED_AT, item.getDeleted_at());
        return db.replace(TABLE_ITEM, null, values);
    }

    public long updateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(item.getId()));
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_NOTES, item.getNotes());
        values.put(KEY_LASTACTION, item.getLastaction());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_USER_ID, Integer.valueOf(item.getUser_id()));
        values.put(KEY_STATUS_ID, Integer.valueOf(item.getStatus_id()));
        values.put(KEY_DEFECT_ID, Integer.valueOf(item.getDefect_id()));
        values.put(KEY_PRIORITY_ID, Integer.valueOf(item.getPriority_id()));
        values.put(KEY_ITEMTYPE_ID, Integer.valueOf(item.getItemtype_id()));
        values.put(KEY_TRADE_ID, Integer.valueOf(item.getTrade_id()));
        values.put(KEY_ACTION_ID, Integer.valueOf(item.getAction_id()));
        values.put(KEY_FLOOR_ID, Integer.valueOf(item.getFloor_id()));
        values.put(KEY_BUILDING_ID, Integer.valueOf(item.getBuilding_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(item.getEstate_id()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(item.getClient_id()));
        values.put(KEY_PROFILE_ID, Integer.valueOf(item.getProfile_id()));
        values.put(KEY_INSPECTION_ID, Integer.valueOf(item.getInspection_id()));
        values.put(KEY_FCISCORE_ID, Integer.valueOf(item.getFciscore_id()));
        values.put(KEY_PODNUMBER, item.getPodnumber());
        values.put(KEY_UPDATED_AT, item.getUpdated_at());
        values.put(KEY_UPLOADED, Integer.valueOf(item.getUploaded()));
        return (long) db.update(TABLE_ITEM, values, "id = " + item.getId(), null);
    }

    public long resetItemUploadStatus(int itemID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(itemID));
        values.put(KEY_UPLOADED, Integer.valueOf(0));
        return (long) db.update(TABLE_ITEM, values, "id = " + itemID, null);
    }

    public long updateItemByGUID(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTES, item.getNotes());
        //values.put(KEY_LOCATION, item.getLocation());
        //values.put(KEY_USER_ID, Integer.valueOf(item.getUser_id()));
        values.put(KEY_STATUS_ID, Integer.valueOf(item.getStatus_id()));
        //values.put(KEY_DEFECT_ID, Integer.valueOf(item.getDefect_id()));
        //values.put(KEY_PRIORITY_ID, Integer.valueOf(item.getPriority_id()));
        //values.put(KEY_ITEMTYPE_ID, Integer.valueOf(item.getItemtype_id()));
        //values.put(KEY_TRADE_ID, Integer.valueOf(item.getTrade_id()));
        //values.put(KEY_ACTION_ID, Integer.valueOf(item.getAction_id()));
        //values.put(KEY_FLOOR_ID, Integer.valueOf(item.getFloor_id()));
        //values.put(KEY_BUILDING_ID, Integer.valueOf(item.getBuilding_id()));
        //values.put(KEY_ESTATE_ID, Integer.valueOf(item.getEstate_id()));
        //values.put(KEY_CLIENT_ID, Integer.valueOf(item.getClient_id()));
        //values.put(KEY_PROFILE_ID, Integer.valueOf(item.getProfile_id()));
        //values.put(KEY_INSPECTION_ID, Integer.valueOf(item.getInspection_id()));
        //values.put(KEY_FCISCORE_ID, Integer.valueOf(item.getFciscore_id()));
        //values.put(KEY_PODNUMBER, item.getPodnumber());
        values.put(KEY_UPDATED_AT, item.getUpdated_at());
        return (long) db.update(TABLE_ITEM, values, "guid = '" + item.getGuid() + "'", null);
    }

    public long updateLocalItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADED, Integer.valueOf(0));
        return (long) db.update(TABLE_ITEM, values, "id = " + item.getId(), null);
    }

    public long updateLocalItemWithAttributes(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADED, Integer.valueOf(0));
        //values.put(KEY_ITEMTYPE_ID, Integer.valueOf(item.getItemtype_id()));
        //values.put(KEY_TRADE_ID, Integer.valueOf(item.getTrade_id()));
        //values.put(KEY_ACTION_ID, Integer.valueOf(item.getAction_id()));
        values.put(KEY_STATUS_ID, Integer.valueOf(item.getStatus_id()));
        //values.put(KEY_DEFECT_ID, Integer.valueOf(item.getDefect_id()));
        //values.put(KEY_PRIORITY_ID, Integer.valueOf(item.getPriority_id()));
        return (long) db.update(TABLE_ITEM, values, "id = " + item.getId(), null);
    }

    public long updateLocalItemFromServer(int item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOADED, Integer.valueOf(0));
        return (long) db.update(TABLE_ITEM, values, "id = " + item_id, null);
    }

    public long deleteLocalItem(int item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        return (long) db.delete(TABLE_ITEM, "id = " + item_id, null);
    }

    public long deleteLocalImage(int item_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        return (long) db.delete(TABLE_IMAGE, "item_id = " + item_id, null);
    }

    public long saveItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_DESCRIPTION, item.getDescription());
        values.put(KEY_NOTES, item.getNotes());
        values.put(KEY_LASTACTION, item.getLastaction());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_USER_ID, Integer.valueOf(item.getUser_id()));
        values.put(KEY_STATUS_ID, Integer.valueOf(item.getStatus_id()));
        values.put(KEY_DEFECT_ID, Integer.valueOf(item.getDefect_id()));
        values.put(KEY_PRIORITY_ID, Integer.valueOf(item.getPriority_id()));
        values.put(KEY_ITEMTYPE_ID, Integer.valueOf(item.getItemtype_id()));
        values.put(KEY_TRADE_ID, Integer.valueOf(item.getTrade_id()));
        values.put(KEY_ACTION_ID, Integer.valueOf(item.getAction_id()));
        values.put(KEY_FLOOR_ID, Integer.valueOf(item.getFloor_id()));
        values.put(KEY_BUILDING_ID, Integer.valueOf(item.getBuilding_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(item.getEstate_id()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(item.getClient_id()));
        values.put(KEY_PROFILE_ID, Integer.valueOf(item.getProfile_id()));
        values.put(KEY_INSPECTION_ID, Integer.valueOf(item.getInspection_id()));
        values.put(KEY_FCISCORE_ID, Integer.valueOf(item.getFciscore_id()));
        values.put(KEY_PODNUMBER, item.getPodnumber());
        values.put(KEY_GUID, item.getGuid());
        values.put(KEY_CREATED_AT, item.getCreated_at());
        values.put(KEY_UPDATED_AT, item.getUpdated_at());
        values.put(KEY_UPLOADED, Integer.valueOf(item.getUploaded()));
        return db.insert(TABLE_ITEM, null, values);
    }

    public Item getItem(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE id = " + id, null);
        Item object = new Item();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
            object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
            object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
            object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
            object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
            object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
            object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
            object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
            object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
            object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
            object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
            object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
            object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
            object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
            object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
            object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
            object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
            object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
            object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            object.setAction(getAction((long) object.getAction_id()));
            object.setDefect(getDefect((long) object.getDefect_id()));
            object.setItemType(getItemType((long) object.getItemtype_id()));
            object.setStatus(getStatus((long) object.getStatus_id()));

            if (object.getPriority_id() > 0) {
                object.setPriority(getPriority((long) object.getPriority_id()));
            }

            if (object.getInspection_id() > 0) {
                object.setInspection(getInspection((long) object.getInspection_id()));
            }

            object.setTrade(getTrade((long) object.getTrade_id()));
            if (object.getProfile_id() > 0) {
                object.setProfile(getProfile((long) object.getProfile_id()));
            }
            Floor floor = getFloor((long) object.getFloor_id());
            if (floor != null) {
                Building building = getBuilding((long) floor.getBuilding_id());
                if (building != null) {
                    Estate estate = getEstate((long) building.getEstate_id());
                    if (estate != null) {
                        Client client = getClient((long) estate.getClient_id());
                        if (client != null) {
                            estate.setClient(client);
                        }
                        building.setEstate(estate);
                    }
                    floor.setBuilding(building);
                }
                object.setFloor(floor);
            }
            Image image = getFeaturedImage((long) object.getId());
            if (image != null) {
                object.setFeaturedImage(image);
            }
            c.close();
        }
        return object;
    }

    public ArrayList<Item> filterItems(int client_id, int profile_id, int estate_id, int building_id, int floor_id, int status_id, int trade_id, int completed, int priority_id) {
        ArrayList<Item> objects = new ArrayList();
        String selectQuery = "SELECT  * FROM items";
        if (client_id > 0 || profile_id > 0 || estate_id > 0 || building_id > 0 || floor_id > 0 || status_id > 0 || trade_id > 0 || priority_id > 0 || completed > 0) {
            selectQuery = selectQuery + " WHERE created_at > '2015-01-01'";
            if (client_id > 0) {
                selectQuery = selectQuery + " AND client_id = " + client_id;
            }
            if (profile_id > 0) {
                selectQuery = selectQuery + " AND profile_id = " + profile_id;
            }
            if (priority_id > 0) {
                selectQuery = selectQuery + " AND priority_id = " + priority_id;
            }
            if (building_id > 0) {
                selectQuery = selectQuery + " AND building_id = " + building_id;
            }
            if (estate_id > 0) {
                selectQuery = selectQuery + " AND estate_id = " + estate_id;
            }
            if (floor_id > 0) {
                selectQuery = selectQuery + " AND floor_id = " + floor_id;
            }
            if (status_id > 0) {
                selectQuery = selectQuery + " AND status_id = " + status_id;
            }
            if (trade_id > 0) {
                selectQuery = selectQuery + " AND trade_id = " + trade_id;
            }
            if (completed > 0) {
                selectQuery = selectQuery + " AND status_id < 5";
            }
        }
        Cursor c = getReadableDatabase().rawQuery(selectQuery + " ORDER BY created_at DESC", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                try {
                    object.setAction(getAction((long) object.getAction_id()));
                    object.setDefect(getDefect((long) object.getDefect_id()));
                    object.setItemType(getItemType((long) object.getItemtype_id()));
                    object.setStatus(getStatus((long) object.getStatus_id()));
                    object.setTrade(getTrade((long) object.getTrade_id()));
                    if (object.getPriority_id() > 0) {
                        object.setPriority(getPriority((long) object.getPriority_id()));
                    }

                    if (object.getProfile_id() > 0) {
                        object.setProfile(getProfile((long) object.getProfile_id()));
                    }
                    Floor floor = getFloor((long) object.getFloor_id());
                    if (floor != null) {
                        Building building = getBuilding((long) floor.getBuilding_id());
                        if (building != null) {
                            Estate estate = getEstate((long) building.getEstate_id());
                            if (estate != null) {
                                Client client = getClient((long) estate.getClient_id());
                                if (client != null) {
                                    estate.setClient(client);
                                }
                                building.setEstate(estate);
                            }
                            floor.setBuilding(building);
                        }
                        object.setFloor(floor);
                    }
                    Image image = getFeaturedImage((long) object.getId());
                    if (image != null) {
                        object.setFeaturedImage(image);
                    }
                } catch (Exception e) {
                }
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE status_id < 5 ORDER BY created_at DESC LIMIT 0, 100", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                try {
                    object.setAction(getAction((long) object.getAction_id()));
                    object.setDefect(getDefect((long) object.getDefect_id()));
                    object.setItemType(getItemType((long) object.getItemtype_id()));
                    object.setStatus(getStatus((long) object.getStatus_id()));
                    if (object.getPriority_id() > 0) {
                        object.setPriority(getPriority((long) object.getPriority_id()));
                    }
                    object.setTrade(getTrade((long) object.getTrade_id()));
                    if (object.getProfile_id() > 0) {
                        object.setProfile(getProfile((long) object.getProfile_id()));
                    }
                    Floor floor = getFloor((long) object.getFloor_id());
                    if (floor != null) {
                        Building building = getBuilding((long) floor.getBuilding_id());
                        if (building != null) {
                            Estate estate = getEstate((long) building.getEstate_id());
                            if (estate != null) {
                                Client client = getClient((long) estate.getClient_id());
                                if (client != null) {
                                    estate.setClient(client);
                                }
                                building.setEstate(estate);
                            }
                            floor.setBuilding(building);
                        }
                        object.setFloor(floor);
                    }
                    Image image = getFeaturedImage((long) object.getId());
                    if (image != null) {
                        object.setFeaturedImage(image);
                    }
                } catch (Exception e) {
                }
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getAllItems(int client_id) {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE status_id < 5 AND client_id = " + client_id + " ORDER BY " + KEY_CREATED_AT + " DESC LIMIT 0, 200", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                try {
                    object.setAction(getAction((long) object.getAction_id()));
                    object.setDefect(getDefect((long) object.getDefect_id()));
                    object.setItemType(getItemType((long) object.getItemtype_id()));
                    object.setStatus(getStatus((long) object.getStatus_id()));
                    object.setTrade(getTrade((long) object.getTrade_id()));
                    if (object.getPriority_id() > 0) {
                        object.setPriority(getPriority((long) object.getPriority_id()));
                    }

                    if (object.getProfile_id() > 0) {
                        object.setProfile(getProfile((long) object.getProfile_id()));
                    }
                    Floor floor = getFloor((long) object.getFloor_id());
                    if (floor != null) {
                        Building building = getBuilding((long) floor.getBuilding_id());
                        if (building != null) {
                            Estate estate = getEstate((long) building.getEstate_id());
                            if (estate != null) {
                                Client client = getClient((long) estate.getClient_id());
                                if (client != null) {
                                    estate.setClient(client);
                                }
                                building.setEstate(estate);
                            }
                            floor.setBuilding(building);
                        }
                        object.setFloor(floor);
                    }
                    Image image = getFeaturedImage((long) object.getId());
                    if (image != null) {
                        object.setFeaturedImage(image);
                    }
                } catch (Exception e) {
                }
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getAllItems(int client_id, int estate_id) {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE status_id < 5 AND client_id = " + client_id + " AND estate_id = " + estate_id + " ORDER BY " + KEY_CREATED_AT + " DESC LIMIT 0, 200", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                object.setUploaded(c.getInt(c.getColumnIndex(KEY_UPLOADED)));
                try {
                    object.setAction(getAction((long) object.getAction_id()));
                    object.setDefect(getDefect((long) object.getDefect_id()));
                    object.setItemType(getItemType((long) object.getItemtype_id()));
                    object.setStatus(getStatus((long) object.getStatus_id()));
                    object.setTrade(getTrade((long) object.getTrade_id()));
                    if (object.getPriority_id() > 0) {
                        object.setPriority(getPriority((long) object.getPriority_id()));
                    }
                    if (object.getProfile_id() > 0) {
                        object.setProfile(getProfile((long) object.getProfile_id()));
                    }
                    Floor floor = getFloor((long) object.getFloor_id());
                    if (floor != null) {
                        Building building = getBuilding((long) floor.getBuilding_id());
                        if (building != null) {
                            Estate estate = getEstate((long) building.getEstate_id());
                            if (estate != null) {
                                Client client = getClient((long) estate.getClient_id());
                                if (client != null) {
                                    estate.setClient(client);
                                }
                                building.setEstate(estate);
                            }
                            floor.setBuilding(building);
                        }
                        object.setFloor(floor);
                    }
                    Image image = getFeaturedImage((long) object.getId());
                    if (image != null) {
                        object.setFeaturedImage(image);
                    }
                } catch (Exception e) {
                }
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getAllItemsAsList() {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items ORDER BY created_at DESC", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getNewItems() {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE uploaded = 2 ORDER BY created_at ASC LIMIT 0,100", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public ArrayList<Item> getUpdatedItems() {
        ArrayList<Item> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM items WHERE uploaded = 1 LIMIT 0,100", null);
        if (c.moveToFirst()) {
            do {
                Item object = new Item();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                object.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
                object.setLastaction(c.getString(c.getColumnIndex(KEY_LASTACTION)));
                object.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
                object.setUser_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                object.setStatus_id(c.getInt(c.getColumnIndex(KEY_STATUS_ID)));
                object.setPriority_id(c.getInt(c.getColumnIndex(KEY_PRIORITY_ID)));
                object.setDefect_id(c.getInt(c.getColumnIndex(KEY_DEFECT_ID)));
                object.setItemtype_id(c.getInt(c.getColumnIndex(KEY_ITEMTYPE_ID)));
                object.setTrade_id(c.getInt(c.getColumnIndex(KEY_TRADE_ID)));
                object.setAction_id(c.getInt(c.getColumnIndex(KEY_ACTION_ID)));
                object.setFloor_id(c.getInt(c.getColumnIndex(KEY_FLOOR_ID)));
                object.setBuilding_id(c.getInt(c.getColumnIndex(KEY_BUILDING_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setProfile_id(c.getInt(c.getColumnIndex(KEY_PROFILE_ID)));
                object.setInspection_id(c.getInt(c.getColumnIndex(KEY_INSPECTION_ID)));
                object.setFciscore_id(c.getInt(c.getColumnIndex(KEY_FCISCORE_ID)));
                object.setPodnumber(c.getString(c.getColumnIndex(KEY_PODNUMBER)));
                object.setGuid(c.getString(c.getColumnIndex(KEY_GUID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long deleteItem(long ID) {
        return 0;
    }

    public long createItemType(ItemType itemtype) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(itemtype.getId()));
        values.put(KEY_NAME, itemtype.getName());
        values.put(KEY_CREATED_AT, itemtype.getCreated_at());
        values.put(KEY_UPDATED_AT, itemtype.getUpdated_at());
        values.put(KEY_DELETED_AT, itemtype.getDeleted_at());
        return db.replace(TABLE_ITEMTYPE, null, values);
    }

    public ItemType getItemType(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM itemtypes WHERE id = " + id, null);
        ItemType object = new ItemType();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public ItemType getItemType(String name) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM itemtypes WHERE name = '" + name + "'", null);
        ItemType object = new ItemType();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<ItemType> getAllItemTypes() {
        List<ItemType> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM itemtypes ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                ItemType object = new ItemType();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createStatus(Status status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(status.getId()));
        values.put(KEY_NAME, status.getName());
        values.put(KEY_CREATED_AT, status.getCreated_at());
        values.put(KEY_UPDATED_AT, status.getUpdated_at());
        values.put(KEY_DELETED_AT, status.getDeleted_at());
        return db.replace(TABLE_STATUS, null, values);
    }

    public Status getStatus(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM statuses WHERE id = " + id, null);
        Status object = new Status();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Status> getAllStatuss() {
        List<Status> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM statuses", null);
        if (c.moveToFirst()) {
            do {
                Status object = new Status();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createTrade(Trade trade) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(trade.getId()));
        values.put(KEY_NAME, trade.getName());
        values.put(KEY_CREATED_AT, trade.getCreated_at());
        values.put(KEY_UPDATED_AT, trade.getUpdated_at());
        values.put(KEY_DELETED_AT, trade.getDeleted_at());
        return db.replace(TABLE_TRADE, null, values);
    }

    public Trade getTrade(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM trades WHERE id = " + id, null);
        Trade object = new Trade();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public Trade getTrade(String name) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM trades WHERE name = '" + name + "'", null);
        Trade object = new Trade();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public List<Trade> getAllTrades() {
        List<Trade> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM trades ORDER BY name ASC", null);
        if (c.moveToFirst()) {
            do {
                Trade object = new Trade();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }

    public long createUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Integer.valueOf(user.getId()));
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_APP, user.getApp());
        values.put(KEY_IS_ADMIN, Integer.valueOf(user.getIsAdmin()));
        values.put(KEY_CLIENT_ID, Integer.valueOf(user.getClient_id()));
        values.put(KEY_ESTATE_ID, Integer.valueOf(user.getEstate_id()));
        values.put(KEY_CREATED_AT, user.getCreated_at());
        values.put(KEY_UPDATED_AT, user.getUpdated_at());
        values.put(KEY_DELETED_AT, user.getDeleted_at());
        return db.replace(TABLE_USER, null, values);
    }

    public User getUser(long id) {
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM users WHERE id = " + id, null);
        User object = new User();
        if (c != null) {
            c.moveToFirst();
            object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            object.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            object.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
            object.setApp(c.getString(c.getColumnIndex(KEY_APP)));
            object.setIsAdmin(c.getInt(c.getColumnIndex(KEY_IS_ADMIN)));
            object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
            object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
            object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
            object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            c.close();
        }
        return object;
    }

    public User getUser(String email) {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM users WHERE email = '" + email + "'", null);
        User object = new User();
        if (c != null) {
            if(c.moveToFirst()){
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                object.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                object.setApp(c.getString(c.getColumnIndex(KEY_APP)));
                object.setIsAdmin(c.getInt(c.getColumnIndex(KEY_IS_ADMIN)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            }
            c.close();
        }
        return object;
    }

    public User login(String username, String password) {
        ArrayList users = (ArrayList) getAllUsers();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM users WHERE (email = '" + username + "' AND " + KEY_APP + " = '" + password + "') OR (name = '" + username + "' AND " + KEY_APP + " = '" + password + "')", null);
        User object = null;
        if (c != null) {
            if (c.moveToFirst()) {
                object = new User();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                object.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                object.setApp(c.getString(c.getColumnIndex(KEY_APP)));
                object.setIsAdmin(c.getInt(c.getColumnIndex(KEY_IS_ADMIN)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
            }
            c.close();
        }
        return object;
    }

    public List<User> getAllUsers() {
        List<User> objects = new ArrayList();
        Cursor c = getReadableDatabase().rawQuery("SELECT  * FROM users", null);
        if (c.moveToFirst()) {
            do {
                User object = new User();
                object.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                object.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                object.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                object.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                object.setApp(c.getString(c.getColumnIndex(KEY_APP)));
                object.setIsAdmin(c.getInt(c.getColumnIndex(KEY_IS_ADMIN)));
                object.setClient_id(c.getInt(c.getColumnIndex(KEY_CLIENT_ID)));
                object.setEstate_id(c.getInt(c.getColumnIndex(KEY_ESTATE_ID)));
                object.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                object.setUpdated_at(c.getString(c.getColumnIndex(KEY_UPDATED_AT)));
                object.setDeleted_at(c.getString(c.getColumnIndex(KEY_DELETED_AT)));
                objects.add(object);
            } while (c.moveToNext());
        }
        c.close();
        return objects;
    }
}
