package com.thuy.android.popularmovie1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tranv on 04-Feb-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private int itemNumber;

    private ArrayList<Movie> lstMovies;

    final private GridItemClickListener itemClickListener;

    final String IMG_URL_BASE = "http://image.tmdb.org/t/p/w600/";

    Context ctx;

    public interface GridItemClickListener {
        void onGridItemClick(int clickItemIndex);
    }

    public MovieAdapter(ArrayList<Movie> movies, GridItemClickListener listener) {
        itemNumber = movies.size();
        lstMovies = movies;
        itemClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
//        if(position >= lstMovies.size())
//            return;
        String imgPath = IMG_URL_BASE + lstMovies.get(position).getMvPoster();
        Picasso.with(ctx).load(imgPath).placeholder(android.R.drawable.ic_menu_gallery).into(holder.imgMovie);
    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.imgv_mv_item);
            itemView.setOnClickListener(this);
        }

        void bind(String imgPath) {

        }

        @Override
        public void onClick(View view) {
            int clickPos = getAdapterPosition();
            itemClickListener.onGridItemClick(clickPos);
        }
    }
}
