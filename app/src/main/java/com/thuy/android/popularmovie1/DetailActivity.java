package com.thuy.android.popularmovie1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.thuy.android.popularmovie1.data.FavContract;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.GridItemClickListener, CompoundButton.OnCheckedChangeListener{

    final String IMG_URL_BASE = "http://image.tmdb.org/t/p/w342/";

    private TrailerAdapter trAdapter;
    private ReviewAdapter rvAdapter;

    ArrayList<String> lstTrailers = new ArrayList<>();
    ArrayList<String> lstReviews = new ArrayList<>();
    ArrayList<String> lstReviewers = new ArrayList<>();

    ImageView imgvPoster;

    TextView txvTitle, txvReleaseDate, txvRating, txvPlot;

    RecyclerView rvListTrailers, rvListReviews;

    ToggleButton btnFave;

    private DetailActivity thisActivity;

    boolean firstLoad = true;

    Movie currentMv;

    boolean isFavedMv;

//    ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        imgvPoster = (ImageView) findViewById(R.id.imgv_poster);
        txvTitle = (TextView) findViewById(R.id.txv_title);
        txvReleaseDate = (TextView) findViewById(R.id.txv_release_date);
        txvRating = (TextView) findViewById(R.id.txv_rating);
        txvPlot = (TextView) findViewById(R.id.txv_plot);
        rvListTrailers = (RecyclerView) findViewById(R.id.rv_list_trailers);
        rvListReviews = (RecyclerView) findViewById(R.id.rv_list_reviews);
        btnFave = (ToggleButton) findViewById(R.id.btn_fave);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thisActivity = this;

        Intent intent = getIntent();
        String sentContent = intent.getStringExtra(Movie.KEY_MV_STRING);
        currentMv = new Movie(sentContent);
        isFavedMv = intent.getBooleanExtra(Movie.KEY_IS_FAVED, false);

        if(isFavedMv) {
            btnFave.setChecked(isFavedMv);
            btnFave.setOnCheckedChangeListener(this);
        }
        else {
            new CheckFaveTask().execute(currentMv.getMvID());
        }

        Picasso.with(this).load(IMG_URL_BASE + currentMv.getMvPoster()).placeholder(android.R.drawable.ic_menu_gallery).into(imgvPoster);
        txvTitle.append(currentMv.getMvTitle());
        txvReleaseDate.append(currentMv.getMvReleaseDate());
        txvRating.append(currentMv.getMvRating());
        txvPlot.append(currentMv.getMvPlot());

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        rvListTrailers.setLayoutManager(layoutManager);

        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rvListReviews.setLayoutManager(layoutManager2);

        LoadingDetailsTask loadingDetailsTask = new LoadingDetailsTask();
        loadingDetailsTask.execute(currentMv.getMvID());

    }

    @Override
    public void onGridItemClick(int clickItemIndex) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lstTrailers.get(clickItemIndex))));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isFaved) {
        if(isFaved) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(FavContract.FavEntry.COLUMN_ID, currentMv.getMvID());
            contentValues.put(FavContract.FavEntry.COLUMN_TITLE, currentMv.getMvTitle());
            contentValues.put(FavContract.FavEntry.COLUMN_POSTER, currentMv.getMvPoster());
            contentValues.put(FavContract.FavEntry.COLUMN_PLOT, currentMv.getMvPlot());
            contentValues.put(FavContract.FavEntry.COLUMN_RATING, currentMv.getMvRating());
            contentValues.put(FavContract.FavEntry.COLUMN_DATE, currentMv.getMvReleaseDate());

            Uri uri = getContentResolver().insert(FavContract.FavEntry.CONTENT_URI, contentValues);

            if(uri != null) {
                Toast.makeText(this, getString(R.string.notify_save_fav), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Uri deleteUri = FavContract.FavEntry.CONTENT_URI.buildUpon().appendPath(currentMv.getMvID()).build();
            int delNum = getContentResolver().delete(deleteUri, null, null);
            if(delNum==1)
                Toast.makeText(this, getString(R.string.notify_remove_fave), Toast.LENGTH_SHORT).show();
        }
    }

    public class LoadingDetailsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ConnectivityManager cm =
                    (ConnectivityManager) thisActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                this.cancel(true);
                Toast.makeText(thisActivity, getString(R.string.no_network_notification), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String trailerResult = null;
            String reviewResult = null;

            try {
                trailerResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildReqMvDetailsURL(params[0], NetworkUtils.OPTION_TRAILERS));
                reviewResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildReqMvDetailsURL(params[0], NetworkUtils.OPTION_REVIEWS));
            } catch (IOException e) {
                e.printStackTrace();
            }
            lstTrailers.clear();
            lstReviewers.clear();
            lstReviews.clear();
            try {
                lstTrailers.addAll(MovieUtils.getListTrailersFromJSON(trailerResult));
                ArrayList<ArrayList> reviewsInfo = MovieUtils.getListReviewsFromJSON(reviewResult);
                lstReviews.addAll(reviewsInfo.get(0));
                lstReviewers.addAll(reviewsInfo.get(1));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
//            String display = "";
//            for (int i = 0; i < lstMovies.size(); i++) {
//                display = display + lstMovies.get(i).toString() + "\n";
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (firstLoad) {
                trAdapter = new TrailerAdapter(lstTrailers, thisActivity);
                rvAdapter = new ReviewAdapter(lstReviews, lstReviewers);
                rvListTrailers.setAdapter(trAdapter);
                rvListReviews.setAdapter(rvAdapter);
                firstLoad = false;
            } else {
                trAdapter.notifyDataSetChanged();
                rvAdapter.notifyDataSetChanged();
            }
        }
    }

    public class CheckFaveTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String movieID = strings[0];
            Uri queryURI = FavContract.FavEntry.CONTENT_URI.buildUpon().appendPath(movieID).build();
            return getContentResolver().query(queryURI, null, null, null, null).getCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1)
            {
                btnFave.setChecked(true);
            }
            btnFave.setOnCheckedChangeListener(thisActivity);
        }
    }
}
