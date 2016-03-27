package com.pt2;

/**
 * Created by DeniCahya on 2/21/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_KONTAK = "kontak";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME + " TEXT," +
                KEY_LAST_NAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_KONTAK + " TEXT," + KEY_ALAMAT + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_STATUS + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String id, String first_name, String last_name,String username,
                        String kontak,String alamat, String email, String status, String created_at) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_FIRST_NAME, first_name);
        values.put(KEY_LAST_NAME, last_name);
        values.put(KEY_USERNAME, username);
        values.put(KEY_KONTAK, kontak);
        values.put(KEY_EMAIL, email);
        values.put(KEY_ALAMAT, alamat);
        values.put(KEY_EMAIL, email);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(KEY_ID, cursor.getString(0));
            user.put(KEY_FIRST_NAME, cursor.getString(1));
            user.put(KEY_LAST_NAME, cursor.getString(2));
            user.put(KEY_USERNAME, cursor.getString(3));
            user.put(KEY_KONTAK, cursor.getString(4));
            user.put(KEY_ALAMAT, cursor.getString(5));
            user.put(KEY_EMAIL, cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
