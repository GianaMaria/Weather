package com.example.weatherapp.model;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class City {
    private String nameCity;
    private long date;

    public String getNameCity() {
        return nameCity;
    }

    public long getDate() {
        return date;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateFormat() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(this.date);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public City(String nameCity, long date) {
        this.nameCity = nameCity;
        this.date = date;
    }
}
