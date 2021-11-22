package com.example.weather2go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather2go.model.Weather;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap = null;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "18a8967084c88b2a4b6e0e5045e5ac03";
    private List<Marker> listMarker = new ArrayList<Marker>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        if (item != null) {
            item.setVisible(false);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        LatLng hcmus = new LatLng(10.762913,106.6821717);
        Marker marker = addMarkerOnMap(10.762913,106.6821717, "HCMUS");
        listMarker.add(marker);

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);

//        googleMap.addMarker(new MarkerOptions().position(hcmus).title("Marker in HCMUS"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hcmus));
        focusOnLatLon(10.762913, 106.6821717, 10);
    };

    public void focusOnLatLon(double lat, double lon, double zoom) {
        if (mMap == null) return;

        LatLng focus = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(focus));
    }

    private Marker addMarkerOnMap(double lat, double lng, String name) {
            LatLng position = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .draggable(true)
                    .visible(true);
            Marker marker = mMap.addMarker(markerOptions);
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

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Marker marker = addMarkerOnMap(latLng.latitude, latLng.longitude,
                "");
        for(Marker p : listMarker) {
            p.remove();
        }
        listMarker.clear();
        listMarker.add(marker);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;
        String tempUrl = url + "?lat=" + lat + "&lon=" + lng + "&appid=" + appid;
        System.out.println(tempUrl);
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
                            Toast.makeText(getActivity(), "Can't load weather here !",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        };

                        // print thong tin thoi tiet
                        String name = weather.getName();
                        int dt = weather.getDt();
                        int temp = (int) weather.main.getTemp() - 273;
                        marker.setTitle(name);
                        toggleBottomSheet(weather);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
        return false;
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

    public void toggleBottomSheet(Weather weather) {
        BottomSheet bottomSheet = BottomSheet.newInstance(weather);
        bottomSheet.show(getActivity().getSupportFragmentManager(), BottomSheet.TAG);
    }
}