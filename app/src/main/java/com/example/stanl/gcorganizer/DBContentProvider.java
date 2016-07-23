package com.example.stanl.gcorganizer;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DBContentProvider extends ContentProvider {
    private DBHandler dbhandler;
    private static final UriMatcher sUriMatcher;
    public static final String AUTHORITY = "com.example.stanl.gcorganizer";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final int CARDS = 1;
    private static final int CARDS_ID = 2;
    private static final int CARDS_RAWQUERY = 3;

    public DBContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CARDS_ID:
                dbhandler.delete(ContentUris.parseId(uri));
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        dbhandler.insert(values);
        // TODO: change
        return null;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbhandler = new DBHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        switch (sUriMatcher.match(uri)) {
            case CARDS:
                return dbhandler.getAllCursor();
            // since I don;t want to implement a parcilable cursor class
            case CARDS_RAWQUERY:
                return dbhandler.rawQuery(selection, selectionArgs);
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        switch (sUriMatcher.match(uri)) {
            case CARDS_ID:
                dbhandler.update(ContentUris.parseId(uri), values);
        }
        return 0;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DBHandler.TABLE_CARDS, CARDS);
        sUriMatcher.addURI(AUTHORITY, DBHandler.TABLE_CARDS + "/#", CARDS_ID);
        sUriMatcher.addURI(AUTHORITY, DBHandler.TABLE_CARDS + "/" + DBHandler.RAW_QUERY , CARDS_RAWQUERY);
    }
}
