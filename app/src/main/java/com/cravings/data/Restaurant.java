package com.cravings.data;

/**
 * Created by mremondi on 7/28/16.
 */
public class Restaurant extends ModelObject{
    private String _id;
    private String restaurant;
    private String description;
    private Loc loc;
    private String address;
    private String zipcode;
    private String[] tags;
    private String[] menus;
    private String restaurant_URL;

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc geometry) {
        this.loc = geometry;
    }

    @Override
    public String getObjectID() {
        return _id;
    }

    public void setObjectID(String objectID) {
        this._id = objectID;
    }

    public String getRestaurantName() {
        return restaurant;
    }

    public void setRestaurantName(String name) {
        this.restaurant = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getRestaurant_URL() {
        return restaurant_URL;
    }

    public void setRestaurant_URL(String restaurant_URL) {
        this.restaurant_URL = restaurant_URL;
    }

    public String[] getMenus() {
        return menus;
    }

    public void setMenus(String[] menus) {
        this.menus = menus;
    }
}

