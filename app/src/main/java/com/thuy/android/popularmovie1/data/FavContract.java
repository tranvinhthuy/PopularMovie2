package com.thuy.android.popularmovie1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tranv on 29-Mar-17.
 */

public class FavContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.thuy.android.popularmovie1";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAV = "favorite";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class FavEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();


        // Task table and column names
        public static final String TABLE_NAME = "favorites";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_ID = "movieID";
        public static final String COLUMN_TITLE = "mvTitle";
        public static final String COLUMN_POSTER = "mvPoster";
        public static final String COLUMN_PLOT = "mvPlot";
        public static final String COLUMN_RATING = "mvRating";
        public static final String COLUMN_DATE = "mvReleaseDate";

    }
}
