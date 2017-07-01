package com.example.hp.popularmovies.Network;

import android.net.Uri;

import com.example.hp.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by hp on 7/1/17.
 */

public class NetworkUtils {
    private static final String posterBase = "http://image.tmdb.org/t/p/w185/";
    private static final String movieDBBase = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY = BuildConfig.API_KEY;

    public enum SortBy{
        POPULAR("popular?api_key="), TOP_RATED("top_rated?api_key=");

        private String sortOrder;
        private SortBy(String sortOrder){
            this.sortOrder = sortOrder;
        }

        @Override
        public String toString() {
            return this.sortOrder;
        }
    }

    public static String getSortByUrl(String sortOrder){
        return movieDBBase+sortOrder+API_KEY;
    }

    public static String getPosterBase(String path){
        return posterBase+path;
    }

    public static URL buildUrl(String baseUrl) {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
