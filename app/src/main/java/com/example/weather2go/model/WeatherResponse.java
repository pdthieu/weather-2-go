package com.example.weather2go.model;

import java.io.Serializable;
import java.util.List;

public class WeatherResponse implements Serializable {
    private String message;
    private String cod;
    private int count;
    private List<WeatherEntity> list;
}
