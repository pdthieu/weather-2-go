package com.example.weather2go;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weather2go.model.Place;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

        mPlaceList.get(1).setLat(13.958347);
        mPlaceList.get(1).setLon(107.9916);

        mPlaceList.get(2).setLat(16.8014069);
        mPlaceList.get(2).setLon(107.053141);

        mPlaceList.get(3).setLat(18.3543226);
        mPlaceList.get(3).setLon(105.8668108);


        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new PlaceListAdapter(this.getActivity(), this.getContext(), mPlaceList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        SwipeRefreshLayout srl = view.findViewById(R.id.swipe_refresh_layout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.update(srl);
            }
        });
    }
}