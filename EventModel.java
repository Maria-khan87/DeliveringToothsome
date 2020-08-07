package com.example.foodapp.Models;

import android.graphics.Bitmap;

public class EventModel {


    public static final String SIGN_UP="signup";
    public static final String BAWARCHI_PROFILE="bawarchiprofile";
    public static final String FOODIE_PROFILE="foodieprofile";
    public static final String BAWARCHI_MENU="bawarchimenu";


    private Bitmap bitmap;
    private String code;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
