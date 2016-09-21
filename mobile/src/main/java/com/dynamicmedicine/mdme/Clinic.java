package com.dynamicmedicine.mdme;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

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
    private DateTime updatedAt;
    private boolean isOpenSunday;
    private LocalTime sundayOpenTime;
    private LocalTime sundayCloseTime;
    private boolean isOpenMonday;
    private LocalTime mondayOpenTime;
    private LocalTime mondayCloseTime;
    private boolean isOpenTuesday;
    private LocalTime tuesdayOpenTime;
    private LocalTime tuesdayCloseTime;
    private boolean isOpenWednesday;
    private LocalTime wednesdayOpenTime;
    private LocalTime wednesdayCloseTime;
    private boolean isOpenThursday;
    private LocalTime thursdayOpenTime;
    private LocalTime thursdayCloseTime;
    private boolean isOpenFriday;
    private LocalTime fridayOpenTime;
    private LocalTime fridayCloseTime;
    private boolean isOpenSaturday;
    private LocalTime saturdayOpenTime;
    private LocalTime saturdayCloseTime;

    public Clinic() {
    }

    public Clinic(int id, String name, String address, String city, String state, String country, String zipcode, String phoneNumber, String fax_number, double latitude, double ne_latitude, double sw_latitude, double longitude, double ne_longitude, double sw_longitude, DateTime updatedAt) {
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
        this.updatedAt = updatedAt;
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

    public void setAddress(String address) {
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

    public void setNeLongitude(double neLongitude) {
        this.neLongitude = neLongitude;
    }

    public double getSwLatitude() { return swLatitude; }

    public void setSwLatitude(double swLatitude) {
        this.swLatitude = swLatitude;
    }

    public double getSwLongitude() { return swLongitude; }

    public void setSwLongitude(double swLongitude) {
        this.swLongitude = swLongitude;
    }

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

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isOpenSunday() {
        return isOpenSunday;
    }

    public void setOpenSunday(boolean openSunday) {
        isOpenSunday = openSunday;
    }

    public LocalTime getSundayOpenTime() {
        return sundayOpenTime;
    }

    public void setSundayOpenTime(LocalTime sundayOpenTime) {
        this.sundayOpenTime = sundayOpenTime;
    }

    public LocalTime getSundayCloseTime() {
        return sundayCloseTime;
    }

    public void setSundayCloseTime(LocalTime sundayCloseTime) {
        this.sundayCloseTime = sundayCloseTime;
    }

    public boolean isOpenMonday() {
        return isOpenMonday;
    }

    public void setOpenMonday(boolean openMonday) {
        isOpenMonday = openMonday;
    }

    public LocalTime getMondayOpenTime() {
        return mondayOpenTime;
    }

    public void setMondayOpenTime(LocalTime mondayOpenTime) {
        this.mondayOpenTime = mondayOpenTime;
    }

    public LocalTime getMondayCloseTime() {
        return mondayCloseTime;
    }

    public void setMondayCloseTime(LocalTime mondayCloseTime) {
        this.mondayCloseTime = mondayCloseTime;
    }

    public boolean isOpenTuesday() {
        return isOpenTuesday;
    }

    public void setOpenTuesday(boolean openTuesday) {
        isOpenTuesday = openTuesday;
    }

    public LocalTime getTuesdayOpenTime() {
        return tuesdayOpenTime;
    }

    public void setTuesdayOpenTime(LocalTime tuesdayOpenTime) {
        this.tuesdayOpenTime = tuesdayOpenTime;
    }

    public LocalTime getTuesdayCloseTime() {
        return tuesdayCloseTime;
    }

    public void setTuesdayCloseTime(LocalTime tuesdayCloseTime) {
        this.tuesdayCloseTime = tuesdayCloseTime;
    }

    public boolean isOpenWednesday() {
        return isOpenWednesday;
    }

    public void setOpenWednesday(boolean openWednesday) {
        isOpenWednesday = openWednesday;
    }

    public LocalTime getWednesdayOpenTime() {
        return wednesdayOpenTime;
    }

    public void setWednesdayOpenTime(LocalTime wednesdayOpenTime) {
        this.wednesdayOpenTime = wednesdayOpenTime;
    }

    public LocalTime getWednesdayCloseTime() {
        return wednesdayCloseTime;
    }

    public void setWednesdayCloseTime(LocalTime wednesdayCloseTime) {
        this.wednesdayCloseTime = wednesdayCloseTime;
    }

    public boolean isOpenThursday() {
        return isOpenThursday;
    }

    public void setOpenThursday(boolean openThursday) {
        isOpenThursday = openThursday;
    }

    public LocalTime getThursdayOpenTime() {
        return thursdayOpenTime;
    }

    public void setThursdayOpenTime(LocalTime thursdayOpenTime) {
        this.thursdayOpenTime = thursdayOpenTime;
    }

    public LocalTime getThursdayCloseTime() {
        return thursdayCloseTime;
    }

    public void setThursdayCloseTime(LocalTime thursdayCloseTime) {
        this.thursdayCloseTime = thursdayCloseTime;
    }

    public boolean isOpenFriday() {
        return isOpenFriday;
    }

    public void setOpenFriday(boolean openFriday) {
        isOpenFriday = openFriday;
    }

    public LocalTime getFridayOpenTime() {
        return fridayOpenTime;
    }

    public void setFridayOpenTime(LocalTime fridayOpenTime) {
        this.fridayOpenTime = fridayOpenTime;
    }

    public LocalTime getFridayCloseTime() {
        return fridayCloseTime;
    }

    public void setFridayCloseTime(LocalTime fridayCloseTime) {
        this.fridayCloseTime = fridayCloseTime;
    }

    public boolean isOpenSaturday() {
        return isOpenSaturday;
    }

    public void setOpenSaturday(boolean openSaturday) {
        isOpenSaturday = openSaturday;
    }

    public LocalTime getSaturdayOpenTime() {
        return saturdayOpenTime;
    }

    public void setSaturdayOpenTime(LocalTime saturdayOpenTime) {
        this.saturdayOpenTime = saturdayOpenTime;
    }

    public LocalTime getSaturdayCloseTime() {
        return saturdayCloseTime;
    }

    public void setSaturdayCloseTime(LocalTime saturdayCloseTime) {
        this.saturdayCloseTime = saturdayCloseTime;
    }
}

