package com.example.foodapp.Models;

public class ScheduleItemModel {

    private String Order_ID;
    private String OrderDate;
    private String ShippingAddress;
    private String ScheduleDate;
    private String ScheduleSlot;
    private String Foodie_Id;
    private String Foodie_UserEmail;
    private String Bawarchi_Id;
    private String Bawarchi_UserEmail;
    private String Status_From_Bawarchi;
    private String Staus_From_Foodie;
    private String OrderDesc;
    private String OrderedItemsDetail;
    private String GrantTotal;
    private float rating=0;
    private String remarks="";


    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getScheduleSlot() {
        return ScheduleSlot;
    }

    public void setScheduleSlot(String scheduleSlot) {
        ScheduleSlot = scheduleSlot;
    }

    public String getFoodie_Id() {
        return Foodie_Id;
    }

    public void setFoodie_Id(String foodie_Id) {
        Foodie_Id = foodie_Id;
    }

    public String getFoodie_UserEmail() {
        return Foodie_UserEmail;
    }

    public void setFoodie_UserEmail(String foodie_UserEmail) {
        Foodie_UserEmail = foodie_UserEmail;
    }

    public String getBawarchi_Id() {
        return Bawarchi_Id;
    }

    public void setBawarchi_Id(String bawarchi_Id) {
        Bawarchi_Id = bawarchi_Id;
    }

    public String getBawarchi_UserEmail() {
        return Bawarchi_UserEmail;
    }

    public void setBawarchi_UserEmail(String bawarchi_UserEmail) {
        Bawarchi_UserEmail = bawarchi_UserEmail;
    }

    public String getStatus_From_Bawarchi() {
        return Status_From_Bawarchi;
    }

    public void setStatus_From_Bawarchi(String status_From_Bawarchi) {
        Status_From_Bawarchi = status_From_Bawarchi;
    }

    public String getStaus_From_Foodie() {
        return Staus_From_Foodie;
    }

    public void setStaus_From_Foodie(String staus_From_Foodie) {
        Staus_From_Foodie = staus_From_Foodie;
    }

    public String getOrderDesc() {
        return OrderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        OrderDesc = orderDesc;
    }

    public String getOrderedItemsDetail() {
        return OrderedItemsDetail;
    }

    public void setOrderedItemsDetail(String orderedItemsDetail) {
        OrderedItemsDetail = orderedItemsDetail;
    }

    public String getGrantTotal() {
        return GrantTotal;
    }

    public void setGrantTotal(String grantTotal) {
        GrantTotal = grantTotal;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
