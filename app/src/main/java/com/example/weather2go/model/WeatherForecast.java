package com.example.weather2go.model;

public class WeatherForecast {
    public String day;
    public String status;
    public String image;
    public String maxTemp;
    public String minTemp;

    public WeatherForecast(String day, String status, String image, String maxTemp, String minTemp) {
        this.day = day;
        this.status = status;
        this.image = image;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}
