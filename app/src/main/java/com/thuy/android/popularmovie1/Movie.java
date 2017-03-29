package com.thuy.android.popularmovie1;

/**
 * Created by tranv on 04-Feb-17.
 */

public class Movie {

    private String mvID;
    private String mvTitle;
    private String mvPoster;
    private String mvPlot;
    private String mvRating;
    private String mvReleaseDate;
    private boolean isFavorited = false;

    public static final String KEY_IS_FAVED = "isFaved";
    public static final String KEY_MV_STRING= "mvString";

    public void setMvTitle(String mvTitle) {
        this.mvTitle = mvTitle;
    }

    public void setMvPoster(String mvPoster) {
        this.mvPoster = mvPoster;
    }

    public void setMvPlot(String mvPlot) {
        this.mvPlot = mvPlot;
    }

    public void setMvRating(String mvRating) {
        this.mvRating = mvRating;
    }

    public void setMvReleaseDate(String mvReleaseDate) {
        this.mvReleaseDate = mvReleaseDate;
    }

    @Override
    public String toString() {
        return this.getMvID() + "\n" + this.getMvTitle() + "\n" + this.getMvPoster()
                + "\n" + this.getMvPlot() + "\n" + this.getMvRating() + "\n" + this.getMvReleaseDate();
    }

    public Movie() {
    }

    public Movie(String fromString) {
        String[] arrayVals = fromString.split("\n");
        mvID = arrayVals[0];
        mvTitle = arrayVals[1];
        mvPoster = arrayVals[2];
        mvPlot = arrayVals[3];
        mvRating = arrayVals[4];
        mvReleaseDate = arrayVals[5];
    }

    public String getMvTitle() {
        return mvTitle;
    }

    public String getMvPoster() {
        return mvPoster;
    }

    public String getMvPlot() {
        return mvPlot;
    }

    public String getMvRating() {
        return mvRating;
    }

    public String getMvReleaseDate() {
        return mvReleaseDate;
    }

    public String getMvID() {
        return mvID;
    }

    public void setMvID(String mvID) {
        this.mvID = mvID;
    }
}
