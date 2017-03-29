package com.thuy.android.popularmovie1;


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tranv on 04-Feb-17.
 */

public class NetworkUtils {

    final static String API_KEY = "";

    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";

    final public static String TOP_RATED = "top_rated";

    final public static String POPULAR = "popular";

    final static String PARAM_KEY = "api_key";

    final static String PARAM_PAGE = "page";

    public final static String OPTION_TRAILERS = "/videos";

    public final static String OPTION_REVIEWS = "/reviews";

    final static String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    static int pageCounter = 1;

    static String currentOptions = "";

    public static URL buildReqListMvURL(String option) {

//        if (currentOptions != option) {
//            currentOptions = option;
//            pageCounter = 1;
//        } else
//            pageCounter++;

        pageCounter = 1;

        Uri uri = Uri.parse(BASE_URL + option).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(pageCounter))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReqMvDetailsURL(String movieID, String detail) {

        Uri uri = Uri.parse(BASE_URL + movieID + detail).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inStream = connection.getInputStream();

            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput)
                return scanner.next();
            else
                return null;
        } finally {
            connection.disconnect();
        }
    }

    public static String getYouTubeURL(String videoCode) {
        return BASE_YOUTUBE_URL + videoCode;
    }

}
