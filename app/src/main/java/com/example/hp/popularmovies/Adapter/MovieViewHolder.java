package com.example.hp.popularmovies.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.popularmovies.MovieDetail;
import com.example.hp.popularmovies.Network.NetworkUtils;
import com.example.hp.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by hp on 7/1/17.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final ImageView mImageView;
    private HashMap<String, String> movieInfo;

    public MovieViewHolder(View itemView) {
        // find the view layout
        super(itemView);

        this.mImageView = (ImageView) itemView.findViewById(R.id.rvImageItem);
        this.mImageView.setClickable(true);
        this.mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.mImageView.getContext(),
                MovieDetail.class);
        intent.putExtra("movieInfo", this.movieInfo);
        view.getContext().startActivity(intent);
    }

    public void bind(HashMap<String, String> movieInfo){
        this.movieInfo = movieInfo;
        String posterURL = NetworkUtils.getPosterBase(movieInfo.get("poster_path"));
        Picasso.with(this.mImageView.getContext()).load(posterURL).into(this.mImageView);
    }

}
