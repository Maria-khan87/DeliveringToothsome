package com.example.foodapp.Models;

public class ProfileModel {


    private String UserEmail;
    private String Password;
    private String Role_Id;
    private String FullName;
    private String FullAddress;
    private String City_Id;
    private String PersonalContactNumber;
    private String OfficalContactNumber;
    private String Deleted;
    private String ProfilePic;
    private float rating=0;

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole_Id() {
        return Role_Id;
    }

    public void setRole_Id(String role_Id) {
        Role_Id = role_Id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getFullAddress() {
        return FullAddress;
    }

    public void setFullAddress(String fullAddress) {
        FullAddress = fullAddress;
    }

    public String getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(String city_Id) {
        City_Id = city_Id;
    }

    public String getPersonalContactNumber() {
        return PersonalContactNumber;
    }

    public void setPersonalContactNumber(String personalContactNumber) {
        PersonalContactNumber = personalContactNumber;
    }

    public String getOfficalContactNumber() {
        return OfficalContactNumber;
    }

    public void setOfficalContactNumber(String officalContactNumber) {
        OfficalContactNumber = officalContactNumber;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
