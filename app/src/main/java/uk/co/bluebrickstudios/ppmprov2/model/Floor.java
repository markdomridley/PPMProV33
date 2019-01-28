package uk.co.bluebrickstudios.ppmprov2.model;

public class Floor {
    Building building;
    int building_id;
    String created_at;
    String deleted_at;
    String floorplan;
    int id;
    String name;
    String updated_at;
    String xlower;
    String xupper;
    String ylower;
    String yupper;

    public Floor(){}

    public Floor(int id, String name, int building_id, String xupper, String xlower, String yupper, String ylower, String floorplan) {
        this.id = id;
        this.name = name;
        this.building_id = building_id;
        this.xupper = xupper;
        this.xlower = xlower;
        this.yupper = yupper;
        this.ylower = ylower;
        this.floorplan = floorplan;
    }

    public Building getBuilding() {
        return this.building;
    }

    public void setBuilding(Building building) {
        this.building = building;
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

    public int getBuilding_id() {
        return this.building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public String getXupper() {
        return this.xupper;
    }

    public void setXupper(String xupper) {
    }

    public String getXlower() {
        return this.xlower;
    }

    public void setXlower(String xlower) {
    }

    public String getYupper() {
        return this.yupper;
    }

    public void setYupper(String yupper) {
    }

    public String getYlower() {
        return this.ylower;
    }

    public void setYlower(String ylower) {
    }

    public String getFloorplan() {
        return this.floorplan;
    }

    public void setFloorplan(String floorplan) {
        this.floorplan = floorplan;
    }
}
