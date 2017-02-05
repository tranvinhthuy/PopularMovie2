package com.thuy.android.popularmovie1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.GridItemClickListener{

    private ProgressBar pgbarLoadMovies;

    private MovieAdapter mvAdapter;
    private RecyclerView mvList;

    private MainActivity thisActivity;

    ArrayList<Movie> lstMovies = new ArrayList<>();

    private String currentSortOption = NetworkUtils.POPULAR;

    boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pgbarLoadMovies = (ProgressBar)findViewById(R.id.pgbar_load_movie);

        mvList = (RecyclerView) findViewById(R.id.rv_list_mv);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mvList.setLayoutManager(layoutManager);

        thisActivity = this;

        LoadingMoviesTask loadingMoviesTask = new LoadingMoviesTask();
        loadingMoviesTask.execute(currentSortOption);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    void showSortOptionDialog(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.sort_dlg_tittle));
        final String[] sortOptions = {getString(R.string.sort_popular), getString(R.string.sort_rating)};
        final String[] sortOptionsVal = {NetworkUtils.POPULAR, NetworkUtils.TOP_RATED};

        int checkedItem =0;
        if(currentSortOption==sortOptionsVal[1])
            checkedItem = 1;

        dialog.setSingleChoiceItems(sortOptions, checkedItem, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(currentSortOption != sortOptionsVal[item]) {
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGridItemClick(int clickItemIndex) {
        Intent intent = new Intent(thisActivity,DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, lstMovies.get(clickItemIndex).toString());
        startActivity(intent);
    }

    public class LoadingMoviesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgbarLoadMovies.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            lstMovies.clear();
            try {
                lstMovies.addAll(MovieUtils.getMovieListFromJSON(result));
            } catch (JSONException e) {
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
            if(firstLoad) {
                mvAdapter = new MovieAdapter(lstMovies, thisActivity);
                mvList.setAdapter(mvAdapter);
                firstLoad = false;
            }
            else {
                mvAdapter.notifyDataSetChanged();
            }
            pgbarLoadMovies.setVisibility(View.INVISIBLE);
        }
    }
}
