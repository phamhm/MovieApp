package com.example.hp.popularmovies.DBUtils.ContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 7/22/17.
 */

public class FavDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "favorite_movie.db";

    private static final int VERSION = 4;

    public FavDbHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + FavMovieContract.FavMovieEntry.TABLE_NAME + " ("+
                FavMovieContract.FavMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_ID + " INTEGER NOT NULL UNIQUE, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_POSTER_PATH + " TEXT NOT NULL, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE+ " TEXT NOT NULL, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_TITLE       + " TEXT NOT NULL, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE+ " TEXT NOT NULL, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_TRAILERS_URL+ " TEXT, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_REVIEWS_URL+ " TEXT, "+
                FavMovieContract.FavMovieEntry.COLUMN_MOVIE_DB_OVERVIEW + " TEXT);";


        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavMovieContract.FavMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
