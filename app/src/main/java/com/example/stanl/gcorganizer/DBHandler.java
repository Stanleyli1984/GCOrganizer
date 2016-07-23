package com.example.stanl.gcorganizer;

/**
 * Created by stanl on 7/15/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

//import android.database.sqlite.SQLiteDatabase;

public class DBHandler extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final String PWD = "Password";
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "FeedReader1.db";
    // Contacts table name
    public static final String TABLE_CARDS = "shops";
    // Shops Table Columns names
    public static final String KEY_ID = "_id";
    public static final String CARD_NUMBER = "card_number";
    public static final String STORE_NAME = "store_name";
    public static final String STORE_ID = "store_id";
    public static final String CARD_EXP_MONTH = "exp_month";
    public static final String CARD_EXP_YEAR = "exp_year";
    public static final String CARD_NOTE = "note";
    public static final String CARD_THUMB = "thumb";
    public static final String CARD_PIC1 = "pic1";
    public static final String CARD_PIC2 = "pic2";
    public static final String RAW_QUERY = "raw_query";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_CARDS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                CARD_NUMBER + " TEXT," +
                STORE_NAME + " TEXT," +
                STORE_ID + " INTEGER" +
                CARD_EXP_MONTH + " INTEGER" +
                CARD_EXP_YEAR + " INTEGER" +
                CARD_NOTE + " TEXT," +
                CARD_THUMB + " BLOB," +
                CARD_PIC1 + " TEXT," +
                CARD_PIC2 + " TEXT" + ")";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        // Creating tables again
        onCreate(db);
    }

    public void insert(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase(PWD);
        db.insert(TABLE_CARDS, null, values);
        db.close(); // Closing database connection
    }

    public void update(long _id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase(PWD);
        db.update(TABLE_CARDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(_id)});
        db.close(); // Closing database connection
    }

    public void delete(long _id) {
        SQLiteDatabase db = this.getWritableDatabase(PWD);
        db.delete(TABLE_CARDS, KEY_ID + " = ?",
                new String[]{String.valueOf(_id)});
        db.close();
    }

    public Cursor query_all_filed(String[] projection, String selection,
                                  String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.getReadableDatabase(PWD);
        Cursor c = db.query(TABLE_CARDS, projection, selection, selectionArgs,
                null, null, sortOrder);
        return c;
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase(PWD);
        Cursor c = db.rawQuery(sql, selectionArgs);
        return c;
    }

    public List<Card> list() {
        List<Card> shopList = new ArrayList<Card>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getWritableDatabase(PWD);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Card card = new Card();
                card.store_name = cursor.getString(cursor.getColumnIndex(STORE_NAME));
                card.card_number = cursor.getString(cursor.getColumnIndex(CARD_NUMBER));
                // Adding contact to list
                shopList.add(card);
            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        cursor.close();
        return shopList;
    }

    public Cursor getAllCursor() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getReadableDatabase(PWD);
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

}
