package com.example.weather2go;

import androidx.annotation.FractionRes;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        DocumentReference docRef = db.collection("users").document(uid);

//        docRef.update("places", FieldValue.arrayRemove("Hanoi"));

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        List<String> places = (List<String>) document.get("places");
//                        for (String p : places) {
//                            Log.d("Place", p);
//                            System.out.println(p);
//                        }
//
//                    } else {
//                        Log.d("Test", "No such document");
//                    }
//                } else {
//                    Log.d("test", "get failed with ", task.getException());
//                }
//            }
//        });


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new MapsFragment(), "Maps");

        FragmentListPlace fragmentListPlace = new FragmentListPlace();
        vpAdapter.addFragment(fragmentListPlace, "Places");
        viewPager.setAdapter(vpAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fragmentListPlace.getAdapter().setFilter("");
                }
                else {
                    fragmentListPlace.getAdapter().update();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                        VPAdapter adapter = (VPAdapter) viewPager.getAdapter();
                        MapsFragment frag = (MapsFragment) adapter.getItem(0);
                        frag.search(query);
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

            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            default:
                // select an option

        }
        return super.onOptionsItemSelected(item);
    }
}
