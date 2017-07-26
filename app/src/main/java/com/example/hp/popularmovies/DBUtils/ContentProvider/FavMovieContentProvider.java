package com.example.hp.popularmovies.DBUtils.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hp on 7/22/17.
 */

public class FavMovieContentProvider extends ContentProvider {

    public static final int FAVS = 100;
    public static final int FAVS_WITH_ID = 101;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private FavDbHelper mFavDbHelper;

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavMovieContract.AUTHORITY, FavMovieContract.FAV_MOVIE_PATH, FAVS);
        uriMatcher.addURI(FavMovieContract.AUTHORITY, FavMovieContract.FAV_MOVIE_PATH + "/#", FAVS_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mFavDbHelper = new FavDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);
        switch (match){
            case FAVS:
                Cursor cursor = db.query(FavMovieContract.FavMovieEntry.TABLE_NAME,
                        projection,selection,selectionArgs,
                        null, null,sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (contentValues == null)
            return null;

        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        switch (match){
            case FAVS:
                long id = db.insert(FavMovieContract.FavMovieEntry.TABLE_NAME,
                        null, contentValues);
                if (id > 0 ){
                    getContext().getContentResolver().notifyChange(uri, null);

                    return ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert into "+uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        int favsDeleted;
        switch (match){
            case FAVS:
                favsDeleted = db.delete(FavMovieContract.FavMovieEntry.TABLE_NAME,
                        s, strings);
                if (favsDeleted != 0 )
                    getContext().getContentResolver().notifyChange(uri, null);
                return favsDeleted;
            case FAVS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favsDeleted = db.delete(FavMovieContract.FavMovieEntry.TABLE_NAME,
                        "_id=?", new String[]{id});
                if (favsDeleted != 0 )
                    getContext().getContentResolver().notifyChange(uri, null);

                return favsDeleted;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
