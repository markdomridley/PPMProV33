package uk.co.bluebrickstudios.ppmprov2;

import android.app.Application;

public class PPMProApp extends Application {
    private static PPMProApp singleton;
    private int building_id;
    private int client_id;
    private int estate_id;
    private int floor_id;
    private int status_id;
    private int itemtype_id;
    private int defect_id;
    private int trade_id;
    private int action_id;
    private boolean isAdmin;
    private boolean loggedIn;
    private int profile_id;
    private int inspection_id;
    private int userId;
    private String username;

    public PPMProApp() {
        this.loggedIn = false;
        this.userId = 0;
        this.estate_id = 0;
        this.building_id = 0;
        this.floor_id = 0;
        this.profile_id = 0;
        this.inspection_id = 0;
        this.status_id = 0;
        this.itemtype_id = 0;
        this.defect_id = 0;
        this.trade_id = 0;
        this.action_id = 0;
    }

    public static PPMProApp getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public int getInspection_id() {
        return inspection_id;
    }

    public void setInspection_id(int inspection_id) {
        this.inspection_id = inspection_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getItemtype_id() {
        return itemtype_id;
    }

    public void setItemtype_id(int itemtype_id) {
        this.itemtype_id = itemtype_id;
    }

    public int getDefect_id() {
        return defect_id;
    }

    public void setDefect_id(int defect_id) {
        this.defect_id = defect_id;
    }

    public int getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(int trade_id) {
        this.trade_id = trade_id;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public int getProfile_id() {
        return this.profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public int getEstate_id() {
        return this.estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

    public int getBuilding_id() {
        return this.building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public int getFloor_id() {
        return this.floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public int getClient_id() {
        return this.client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
