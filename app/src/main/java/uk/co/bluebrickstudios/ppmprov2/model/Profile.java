package uk.co.bluebrickstudios.ppmprov2.model;

public class Profile {
    String actions;
    int client_id;
    int estate_id;
    String created_at;
    String defects;
    String deleted_at;
    int id;
    String itemtypes;
    String name;
    String updated_at;
    int user_id;
    String trades;

    public Profile(int id, int user_id, int client_id, String name, String itemtypes, String defects, String actions, String trades, int estate_id) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.client_id = client_id;
        this.estate_id = estate_id;
        this.itemtypes = itemtypes;
        this.defects = defects;
        this.actions = actions;
        this.trades = trades;
    }

    public Profile(){}

    public String getTrades() {
        return trades;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getClient_id() {
        return this.client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getEstate_id() {
        return this.estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemtypes() {
        return this.itemtypes;
    }

    public void setItemtypes(String itemtypes) {
        this.itemtypes = itemtypes;
    }

    public String getDefects() {
        return this.defects;
    }

    public void setDefects(String defects) {
        this.defects = defects;
    }

    public String getActions() {
        return this.actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return this.deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
