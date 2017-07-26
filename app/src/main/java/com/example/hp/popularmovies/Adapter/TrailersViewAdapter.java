package com.example.hp.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.popularmovies.R;

import java.util.List;

/**
 * Created by hp on 7/23/17.
 */

public class TrailersViewAdapter extends RecyclerView.Adapter<TrailersViewAdapter.TrailersViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> trailersUrl;

    public TrailersViewAdapter(Context context, List<String> trailersStringUrl) {
        layoutInflater = LayoutInflater.from(context);
        this.trailersUrl = trailersStringUrl;
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        String item = trailersUrl.get(position);
        holder.bind(item);
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                this.layoutInflater.inflate(R.layout.trailer_item, parent,
                        false);
        return new TrailersViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (trailersUrl == null)
            return 0;

        return trailersUrl.size();
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        public TrailersViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.trailer_text);
        }

        public void bind (String item){
            textView.setText(item);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
