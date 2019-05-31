package com.example.mycontactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact2019.db";
    public static final String TABLE_NAME = "Contact2019_2";
    public static final String ID = "ID";
    public static final String COLUMN_NAME_CONTACT = "contact";
    public static final String COLUMN_NAME_BIRTHDAY = "birthday";
    public static final String COLUMN_NAME_FAVORITE_COLOR = "favorite_color";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_CONTACT + " TEXT," + COLUMN_NAME_BIRTHDAY + " TEXT," + COLUMN_NAME_FAVORITE_COLOR
            + " TEXT)";
    public static final String SQL_RESET_PRIMARY_KEY = "DELETE FROM SQLITE_SEQUENCE WHERE NAME=" + "'"+TABLE_NAME+"'";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CLEAR_TABLE = "DELETE FROM " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
       SQLiteDatabase db = this.getWritableDatabase(); //FOR TEST ONLY - WILL REMOVE LATER
        Log.d("MyContactApp","DatabaseHelper: constructed DatabaseHelper" );
       onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MyContactApp","DatabaseHelper: creating DatabaseHelper" );
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("MyContactApp","DatabaseHelper: upgrading DatabaseHelper" );
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertData(String name, String birthday, String favColor) {
        Log.d("MyContactApp", "DatabaseHelper: inserting data");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = this.getAllData();
        while(res.moveToNext())
        {
            if (res.getString(1).equals(name) && res.getString(2).equals(birthday) && res.getString(3).equals(favColor)) {
                Log.d("MyContactApp", "DatabaseHelper: Same contact attempted to be inserted twice");
                return false;
            }



        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CONTACT, name);
        contentValues.put(COLUMN_NAME_BIRTHDAY,birthday);
        contentValues.put(COLUMN_NAME_FAVORITE_COLOR, favColor);


        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
           Log.d("MyContactApp", "DatabaseHelper: Contact insert FAILED");
           return false;
        }
        else {
            Log.d("MyContactApp", "DatabaseHelper: Contact insert PASSED");
            return true;
        }

    }

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_CLEAR_TABLE);
        db.execSQL(SQL_RESET_PRIMARY_KEY);

    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}
