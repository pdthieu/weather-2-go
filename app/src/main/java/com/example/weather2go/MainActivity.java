package com.example.weather2go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//        String TAG = "TEST";
//
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new MapsFragment(), "Maps");
        vpAdapter.addFragment(new FragmentListPlace(), "Places");
        viewPager.setAdapter(vpAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            final VPAdapter vpAdapter = (VPAdapter)viewPager.getAdapter();
            final FragmentListPlace fragmentListPlace = (FragmentListPlace) vpAdapter.getItem(1);
            PlaceListAdapter placeListAdapter = fragmentListPlace.getAdapter();

            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        // TODO: map search logic implement here
                        break;
                    case 1:
                        placeListAdapter.filter(query);
                        break;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        // TODO: map search logic implement here
                        break;
                    case 1:
                        placeListAdapter.filter(newText);
                        break;
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_setting:
                //settings

                Intent settings = new Intent(this, AccountActivity.class);
                startActivity(settings);
                break;

            default:
                // select an option

        }
        return super.onOptionsItemSelected(item);
    }
}
