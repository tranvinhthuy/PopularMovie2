package com.thuy.android.popularmovie1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tranv on 04-Feb-17.
 */

public class MovieUtils {

    final static String OWN_RESULTS = "results";

    final static String OWN_TITLE = "original_title";
    final static String OWN_POSTER = "poster_path";
    final static String OWN_PLOT = "overview";
    final static String OWN_RATING = "vote_average";
    final static String OWN_RELEASE_DATE = "release_date";

    public static ArrayList<Movie> getMovieListFromJSON(String jsonString) throws JSONException {
        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject resultObj = new JSONObject(jsonString);

        JSONArray listMvJSON = resultObj.getJSONArray(OWN_RESULTS);

        for(int i = 0; i<listMvJSON.length(); i++) {
            JSONObject jsonMovie = listMvJSON.getJSONObject(i);
            Movie movie = new Movie();
            movie.setMvTitle(jsonMovie.getString(OWN_TITLE));
            movie.setMvPoster(jsonMovie.getString(OWN_POSTER));
            movie.setMvPlot(jsonMovie.getString(OWN_PLOT));
            movie.setMvRating(jsonMovie.getString(OWN_RATING));
            movie.setMvReleaseDate(jsonMovie.getString(OWN_RELEASE_DATE));
            movieList.add(movie);
        }

        return movieList;
    }
}
