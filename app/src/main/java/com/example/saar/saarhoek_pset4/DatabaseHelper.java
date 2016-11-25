package com.example.saar.saarhoek_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Saar on 25-11-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // set database scheme
    static final String DATABASE_NAME = "TODO.db";
    static final int DATABASE_VERSION = 2;
    public static final String TABLE = "todo";
    public static final String _ID = "_id";

    public static final String TASK = "task";
    public static final String CHECK = "image";

    // constructor
    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // oncreate, onupgrade
    // not to call upon
    public void onCreate (SQLiteDatabase sqLiteDatabase){
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHECK +
                 " integer not null, " + TASK + " text not null);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    // CRUD methods
    public void create(int imageID, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK, task);
        values.put(CHECK, imageID);
        db.insert(TABLE, null, values);
    }


    public Cursor fetch() {
        // open database
        SQLiteDatabase db = this.getWritableDatabase();

        // get a cursor reading out the database
        String[] columns = new String[] {_ID, CHECK, TASK};
        Cursor cursor = db.query(TABLE, columns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        // close database
        db.close();
        return cursor;
    }

    // get selective row by id
    public Cursor location(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[] {_ID, CHECK, TASK};
        String where = _ID + " = " + _id;
        Cursor c = 	db.query(true, TABLE, columns,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean update(long _id, int imageID, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = _ID + " = " + _id;

        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(CHECK, imageID);
        newValues.put(TASK, task);

        // Insert it into the database.
        return db.update(TABLE, newValues, where, null) != 0;
    }

    //delete
    public void delete(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, _ID + " = " + _id, null);
        db.close();
    }
}
