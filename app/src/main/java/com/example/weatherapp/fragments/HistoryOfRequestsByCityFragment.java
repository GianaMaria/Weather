package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.weather.R;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.adapters.HistoryOfRequestsByCityAdapter;

public class HistoryOfRequestsByCityFragment extends Fragment {

    ListView ListViewRequestDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_of_requests_by_city, container, false);

        HistoryOfRequestsByCityAdapter historyOfRequestsByCityAdapter =
                new HistoryOfRequestsByCityAdapter(getContext(), ((MainActivity) getActivity()).getArrayCities());

        ListViewRequestDate = view.findViewById(R.id.ListViewRequestDate);
        ListViewRequestDate.setAdapter(historyOfRequestsByCityAdapter);

        return view;
    }
}