package com.thuy.android.popularmovie1;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.thuy.android.popularmovie1.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    final String IMG_URL_BASE = "http://image.tmdb.org/t/p/w342/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String sentContent = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie mv = new Movie(sentContent);

        Picasso.with(this).load(IMG_URL_BASE + mv.getMvPoster()).placeholder(android.R.drawable.ic_menu_gallery).into(binding.imgvPoster);
        binding.txvTitle.append(mv.getMvTitle());
        binding.txvReleaseDate.append(mv.getMvReleaseDate());
        binding.txvRating.append(mv.getMvRating());
        binding.txvPlot.append(mv.getMvPlot());

    }
}
