package com.db.app.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


public class SqliteService extends SQLiteOpenHelper {

    public SqliteService(Context context) {
        super(context, "HrbustAPP", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE data (" +
                "time integer NOT NULL," +
                "wave TEXT," +
                "accel TEXT," +
                "PRIMARY KEY (time))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String wave, String accel) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("time", new Date().getTime());
        cv.put("wave", wave);
        cv.put("accel", accel);
        dbWrite.insert("data", null, cv);
        dbWrite.close();
    }

    public void readWave() {

    }

    public void readAccel() {

    }

    public void readAll() {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor cursor = dbRead.query("user", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(cursor.getColumnIndex("name")));
            System.out.println(cursor.getString(cursor.getColumnIndex("sex")));
        }
    }


}
