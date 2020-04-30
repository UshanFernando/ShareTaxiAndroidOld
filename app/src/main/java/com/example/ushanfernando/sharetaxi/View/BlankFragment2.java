package com.example.ushanfernando.sharetaxi.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ushanfernando.sharetaxi.Control.FetchTrips;
import com.example.ushanfernando.sharetaxi.Model.Trip;
import com.example.ushanfernando.sharetaxi.R;

import java.util.List;

;

public class BlankFragment2 extends Fragment implements FetchTrips.OnFetchTripRequestComplete {
    List<String> slis;
    List<Trip> mTrips =null;
    TripAdapter adapter;

    public BlankFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        FetchTrips fetchTrips = new FetchTrips(getContext(), this);
        fetchTrips.fetch("st", 1);
//        slis = new ArrayList<>();

//        slis.add("test one");
//        slis.add("test 3ne");
//        slis.add("test o2");
//        slis.add("test o3");
//        slis.add("test o4e");


            adapter = new TripAdapter(mTrips);
            rv.setAdapter(adapter);



        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);



        return rootView;
    }

    @Override
    public void onFetchTripRequestComplete(List<Trip> trips) {
        mTrips = trips;
        adapter.loadNewData(mTrips);
    }

}