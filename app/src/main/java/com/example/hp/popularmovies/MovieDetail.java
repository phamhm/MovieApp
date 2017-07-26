package com.example.hp.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hp.popularmovies.DBUtils.ContentProvider.FavMovieContract;
import com.example.hp.popularmovies.DBUtils.MovieDBUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MovieDetail extends AppCompatActivity {
    HashMap<String, String> movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.setTitle("Detail");

        Intent intent = getIntent();

        movieInfo = (HashMap<String, String>) intent.getSerializableExtra("movieInfo");

        String[] selectionArgs = {movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID)};
        Cursor cursor =
                getContentResolver().query(FavMovieContract.FavMovieEntry.CONTENT_URI, null,
                        FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID+"=?", selectionArgs,
                        null, null);

        final ToggleButton favButton = (ToggleButton) findViewById(R.id.fav_star);
        if (cursor.getCount() != 0)
            favButton.setChecked(true);
        else
            favButton.setChecked(false);


        setPosterAndOthers();

        setTitleAndOverview();

        try {
            String value =
                    movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_TRAILERS_URL);
            setListViewTrailers(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            String value =
                    movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_REVIEWS_URL);
            setReviews(value);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setReviews(String value) throws JSONException {
        JSONArray jsonArray = new JSONArray(value);

        int jsonLen = jsonArray.length();
        LinearLayout detail_linear_layout = (LinearLayout) findViewById(R.id.detail_linear_layout);
        for(int i=0; i < jsonLen; i++)
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);

                String author  = jObj.getString("author");
                String content = jObj.getString("content");

                String review = "Author:"+author+"\n"+
                        "Review:"+content;

                TextView tv = new TextView(this);
                tv.setTextSize(22);
                tv.setText(review);
                detail_linear_layout.addView(tv);

                View view = new View(this);
                view.setBackgroundColor(getColor(android.R.color.darker_gray));
                detail_linear_layout.addView(view);

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void setTitleAndOverview() {
        TextView tv;
        String value;

        tv = (TextView) findViewById(R.id.tv_Title);
        value = movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_TITLE);
        tv.setText(value);

        tv = (TextView) findViewById(R.id.tv_summary);
        value = movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_OVERVIEW);
        tv.setText("Overview: "+value);
    }

    private void setPosterAndOthers() {
        final TextView posterAndOthers = (TextView) findViewById(R.id.poster_and_other_info);


        String posterURL =
                MovieDBUtils.getPosterBase(movieInfo.get(
                        FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_POSTER_PATH));

        String releaseDate =
                "Release:" + movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE);
        String voteAverge =
                "Average: "+ movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE);
        posterAndOthers.setText(releaseDate + "\n\n" + voteAverge);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable posterDrawable = new BitmapDrawable(getResources(), bitmap);

                int h = posterDrawable.getIntrinsicHeight();
                int w = posterDrawable.getIntrinsicWidth();
                posterDrawable.setBounds(0,0,h * 2,w * 4);

                posterAndOthers.setCompoundDrawables(
                        posterDrawable, null, null, null);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                posterAndOthers.setCompoundDrawablesWithIntrinsicBounds(
                        errorDrawable, null, null, null);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this.getBaseContext()).load(posterURL)
                .error(getDrawable(android.R.drawable.stat_notify_error))
                .into(target);
    }

    private void setListViewTrailers(String value) throws JSONException{
        JSONArray jsonArray = new JSONArray(value);

        int jsonLen = jsonArray.length();
        LinearLayout detail_linear_layout = (LinearLayout) findViewById(R.id.detail_linear_layout);
        for(int i=0; i < jsonLen; i++)
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);

                final String youtubeId = jObj.getString("key");
                String text = jObj.getString("name");

                Button button = new Button(this);
                button.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(android.R.drawable.ic_media_play, null),
                        null, null,
                        getDrawable(android.R.drawable.divider_horizontal_dark));
                button.setText(text);
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MovieDBUtils.playYouTube(getBaseContext(), youtubeId);
                            }
                        }
                );

                detail_linear_layout.addView(button);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public void onClickToggleFavorite(View view) {
        String movieId = movieInfo.get(FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID);
        String[] selectionArgs = {movieId};
        Cursor cursor =
                getContentResolver().query(FavMovieContract.FavMovieEntry.CONTENT_URI, null,
                        FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID+"=?", selectionArgs,
                        null, null);
        if (cursor.getCount() !=0 ){
            getContentResolver().delete(FavMovieContract.FavMovieEntry.CONTENT_URI,
                    FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID+"=?",
                    selectionArgs);
            return;
        }

        ContentValues contentValues = new ContentValues();
        for(String key : MovieDBUtils.jsonKey){
            String value = movieInfo.get(key);
            contentValues.put(key, value);
        }
        getContentResolver().insert(FavMovieContract.FavMovieEntry.CONTENT_URI,
                contentValues);

    }
}
