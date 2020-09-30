package com.example.weatherapp.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weatherapp.model.City;


@Database(entities = {City.class}, version = 1)
public abstract class SQLWeatherDatabase extends RoomDatabase {

    public abstract RequestDao getRequestDao();
}
