package com.db.app.service;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.db.app.MyApplication;


public class SqliteService extends SQLiteOpenHelper {
    private final static String DATABASE = "HrbustAPP";
    private final static String TABLE = "data";


    public SqliteService(Context context) {
        super(context, DATABASE, null, 1);
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

    // 插入数据
    public void insert(MyApplication myApplication) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("time", myApplication.getCurrInfo().getTime());
        cv.put("wave", myApplication.getCurrInfo().getWave());
        cv.put("accel", myApplication.getCurrInfo().getAccel());
        dbWrite.insert(TABLE, null, cv);
        dbWrite.close();
    }

    // 清空表
    public void clear() {
        this.getWritableDatabase().execSQL("DELETE FROM " + TABLE);
    }

    public void readWave() {

    }
}
