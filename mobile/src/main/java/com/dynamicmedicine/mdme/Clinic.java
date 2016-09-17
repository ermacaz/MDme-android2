package com.dynamicmedicine.mdme;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/13/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class Clinic implements Serializable {

    private int id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String phoneNumber;
    private String faxNumber;
    private double latitude;
    private double neLatitude;
    private double swLatitude;
    private double longitude;
    private double neLongitude;
    private double swLongitude;
    private DateTime updated_at;

    public Clinic() {

    }

    public Clinic(int id, String name, String address, String city, String state, String country, String zipcode, String phoneNumber, String fax_number, double latitude, double ne_latitude, double sw_latitude, double longitude, double ne_longitude, double sw_longitude, DateTime updated_at) {
        this.id  = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.faxNumber = fax_number;
        this.latitude = latitude;
        this.neLatitude = ne_latitude;
        this.swLatitude = sw_latitude;
        this.longitude = longitude;
        this.neLongitude = ne_longitude;
        this.swLongitude = sw_longitude;
        this.updated_at = updated_at;
    }


    public Clinic(int id, String name, String address, String city, String state, String country, String zipcode, String phoneNumber, String fax_number, double latitude, double ne_latitude, double sw_latitude, double longitude, double ne_longitude, double sw_longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.faxNumber = fax_number;
        this.latitude = latitude;
        this.neLatitude = ne_latitude;
        this.swLatitude = sw_latitude;
        this.longitude = longitude;
        this.neLongitude = ne_longitude;
        this.swLongitude = sw_longitude;
    }

    @Override
    public String toString() { return name; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAddress(String address1) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public double getNeLatitude() { return neLatitude; }

    public void setNeLatitude(double neLatitude) { this.neLatitude = neLatitude; }

    public double getNeLongitude() { return neLongitude; }

    public void setNeLongitude(double neLongitude) { this.neLongitude = neLongitude; }

    public double getSwLatitude() { return swLatitude; }

    public void setSwLatitude(double swLatitude) { this.swLatitude = swLatitude;
    }

    public double getSwLongitude() { return swLongitude; }

    public void setSwLongitude(double swLongitude) { this.swLongitude = swLongitude; }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public DateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(DateTime updated_at) {
        this.updated_at = updated_at;
    }
}

