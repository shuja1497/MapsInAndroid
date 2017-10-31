package com.shuja1497.mapsnearbyplaces;

import android.os.AsyncTask;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shuja1497 on 10/31/17.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    String googlePlacesData;
    GoogleMap googleMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {

        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }



    @Override
    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();

        nearbyPlaceList = parser.parse(s);

        showNearbyPlaces(nearbyPlaceList);
    }

// shows all places in the list
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){

        for (int i = 0 ; i<nearbyPlacesList.size();i++){

            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String , String> googlePlace = nearbyPlacesList.get(i);

            String placeName  =googlePlace.get("place_name");
            String vicinity  = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));//google places retuens a string so parse
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat , lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName +":" + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            googleMap.addMarker(markerOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        }
    }
}
