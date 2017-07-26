package com.example.hp.popularmovies.DBUtils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.hp.popularmovies.BuildConfig;
import com.example.hp.popularmovies.DBUtils.ContentProvider.FavMovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.example.hp.popularmovies.DBUtils.ContentProvider.FavMovieContract.FavMovieEntry;

/**
 * Created by hp on 7/1/17.
 */

public class MovieDBUtils {
    private static final String posterBase = "http://image.tmdb.org/t/p/w185/";
    private static final String movieDBBase = "http://api.themoviedb.org/3/movie/";


    private static final String API_KEY = BuildConfig.API_KEY;

    public static final String[] jsonKey =
                   {FavMovieEntry.COLUMN_MOVIE_DB_ID,
                           FavMovieEntry.COLUMN_MOVIE_DB_TITLE,
                           FavMovieEntry.COLUMN_MOVIE_DB_RELEASE_DATE,
                           FavMovieEntry.COLUMN_MOVIE_DB_VOTE_AVERAGE,
                           FavMovieEntry.COLUMN_MOVIE_DB_OVERVIEW,
                           FavMovieEntry.COLUMN_MOVIE_DB_TRAILERS_URL,
                           FavMovieEntry.COLUMN_MOVIE_DB_REVIEWS_URL,
                           FavMovieEntry.COLUMN_MOVIE_DB_POSTER_PATH};

    private static JSONArray fetch(URL searchUrl) {
        String response = null;

        try{
            response = MovieDBUtils.getResponseFromHttpUrl(searchUrl);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if (response != null){
            try {
                JSONObject mJson =  new JSONObject(response);
                return mJson.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<HashMap<String, String>> fetchFromMovieDB(String urlString) {

        List<HashMap<String, String>> moviesInfo = new ArrayList<>();

        URL url = buildUrl(urlString);
        JSONArray mvJsonArray = fetch(url);

        if (mvJsonArray != null){
            int jsonLen = mvJsonArray.length();

            for(int i=0; i < jsonLen; i++){
                try {
                    JSONObject jObj = mvJsonArray.getJSONObject(i);
                    HashMap<String,String> tmpMap = new HashMap<>();

                    String value, id, stringUrl;
                    JSONArray jArray;
                    for (String key : jsonKey){
                        if (key == FavMovieEntry.COLUMN_MOVIE_DB_TRAILERS_URL){
                            id = jObj.getString(FavMovieEntry.COLUMN_MOVIE_DB_ID);
                            stringUrl = getVideosUrl(id);

                            jArray = fetch(MovieDBUtils.buildUrl(stringUrl));
                            if (jArray != null)
                                value = "" + jArray;
                            else
                                value = "";
                        }
                        else if (key == FavMovieEntry.COLUMN_MOVIE_DB_REVIEWS_URL){
                            id = jObj.getString(FavMovieEntry.COLUMN_MOVIE_DB_ID);
                            stringUrl = getReviewsUrl(id);

                            jArray = fetch(MovieDBUtils.buildUrl(stringUrl));
                            if (jArray != null)
                                value = "" + jArray;
                            else
                                value = "";
                        }
                        else{
                            value = jObj.getString(key);
                        }
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

    public static void playYouTube(Context context, String youtubeId){
        Uri youtubeAppUri = Uri.parse("vnd.youtube:"+youtubeId);
        Uri internetUri = Uri.parse("http://youtube.com/watch?v="+youtubeId);
        Intent intent;
        try{
            intent =new Intent(Intent.ACTION_VIEW, youtubeAppUri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e){
            intent =  new Intent(Intent.ACTION_VIEW, internetUri);
            context.startActivity(intent);
        }
    }


    public static List<String> getListFromJsonArray(JSONArray jsonArray, String key) {
        if (jsonArray == null)
            return null;

        List<String> res = new ArrayList<>();

        int jsonLen = jsonArray.length();

        for(int i=0; i < jsonLen; i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                String jsonItem = jObj.getString(key);
                res.add(jsonItem);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return res;
    }

    public static List<HashMap<String, String>> fetchFavoriteMovies(Context context) {
        Cursor cursor =
                context.getContentResolver()
                        .query(FavMovieContract.FavMovieEntry.CONTENT_URI,
                                null,null,null,null);
        List<HashMap<String, String>> moviesInfo = new ArrayList<>();

        while(cursor.moveToNext()){
            HashMap<String,String> tmpMap = new HashMap<>();
            for(String key : jsonKey){
                int index = cursor.getColumnIndex(key);
                String value = cursor.getString(index);
                tmpMap.put(key, value);
            }
            if (tmpMap.size()!=0)
                moviesInfo.add(tmpMap);
        }

        return moviesInfo;
    }

    public interface SortBy{
        String POPULAR = "popular?api_key=";
        String TOP_RATED = "top_rated?api_key=";
    }

    public static String getSortByUrl(String sortOrder){
        return movieDBBase+sortOrder+API_KEY;
    }

    public static String getPosterBase(String path){
        return posterBase+path;
    }

    public static String getVideosUrl(String id){
        return movieDBBase+id+"/videos?api_key="+API_KEY;
    }

    public static String getReviewsUrl(String id){
        return movieDBBase+id+"/reviews?api_key="+API_KEY;
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
