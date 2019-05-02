package com.db.app.fragment.profile;


public class ProfileItem {
    private String itemName;
    private String itemValue;

    public ProfileItem(String itemName, String itemValue) {
        this.itemName = itemName;
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Override
    public String toString() {
        return "ProfileItem{" +
                "itemName='" + itemName + '\'' +
                ", itemValue='" + itemValue + '\'' +
                '}';
    }
}
