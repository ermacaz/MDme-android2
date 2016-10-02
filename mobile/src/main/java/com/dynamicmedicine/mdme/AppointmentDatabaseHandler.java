package com.dynamicmedicine.mdme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 10/1/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class AppointmentDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mdme_scheduling_appointments";
    private static final String TABLE_NAME = "appointments";

    //columns
    private static final String KEY_ID = "_id";
    private static final String KEY_CLINIC_ID = "clinic_id";
    private static final String KEY_DOCTOR_ID = "doctor_id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_DURATION = "DURATION";
    private static final String KEY_DESCRIPTION = "DESCRIPTION";
    private static final String KEY_STATUS = "STATUS";

    public AppointmentDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CLINIC_ID + " INTEGER," + KEY_DOCTOR_ID + " INTEGER,"
                + KEY_START_TIME + " INTEGER," + KEY_END_TIME + " INTEGER,"
                + KEY_DURATION + " INTEGER," + KEY_DESCRIPTION + " TEXT,"
                + KEY_STATUS + " TEXT" +
                ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    //should reset db before loading appointments for each date.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void resetDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean appointmentExists(int appointmentId) {
        String cId = String.valueOf(appointmentId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE _id = ?",
                new String[] { cId });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public void addAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, appointment.getId());
        values.put(KEY_CLINIC_ID, appointment.getClinicId());
        values.put(KEY_DOCTOR_ID, appointment.getDoctorId());
        values.put(KEY_START_TIME, appointment.getStartTime().getMillisOfDay());
        values.put(KEY_END_TIME, appointment.getEndTime().getMillisOfDay());
        values.put(KEY_DURATION, appointment.getDuration());
        values.put(KEY_DESCRIPTION, appointment.getDescription());
        values.put(KEY_STATUS, appointment.getStatus());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void updateAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, appointment.getId());
        values.put(KEY_CLINIC_ID, appointment.getClinicId());
        values.put(KEY_DOCTOR_ID, appointment.getDoctorId());
        values.put(KEY_START_TIME, appointment.getStartTime().getMillisOfDay());
        values.put(KEY_END_TIME, appointment.getEndTime().getMillisOfDay());
        values.put(KEY_DURATION, appointment.getDuration());
        values.put(KEY_DESCRIPTION, appointment.getDescription());
        values.put(KEY_STATUS, appointment.getStatus());
        // Inserting Row
        db.update(TABLE_NAME, values, "_id="+appointment.getId(), null);
        db.close(); // Closing database connection
    }

    public Appointment getAppointment(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Appointment appointment = new Appointment();
        if (!cursor.isNull(cursor.getColumnIndex(KEY_ID)))
            appointment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        if (!cursor.isNull((cursor.getColumnIndex(KEY_CLINIC_ID))))
            appointment.setClinicId(cursor.getInt(cursor.getColumnIndex(KEY_CLINIC_ID)));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_DOCTOR_ID)))
            appointment.setDoctorId(cursor.getInt(cursor.getColumnIndex(KEY_DOCTOR_ID)));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_START_TIME)))
            appointment.setStartTime(new DateTime(cursor.getLong(cursor.getColumnIndex(KEY_START_TIME))));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_END_TIME)))
            appointment.setEndTime(new DateTime(cursor.getLong(cursor.getColumnIndex(KEY_END_TIME))));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_DURATION)))
            appointment.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_DESCRIPTION)))
            appointment.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
        if (!cursor.isNull(cursor.getColumnIndex(KEY_STATUS)))
            appointment.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
        cursor.close();
        db.close();
        return appointment;
    }

}
