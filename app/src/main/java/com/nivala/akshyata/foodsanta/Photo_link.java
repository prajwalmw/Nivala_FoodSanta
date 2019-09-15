package com.nivala.akshyata.foodsanta;

public class Photo_link {
    private String photoUrl;
    private String titleData;
    private String expiryData;
    private String addressData;
    private String mobileData;


    public Photo_link(String photoUrl,String titleData,String expiryData, String addressData,String mobileData) {
        this.photoUrl = photoUrl;
        this.titleData = titleData;
        this.expiryData = expiryData;
        this.addressData = addressData;
        this.mobileData = mobileData;
    }

    public Photo_link()
    {

    }

    //photo
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    //title
    public String getTitleData() {
        return titleData;
    }

    public void setTitleData(String titleData) {
        this.titleData = titleData;
    }

    //expiry
    public String getExpiryData() {
        return expiryData;
    }

    public void setExpiryData(String expiryData) {
        this.expiryData = expiryData;
    }

    //address
    public String getAddressData() {
        return addressData;
    }

    public void setAddressData(String addressData) {this.addressData = addressData; }

    //mobile no.
    public String getMobileData() {
        return mobileData;
    }

    public void setMobileData(String mobileData) {
        this.mobileData = mobileData;
    }




}
