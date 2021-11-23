package com.example.weather2go;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather2go.model.Place;
import com.example.weather2go.model.Weather;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private final Activity mActivity;
    private final LayoutInflater mInflater;
    private final ArrayList<String> mPlaceList;
    private final ArrayList<String> visiblePlaceList;
    private final SwipeRefreshLayout mSwipeRefreshLayout;
    private final RecyclerView mRecyclerView;
    private final TextView mEmptyView;
    private String mFilter;

    private final FirebaseFirestore mDB;
    private final String mUid;


    public PlaceListAdapter(Activity activity, Context context, ArrayList<String> placeList, SwipeRefreshLayout swipeRefreshLayout, TextView emptyView) {
        mActivity = activity;
        mInflater = LayoutInflater.from(context);
        mPlaceList = placeList;
        java.util.Collections.sort(mPlaceList);

        visiblePlaceList = new ArrayList<>(mPlaceList);
        mSwipeRefreshLayout = swipeRefreshLayout;
        mRecyclerView = mSwipeRefreshLayout.findViewById(R.id.recyclerview);
        mEmptyView = emptyView;
        mFilter = "";

        mDB = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUid = user.getUid();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final PlaceListAdapter mAdapter;

        private TextView mName;
        private TextView mDateTime;
        private TextView mTemp;
        private TextView mHumidity;
        private TextView mCloud;
        private TextView mWind;
        private ImageView mIcon;
        private ImageView mHeart;

        public PlaceViewHolder(View itemView, PlaceListAdapter adapter) {
            super(itemView);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);

            mName = itemView.findViewById(R.id.place_name);
            mDateTime = itemView.findViewById(R.id.place_dt);
            mTemp = itemView.findViewById(R.id.place_temp);
            mHumidity = itemView.findViewById(R.id.place_humidity);
            mCloud = itemView.findViewById(R.id.place_cloud);
            mWind = itemView.findViewById(R.id.place_wind);
            mIcon = itemView.findViewById(R.id.weather_icon);
            mHeart = itemView.findViewById(R.id.place_heart);

            mHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = mHeart.getTag().toString();
                    if (tag.equals("white")) {
                        mHeart.setTag("red");
                        mHeart.setImageResource(R.drawable.heart_red);
                        mDB.collection("users").document(mUid).update("places", FieldValue.arrayUnion(mName.getText()));
                    }
                    else {
                        mHeart.setTag("white");
                        mHeart.setImageResource(R.drawable.heart);
                        mDB.collection("users").document(mUid).update("places", FieldValue.arrayRemove(mName.getText()));
                    }
                }
            });
        }

        public void bindPlace(Context context, String place) {
            mHeart.setTag("red");
            mHeart.setImageResource(R.drawable.heart_red);

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + place.replace(' ', '+') + "&appid=18a8967084c88b2a4b6e0e5045e5ac03";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Weather weather = null;
                            try {
                                weather = WeatherJSONParser.getWeather(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (weather == null) return;

                            // do something with weather
                            mName.setText(weather.getName());

                            Date date = new Date(weather.getDt() * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd " + "HH:mm");
                            mDateTime.setText(simpleDateFormat.format(date));

                            String temp = String.valueOf((int)weather.main.getTemp() - 273) + "°C";
                            mTemp.setText(temp);

                            String humidity = String.valueOf(weather.main.getHumidity()) + "%";
                            mHumidity.setText(humidity);

                            String cloud = String.valueOf(weather.cloud.getClouds()) + "%";
                            mCloud.setText(cloud);

                            String wind = String.format("%.1f", weather.wind.getSpeed()) + "m/s";
                            mWind.setText(wind);

                            try {
                                String icon = weather.currentWeather.getIcon();
                                Picasso.with(mIcon.getContext()).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(mIcon);
                            }
                            catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(stringRequest);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();

            // Use that to access the affected item in mWordList.
            String place = visiblePlaceList.get(mPosition);

            ViewPager viewPager = mActivity.findViewById(R.id.viewPager);
            viewPager.setCurrentItem(0);

            VPAdapter vpAdapter = (VPAdapter) viewPager.getAdapter();

            if (vpAdapter == null) {
                return;
            }


            MapsFragment map = (MapsFragment) vpAdapter.getItem(0);

            getLatLng(place, map);
//            map.focusOnLatLon(position.latitude, position.longitude, 15);
        }
    }

    private void getLatLng(String place, MapsFragment map) {
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        String tempUrl = "https://api.openweathermap.org/data/2.5/weather?q="+place+"&appid=18a8967084c88b2a4b6e0e5045e5ac03";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Weather weather = null;
                        try {
                            weather = WeatherJSONParser.getWeather(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (weather == null) {
                            Toast.makeText(mActivity, "Can't load weather here !",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        };

                        // print thong tin thoi tiet
                        double lat = weather.coord.getLat();
                        double lng = weather.coord.getLon();
                        map.addMarkerOnMap(lat, lng, place);
                        map.focusOnLatLon(lat, lng, 10);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mActivity, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(PlaceListAdapter.PlaceViewHolder holder, int position) {
        // Retrieve the data for that position.
        String mCurrent = visiblePlaceList.get(position);
        // Add the data to the view holder.
        holder.bindPlace(mInflater.getContext(), mCurrent);
    }

    @Override
    public int getItemCount() {
        return visiblePlaceList.size();
    }

    public void update() {

        final DocumentReference docRef = mDB.collection("users").document(mUid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> places = (ArrayList<String>) document.get("places");

                        mPlaceList.clear();
                        for (String p : places) {
                            mPlaceList.add(p);
                            Log.d("Place", p);
                        }
                        java.util.Collections.sort(mPlaceList);

                    } else {
                        Log.d("Test", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }

                filter(mFilter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void filter(String text) {
        mFilter = text;
        visiblePlaceList.clear();
        if (text.isEmpty()) {
            visiblePlaceList.addAll(mPlaceList);
        }
        else {
            text = text.toLowerCase();
            for (String place: mPlaceList) {
                if (place.toLowerCase().contains(text)) {
                    visiblePlaceList.add(place);
                }
            }
        }

        if (visiblePlaceList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        notifyDataSetChanged();
    }
}
