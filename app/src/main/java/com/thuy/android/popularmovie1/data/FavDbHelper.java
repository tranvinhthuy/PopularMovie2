package com.thuy.android.popularmovie1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thuy.android.popularmovie1.data.FavContract.*;

/**
 * Created by tranv on 29-Mar-17.
 */

public class FavDbHelper extends SQLiteOpenHelper{

    // The name of the database
    private static final String DATABASE_NAME = "favesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    FavDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + FavContract.FavEntry.TABLE_NAME + " (" +
                FavEntry.COLUMN_ID                + " TEXT PRIMARY KEY, " +
                FavEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FavEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavEntry.COLUMN_DATE   + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavEntry.TABLE_NAME);
        onCreate(db);
    }

}
