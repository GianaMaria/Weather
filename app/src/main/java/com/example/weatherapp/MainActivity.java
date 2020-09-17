package com.example.weatherapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weather.R;
import com.example.weatherapp.fragments.DialogBuilderFragment;
import com.example.weatherapp.fragments.HistoryOfRequestsByCityFragment;
import com.example.weatherapp.fragments.MainFragment;
import com.example.weatherapp.fragments.OtherCitiesFragment;
import com.example.weatherapp.fragments.SettingsFragment;
import com.example.weatherapp.model.City;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    DialogBuilderFragment dialogBuilderFragment;
    OtherCitiesFragment otherCitiesFragment = new OtherCitiesFragment();

    private ArrayList<City> cities = new ArrayList<City>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTitle = mDrawerTitle = getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                toolbar, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            //Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            fragmentTransaction(new MainFragment());
        }

        dialogBuilderFragment = new DialogBuilderFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);

        final SearchView searchText = (SearchView) search.getActionView();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Snackbar.make(searchText, s, Snackbar.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(!mDrawerLayout.isDrawerOpen(GravityCompat.START));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment;

        switch (id) {
            case R.id.nav_home:
                fragment = new MainFragment();
                mTitle = getString(R.string.app_name);
                break;
            case R.id.nav_cities:
                fragment = new OtherCitiesFragment();
                mTitle = getString(R.string.cities_nav);
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                mTitle = getString(R.string.action_settings);
                break;
            case R.id.nav_history:
                fragment = new HistoryOfRequestsByCityFragment();
                mTitle = getString(R.string.history_nav);
                break;
//            case R.id.nav_send:
//                fragment = new SendFragment();
//                mTitle  = getString(R.string.send);
//                break;
            default:
                fragment = new MainFragment();
                mTitle = getString(R.string.app_name);
                break;
        }

        fragmentTransaction(fragment);

        return true;
    }

    public void fragmentTransaction(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state     after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    // Вызов диалога с билдером
    public void onClickDialogBuilder(View view) {
        dialogBuilderFragment.show(getSupportFragmentManager(), "dialogBuilder");

    }

    // Метод для общения с диалоговыми окнами
    public void onDialogResult(String resultDialog) {
        fragmentTransaction(otherCitiesFragment);
        Toast.makeText(this, "Выбрано " + resultDialog, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<City> getArrayCities() {
        return cities;
    }

    public void addCityArray(String city, long date) {
        cities.add(new City(city, date));
    }

}