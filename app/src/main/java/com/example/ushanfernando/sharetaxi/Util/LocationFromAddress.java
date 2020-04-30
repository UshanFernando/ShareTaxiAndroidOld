package com.example.ushanfernando.sharetaxi.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class LocationFromAddress extends AsyncTask<String,Integer,LatLng> {

    Context context;
    private final OnFetchLocationFromAddress mOnFetchLocationFromAddress;

   public interface OnFetchLocationFromAddress{
        void fetchLocationComplete(LatLng data,int status,String name);
    }
    private static final String TAG = "LocationFromAddress";
    private String name;

    public LocationFromAddress(Context context, OnFetchLocationFromAddress onFetchTripRequestComplete) {
       this.context = context;
       mOnFetchLocationFromAddress = onFetchTripRequestComplete;
    }

    @Override
    protected void onPostExecute(LatLng s) {

        int status;
        if (s == null){
            status = 1;
        }else{
            status =0;}
        Log.d(TAG, "onPostExecute: called");
        mOnFetchLocationFromAddress.fetchLocationComplete(s,status,name);
    }

    @Override
    protected LatLng doInBackground(String... strings) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        if (strings.length>1) {
            name = strings[1];
        }
        String locationText = strings[0];
        Log.d(TAG, "doInBackground: "+locationText);

        try {
            // May throw an IOException
            address = coder.getFromLocationName(locationText, 5);

            if (address == null) {
                return null;
            }
            if (address.size()> 0) {
                Address location = address.get(0);

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }
        Log.d(TAG, "getLocationFromAddress: p1"+p1);
        return p1;
    }

    public void executeOnSameThread(String s,String name){
        Log.d(TAG, "executeOnSameThread: sTARTED");
        int status;
       LatLng latLng = doInBackground(s);
        if (latLng == null){
            status = 1;
        }else{
            status =0;}
        if (null != mOnFetchLocationFromAddress){
            mOnFetchLocationFromAddress.fetchLocationComplete(latLng,status,name);
        }

    }
}
