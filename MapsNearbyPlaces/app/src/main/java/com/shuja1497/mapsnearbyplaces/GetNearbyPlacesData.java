package com.shuja1497.mapsnearbyplaces;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by shuja1497 on 10/31/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    String googlePlacesData;
    GoogleMap googleMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
