package com.example.weatherapp;

import java.io.Serializable;

public class Parcel implements Serializable {
    private int TemperIndex;
    private String cityName;

    public int getTemperIndex() {
        return TemperIndex;
    }

    public String getCityName() {
        return cityName;
    }

    public Parcel(int temperIndex, String cityName) {
        TemperIndex = temperIndex;
        this.cityName = cityName;
    }
}
