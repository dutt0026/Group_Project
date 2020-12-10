package com.example.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
This class extend SQLiteOpenHelper, is used to create database and table.
 */
public class EventDatabaseHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "EventDetails1.db"; // DATABASE  name
    private static int VERSION_NUM = 2;
    public static final String KEY_ID = "_id";    // column ID
    public static final String KEY_EVENT = "Event"; // table column Event
    public static final String KEY_DATE = "Start_Date"; // table column Start Date
    public static final String KEY_MIN = "Min_Price";  // Table column Price Range
    public static final String KEY_MAX = "Max_Price";
    public static final String KEY_URL = "URL"; // Table column URL
    public static final String TABLE_NAME = "EventTable"; // TABLE NAME
    public static final String KEY_IMAGE ="PromotionalImage"; // Table column image data.

    // This string is used to create table.
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_IMAGE + " blob,"
            + KEY_EVENT  + " text,"
            + KEY_DATE + " text,"
            + KEY_MIN + " text,"
            + KEY_MAX + " text,"
            + KEY_URL + " text);";

    public EventDatabaseHelper(Context ctx){
        super(ctx,DATABASE_NAME,null,VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);//create table.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteItem(int key){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+KEY_ID
                +" = "+ key);
    }
    // Delete the whole table
    public void delete(){
        this.getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME);
    }
}
