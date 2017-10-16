package com.letsappbuilder.Adapter;

import com.google.android.gms.location.places.Place;

public class UserLocation {
    private double latitude;
    private double longitude;
    private String name;
    private String address;

    public UserLocation() {
    }

    public UserLocation(Place place) {
        name = place.getName().toString();
        address = place.getAddress().toString();
        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
       // Log.e("@@@",name+"&"+address+"&"+latitude+"&"+longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}