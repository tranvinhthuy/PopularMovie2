package com.thuy.android.popularmovie1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.thuy.android.popularmovie1.data.FavContract.FavEntry.TABLE_NAME;

/**
 * Created by tranv on 29-Mar-17.
 */

public class FavContentProvider extends ContentProvider{

    public static final int FAVES = 100;
    public static final int FAVE_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_FAV, FAVES);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_FAV + "/#", FAVE_WITH_ID);

        return uriMatcher;
    }

    private FavDbHelper mFavDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavDbHelper = new FavDbHelper(context);
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavContract.FavEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAVES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVE_WITH_ID:
                String movieID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieID};

                retCursor = mFavDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        FavContract.FavEntry.TABLE_NAME,
                        projection,
                        FavContract.FavEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int faveDeleted; // starts as 0

        switch (match) {
            case FAVE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                faveDeleted = db.delete(TABLE_NAME, "movieID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (faveDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return faveDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
