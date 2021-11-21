package com.example.weather2go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private final String url = "https://api.openweathermap.org/data/2.5/find";
    private final String appid = "18a8967084c88b2a4b6e0e5045e5ac03";

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng hcmus = new LatLng(10.762913,106.6821717);
        addMarkerOnMap(10.762913,106.6821717, "HCMUS");

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.setOnMarkerDragListener(this);

        mMap.setOnInfoWindowClickListener(this);


//        googleMap.addMarker(new MarkerOptions().position(hcmus).title("Marker in HCMUS"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hcmus));
    };

    private Marker addMarkerOnMap(double lat, double lng, String name) {
            LatLng position = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(name)
                    .draggable(true)
                    .visible(true);
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag("https://www.hcmus.edu.vn");
            return marker;
        };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private int nextAvailableID = 1;
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Marker marker = addMarkerOnMap(latLng.latitude, latLng.longitude,
                String.valueOf(nextAvailableID++));
        marker.setTitle(marker.getPosition().toString());
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
//        System.out.println(marker.getPosition());
        marker.setTitle("Here");
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;
        String tempUrl = url + "?lat=" + lat + "&lon=" + lng + "&cnt=1&appid=" + appid;
        System.out.println(tempUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // lay 1 object weather
                    JSONObject jsonObject =
                            new JSONObject(response).getJSONArray("list").getJSONObject(0);
                    //
                    // --- lay thong tin thoi tiet ----
                    String name = jsonObject.getString("name");
                    String day = jsonObject.getString("dt");
                    long l = Long.valueOf();
                    Date date = new Date(l*1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd " +
                            "HH-mm-ss");
                    day = simpleDateFormat.format(date);





                    // Hien thi thong tin thoi tiet
                    Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        marker.remove();
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }
}