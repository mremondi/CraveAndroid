package com.cravings.data;

import java.util.List;

/**
 * Created by mremondi on 7/28/16.
 */
public class Menu extends ModelObject{

    private String restaurantID;
    private String menuName;
    private String _id;


    public String getMenuName() {
        return menuName;
    }

    public void setName(String name) {
        this.menuName = name;
    }


    @Override
    public String getObjectID() {
        return _id;
    }

    public void setObjectID(String objectID) {
        this._id = objectID;
    }
}