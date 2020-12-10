package com.example.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EventDatabaseSingleton {
    private static EventDatabaseSingleton mInstance = null;
    private SQLiteDatabase eventDBS;
    private EventDatabaseHelper cdb;

    private EventDatabaseSingleton(Context ctx){
        cdb = new EventDatabaseHelper(ctx);
        eventDBS = cdb.getWritableDatabase();
    }

    public static EventDatabaseSingleton getInstance(Context ctx){
        if(mInstance == null)
        {
            mInstance = new EventDatabaseSingleton(ctx);
        }
        return mInstance;
    }
    public static EventDatabaseSingleton getInstance(){
        return mInstance;
    }

    public SQLiteDatabase getEventDBS(){
        return this.eventDBS;
    }

    public EventDatabaseHelper getEventDataBaseHelper(){
        return cdb;
    }

    public void setEventDBS(SQLiteDatabase db){
        eventDBS = db;
    }
}
