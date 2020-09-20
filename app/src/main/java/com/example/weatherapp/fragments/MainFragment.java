package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weather.BuildConfig;
import com.example.weather.R;
import com.example.weatherapp.HttpWeather;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.Parcel;
import com.example.weatherapp.model.WeatherRequest;
import com.example.weatherapp.util.ServerAPI;
import com.example.weatherapp.util.ServerWeatherAPIGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    private static ServerWeatherAPIGenerator serverWeatherAPIGenerator;
    private HttpWeather httpClient;

    Handler handler;

    EditText city;
    EditText temperature;
    EditText windSPD;
    EditText forecast;

    Button buttonRefresh;
    Button buttonHistory;
    String mCity;

    private static final String TAG = "WEATHER";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        init(view);

        loadWeather();
        buttonSetClick();

        return view;
    }


    private void buttonSetClick() {

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWeather();
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HistoryFragment historyFragment = new HistoryFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, historyFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();

                Bundle args = new Bundle();
                args.putString("city", mCity);
                historyFragment.setArguments(args);

            }
        });
    }

    private void loadWeather() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                ServerAPI serverAPI = serverWeatherAPIGenerator.createServer();

                Call<WeatherRequest> weatherRequestRet = serverAPI.listWeather(mCity, BuildConfig.WEATHER_API_KEY);

                final Response<WeatherRequest> response;

                try {
                    response = weatherRequestRet.execute();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            displayWeather(response.body());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void displayWeather(WeatherRequest weatherRequest) {

        if (weatherRequest.getCod() == 200) {

            city.setText(weatherRequest.getName());
            temperature.setText(String.format(Locale.getDefault(), "%s °С", (int) weatherRequest.getMain().getTemp() - 273));
            windSPD.setText(String.format(Locale.getDefault(), "%s m/c", weatherRequest.getWind().getSpeed()));
            forecast.setText(weatherRequest.getWeather()[0].getDescription());

        } else {
            ((MainActivity) getActivity()).onClickDialogBuilder(getView());
        }

    }

    private void init(View view) {
        buttonRefresh = (Button) view.findViewById(R.id.buttonRefresh);
        buttonHistory = (Button) view.findViewById(R.id.buttonForecast5);

        city = view.findViewById(R.id.textCity);
        temperature = view.findViewById(R.id.textTemper);
        windSPD = view.findViewById(R.id.textWind);
        forecast = view.findViewById(R.id.textForecast);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Parcel parcel = (Parcel) bundle.getSerializable("city");
            mCity = parcel.getCityName();
        } else {
            mCity = "Moscow";

        }

        handler = new Handler();
        httpClient = new HttpWeather();
    }
}