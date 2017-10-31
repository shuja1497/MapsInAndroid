package com.shuja1497.mapsnearbyplaces;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // will return a list of hashmap
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray){

        int count = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        for (int i = 0 ; i < count ; i ++){

            try {
                placeMap = getPlace((JSONObject)jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    //parse calls getPlaces . which then calls getPlace

    public List<HashMap<String, String>> parse(String jsonData){

        JSONArray jsonArray = null;
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return getPlaces(jsonArray);
    }


}
