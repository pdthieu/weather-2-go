package com.example.weather2go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleMap mMap;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "18a8967084c88b2a4b6e0e5045e5ac03";
    private List<Marker> listMarker = new ArrayList<Marker>();
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    Marker mCurrLocationMarker;
    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.action_search);
//        if (item != null) {
//            item.setVisible(false);
//        }
//    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMarkerCommon();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When permission grated
            // Call method
            Log.e("kiemtra", "---------------------------");
            getCurrentLocation();
        } else {
            // When permission denied
            // Request permission
            Log.e("kiemtra2", "---------------------------");
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    public void search(String location) {

        // below line is to create a list of address
        // where we will store the list of all address.
        List<Address> addressList = null;

        // checking if the entered location is null or not.
        if (location != null && !location.isEmpty()) {
            // on below line we are creating and initializing a geo coder.
            Geocoder geocoder = new Geocoder(mView.getContext());
            try {
                // on below line we are getting location from the
                // location name and adding that location to address list.
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // on below line we are getting the location
            // from our list a first position.
            if (addressList == null || addressList.isEmpty()) {
                return;
            }
            Address address = addressList.get(0);

            // on below line we are creating a variable for our location
            // where we will add our locations latitude and longitude.
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            // on below line we are adding marker to that position.
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));

            // below line is to animate camera to that position.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }

    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // When success
                Log.e("success", "----------------------------------------");
                if (location != null) {
                    // Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            // Initialize lat lng
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            Log.e("curent", "--------------------" + latLng);
                            // Add marker
                            Marker marker = addMarkerOnMap(location.getLatitude(),
                                    location.getLongitude(), "You're here");
                            listMarker.add(marker);
                        }
                    });
                }
            }
        });
    }
     //   focusOnLatLon(10.762913, 106.6821717, 10);

    public void focusOnLatLon(double lat, double lon, double zoom) {
        if (mMap == null) return;

        LatLng focus = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(focus));
    }

    public Marker addMarkerOnMap(double lat, double lng, String name) {
            LatLng position = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .draggable(true)
                    .visible(true)
                    .title(name);
            if (mMap == null) return null;
            Marker marker = mMap.addMarker(markerOptions);
            marker.showInfoWindow();
            for(Marker p : listMarker) {
                p.remove();
            }
            listMarker.clear();
            listMarker.add(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 6));
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
         mView = view;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    private void setMarkerCommon() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DocumentReference docRef = db.collection("common").document("markers");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        ArrayList<Number> lat = (ArrayList<Number>) document.get("lat");
                        ArrayList<Number> lon = (ArrayList<Number>) document.get("lon");
                        ArrayList<String> name = (ArrayList<String>) document.get("name");
                        for (int i = 0; i < lat.size(); ++i) {
                            double llat = lat.get(i).doubleValue();
                            double llon = lon.get(i).doubleValue();
                            Log.e("ok", llat + " " + llon);
                            LatLng position = new LatLng(llat, llon);
                            Log.e("ok", position.toString());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(position)
                                    .draggable(false)
                                    .visible(true)
                                    .title(name.get(i))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                            mMap.addMarker(markerOptions);
                        }
                    } else {
                        Log.d("Test", "No such document");
                    }
                } else {
                    Log.d("test", "get failed with ", task.getException());
                }

            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Marker marker = addMarkerOnMap(latLng.latitude, latLng.longitude,
                "");

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        String title = marker.getTitle();
        if (!title.equals("Hanoi") && !title.equals("Ho Chi Minh City") && !title.equals("Hue") && !title.equals("Yen Vinh")) {
            marker.remove();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("kiemtra4", "---------------------------" + requestCode);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // When permission grated
                // call method
                Log.e("kiemtra3", "-------------------------");
                getCurrentLocation();
            }
        }
    }
}