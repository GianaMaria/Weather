package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weather.R;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.Parcel;
import com.example.weatherapp.adapters.CitiesAdapter;
import java.util.Date;

public class OtherCitiesFragment extends Fragment {
    Parcel parcel;

    private String[] cities;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_other_cities, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        cities = getResources().getStringArray(R.array.cities);
        final CitiesAdapter adapter = new CitiesAdapter(cities);

        recyclerView.setAdapter(adapter);

        final Date date = new Date();

        adapter.setOnCityClickListener(new CitiesAdapter.OnCityClickListener() {
            @Override
            public void onClicked(String city) {
                int fi = 0;
                for (int i = 0; i < cities.length; i++) {
                    if (city.equals(cities[i])) {
                        fi = i;
                    }
                }
                //здесь при нажатии открывать другой фрагмент и менять текст
                MainFragment mainCityFragment = new MainFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, mainCityFragment);  // замена фрагмента
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                parcel = new Parcel(fi, getResources().getStringArray(R.array.cities)[fi]);
                Bundle args = new Bundle();
                args.putSerializable("city", parcel);
                mainCityFragment.setArguments(args);

                ((MainActivity) getActivity()).addCityArray(city, date.getTime());
            }

        });

        return view;
    }
}