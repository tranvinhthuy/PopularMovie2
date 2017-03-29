package com.thuy.android.popularmovie1;

/**
 * Created by tranv on 28-Mar-17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thuy.android.popularmovie1.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private int itemNumber;

    private ArrayList<String> lstReviews;

    private ArrayList<String> lstReviewers;

    Context ctx;

    public interface GridItemClickListener {
        void onGridItemClick(int clickItemIndex);
    }

    public ReviewAdapter(ArrayList<String> reviews, ArrayList<String> reviewers) {
        itemNumber = reviews.size();
        lstReviews = reviews;
        lstReviewers = reviewers;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.txvReview.setText(lstReviews.get(position));
        holder.txvReviewer.setText(lstReviewers.get(position));
    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView txvReview, txvReviewer;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            txvReview = (TextView) itemView.findViewById(R.id.txv_review);
            txvReviewer = (TextView) itemView.findViewById(R.id.txv_reviewer);
        }

        void bind(String imgPath) {

        }

    }
}

