package com.cravings.data;

/**
 * Created by mremondi on 9/18/16.
 */
public class Rating {
    private String itemID;
    private Float rating;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}
