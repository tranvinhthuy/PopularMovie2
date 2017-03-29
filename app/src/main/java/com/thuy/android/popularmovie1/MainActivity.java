package com.thuy.android.popularmovie1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thuy.android.popularmovie1.data.FavContract;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.GridItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private ProgressBar pgbarLoadMovies;

    private MovieAdapter mvAdapter;
    private RecyclerView mvList;

    private MainActivity thisActivity;

    ArrayList<Movie> lstMovies = new ArrayList<>();

    private String currentSortOption = NetworkUtils.POPULAR;

    boolean firstLoad = true;

    boolean isShowingFaves = false;

    private static final int FAV_LOADER_ID = 0;

    Menu activityMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pgbarLoadMovies = (ProgressBar) findViewById(R.id.pgbar_load_movie);

        mvList = (RecyclerView) findViewById(R.id.rv_list_mv);

        // Check screen size to increase column number for tablet
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        GridLayoutManager layoutManager;
        if(dpHeight >= 600.0 || dpWidth >= 600.0)
            layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        else
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mvList.setLayoutManager(layoutManager);

        thisActivity = this;

        LoadingMoviesTask loadingMoviesTask = new LoadingMoviesTask();
        loadingMoviesTask.execute(currentSortOption);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        activityMenu = menu;
        return true;
    }

    void showSortOptionDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.sort_dlg_tittle));
        final String[] sortOptions = {getString(R.string.sort_popular), getString(R.string.sort_rating)};
        final String[] sortOptionsVal = {NetworkUtils.POPULAR, NetworkUtils.TOP_RATED};

        int checkedItem = 0;
        if (currentSortOption == sortOptionsVal[1])
            checkedItem = 1;

        dialog.setSingleChoiceItems(sortOptions, checkedItem, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (currentSortOption != sortOptionsVal[item]) {
                    currentSortOption = sortOptionsVal[item];
                    dialog.dismiss();
                    LoadingMoviesTask loadingMoviesTask = new LoadingMoviesTask();
                    loadingMoviesTask.execute(currentSortOption);
                }

            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sort) {
            showSortOptionDialog();
        } else if (item.getItemId() == R.id.menu_show_fave) {
            isShowingFaves = !item.isChecked();
            item.setChecked(isShowingFaves);
            activityMenu.getItem(0).setEnabled(!isShowingFaves);
            if (isShowingFaves) {
                getSupportLoaderManager().restartLoader(FAV_LOADER_ID, null, this);
            } else {
                LoadingMoviesTask loadingMoviesTask = new LoadingMoviesTask();
                loadingMoviesTask.execute(currentSortOption);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGridItemClick(int clickItemIndex) {
        Intent intent = new Intent(thisActivity, DetailActivity.class);
        intent.putExtra(Movie.KEY_MV_STRING, lstMovies.get(clickItemIndex).toString());
        intent.putExtra(Movie.KEY_IS_FAVED, isShowingFaves);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFaveData = null;

            @Override
            protected void onStartLoading() {
//                if (mFaveData != null) {
//                    deliverResult(mFaveData);
//                } else {
                if (!isShowingFaves) {
                    cancelLoad();
                    return;
                }
                forceLoad();
//                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavContract.FavEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mFaveData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        Toast.makeText(this, cursor.getCount()+"", Toast.LENGTH_SHORT).show();
        lstMovies.clear();
        int idIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_TITLE);
        int posterIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_POSTER);
        int plotIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_PLOT);
        int ratingIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_RATING);
        int dateIndex = cursor.getColumnIndex(FavContract.FavEntry.COLUMN_DATE);
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setMvID(cursor.getString(idIndex));
            movie.setMvTitle(cursor.getString(titleIndex));
            movie.setMvPoster(cursor.getString(posterIndex));
            movie.setMvPlot(cursor.getString(plotIndex));
            movie.setMvRating(cursor.getString(ratingIndex));
            movie.setMvReleaseDate(cursor.getString(dateIndex));
            lstMovies.add(movie);
        }

        mvAdapter = new MovieAdapter(lstMovies, thisActivity);
        mvList.setAdapter(mvAdapter);
        firstLoad = true;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class LoadingMoviesTask extends AsyncTask<String, Void, Void> {

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
            } else
                pgbarLoadMovies.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildReqListMvURL(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            lstMovies.clear();
            try {
                lstMovies.addAll(MovieUtils.getMovieListFromJSON(result));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            String display = "";
            for (int i = 0; i < lstMovies.size(); i++) {
                display = display + lstMovies.get(i).toString() + "\n";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (firstLoad) {
                mvAdapter = new MovieAdapter(lstMovies, thisActivity);
                mvList.setAdapter(mvAdapter);
                firstLoad = false;
            } else {
                mvAdapter.notifyDataSetChanged();
            }
            pgbarLoadMovies.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        if (isShowingFaves) {
            activityMenu.getItem(0).setEnabled(false);
            activityMenu.getItem(1).setChecked(true);
        }
        super.onResume();
    }
}
