package com.example.hp.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.popularmovies.Network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MovieDetail extends AppCompatActivity {
    private String[] jsonKey = {"title", "release_date",
            "vote_average", "overview", "poster_path"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        this.setTitle("Detail");

        Intent intent = getIntent();
        HashMap<String, String> movieInfo =
                (HashMap<String, String>) intent.getSerializableExtra("movieInfo");

        TextView tv;

        for(int i = 0; i < jsonKey.length; i++){
            String value = movieInfo.get(jsonKey[i]);
            tv = null;
            switch (jsonKey[i]){
                case "poster_path":
                    ImageView iv = (ImageView) findViewById(R.id.iv_thumbnail);
                    String posterURL = NetworkUtils.getPosterBase(value);
                    Picasso.with(this.getBaseContext()).load(posterURL).into(iv);
                    break;
                case "title":
                    tv = (TextView) findViewById(R.id.tv_Title);
                    tv.setText(value);
                    break;
                case "release_date":
                    tv = (TextView) findViewById(R.id.release_date);
                    tv.setText("Release: "+value);
                    break;
                case "vote_average":
                    tv = (TextView) findViewById(R.id.vote_average);
                    tv.setText("Avg: "+value);
                    break;
                case "overview":
                    tv = (TextView) findViewById(R.id.tv_summary);
                    tv.setText("Plot: "+value);
                default:break;

            }
        }
    }
}
