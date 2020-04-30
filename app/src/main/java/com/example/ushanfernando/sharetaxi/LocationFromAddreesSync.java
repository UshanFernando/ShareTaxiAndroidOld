package com.example.ushanfernando.sharetaxi;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class LocationFromAddreesSync {

    private static final String TAG = "LocationFromAddress";

    public static LatLng getLocationFromAddress(Context context, String locationText) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(locationText, 5);

            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                Address location = address.get(0);

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }
        Log.d(TAG, "getLocationFromAddress: "+locationText+"--> "+p1);
        return p1;
    }

}


