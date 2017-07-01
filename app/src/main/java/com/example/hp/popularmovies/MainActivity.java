package com.example.hp.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.popularmovies.Adapter.MovieAdapter;
import com.example.hp.popularmovies.Network.MovieDBQuery;
import com.example.hp.popularmovies.Network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        String urlString = NetworkUtils.getSortByUrl(NetworkUtils.SortBy.POPULAR.toString());

        MovieAdapter mMovieAdapter = new MovieAdapter(this,
                fetchMoviesInfo(urlString));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMoviesGrid);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String urlString = null;
        String title = null;
        switch (item.getItemId()){
            case R.id.top_rated:
                title = "Sort By: Top Rated";
                urlString = NetworkUtils.getSortByUrl(NetworkUtils.SortBy.TOP_RATED.toString());
                break;
            case R.id.popular:
                title = "Sort By: Popular";
                urlString = NetworkUtils.getSortByUrl(NetworkUtils.SortBy.POPULAR.toString());
                break;
            default:
                urlString = null;
                break;
        }

        if (urlString != null){
            MovieAdapter mMovieAdapter = new MovieAdapter(this,
                    fetchMoviesInfo(urlString));

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMoviesGrid);
            recyclerView.setAdapter(mMovieAdapter);
            this.setTitle(title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<HashMap<String, String>> fetchMoviesInfo(String urlString) {

        List<HashMap<String, String>> moviesInfo = new ArrayList<>();

        URL url = NetworkUtils.buildUrl(urlString);
        JSONArray mvJsonArray = null;

        MovieDBQuery queryTask = new MovieDBQuery();
        queryTask.execute(url);

        try {
            mvJsonArray = queryTask.get(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        String[] jsonKey = {"title", "release_date",
                "vote_average", "overview", "poster_path"};

        if (mvJsonArray != null){
            int jsonLen = mvJsonArray.length();
            for(int i=0; i < jsonLen; i++){
                try {
                    JSONObject jObj = mvJsonArray.getJSONObject(i);
                    HashMap<String,String> tmpMap = new HashMap<>();

                    for (int j=0; j < jsonKey.length; j++){
                        String key = jsonKey[j];
                        String value = jObj.getString(key);
                        tmpMap.put(key, value);

                    }

                    moviesInfo.add(tmpMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return moviesInfo;
    }
}
