package com.example.weather2go;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather2go.model.Weather;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomSheet extends BottomSheetDialogFragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txtName, txtStatus, txtCountry, txtTemp, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon, heart;
    Button btnForecast;
    CheckBox checkBox;
    private String cityName = "";

    private Weather weather;

    public static final String TAG = "BottomSheet";

    public static BottomSheet newInstance(Weather input) {
        return new BottomSheet(input);
    }

    public BottomSheet(Weather input) {
        weather = input;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        NhapThongTin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = (TextView) view.findViewById(R.id.textViewCityName);
        txtCountry = (TextView) view.findViewById(R.id.textViewCountry);
        txtCloud = (TextView) view.findViewById(R.id.textViewCloud);
        txtStatus = (TextView) view.findViewById(R.id.textViewStatus);
        txtHumidity = (TextView) view.findViewById(R.id.textViewHumidity);
        txtWind = (TextView) view.findViewById(R.id.textViewWind);
        txtDay = (TextView) view.findViewById(R.id.textViewDay);
        txtTemp = (TextView) view.findViewById(R.id.textViewTemp);
        imgIcon = (ImageView) view.findViewById(R.id.imageIcon);
        btnForecast = (Button) view.findViewById(R.id.buttonForecast);
        heart = (ImageView) view.findViewById(R.id.heart);
        NhapThongTin();
        btnForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForecastWeather.class);
                intent.putExtra("name", cityName);
                startActivity(intent);
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = heart.getTag().toString();
                if (tag.equals("white")) {
                    heart.setTag("red");
                    heart.setImageResource(R.drawable.heart_red);
                    db.collection("users").document(auth.getUid()).update("places",
                            FieldValue.arrayUnion(cityName));
                }
                else {
                    heart.setTag("white");
                    heart.setImageResource(R.drawable.heart);
                    db.collection("users").document(auth.getUid()).update("places",
                            FieldValue.arrayRemove(cityName));
                }
            }
        });
    }

    private List<String> getData() {
        List<String> city = new ArrayList<String>();
        String uid = auth.getUid();
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> places = (List<String>) document.get("places");
                        for (String p : places) {
                            Log.e("Place", p);
                            city.add(p);
                            if (p.toString().equals(cityName.toString())) {
//                                checkBox.setChecked(true);
                                heart.setTag("red");
                                heart.setImageResource(R.drawable.heart_red);
                            }
                        }

                    } else {
                        Log.e("Test", "No such document");
                    }
                } else {
                    Log.e("test", "get failed with ", task.getException());
                }
            }
        });
        return city;
    }

    private void NhapThongTin() {
        try {
            getData();
            String name = weather.getName();
            txtName.setText("Cityname: " + name);
            cityName = name;

            String country = weather.sys.getCountry();
            txtCountry.setText("Country: " + country);

            int cloud = weather.cloud.getClouds();
            txtCloud.setText(String.valueOf(cloud)+"%");

            String status = weather.currentWeather.getDescription();
            txtStatus.setText(status);
//
            int humidity = weather.main.getHumidity();
            txtHumidity.setText(String.valueOf(humidity) +"%");

            int wind = (int) weather.wind.getSpeed();
            txtWind.setText(String.valueOf(wind)+"m/s");
//
            int day = weather.getDt();
            long l = Long.valueOf(day);
            Date date = new Date(l * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd " +
                    "HH:mm", new Locale("en"));
            String Day = simpleDateFormat.format(date);
            txtDay.setText(Day);
//
            int temp = (int) weather.main.getTemp() - 273;
            txtTemp.setText(String.valueOf(temp) + "°C");
//
            String icon = weather.currentWeather.getIcon();
            Picasso.with(imgIcon.getContext()).load("https://openweathermap.org/img/wn/" + icon +
                    "@2x.png").into(imgIcon);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}