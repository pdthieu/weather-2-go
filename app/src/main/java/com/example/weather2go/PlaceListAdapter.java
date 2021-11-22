package com.example.weather2go;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather2go.model.Place;
import com.example.weather2go.model.Weather;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private final LayoutInflater mInflater;
    private final ArrayList<Place> mPlaceList;


    public PlaceListAdapter(Context context, ArrayList<Place> placeList) {
        mInflater = LayoutInflater.from(context);
        mPlaceList = placeList;
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
        }

        public void bindPlace(Context context, Place place) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + place.getLat() + "&lon=" + place.getLon() + "&appid=18a8967084c88b2a4b6e0e5045e5ac03";

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
                                URL url = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                mIcon.setImageBitmap(bmp);
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
//            int mPosition = getLayoutPosition();
//
//            // Use that to access the affected item in mWordList.
//            String element = mWordList.get(mPosition);
//            // Change the word in the mWordList.
//
//            mWordList.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(PlaceListAdapter.PlaceViewHolder holder, int position) {
        // Retrieve the data for that position.
        Place mCurrent = mPlaceList.get(position);
        // Add the data to the view holder.
        holder.bindPlace(mInflater.getContext(), mCurrent);
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }
}
