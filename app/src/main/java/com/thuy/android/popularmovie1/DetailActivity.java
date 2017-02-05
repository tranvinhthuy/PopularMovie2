package com.thuy.android.popularmovie1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView txvTitle, txvReleaseDate, txvRating, txvPlot;
    private ImageView imgvPoster;

    final String IMG_URL_BASE = "http://image.tmdb.org/t/p/w342/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txvTitle = (TextView)findViewById(R.id.txv_title);
        txvReleaseDate = (TextView)findViewById(R.id.txv_release_date);
        txvRating = (TextView)findViewById(R.id.txv_rating);
        txvPlot = (TextView)findViewById(R.id.txv_plot);
        imgvPoster = (ImageView)findViewById(R.id.imgv_poster);

        Intent intent = getIntent();
        String sentContent = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie mv = new Movie(sentContent);

        Picasso.with(this).load(IMG_URL_BASE+mv.getMvPoster()).into(imgvPoster);
        txvTitle.append(mv.getMvTitle());
        txvReleaseDate.append(mv.getMvReleaseDate());
        txvRating.append(mv.getMvRating());
        txvPlot.append(mv.getMvPlot());

    }
}
