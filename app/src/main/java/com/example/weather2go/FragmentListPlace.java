package com.example.weather2go;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weather2go.model.Place;

import java.util.ArrayList;

public class FragmentListPlace extends Fragment {

    View view;

    private final ArrayList<Place> mPlaceList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private PlaceListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_place, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: get favor place list from firebase

        mPlaceList.add(new Place());
        mPlaceList.add(new Place());
        mPlaceList.add(new Place());
        mPlaceList.add(new Place());
        mPlaceList.add(new Place());

        mPlaceList.get(0).setLat(10.762913);
        mPlaceList.get(0).setLon(106.6821717);

        mPlaceList.get(1).setLat(13.7097838);
        mPlaceList.get(1).setLon(107.6475153);

        mPlaceList.get(2).setLat(16.7356687);
        mPlaceList.get(2).setLon(106.6722456);

        mPlaceList.get(3).setLat(18.3607329);
        mPlaceList.get(3).setLon(105.5240866);

        mPlaceList.get(4).setLat(20.9740874);
        mPlaceList.get(4).setLon(105.3724892);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new PlaceListAdapter(this.getContext(), mPlaceList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}