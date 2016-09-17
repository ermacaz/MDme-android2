package com.dynamicmedicine.mdme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dynamicmedicine.mdme.Clinic;

import org.joda.time.DateTime;

import java.util.Date;

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
    private static final int DATABASE_VERSION = 4;

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
                + KEY_FAX_NO + " TEXT," + KEY_LATITUDE + " TEXT,"
                + KEY_NE_LATITUDE + " FLOAT," + KEY_SW_LATITUDE + " FLOAT,"
                + KEY_LONGITUDE + " FLOAT," + KEY_NE_LONGITUDE + " FLOAT,"
                + KEY_SW_LONGITUDE + " FLOAT," + KEY_UPDATED_AT + " INTEGER" +
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
        values.put(KEY_UPDATED_AT, clinic.getUpdated_at().toDateTime().getMillis());
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
        values.put(KEY_UPDATED_AT, clinic.getUpdated_at().toDateTime().getMillis());

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
        Clinic clinic = new Clinic(Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getString(6),cursor.getString(7), cursor.getString(8),cursor.getString(9),
                cursor.getDouble(10), cursor.getDouble(11), cursor.getDouble(12), cursor.getDouble(13),
                cursor.getDouble(14), cursor.getDouble(15), new DateTime(cursor.getLong(16)));
        // return clinic
        cursor.close();
        return clinic;
    }
}
