package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather.BuildConfig;
import com.example.weather.R;
import com.example.weatherapp.HttpWeather;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.adapters.WeatherStoryAdapter;
import com.example.weatherapp.model.WeatherStory;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static final String WEATHER_STORY_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s";
    private HttpWeather httpClient;
    Handler handler;
    String mCity;

    TextView textViewHistoryCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        httpClient = new HttpWeather();
        handler = new Handler();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCity = bundle.getString("city");
            loadWeatherStory(mCity);
        } else {
            Log.e("cityHistory", "Ошибка");
        }

        textViewHistoryCity = (TextView) view.findViewById(R.id.textViewHistoryCity);
        textViewHistoryCity.setText(mCity);

        return view;
    }

    private void loadWeatherStory(final String mCity) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = String.format(WEATHER_STORY_URL,
                        mCity, BuildConfig.WEATHER_API_KEY);
                try {
                    final WeatherStory weatherRequest = httpClient.getWeatherStory(url);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            displayWeatherStory(weatherRequest);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void displayWeatherStory(WeatherStory weatherStory) {

        if (weatherStory.getCod() == 200) {

            ArrayList<String[]> story = new ArrayList<>();
            String[] temp = new String[6];

            for (int i = 0; i < weatherStory.getList().length; i++) {
                temp[0] = String.valueOf(weatherStory.getList()[i].getDt());
                temp[1] = weatherStory.getList()[i].getMain().getTempCels() + " °С";
                temp[2] = weatherStory.getList()[i].getMain().getPressure() + " mmHg";
                temp[3] = weatherStory.getList()[i].getMain().getHumidity() + " %";
                temp[4] = String.valueOf(weatherStory.getList()[i].getWeather()[0].getMain());
                temp[5] = weatherStory.getList()[i].getWind().getSpeed() + " m/c";
                if (i % 8 == 1) {
                    story.add(temp);
                }
                temp = new String[temp.length];
            }

            WeatherStoryAdapter adapter = new WeatherStoryAdapter(getContext(), story);
            ListView listViewStory = (ListView) getView().findViewById(R.id.listViewHistory);
            listViewStory.setAdapter(adapter);
        } else {
            ((MainActivity) getActivity()).onClickDialogBuilder(getView());
        }
    }
}