package com.example.weather2go;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather2go.model.Weather;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BottomSheet extends BottomSheetDialogFragment {

    TextView txtName, txtStatus, txtCountry, txtTemp, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;
    Button btnForecast;
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
//        Anhxa();
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
        NhapThongTin();
        btnForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForecastWeather.class);
                intent.putExtra("name", cityName);
                startActivity(intent);
            }
        });
    }

    private void NhapThongTin() {
        try {
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
                    "HH:mm:ss", new Locale("en"));
            String dday = simpleDateFormat.format(date);
            txtDay.setText(dday);
//
            int temp = (int) weather.main.getTemp() - 273;
            txtTemp.setText(String.valueOf(temp) + "°C");
//
            String icon = weather.currentWeather.getIcon();
            URL url = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
            Picasso.with(imgIcon.getContext()).load("https://openweathermap.org/img/wn/" + icon +
                    ".png").into(imgIcon);
            System.out.println(url);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}