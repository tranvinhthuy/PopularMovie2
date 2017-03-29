package com.thuy.android.popularmovie1;

import com.thuy.android.popularmovie1.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tranv on 04-Feb-17.
 */

public class MovieUtils {

    final static String OWN_RESULTS = "results";

    final static String OWN_ID = "id";
    final static String OWN_TITLE = "original_title";
    final static String OWN_POSTER = "poster_path";
    final static String OWN_PLOT = "overview";
    final static String OWN_RATING = "vote_average";
    final static String OWN_RELEASE_DATE = "release_date";

    final static String VIDEO_CODE = "key";

    final static String OWN_REVIEWER = "author";
    final static String OWN_REVIEW = "content";


    public static ArrayList<Movie> getMovieListFromJSON(String jsonString) throws JSONException {
        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject resultObj = new JSONObject(jsonString);

        JSONArray listMvJSON = resultObj.getJSONArray(OWN_RESULTS);

        for(int i = 0; i<listMvJSON.length(); i++) {
            JSONObject jsonMovie = listMvJSON.getJSONObject(i);
            Movie movie = new Movie();
            movie.setMvID(jsonMovie.getString(OWN_ID));
            movie.setMvTitle(jsonMovie.getString(OWN_TITLE));
            movie.setMvPoster(jsonMovie.getString(OWN_POSTER));
            movie.setMvPlot(jsonMovie.getString(OWN_PLOT));
            movie.setMvRating(jsonMovie.getString(OWN_RATING));
            movie.setMvReleaseDate(jsonMovie.getString(OWN_RELEASE_DATE));
            movieList.add(movie);
        }

        return movieList;
    }

    public static ArrayList<String> getListTrailersFromJSON(String jsonString) throws JSONException {
        ArrayList<String> trailerList = new ArrayList<>();

        JSONObject resultObj = new JSONObject(jsonString);

        JSONArray listTrailerJSON = resultObj.getJSONArray(OWN_RESULTS);

        for(int i = 0; i<listTrailerJSON.length(); i++) {
            JSONObject jsonTrailers = listTrailerJSON.getJSONObject(i);
            Movie movie = new Movie();
            trailerList.add(NetworkUtils.getYouTubeURL(jsonTrailers.getString(VIDEO_CODE)));
        }

        return trailerList;
    }

    public static ArrayList<ArrayList> getListReviewsFromJSON(String jsonString) throws JSONException {
        ArrayList<String> reviewList = new ArrayList<>();
        ArrayList<String> reviewerList = new ArrayList<>();

        JSONObject resultObj = new JSONObject(jsonString);

        JSONArray listTrailerJSON = resultObj.getJSONArray(OWN_RESULTS);

        for(int i = 0; i<listTrailerJSON.length(); i++) {
            JSONObject jsonReview = listTrailerJSON.getJSONObject(i);
            reviewList.add(jsonReview.getString(OWN_REVIEW));
            reviewerList.add(jsonReview.getString(OWN_REVIEWER));
        }

        ArrayList<ArrayList> result = new ArrayList<>();
        result.add(reviewList);
        result.add(reviewerList);
        return result;
    }
}
