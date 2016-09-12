package com.cravings.data;

/**
 * Created by mremondi on 7/28/16.
 */
public class MenuItem extends ModelObject{

    private String _id;
    private String name;
    private String description;
    private float rating;
    private int numberOfRatings;
    private String price;
    private String menuSection;
    private String menuSubSection;
    private String[] dietaryInfo;
    private String[] tags;
    private String menuID;
    private String restaurant_id;
    private String restaurant_name;


    public String getRestaurantID() {
        return restaurant_id;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurant_id = restaurantID;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    @Override
    public String getObjectID() {
        return _id;
    }

    public void setObjectID(String objectID) {
        this._id = objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuSection() {
        return menuSection;
    }

    public void setMenuSection(String menuSection) {
        this.menuSection = menuSection;
    }

    public String getMenuSubSection() {
        return menuSubSection;
    }

    public void setMenuSubSection(String menuSubSection) {
        this.menuSubSection = menuSubSection;
    }

    public String[] getDietaryInfo() {
        return dietaryInfo;
    }

    public void setDietaryInfo(String[] dietaryInfo) {
        this.dietaryInfo = dietaryInfo;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }
}