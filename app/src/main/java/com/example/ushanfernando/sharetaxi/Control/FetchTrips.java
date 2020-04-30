package com.example.ushanfernando.sharetaxi.Control;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.ushanfernando.sharetaxi.LocationFromAddreesSync;
import com.example.ushanfernando.sharetaxi.Model.STApiRequest;
import com.example.ushanfernando.sharetaxi.Model.Trip;
import com.example.ushanfernando.sharetaxi.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchTrips extends Application implements STApiRequest.OnRequestComplete {
    private static final String TAG = "Login";
//    private static final String fetchTripsUrl = "http://10.0.2.2:8080/server/fetch/trips.php";

    //public server
    private static final String fetchTripsUrl =  Constants.Domain+"/api/fetch/trips.php";


    Context mContext;

    private final OnFetchTripRequestComplete mOnFetchTripRequestComplete;

   public interface OnFetchTripRequestComplete {
        public void onFetchTripRequestComplete(List<Trip> trips);
    }

    public FetchTrips(Context context, OnFetchTripRequestComplete onFetchTripRequestComplete) {
        mOnFetchTripRequestComplete = onFetchTripRequestComplete;
        mContext = context;
    }

    public void fetch(String type, int page) {

        Map<String, String> loginRequestTags = new HashMap<String, String>();
        loginRequestTags.put("type", type);
        loginRequestTags.put("currentpage", String.valueOf(page));

        STApiRequest fetchTrips = new STApiRequest(FetchTrips.this, fetchTripsUrl, "fetchTrips");
        fetchTrips.newRequest(mContext, loginRequestTags);
        Log.d(TAG, "login: req made");

    }

    @Override
    public void onSuccessRequest(JSONObject result, String instanceName) {
        Log.d(TAG, "onSuccessRequest: " + result);
        List<Trip> tripList = new ArrayList<>();
        try {
            if (result.has("pageEnd")){
                Log.i(TAG, "onSuccessTripRequest: PageEnd!");
            }else {
                JSONArray trips;
                JSONObject trip;
                trips = result.getJSONArray("trips");
                Log.d(TAG, "array size : " + trips.length());
                for (int i = 0; i < trips.length(); i++) {
                    trip = trips.getJSONObject(i);
//                Log.d(TAG, "onSuccessRequest: name posted  :" + trip.getString("destination"));
                    Trip currentTrip = new Trip();
                    currentTrip.setId(Integer.valueOf(trip.getString("id")));
                    currentTrip.setType(trip.getString("type"));
                    currentTrip.setStart(trip.getString("start"));
                    currentTrip.setDestination(trip.getString("destination"));
                    currentTrip.setDate(trip.getString("date"));
                    currentTrip.setTime(trip.getString("time"));
                    currentTrip.setIdPosted(Integer.valueOf(trip.getString("userPosted")));
                    currentTrip.setTimePosted(trip.getString("timePosted"));
                    currentTrip.setNamePosted(trip.getString("name"));
//                    currentTrip.setStartLatLng(LocationFromAddreesSync.getLocationFromAddress(FetchTrips.this,currentTrip.getStart()));
                    tripList.add(currentTrip);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "onSuccessRequest: Failed to decrypt json " + e);
        }
                mOnFetchTripRequestComplete.onFetchTripRequestComplete(tripList);
    }

}
