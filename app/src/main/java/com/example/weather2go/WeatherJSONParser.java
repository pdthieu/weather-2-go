package com.example.weather2go;

import com.example.weather2go.model.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherJSONParser {

    private static final String TAG = "WeatherJSONParser";

    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        JSONObject weatherData = new JSONObject(data);

        if (weatherData.has("timezone")) {
            weather.setTimezone(weatherData.getInt("timezone"));
        }

        if (weatherData.has("id")) {
            weather.setID(weatherData.getInt("id"));
        }

        if (weatherData.has("name")) {
            weather.setName(weatherData.getString("name"));
        }

        if (weatherData.has("cod")) {
            weather.setCod(weatherData.getInt("cod"));
        }

        if (weatherData.has("coord")) {
            JSONObject coordObj = weatherData.getJSONObject("coord");

            if (coordObj.has("lat")) {
                weather.coord.setLat(coordObj.getDouble("lat"));
            }
            if (coordObj.has("lon")) {
                weather.coord.setLon(coordObj.getDouble("lon"));
            }
        }

        if (weatherData.has("weather")) {
            JSONArray weatherArray = weatherData.getJSONArray("weather");

            JSONObject weatherObj = weatherArray.getJSONObject(0);
            if (weatherObj.has("id")) {
                weather.currentWeather.setID(weatherObj.getInt("id"));
            }
            if (weatherObj.has("name")) {
                weather.currentWeather.setName(weatherObj.getString("name"));
            }
            if (weatherObj.has("description")) {
                weather.currentWeather.setDescription(weatherObj.getString("description"));
            }
            if (weatherObj.has("icon")) {
                weather.currentWeather.setIcon(weatherObj.getString("icon"));
            }
        }

        if (weatherData.has("base")) {
            weather.setBase(weatherData.getString("base"));
        }

        if (weatherData.has("main")) {
            JSONObject mainObj = weatherData.getJSONObject("main");

            if (mainObj.has("temp")) {
                weather.main.setTemp(mainObj.getDouble("temp"));
            }
            if (mainObj.has("feels_like")) {
                weather.main.setFeelsLike(mainObj.getDouble("feels_like"));
            }
            if (mainObj.has("temp_min")) {
                weather.main.setTempMin(mainObj.getDouble("temp_min"));
            }
            if (mainObj.has("temp_max")) {
                weather.main.setTempMax(mainObj.getDouble("temp_max"));
            }
            if (mainObj.has("pressure")) {
                weather.main.setPressure(mainObj.getInt("pressure"));
            }
            if (mainObj.has("humidity")) {
                weather.main.setHumidity(mainObj.getInt("humidity"));
            }
        }

        if (weatherData.has("wind")) {
            JSONObject windObj = weatherData.getJSONObject("wind");

            if (windObj.has("speed")) {
                weather.wind.setSpeed(windObj.getDouble("speed"));
            }
            if (windObj.has("deg")) {
                weather.wind.setDirection(windObj.getDouble("deg"));
            }
        }

        if (weatherData.has("clouds")) {
            JSONObject cloudsObj = weatherData.getJSONObject("clouds");

            if (cloudsObj.has("all")) {
                weather.cloud.setClouds(cloudsObj.getInt("all"));
            }
        }

        if (weatherData.has("dt")) {
            weather.setDt(weatherData.getInt("dt"));
        }

        if (weatherData.has("sys")) {
            JSONObject sysObj = weatherData.getJSONObject("sys");

            if (sysObj.has("type")) {
                weather.sys.setType(sysObj.getInt("type"));
            }
            if (sysObj.has("id")) {
                weather.sys.setID(sysObj.getInt("id"));
            }
            if (sysObj.has("message")) {
                weather.sys.setMessage(sysObj.getDouble("message"));
            }
            if (sysObj.has("country")) {
                weather.sys.setCountry(sysObj.getString("country"));
            }
            if (sysObj.has("sunrise")) {
                weather.sys.setSunrise(sysObj.getInt("sunrise"));
            }
            if (sysObj.has("sunset")) {
                weather.sys.setSunset(sysObj.getInt("sunset"));
            }
        }

        return weather;
    }
}
