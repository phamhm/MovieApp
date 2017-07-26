package com.example.hp.popularmovies.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.popularmovies.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 7/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>{
    private LayoutInflater layoutInflater;
    private List<HashMap<String,String>> moviesInfoList;

    public MovieAdapter(Context context) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.moviesInfoList = null;
    }

    @Override
    public int getItemCount() {
        if (moviesInfoList == null)
            return 0;
        return moviesInfoList.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout.
        // then pass the view into the ViewHolder, then return the viewholder
        View view =
                this.layoutInflater.inflate(R.layout.rv_item, parent,
                        false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        HashMap<String, String> movieInfo = this.moviesInfoList.get(position);
        holder.bind(movieInfo);
    }

    public void updateView(List<HashMap<String,String>> newMoviesInfoList){
        this.moviesInfoList = newMoviesInfoList;
        this.notifyDataSetChanged();
    }
}
