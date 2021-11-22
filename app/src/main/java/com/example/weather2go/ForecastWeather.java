package com.example.weather2go;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ForecastWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_weather);
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("name");
        System.out.println("-------------------" + cityName);
        get7DaysData(cityName);
    }

    private void get7DaysData(String cityname) {
        String url = "https://api.openweathermap.org/data/2" +
                ".5/forecast/daily?q=Hanoi&units=metric&cnt=7&appid" +
                "=53fbf527d52d4d773e828243b90c1f8e";
        System.out.println(url);
        RequestQueue requestQueue = Volley.newRequestQueue(ForecastWeather.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("logout:", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForecastWeather.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}