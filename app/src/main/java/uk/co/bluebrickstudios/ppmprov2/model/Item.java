package uk.co.bluebrickstudios.ppmprov2.model;

public class Item {
    Action action;
    int action_id;
    int building_id;
    int client_id;
    String created_at;
    Defect defect;
    int defect_id;
    String deleted_at;
    String description;
    int device_itemid;
    int estate_id;
    Image featuredImage;
    Floor floor;
    int floor_id;
    String guid;
    int id;
    ItemType itemType;
    int itemtype_id;
    String lastaction;
    String location;
    String name;
    String notes;
    String podnumber;
    Profile profile;
    Inspection inspection;
    String inspection_type;
    String job_number;
    int profile_id;
    int inspection_id;
    Priority priority;
    int priority_id;
    Status status;
    int status_id;
    Trade trade;
    int trade_id;
    String updated_at;
    int uploaded;
    int user_id;
    int fciscore_id = 0;

    public String getInspection_type() {
        return inspection_type;
    }

    public void setInspection_type(String inspection_type) {
        this.inspection_type = inspection_type;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public int getFciscore_id() {
        return fciscore_id;
    }

    public void setFciscore_id(int fciscore_id) {
        this.fciscore_id = fciscore_id;
    }

    public int getInspection_id() {
        return inspection_id;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public void setInspection_id(int inspection_id) {
        this.inspection_id = inspection_id;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(int priority_id) {
        this.priority_id = priority_id;
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getProfile_id() {
        return this.profile_id;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getPodnumber() {
        return this.podnumber;
    }

    public void setPodnumber(String podnumber) {
        this.podnumber = podnumber;
    }

    public int getBuilding_id() {
        return this.building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }

    public int getEstate_id() {
        return this.estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

    public int getClient_id() {
        return this.client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getDevice_itemid() {
        return this.device_itemid;
    }

    public void setDevice_itemid(int device_itemid) {
        this.device_itemid = device_itemid;
    }

    public Image getFeaturedImage() {
        return this.featuredImage;
    }

    public void setFeaturedImage(Image featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Trade getTrade() {
        return this.trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Defect getDefect() {
        return this.defect;
    }

    public void setDefect(Defect defect) {
        this.defect = defect;
    }

    public Item() {
        this.user_id = 0;
        this.uploaded = 0;
        this.profile_id = 0;
    }

    public Item(int id, String name) {
        this.user_id = 0;
        this.uploaded = 0;
        this.profile_id = 0;
        this.id = id;
        this.name = name;
    }

    public Item(String name, String description, String notes, String lastaction, String location, int user_id, int status_id, int defect_id, int itemtype_id, int trade_id, int action_id, int floor_id, int uploaded) {
        this.user_id = 0;
        this.uploaded = 0;
        this.profile_id = 0;
        this.id = this.id;
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.lastaction = lastaction;
        this.location = location;
        this.user_id = user_id;
        this.status_id = status_id;
        this.defect_id = defect_id;
        this.itemtype_id = itemtype_id;
        this.trade_id = trade_id;
        this.action_id = action_id;
        this.floor_id = floor_id;
        this.uploaded = uploaded;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLastaction() {
        return this.lastaction;
    }

    public void setLastaction(String lastaction) {
        this.lastaction = lastaction;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUser_id() {
        if (this.user_id == 0) {
            this.user_id = 1;
        }
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus_id() {
        return this.status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getDefect_id() {
        return this.defect_id;
    }

    public void setDefect_id(int defect_id) {
        this.defect_id = defect_id;
    }

    public int getItemtype_id() {
        return this.itemtype_id;
    }

    public void setItemtype_id(int itemtype_id) {
        this.itemtype_id = itemtype_id;
    }

    public int getTrade_id() {
        return this.trade_id;
    }

    public void setTrade_id(int trade_id) {
        this.trade_id = trade_id;
    }

    public int getAction_id() {
        return this.action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public int getFloor_id() {
        return this.floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public int getUploaded() {
        return this.uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
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
