package com.example.ushanfernando.sharetaxi.View;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ushanfernando.sharetaxi.Model.Trip;
import com.example.ushanfernando.sharetaxi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URI;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.RecycleViewHolder> {


    Context mContext;
    List<Trip> mTrips;
    List<String> mTest;
    private static final String TAG = "RecycleViewArrayAdapter";

    public TripAdapter(List<Trip> trips) {
//        mContext = context;
        mTrips = trips;

    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_trip, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_layout, viewGroup, false);
        }
        mContext = view.getContext();
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder recycleViewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: called");
        if (getItemViewType(i) != 1) {
            Trip tripCurrent = mTrips.get(i);
            String looking = recycleViewHolder.itemView.getContext().getString(R.string.looking_for);
            if (tripCurrent.getType().equals("fp")) {
                looking = recycleViewHolder.itemView.getContext().getString(R.string.looking_for_pas);
            } else if (tripCurrent.getType().equals("st")) {
//                recycleViewHolder.mLooking.setText(R.string.looking_for_taxi);

                looking = recycleViewHolder.itemView.getContext().getString(R.string.looking_for_taxi);
            }
           SpannableStringBuilder name = new SpannableStringBuilder(tripCurrent.getNamePosted() + " " + looking);
            name.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                  0, tripCurrent.getNamePosted().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            recycleViewHolder.mName.setText(name);
            recycleViewHolder.mFrom.setText(tripCurrent.getStart());
            recycleViewHolder.mTo.setText(tripCurrent.getDestination());
            recycleViewHolder.mOn.setText(tripCurrent.getDate());
            recycleViewHolder.mAt.setText(tripCurrent.getTime());
//            recycleViewHolder.bindView(tripCurrent.getStartLatLng());
//            Uri.Builder builder = new Uri.Builder();
//            builder.scheme("https")
//                    .authority("maps.googleapis.com")
//                    .appendPath("maps")
//                    .appendPath("api")
//                    .appendPath("staticmap")
//                    .appendQueryParameter("size", "512x212")
//                    .appendQueryParameter("maptype", "roadmap")
//                    .appendQueryParameter("markers", "size:mid%7Ccolor:red%7C"+tripCurrent.getStart())
//                    .appendQueryParameter("key", "AIzaSyCZZixip_wFMiGvO1G2HYjeNOjmtBP5RZM");

//            String url = builder.build().toString();
            String imgUrl = "https://maps.googleapis.com/maps/api/staticmap?size=512x212&maptype=roadmap\\&markers=size:mid%7Ccolor:red%7C"+tripCurrent.getStart()+",CA%7C"+tripCurrent.getDestination()+"&key=AIzaSyCZZixip_wFMiGvO1G2HYjeNOjmtBP5RZM";
            Log.i(TAG, "onBindViewHolder: Url Created : "+imgUrl);
            Glide.with(mContext).load(imgUrl).into(recycleViewHolder.mStaticMapView);

            Log.i(TAG, "onBindViewHolder: map called"+tripCurrent.getStart());
        }
//        Log.d(TAG, "onBindViewHolder: set item recy : " + i);
    }


    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: items" + mTest.size());
        return (mTrips != null && mTrips.size() != 0) ? (mTrips.size()) : 1;
//        return mTrips.size();

    }

    public void loadNewData(List<Trip> trips) {
        if (null!=mTrips) {
            mTrips.addAll(trips);
        } else {
            mTrips = trips;
        }

       notifyDataSetChanged();

        Log.e(TAG, "loadNewData: called");
    }

    public Trip getTrip(int position) {
        return (mTrips != null && mTrips.size() != 0 ? mTrips.get(position) : null);
    }

    static class RecycleViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mFrom;
        TextView mTo;
        TextView mOn;
        TextView mAt;
        TextView mPosted;
        TextView mLooking;
        MapView mMapView;
       ImageView mStaticMapView;
        View layout;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
//            mMapView = itemView.findViewById(R.id.mapView);
            mStaticMapView = itemView.findViewById(R.id.staticMapView);
            layout = itemView;
            mName = itemView.findViewById(R.id.trip_name);
            mFrom = itemView.findViewById(R.id.trip_from);
            mAt = itemView.findViewById(R.id.trip_at);
            mTo = itemView.findViewById(R.id.trip_to);
            mOn = itemView.findViewById(R.id.trip_on);
            mPosted = itemView.findViewById(R.id.trip_posted);
            mLooking = itemView.findViewById(R.id.islooking);



        }

            private void bindView(LatLng location) {


                if (null!=location){

                }



        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    private static class NamedLocation {

        public final String name;
        public final LatLng location;

        NamedLocation(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }
    }


}

