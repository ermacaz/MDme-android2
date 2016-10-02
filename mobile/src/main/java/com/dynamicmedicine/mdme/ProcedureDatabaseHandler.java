package com.dynamicmedicine.mdme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/27/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class ProcedureDatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mdme_procedures";

    // Contacts table name
    private static final String TABLE_NAME = "procedures";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_CLINIC_ID = "clinic_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC  = "description";
    private static final String KEY_DURATION  = "duration";

    public ProcedureDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROCEDURES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CLINIC_ID + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_DURATION + " INTEGER" +
                ")";
        db.execSQL(CREATE_PROCEDURES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean procedureExists(int procedureId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE _id = ?",
                new String[] { String.valueOf(procedureId) });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public DateTime getLatestUpdateTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT updated_at FROM procedures" +
                "   ORDER BY updated_at DESC" +
                "   LIMIT 1;", null);
        DateTime latest = null;
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                latest = new DateTime(cursor.getLong(0));
            }
            cursor.close();
        }
        return latest;
    }

    public void addProcedure(Procedure procedure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, procedure.getId());
        values.put(KEY_CLINIC_ID, procedure.getClinicId());
        values.put(KEY_NAME, procedure.getName());
        values.put(KEY_DESC, procedure.getDescription());
        values.put(KEY_DURATION, procedure.getDuration());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public void updateProcedure(Procedure procedure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, procedure.getId());
        values.put(KEY_CLINIC_ID, procedure.getClinicId());
        values.put(KEY_NAME, procedure.getName());
        values.put(KEY_DESC, procedure.getDescription());
        values.put(KEY_DURATION, procedure.getDuration());
        db.update(TABLE_NAME, values, "_id="+procedure.getId(), null);
    }

    //    searches on MDME id rather than primary key
    public Procedure getProcedure(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        //ignore db id
        Procedure procedure = new Procedure();
        procedure.setId(Integer.parseInt(cursor.getString(0)));
        procedure.setClinicId((Integer.parseInt(cursor.getString(1))));
        procedure.setName(cursor.getString(2));
        procedure.setDescription(cursor.getString(3));
        procedure.setDuration(Integer.parseInt(cursor.getString(4)));

        // return clinic
        cursor.close();
        return procedure;
    }

    public List<Procedure> getAllProceduresForClinic(int clinicId) {
        List<Procedure> procedures = new ArrayList<Procedure>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id" }, KEY_CLINIC_ID + "=?",
                new String[] { String.valueOf(clinicId) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Procedure procedure = getProcedure(cursor.getInt(0));
                procedures.add(procedure);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return procedures;


    };
}
