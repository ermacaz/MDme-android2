package com.dynamicmedicine.mdme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dynamicmedicine.mdme.Clinic;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/13/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

//http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
public class ClinicDatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "mdme";

    // Contacts table name
    private static final String TABLE_NAME = "clinics";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_MDME_ID = "mdme_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO  = "phone_number";
    private static final String KEY_FAX_NO  = "fax_number";
    private static final String KEY_ADDRESS  = "address";
    private static final String KEY_CITY  = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_ZIPCODE  = "zipcode";
    private static final String KEY_LATITUDE  = "latitude";
    private static final String KEY_NE_LATITUDE  = "ne_latitude";
    private static final String KEY_SW_LATITUDE  = "sw_latitude";
    private static final String KEY_LONGITUDE  = "longitude";
    private static final String KEY_NE_LONGITUDE  = "ne_longitude";
    private static final String KEY_SW_LONGITUDE  = "sw_longitude";
    private static final String KEY_UPDATED_AT  = "updated_at";
    private static final String KEY_SUNDAY_OPEN_TIME  = "sunday_open_time";
    private static final String KEY_SUNDAY_CLOSE_TIME  = "sunday_close_time";
    private static final String KEY_IS_OPEN_SUNDAY  = "is_open_sunday";
    private static final String KEY_MONDAY_OPEN_TIME  = "monday_open_time";
    private static final String KEY_MONDAY_CLOSE_TIME  = "monday_close_time";
    private static final String KEY_IS_OPEN_MONDAY  = "is_open_monday";
    private static final String KEY_TUESDAY_OPEN_TIME  = "tuesday_open_time";
    private static final String KEY_TUESDAY_CLOSE_TIME  = "tuesday_close_time";
    private static final String KEY_IS_OPEN_TUESDAY  = "is_open_tuesday";
    private static final String KEY_WEDNESDAY_OPEN_TIME  = "wednesday_open_time";
    private static final String KEY_WEDNESDAY_CLOSE_TIME  = "wednesday_close_time";
    private static final String KEY_IS_OPEN_WEDNESDAY  = "is_open_wednesday";
    private static final String KEY_THURSDAY_OPEN_TIME  = "thursday_open_time";
    private static final String KEY_THURSDAY_CLOSE_TIME  = "thursday_close_time";
    private static final String KEY_IS_OPEN_THURSDAY  = "is_open_thursday";
    private static final String KEY_FRIDAY_OPEN_TIME  = "friday_open_time";
    private static final String KEY_FRIDAY_CLOSE_TIME  = "friday_close_time";
    private static final String KEY_IS_OPEN_FRIDAY  = "is_open_friday";
    private static final String KEY_SATURDAY_OPEN_TIME  = "saturday_open_time";
    private static final String KEY_SATURDAY_CLOSE_TIME  = "saturday_close_time";
    private static final String KEY_IS_OPEN_SATURDAY  = "is_open_saturday";


    public ClinicDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MDME_ID + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_CITY + " TEXT,"
                + KEY_STATE + " TEXT," + KEY_COUNTRY + " TEXT,"
                + KEY_ZIPCODE + " TEXT," + KEY_PH_NO + " TEXT,"
                + KEY_FAX_NO + " TEXT," + KEY_LATITUDE + " FLOAT,"
                + KEY_NE_LATITUDE + " FLOAT," + KEY_SW_LATITUDE + " FLOAT,"
                + KEY_LONGITUDE + " FLOAT," + KEY_NE_LONGITUDE + " FLOAT,"
                + KEY_SW_LONGITUDE + " FLOAT," + KEY_UPDATED_AT + " INTEGER,"
                + KEY_IS_OPEN_SUNDAY + " BOOLEAN," + KEY_SUNDAY_OPEN_TIME + " INTEGER," + KEY_SUNDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_MONDAY + " BOOLEAN," + KEY_MONDAY_OPEN_TIME + " INTEGER," + KEY_MONDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_TUESDAY + " BOOLEAN," + KEY_TUESDAY_OPEN_TIME + " INTEGER," + KEY_TUESDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_WEDNESDAY + " BOOLEAN," + KEY_WEDNESDAY_OPEN_TIME + " INTEGER," + KEY_WEDNESDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_THURSDAY + " BOOLEAN," + KEY_THURSDAY_OPEN_TIME + " INTEGER," + KEY_THURSDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_FRIDAY + " BOOLEAN," + KEY_FRIDAY_OPEN_TIME + " INTEGER," + KEY_FRIDAY_CLOSE_TIME + " INTEGER,"
                + KEY_IS_OPEN_SATURDAY + " BOOLEAN," + KEY_SATURDAY_OPEN_TIME + " INTEGER," + KEY_SATURDAY_CLOSE_TIME + " INTEGER" +
        ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public DateTime getLatestUpdateTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT updated_at FROM clinics" +
                "   ORDER BY updated_at DESC" +
                "   LIMIT 1;",null);
        cursor.moveToFirst();
        DateTime latest = new DateTime(cursor.getLong(0));
        cursor.close();
        return latest;
    }

    public boolean clinicExists(String clinicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE _id = ?",
                new String[] { clinicId });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void addClinic(Clinic clinic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MDME_ID, clinic.getId());
        values.put(KEY_ID, clinic.getId());
        values.put(KEY_NAME, clinic.getName());
        values.put(KEY_ADDRESS, clinic.getAddress());
        values.put(KEY_CITY, clinic.getCity());
        values.put(KEY_STATE, clinic.getState());
        values.put(KEY_ZIPCODE, clinic.getZipcode());
        values.put(KEY_COUNTRY, clinic.getCountry());
        values.put(KEY_PH_NO, clinic.getPhoneNumber());
        values.put(KEY_FAX_NO, clinic.getFaxNumber());
        values.put(KEY_NE_LATITUDE, clinic.getNeLatitude());
        values.put(KEY_SW_LATITUDE, clinic.getSwLatitude());
        values.put(KEY_LATITUDE, clinic.getLatitude());
        values.put(KEY_SW_LONGITUDE, clinic.getSwLongitude());
        values.put(KEY_NE_LONGITUDE, clinic.getNeLongitude());
        values.put(KEY_LONGITUDE, clinic.getLatitude());
        values.put(KEY_UPDATED_AT, clinic.getUpdatedAt().toDateTime().getMillis());
        values.put(KEY_IS_OPEN_SUNDAY, clinic.isOpenSunday());
        if (clinic.isOpenSunday()) {
            values.put(KEY_SUNDAY_OPEN_TIME, clinic.getSundayOpenTime().getMillisOfDay());
            values.put(KEY_SUNDAY_CLOSE_TIME, clinic.getSundayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_MONDAY, clinic.isOpenMonday());
        if (clinic.isOpenMonday()) {
            values.put(KEY_MONDAY_OPEN_TIME, clinic.getMondayOpenTime().getMillisOfDay());
            values.put(KEY_MONDAY_CLOSE_TIME, clinic.getMondayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_TUESDAY, clinic.isOpenTuesday());
        if (clinic.isOpenTuesday()) {
            values.put(KEY_TUESDAY_OPEN_TIME, clinic.getTuesdayOpenTime().getMillisOfDay());
            values.put(KEY_TUESDAY_CLOSE_TIME, clinic.getTuesdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_WEDNESDAY, clinic.isOpenWednesday());
        if (clinic.isOpenWednesday()) {
            values.put(KEY_WEDNESDAY_OPEN_TIME, clinic.getWednesdayOpenTime().getMillisOfDay());
            values.put(KEY_WEDNESDAY_CLOSE_TIME, clinic.getWednesdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_THURSDAY, clinic.isOpenThursday());
        if (clinic.isOpenThursday()) {
            values.put(KEY_THURSDAY_OPEN_TIME, clinic.getThursdayOpenTime().getMillisOfDay());
            values.put(KEY_THURSDAY_CLOSE_TIME, clinic.getThursdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_FRIDAY, clinic.isOpenFriday());
        if (clinic.isOpenFriday()) {
            values.put(KEY_FRIDAY_OPEN_TIME, clinic.getFridayOpenTime().getMillisOfDay());
            values.put(KEY_FRIDAY_CLOSE_TIME, clinic.getFridayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_SATURDAY, clinic.isOpenSaturday());
        if (clinic.isOpenSaturday()) {
            values.put(KEY_SATURDAY_OPEN_TIME, clinic.getSaturdayOpenTime().getMillisOfDay());
            values.put(KEY_SATURDAY_CLOSE_TIME, clinic.getSaturdayCloseTime().getMillisOfDay());
        }
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void updateClinic(Clinic clinic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MDME_ID, clinic.getId());
        values.put(KEY_ID, clinic.getId());
        values.put(KEY_NAME, clinic.getName());
        values.put(KEY_ADDRESS, clinic.getAddress());
        values.put(KEY_CITY, clinic.getCity());
        values.put(KEY_STATE, clinic.getState());
        values.put(KEY_ZIPCODE, clinic.getZipcode());
        values.put(KEY_COUNTRY, clinic.getCountry());
        values.put(KEY_PH_NO, clinic.getPhoneNumber());
        values.put(KEY_FAX_NO, clinic.getFaxNumber());
        values.put(KEY_NE_LATITUDE, clinic.getNeLatitude());
        values.put(KEY_SW_LATITUDE, clinic.getSwLatitude());
        values.put(KEY_LATITUDE, clinic.getLatitude());
        values.put(KEY_SW_LONGITUDE, clinic.getSwLongitude());
        values.put(KEY_NE_LONGITUDE, clinic.getNeLongitude());
        values.put(KEY_LONGITUDE, clinic.getLatitude());
        values.put(KEY_UPDATED_AT, clinic.getUpdatedAt().toDateTime().getMillis());
        values.put(KEY_IS_OPEN_SUNDAY, clinic.isOpenSunday());
        if (clinic.isOpenSunday()) {
            values.put(KEY_SUNDAY_OPEN_TIME, clinic.getSundayOpenTime().getMillisOfDay());
            values.put(KEY_SUNDAY_CLOSE_TIME, clinic.getSundayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_MONDAY, clinic.isOpenMonday());
        if (clinic.isOpenMonday()) {
            values.put(KEY_MONDAY_OPEN_TIME, clinic.getMondayOpenTime().getMillisOfDay());
            values.put(KEY_MONDAY_CLOSE_TIME, clinic.getMondayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_TUESDAY, clinic.isOpenTuesday());
        if (clinic.isOpenTuesday()) {
            values.put(KEY_TUESDAY_OPEN_TIME, clinic.getTuesdayOpenTime().getMillisOfDay());
            values.put(KEY_TUESDAY_CLOSE_TIME, clinic.getTuesdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_WEDNESDAY, clinic.isOpenWednesday());
        if (clinic.isOpenWednesday()) {
            values.put(KEY_WEDNESDAY_OPEN_TIME, clinic.getWednesdayOpenTime().getMillisOfDay());
            values.put(KEY_WEDNESDAY_CLOSE_TIME, clinic.getWednesdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_THURSDAY, clinic.isOpenThursday());
        if (clinic.isOpenThursday()) {
            values.put(KEY_THURSDAY_OPEN_TIME, clinic.getThursdayOpenTime().getMillisOfDay());
            values.put(KEY_THURSDAY_CLOSE_TIME, clinic.getThursdayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_FRIDAY, clinic.isOpenFriday());
        if (clinic.isOpenFriday()) {
            values.put(KEY_FRIDAY_OPEN_TIME, clinic.getFridayOpenTime().getMillisOfDay());
            values.put(KEY_FRIDAY_CLOSE_TIME, clinic.getFridayCloseTime().getMillisOfDay());
        }
        values.put(KEY_IS_OPEN_SATURDAY, clinic.isOpenSaturday());
        if (clinic.isOpenSaturday()) {
            values.put(KEY_SATURDAY_OPEN_TIME, clinic.getSaturdayOpenTime().getMillisOfDay());
            values.put(KEY_SATURDAY_CLOSE_TIME, clinic.getSaturdayCloseTime().getMillisOfDay());
        }

        db.update(TABLE_NAME, values, "_id="+clinic.getId(), null);
    }

//    searches on MDME id rather than primary key
    public Clinic getClinic(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { }, KEY_MDME_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        //ignore db id
        Clinic clinic = new Clinic();
        clinic.setId(Integer.parseInt(cursor.getString(1)));
        clinic.setName(cursor.getString(2));
        clinic.setAddress(cursor.getString(3));
        clinic.setCity(cursor.getString(4));
        clinic.setState(cursor.getString(5));
        clinic.setCountry(cursor.getString(6));
        clinic.setZipcode(cursor.getString(7));
        clinic.setPhoneNumber(cursor.getString(8));
        clinic.setFaxNumber(cursor.getString(9));
        clinic.setLatitude(cursor.getDouble(10));
        clinic.setNeLatitude(cursor.getDouble(11));
        clinic.setSwLatitude(cursor.getDouble(12));
        clinic.setLongitude(cursor.getDouble(13));
        clinic.setNeLongitude(cursor.getDouble(14));
        clinic.setSwLongitude(cursor.getDouble(15));
        clinic.setUpdatedAt(new DateTime(cursor.getLong(16)));
        clinic.setOpenSunday((cursor.getInt(17) > 0));
        clinic.setSundayOpenTime(new LocalTime(cursor.getInt(18)));
        clinic.setSundayCloseTime(new LocalTime(cursor.getInt(19)));
        clinic.setOpenMonday((cursor.getInt(20) > 0));
        clinic.setMondayOpenTime(new LocalTime(cursor.getInt(21)));
        clinic.setMondayCloseTime(new LocalTime(cursor.getInt(22)));
        clinic.setOpenTuesday((cursor.getInt(23) > 0));
        clinic.setTuesdayOpenTime(new LocalTime(cursor.getInt(24)));
        clinic.setTuesdayCloseTime(new LocalTime(cursor.getInt(25)));
        clinic.setOpenWednesday((cursor.getInt(26) > 0));
        clinic.setWednesdayOpenTime(new LocalTime(cursor.getInt(27)));
        clinic.setWednesdayCloseTime(new LocalTime(cursor.getInt(28)));
        clinic.setOpenThursday((cursor.getInt(29) > 0));
        clinic.setThursdayOpenTime(new LocalTime(cursor.getInt(30)));
        clinic.setThursdayCloseTime(new LocalTime(cursor.getInt(31)));
        clinic.setOpenFriday((cursor.getInt(32) > 0));
        clinic.setFridayOpenTime(new LocalTime(cursor.getInt(33)));
        clinic.setFridayCloseTime(new LocalTime(cursor.getInt(34)));
        clinic.setOpenSaturday((cursor.getInt(35) > 0));
        clinic.setSaturdayOpenTime(new LocalTime(cursor.getInt(36)));
        clinic.setSaturdayCloseTime(new LocalTime(cursor.getInt(37)));

        // return clinic
        cursor.close();
        return clinic;
    }

    public List<Clinic> getAllClinics() {
        List<Clinic> clinics = new ArrayList<Clinic>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id" }, null,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Clinic clinic = getClinic(cursor.getInt(0));
                clinics.add(clinic);
                cursor.moveToNext();
            }
        }
        return clinics;


    };
}
