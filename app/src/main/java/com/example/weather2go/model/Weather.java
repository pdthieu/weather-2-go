package com.example.weather2go.model;

public class Weather {
    private int Timezone;
    private int ID;
    private String Name;
    private int Cod;

    public Coord coord = new Coord();
    public CurrentWeather currentWeather = new CurrentWeather();

    private String Base;

    public Main main = new Main();
    public Wind wind = new Wind();
    public Cloud cloud = new Cloud();

    private int Dt;

    public Sys sys = new Sys();

    public class Coord {
        private double Lat;
        private double Lon;

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public double getLon() {
            return Lon;
        }

        public void setLon(double lon) {
            Lon = lon;
        }
    }

    public class Main {
        private double Temp;
        private double FeelsLike;
        private double TempMin;
        private double TempMax;
        private int Pressure;
        private int Humidity;

        public double getTemp() {
            return Temp;
        }

        public void setTemp(double temp) {
            Temp = temp;
        }

        public double getFeelsLike() {
            return FeelsLike;
        }

        public void setFeelsLike(double feelsLike) {
            FeelsLike = feelsLike;
        }

        public double getTempMin() {
            return TempMin;
        }

        public void setTempMin(double tempMin) {
            TempMin = tempMin;
        }

        public double getTempMax() {
            return TempMax;
        }

        public void setTempMax(double tempMax) {
            TempMax = tempMax;
        }

        public int getPressure() {
            return Pressure;
        }

        public void setPressure(int pressure) {
            Pressure = pressure;
        }

        public int getHumidity() {
            return Humidity;
        }

        public void setHumidity(int humidity) {
            Humidity = humidity;
        }
    }


    public class Wind {
        private double Speed;
        private double Direction;

        public double getSpeed() {
            return Speed;
        }

        public void setSpeed(double speed) {
            Speed = speed;
        }

        public double getDirection() {
            return Direction;
        }

        public void setDirection(double direction) {
            Direction = direction;
        }
    }

    public class CurrentWeather {
        private int ID;
        private String Name;
        private String Description;
        private String Icon;

        public int getID() {
            return ID;
        }

        public void setID(int id) {
            ID = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description.substring(0, 1).toUpperCase() + description.substring(1);
        }

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String icon) {
            Icon = icon;
        }
    }

    public class Cloud {
        private int Clouds;

        public int getClouds() {
            return Clouds;
        }

        public void setClouds(int clouds) {
            Clouds = clouds;
        }
    }

    public class Sys {
        private int Type;
        private int ID;
        private double Message;
        private String Country;
        private int Sunrise;
        private int Sunset;

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public int getID() {
            return ID;
        }

        public void setID(int id) {
            ID = id;
        }

        public double getMessage() {
            return Message;
        }

        public void setMessage(double message) {
            Message = message;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public int getSunrise() {
            return Sunrise;
        }

        public void setSunrise(int sunrise) {
            Sunrise = sunrise;
        }

        public int getSunset() {
            return Sunset;
        }

        public void setSunset(int sunset) {
            Sunset = sunset;
        }
    }

    public int getTimezone() {
        return Timezone;
    }

    public void setTimezone(int timezone) {
        Timezone = timezone;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCod() {
        return Cod;
    }

    public void setCod(int cod) {
        Cod = cod;
    }

    public String getBase() {
        return Base;
    }

    public void setBase(String base) {
        Base = base;
    }

    public int getDt() {
        return Dt;
    }

    public void setDt(int dt) {
        Dt = dt;
    }
}
