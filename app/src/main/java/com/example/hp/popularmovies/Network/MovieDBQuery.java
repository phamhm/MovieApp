package com.example.hp.popularmovies.Network;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by hp on 7/1/17.
 */

public class MovieDBQuery extends AsyncTask<URL, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(URL... urls) {
        URL searchUrl = urls[0];

        String response = null;

        try{
            response = NetworkUtils.getResponseFromHttpUrl(searchUrl);
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
}
