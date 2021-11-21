package com.example.weather2go.model;

public class WeatherEntity {
    private int id;
    private String name;
    private CoordEntity coord;
    private MainEntity main;
    private int dt;
    private WindEntity wind;
    private SysEntity sys;
    private RainEntity rain;
    private SnowEntity snow;
    private CloudsEntity clouds;
    private Weather weather;

    public int getId() { return id; }
    public String getName() { return name; }
    public CoordEntity getCoord() { return coord;}
    public MainEntity getMain() { return main;}
    public int getDt() { return dt;}
    public WindEntity getWind() { return wind;}
    public SysEntity getSys() { return sys;}
    public RainEntity getRain() { return rain;}
    public SnowEntity getSnow() { return snow;}
    public CloudsEntity getClouds() { return clouds;}
    public Weather getWeather() { return weather;}
}

class CoordEntity {
    private float lat;
    private float lon;

    public float getLat() { return lat; }
    public float getLon() { return lon; }
}

class MainEntity {
    private float temp;
    private float feelsLike;
    private float tempMin;
    private float tempMax;
    private float pressure;
    private float humidity;

    public float getFeelsLike() { return feelsLike;}
    public float getHumidity() { return humidity; }
    public float getPressure() { return pressure;}
    public float getTemp() { return temp; }
    public float getTempMax() { return tempMax; }
    public float getTempMin() { return tempMin; }
}

class WindEntity {
    private float speed;
    private int deg;

    public float getSpeed() { return speed; }
    public int getDeg() { return deg; }
}

class SysEntity {
    private String country;

    public String getCountry() { return country; }
}

class RainEntity { }

class SnowEntity { }

class CloudsEntity {
    private int all;

    public int getAll() { return all; }
}

class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public String getMain() { return main; }
}
