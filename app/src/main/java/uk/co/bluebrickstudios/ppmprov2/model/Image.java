package uk.co.bluebrickstudios.ppmprov2.model;

import android.graphics.Bitmap;

public class Image {
    String created_at;
    String deleted_at;
    int featured;
    String filename;
    int id;
    private Bitmap image;
    int item_id;
    String localImageRef;
    String name;
    String thumbnail;
    String updated_at;
    int uploaded;

    public Image(){

    }

    public Image(int id, String name, String filename, String thumbnail, int featured, int item_id) {
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.thumbnail = thumbnail;
        this.featured = featured;
        this.item_id = item_id;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getLocalImageRef() {
        return this.localImageRef;
    }

    public void setLocalImageRef(String localImageRef) {
        this.localImageRef = localImageRef;
    }

    public int getUploaded() {
        return this.uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public int getItem_id() {
        return this.item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFeatured() {
        return this.featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
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
}
