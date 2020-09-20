package com.example.weatherapp.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerWeatherAPIGenerator {

    private static String url = "http://api.openweathermap.org/data/2.5/";

    public static ServerAPI createServer() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI serverAPI = retrofit.create(ServerAPI.class);

        return serverAPI;
    }
}
