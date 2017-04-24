package com.timezone.organizer;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper{

    public Database(Context context){
        super(context, "Events", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d("DB","onCreate method called");
        db.execSQL("create table Events ("
            + "id integer primary key autoincrement,"
            + "name text not null,"
            + "description text,"
            + "date text not null" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void cleanDb(SQLiteDatabase db){
        db.delete("Events", null, null);
    }
}
