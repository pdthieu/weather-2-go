package com.example.weather2go;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather2go.model.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForecastWeather extends AppCompatActivity {
    TextView cityName;
    ImageView imgBack;
    ListView lv;
    CustomAdapterForecast customAdapterForecast;
    ArrayList<WeatherForecast> listWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_weather);

        anhXa();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        get7DaysData(name);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void get7DaysData(String data) {
        String url = "https://api.openweathermap.org/data/2" +
                ".5/forecast/daily?q="+data+"&units=metric&cnt=14&appid" +
                "=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(ForecastWeather.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                    String name = jsonObjectCity.getString("name");
                    cityName.setText(name);

                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    for(int i = 0; i < jsonArrayList.length(); i++) {
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String day = jsonObjectList.getString("dt");

                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\n " +
                                "yyyy-MM-dd", new Locale("en"));
                        String Day = simpleDateFormat.format(date);

                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                        String maxTemp = jsonObjectTemp.getString("max");
                        maxTemp = String.valueOf(Double.valueOf(maxTemp).intValue());
                        String minTemp = jsonObjectTemp.getString("min");
                        minTemp = String.valueOf(Double.valueOf(minTemp).intValue());

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String status = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");

                        listWeather.add(new WeatherForecast(Day, status, icon, maxTemp, minTemp));

                    }
                    customAdapterForecast.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForecastWeather.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void anhXa() {
        imgBack = (ImageView) findViewById(R.id.imageViewBack);
        cityName = (TextView) findViewById(R.id.cityName);
        lv = (ListView) findViewById(R.id.listView);
        listWeather = new ArrayList<WeatherForecast>();
        customAdapterForecast = new CustomAdapterForecast(ForecastWeather.this, listWeather);
        lv.setAdapter(customAdapterForecast);

    }
}