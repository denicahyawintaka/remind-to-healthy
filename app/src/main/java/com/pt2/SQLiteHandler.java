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

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 16;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_OBAT = "obat";
    private static final String TABLE_KONSULTASI = "konsultasi";

    private static final String KEY_ID = "id";

    // Login Table Columns names

    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_KONTAK = "kontak";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CREATED_AT = "created_at";

      // Obat Table Columns names
    private static final String KEY_NAMA_OBAT = "nama_obat";
    private static final String KEY_ID_KONSULTASI = "id_konsultasi";
    private static final String KEY_FREKUENSI = "frekuensi";
    private static final String KEY_interval = "interval";
    private static final String KEY_Dosis = "dosis";
    private static final String KEY_Penggunaan = "penggunaan";

    // Konsultasi Table Columns names
    //private static final String KEY_IDKonsultasi = "idKonsultasi";
    private static final String KEY_NamaPengirim = "first_name";

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

        String CREATE_Obat= "CREATE TABLE " + TABLE_OBAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA_OBAT + " TEXT," +
                KEY_ID_KONSULTASI + " TEXT," + KEY_FREKUENSI + " TEXT," + KEY_interval + " TEXT," +  KEY_Dosis + " TEXT," + KEY_Penggunaan + " TEXT" + ")";

        String Create_konsultasi= "CREATE TABLE " + TABLE_KONSULTASI + "("
                + KEY_ID_KONSULTASI + " TEXT," + KEY_NamaPengirim + " TEXT"+ ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_Obat);
        db.execSQL(Create_konsultasi);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONSULTASI);

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
        values.put(KEY_ALAMAT, alamat);
        values.put(KEY_EMAIL, email);
        values.put(KEY_STATUS, status);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addObat(String idObat, String idKonsultasi, String namaObat,String frekuensi,
                        String interval,String dosis,String penggunaan) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, idObat);
        values.put(KEY_ID_KONSULTASI, idKonsultasi);
        values.put(KEY_NAMA_OBAT, namaObat);
        values.put(KEY_FREKUENSI, frekuensi);
        values.put(KEY_interval, interval);
        values.put(KEY_Dosis, dosis);
        values.put(KEY_Penggunaan, penggunaan);

        // Inserting Row
        db.insert(TABLE_OBAT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Obat inserted into sqlite: " + idObat);
    }

    public void addKonsultasi(String idKonsultasi, String namaPengirim) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_KONSULTASI, idKonsultasi);
        values.put(KEY_NamaPengirim, namaPengirim);


        // Inserting Row
        db.insert(TABLE_KONSULTASI, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Obat inserted into sqlite: " + idKonsultasi);
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
            user.put(KEY_STATUS, cursor.getString(7));
            user.put(KEY_CREATED_AT, cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getObatDetail() {
        HashMap<String, String> obat = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_OBAT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            obat.put(KEY_ID, cursor.getString(0));
            obat.put(KEY_NAMA_OBAT, cursor.getString(1));
            obat.put(KEY_ID_KONSULTASI, cursor.getString(2));
            obat.put(KEY_FREKUENSI, cursor.getString(3));
            obat.put(KEY_interval, cursor.getString(4));
            obat.put(KEY_Dosis, cursor.getString(5));
            obat.put(KEY_Penggunaan, cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + obat.toString());

        return obat;
    }

    public ArrayList<HashMap> getObatArray() {
        HashMap<String, String> obat = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_OBAT;
        //HashMap<String, String> obat = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<HashMap> listObat = new ArrayList<HashMap>();
        // Move to first row
        cursor.moveToFirst();
        int i=0;
        while(cursor.getCount() > i) {
            obat.put(KEY_NAMA_OBAT, cursor.getString(1));
            obat.put(KEY_Dosis, cursor.getString(5));
            obat.put(KEY_Penggunaan, cursor.getString(6));
           listObat.add(obat);
            i++;
        }
        cursor.close();
        db.close();
        // return user
        return listObat;
    }

    public HashMap<String, String> getKonsultasi() {
        HashMap<String, String> konsultasi = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_KONSULTASI;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            konsultasi.put(KEY_ID_KONSULTASI, cursor.getString(0));
            konsultasi.put(KEY_NamaPengirim, cursor.getString(1));

        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + konsultasi.toString());

        return konsultasi;
    }

    public void updateFrekuensi() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "update obat set frekuensi = frekuensi-1";
        db.execSQL(selectQuery);
        Log.d(TAG, "OBAT BERHASIL DIUPDATE");
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
    public void deleteKonsultasidanObat() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_KONSULTASI, null, null);
        db.delete(TABLE_OBAT, null, null);
        db.close();
        Log.d(TAG, "Deleted all Konsultasi info from sqlite");
    }


}
