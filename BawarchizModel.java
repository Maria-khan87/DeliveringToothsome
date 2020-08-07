package com.example.foodapp.Models;

import android.graphics.Bitmap;

public class BawarchizModel {

    private String name;
    private String fullName;
    private Bitmap pic;
    private String bawarchiId;
    private float rating=0;

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBawarchiId() {
        return bawarchiId;
    }

    public void setBawarchiId(String bawarchiId) {
        this.bawarchiId = bawarchiId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
