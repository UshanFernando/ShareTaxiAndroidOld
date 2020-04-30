package com.example.ushanfernando.sharetaxi.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ushanfernando.sharetaxi.EndlessRecyclerViewScrollListener;
import com.example.ushanfernando.sharetaxi.Control.FetchTrips;
import com.example.ushanfernando.sharetaxi.Control.FindMatch;
import com.example.ushanfernando.sharetaxi.Model.Trip;
import com.example.ushanfernando.sharetaxi.R;
import com.example.ushanfernando.sharetaxi.RecyclerItemClickListener;
import com.example.ushanfernando.sharetaxi.Model.TripView;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;


public class BlankFragment extends Fragment implements FetchTrips.OnFetchTripRequestComplete {
    List<String> slis;
    List<Trip> mTrips = null;
    TripAdapter adapter;
    RecyclerView rv;
    LinearLayoutManager llm;

    private EndlessRecyclerViewScrollListener scrollListener;
    private static final String TAG = "BlankFragment";

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private RecyclerView.RecyclerListener mRecycleListener = new RecyclerView.RecyclerListener() {

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            TripAdapter.RecycleViewHolder mapHolder = (TripAdapter.RecycleViewHolder) holder;

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        FetchTrips fetchTrips = new FetchTrips(getContext(), this);
        fetchTrips.fetch("fp", 1);

        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setRecyclerListener(mRecycleListener);

        return rootView;
    }

//        private void loadMoreData(int page) {
//        FetchTrips fetchTrips = new FetchTrips(getContext(), this);
//        fetchTrips.fetch("fp", page);
//        Log.d(TAG, "loadNextDataFromApi: called");
//    }
    public void loadNextDataFromApi(int offset) {

//        callYourURL();  // and on success response set loadingMore=false and add your data in in last position in your arraylist
        FetchTrips fetchTrips = new FetchTrips(getContext(), this);
        fetchTrips.fetch("fp", offset);
        Log.i(TAG, "loadMoreData: called" + offset);
    }

    @Override
    public void onFetchTripRequestComplete(List<Trip> trips) {
        Log.d(TAG, "onFetchTripRequestComplete: ");
        mTrips = trips;
        // There is no need to use notifyDataSetChanged()
        adapter.loadNewData(mTrips);
        rv.invalidate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TripAdapter(mTrips);
        rv.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
//         Adds the scroll listener to RecyclerView
        rv.addOnScrollListener(scrollListener);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent;
                        if(position == 0 ){
                            intent = new Intent(getContext(), FindMatch.class);
                        }else{
                            Toast.makeText(getContext(), "clicked" + position, Toast.LENGTH_SHORT).show();
                            intent = new Intent(getContext(), TripView.class);
                            intent.putExtra("trip",adapter.getTrip(position));
//                            String uri = "http://maps.google.com/maps?saddr=" + adapter.getTrip(position).getStart()+"&daddr="+adapter.getTrip(position).getDestination();
//                            Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//                            intent1.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                            startActivity(intent1);
                        }

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(getContext(), "Long clicked" + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }
}