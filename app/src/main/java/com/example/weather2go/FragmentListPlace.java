package com.example.weather2go;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weather2go.model.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FragmentListPlace extends Fragment {

    View view;

    private final ArrayList<String> mPlaceList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
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

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> places = (ArrayList<String>) document.get("places");
                        for (String p : places) {
                            mPlaceList.add(p);
                            Log.d("Place", p);
                        }

                    } else {
                        Log.d("Test", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }

                SwipeRefreshLayout srl = view.findViewById(R.id.swipe_refresh_layout);
                srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mAdapter.update();
                    }
                });

                mRecyclerView = view.findViewById(R.id.recyclerview);
                mEmptyView = view.findViewById(R.id.empty_view);
                mAdapter = new PlaceListAdapter(getActivity(), getContext(), mPlaceList, srl, mEmptyView);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }

    public PlaceListAdapter getAdapter() {
        return mAdapter;
    }
}