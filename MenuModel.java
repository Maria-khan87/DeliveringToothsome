package com.example.foodapp.Models;

import android.graphics.Bitmap;

public class MenuModel {

    private String Item_ID;
    private String ItemName;
    private String ItemDesc;
    private String ItemPrice;
    private Bitmap ItemPic;
    private String Bawarchi;
    private String Deleted;


    public String getItem_ID() {
        return Item_ID;
    }

    public void setItem_ID(String item_ID) {
        Item_ID = item_ID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public Bitmap getItemPic() {
        return ItemPic;
    }

    public void setItemPic(Bitmap itemPic) {
        ItemPic = itemPic;
    }

    public String getBawarchi() {
        return Bawarchi;
    }

    public void setBawarchi(String bawarchi) {
        Bawarchi = bawarchi;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }
}
