package com.example.ushanfernando.sharetaxi.Model;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import com.example.ushanfernando.sharetaxi.Control.DirectionsJsonParser;
import com.example.ushanfernando.sharetaxi.Util.LocationFromAddress;
import com.example.ushanfernando.sharetaxi.R;
import com.example.ushanfernando.sharetaxi.SimpleRequest;
import com.example.ushanfernando.sharetaxi.Util.TimesAgo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripView extends FragmentActivity implements OnMapReadyCallback, SimpleRequest.OnRequestComplete, LocationFromAddress.OnFetchLocationFromAddress {

    private GoogleMap mMap;
    Intent i;
    Trip trip;
    LatLng origin;
    LatLng dest;
    private static final String TAG = "TripView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        i = getIntent();
        trip = (Trip) i.getParcelableExtra("trip");

        LocationFromAddress forOrigin = new LocationFromAddress(this,this);
        forOrigin.executeOnSameThread(trip.getStart(),"origin");

        LocationFromAddress forDest = new LocationFromAddress(this,this);
        forDest.executeOnSameThread(trip.getDestination(),"dest");

        TextView dateTextView = (TextView) findViewById(R.id.date_tripView);
        TextView timeTextView = findViewById(R.id.trip_view_time);
        TextView startTextView = findViewById(R.id.trip_view_start);
        TextView destTextView = findViewById(R.id.trip_view_destination);
        TextView posted = findViewById(R.id.trip_view_posted);

        dateTextView.setText(trip.getDate());
        timeTextView.setText(trip.getTime());
        startTextView.setText(trip.getStart());
        destTextView.setText(trip.getDestination());
//        posted.setText(TimesAgo.covertTimeToText(trip.getTimePosted()));


        SpannableStringBuilder postedText = new SpannableStringBuilder("Posted by "+trip.getNamePosted()+" "+ TimesAgo.covertTimeToText(trip.getTimePosted()));
        postedText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                9, trip.getNamePosted().length()+9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        posted.setText(postedText);
    }

    @Override
    public void fetchLocationComplete(LatLng data, int status,String name) {

        if (name.equals("origin")){

            origin = data;
        }else {
            dest = data;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Log.e(TAG, "onMapReady: "+origin+" ||| "+dest);
        if (null!=origin) {
            mMap.addMarker(new MarkerOptions().position(origin).title("Trip Starts from here"));
        }
        if (null!=dest) {
            mMap.addMarker(new MarkerOptions().position(dest).title("Trip Ends here"));
        }


        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        SimpleRequest downloadTask = new SimpleRequest(this,url);

        // Start downloading json data from Google Directions API
        downloadTask.newRequestEmpty(this);

        Log.d(TAG, "onMapReady: "+url);
    }




    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode+"&key=AIzaSyDMTkQBXnO8FzQdIEkSU_suAa8TX9WTqlU";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    @Override
    public void onSuccessRequest(String result) {
        Log.d(TAG, "onSuccessRequest: "+result);
        ParserTask parserTask = new ParserTask();
        parserTask.execute(result);

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJsonParser parser = new DirectionsJsonParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if (null!=lineOptions) {
                mMap.addPolyline(lineOptions);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));
            }
        }
    }

}
