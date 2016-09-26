package com.cravings.data;


/**
 * Created by mremondi on 7/28/16.
 */
public class Menu extends ModelObject{

    private String restaurantID;
    private String menuName;
    private String _id;
    private String[] sections;

    public String[] getSections(){
        return sections;
    }

    public void setSections(String[] sections){
        this.sections = sections;
    }

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