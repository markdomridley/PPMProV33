package uk.co.bluebrickstudios.ppmprov2.model;

public class Building {
    String created_at;
    String deleted_at;
    Estate estate;
    int estate_id;
    int id;
    String name;
    String updated_at;

    public Building(){}

    public Building(int id, String name, int estate_id) {
        this.id = id;
        this.name = name;
        this.estate_id = estate_id;
    }

    public Estate getEstate() {
        return this.estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getEstate_id() {
        return this.estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }
}
