package com.shuja1497.mapsnearbyplaces;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by shuja1497 on 10/31/17.
 */

public class DataParser {

    private HashMap<String , String> getPlace(JSONObject googlePlaceJson){

        HashMap<String , String> googlePlacaMap = new HashMap<>();

        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitiude = "" ;
        String longitude = "";
        String reference = "";


        try
        {

            if (!googlePlaceJson.isNull("name"))
            {
                placeName = googlePlaceJson.getString("name");
            }

            if(!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitiude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            //data fetched .. now put in googl hashmap

            googlePlacaMap.put("place_name", placeName);
            googlePlacaMap.put("vicinity", vicinity);
            googlePlacaMap.put("lat", latitiude);
            googlePlacaMap.put("lng", longitude);
            googlePlacaMap.put("reference", reference);

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }
            return googlePlacaMap;
        }
}
