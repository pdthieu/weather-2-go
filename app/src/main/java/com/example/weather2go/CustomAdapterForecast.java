package com.example.weather2go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather2go.model.WeatherForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterForecast extends BaseAdapter {
    Context context;
    ArrayList<WeatherForecast> arrayList;

    public CustomAdapterForecast(Context context, ArrayList<WeatherForecast> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_listview, null);

        WeatherForecast weatherForecast = arrayList.get(i);

        TextView txtDay = (TextView) view.findViewById(R.id.textViewDay);
        TextView txtStatus = (TextView) view.findViewById(R.id.textViewStatus);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textviewMaxTemp);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textviewMinTemp);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imageviewStatus);

        txtDay.setText(weatherForecast.day);
        txtStatus.setText(weatherForecast.status);
        txtMaxTemp.setText(weatherForecast.maxTemp+"°C");
        txtMinTemp.setText(weatherForecast.minTemp+"°C");

        Picasso.with(context).load("https://openweathermap.org/img/wn/" + weatherForecast.image +
                "@2x.png").into(imgStatus);

        return view;
    }
}
