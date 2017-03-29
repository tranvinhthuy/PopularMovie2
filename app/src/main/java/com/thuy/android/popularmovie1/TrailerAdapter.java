package com.thuy.android.popularmovie1;

/**
 * Created by tranv on 28-Mar-17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thuy.android.popularmovie1.R;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private int itemNumber;

    private ArrayList<String> lstTrailers;

    final private GridItemClickListener itemClickListener;

    final String IMG_URL_BASE = "http://image.tmdb.org/t/p/w600MovieAdapter/";

    Context ctx;

    public interface GridItemClickListener {
        void onGridItemClick(int clickItemIndex);
    }

    public TrailerAdapter(ArrayList<String> trailers, GridItemClickListener listener) {
        itemNumber = trailers.size();
        lstTrailers = trailers;
        itemClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        int trailerCount = position+1;
        holder.txvTrailerCount.setText(trailerCount+"");
        holder.txvTrailerCount.setTag(lstTrailers.get(position));
    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imgTrailer;
        TextView txvTrailerCount;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            imgTrailer = (ImageView) itemView.findViewById(R.id.img_btn_trailer);
            txvTrailerCount = (TextView) itemView.findViewById(R.id.txv_trailer_count);
            imgTrailer.setOnClickListener(this);
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

