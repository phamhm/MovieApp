package com.example.hp.popularmovies.DBUtils.ContentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hp on 7/22/17.
 */

public class FavMovieContract {
    private FavMovieContract(){};

    public static final String AUTHORITY = "com.hp.android.popular_movie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String FAV_MOVIE_PATH = "favorites";

    public static final class FavMovieEntry implements BaseColumns {
        private FavMovieEntry(){};

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(FAV_MOVIE_PATH).build();

        public static final String TABLE_NAME = "fav_movies";

        /*
        public static final String[] jsonKey =
                {"id", "title", "release_date", "vote_average", "overview", "poster_path"};
                */
        public static final String COLUMN_MOVIE_DB_ID = "id";

        public static final String COLUMN_MOVIE_DB_TITLE = "title";

        public static final String COLUMN_MOVIE_DB_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_DB_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_MOVIE_DB_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_DB_POSTER_PATH = "poster_path";

        public static final String COLUMN_MOVIE_DB_TRAILERS_URL = "trailers";

        public static final String COLUMN_MOVIE_DB_REVIEWS_URL = "reviews";

    }
}
