package com.example.hp.popularmovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.hp.popularmovies.Adapter.MovieAdapter;
import com.example.hp.popularmovies.DBUtils.MovieDBUtils;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<HashMap<String, String>>>{

    int currentMenuItem = R.id.popular;

    final static String MENU_ITEM_CALLBACK_KEY = "Current_Menu_Item";

    MovieAdapter mMovieAdapter;

    private static final int MOVIES_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            currentMenuItem = savedInstanceState.getInt(MENU_ITEM_CALLBACK_KEY);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);

        mMovieAdapter = new MovieAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMoviesGrid);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MENU_ITEM_CALLBACK_KEY, currentMenuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        setTitleFromMenuItem();
        return true;
    }

    private void setTitleFromMenuItem(){
        String title;
        switch (currentMenuItem){
            case R.id.top_rated:
                title = "Sort By: Top Rated";
                break;
            case R.id.popular:
                title = "Sort By: Popular";
                break;
            case R.id.favorites:
                title = "Sort By: Favorites";
                break;
            default:
                throw new UnsupportedOperationException("Unexpected Sorting Criteria: "+
                        currentMenuItem);
        }

        setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentMenuItem = item.getItemId();
        setTitleFromMenuItem();
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
        setTitleFromMenuItem();
    }

    @Override
    public Loader<List<HashMap<String, String>>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<HashMap<String, String>>>(this) {
            List<HashMap<String,String>> moviesInfo = null;

            @Override
            protected void onStartLoading() {
                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
                pb.setVisibility(View.VISIBLE);

                if (moviesInfo != null)
                    deliverResult(moviesInfo);
                else
                    forceLoad();
            }

            public void deliverResult(List<HashMap<String,String>> info){
                this.moviesInfo = info;
                super.deliverResult(info);
            }

            @Override
            public List<HashMap<String, String>> loadInBackground() {
                String urlString;
                switch (currentMenuItem){
                    case R.id.top_rated:
                        urlString = MovieDBUtils.getSortByUrl(MovieDBUtils.SortBy.TOP_RATED);
                        return MovieDBUtils.fetchFromMovieDB(urlString);
                    case R.id.popular:
                        urlString = MovieDBUtils.getSortByUrl(MovieDBUtils.SortBy.POPULAR);
                        return MovieDBUtils.fetchFromMovieDB(urlString);
                    case R.id.favorites:
                        return MovieDBUtils.fetchFavoriteMovies(getBaseContext());
                    default:
                        throw new UnsupportedOperationException("Unexpected Sorting Criteria: "+ currentMenuItem);
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<HashMap<String, String>>> loader,
                               List<HashMap<String, String>> data) {

        mMovieAdapter.updateView(data);
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<HashMap<String, String>>> loader) {
        mMovieAdapter.updateView(null);
    }
}
