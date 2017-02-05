package com.thuy.android.popularmovie1;

/**
 * Created by tranv on 04-Feb-17.
 */

public class Movie {

    private String mvTitle;
    private String mvPoster;
    private String mvPlot;
    private String mvRating;
    private String mvReleaseDate;

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
        return this.getMvTitle() + "\n" + this.getMvPoster() + "\n" + this.getMvPlot() + "\n" + this.getMvRating() + "\n" + this.getMvReleaseDate();
    }

    public Movie() {}

    public Movie(String fromString) {
        String[] arrayVals = fromString.split("\n");
        mvTitle = arrayVals[0];
        mvPoster = arrayVals[1];
        mvPlot = arrayVals[2];
        mvRating = arrayVals[3];
        mvReleaseDate = arrayVals[4];
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
}
