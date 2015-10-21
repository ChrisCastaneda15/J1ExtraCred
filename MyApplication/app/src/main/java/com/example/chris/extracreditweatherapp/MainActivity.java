package com.example.chris.extracreditweatherapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    // KEY: eef96dd44908b8b6

    //****************** UI ELEMENTS ***********************
    //Static Elements
    Spinner pageSpinner;
    TextView lastUpdated;

    //Page One
    ImageView iconIV;
    TextView temperatureTV;
    TextView locationTV;

    //Page Two
    ListView hourlyLV;

    //Page Three
    ListView tenDayLV;

    //******************* DATA *******************************
    ArrayList<HourlyForecast> myHourlyForecast = new ArrayList<>();
    ArrayList<TenDayForecast> myTenDayForecast = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> spinnerPages = new ArrayList<>();
        spinnerPages.add("Current Forecast");
        spinnerPages.add("Hourly Forecast");
        spinnerPages.add("10 day Forecast");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                spinnerPages);

        //Static
        pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        pageSpinner.setAdapter(spinnerAdapter);
        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
                String clicked = (String) adapter.getItem(position);
                if (clicked == "Current Forecast") {
                    iconIV.setVisibility(View.VISIBLE);
                    temperatureTV.setVisibility(View.VISIBLE);
                    locationTV.setVisibility(View.VISIBLE);

                    hourlyLV.setVisibility(View.GONE);
                    tenDayLV.setVisibility(View.GONE);
                }
                else if (clicked == "Hourly Forecast"){
                    iconIV.setVisibility(View.GONE);
                    temperatureTV.setVisibility(View.GONE);
                    locationTV.setVisibility(View.GONE);

                    hourlyLV.setVisibility(View.VISIBLE);
                    tenDayLV.setVisibility(View.GONE);
                }
                else {
                    iconIV.setVisibility(View.GONE);
                    temperatureTV.setVisibility(View.GONE);
                    locationTV.setVisibility(View.GONE);

                    hourlyLV.setVisibility(View.GONE);
                    tenDayLV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lastUpdated = (TextView) findViewById(R.id.lastUpdatedTextView);

        //p1
        iconIV = (ImageView) findViewById(R.id.conditionImageView);
        temperatureTV = (TextView) findViewById(R.id.temperatureTextView);
        locationTV = (TextView) findViewById(R.id.locationTextView);

        //p2
        //May be something broken
        final hourlyBaseAdapter hourBase = new hourlyBaseAdapter(this, myHourlyForecast);
        hourlyLV = (ListView) findViewById(R.id.hourlyListView);
        hourlyLV.setAdapter(hourBase);

        //p3
        //May be something broken
        final TenDayBaseAdapter tenBase = new TenDayBaseAdapter(this, myTenDayForecast);
        tenDayLV = (ListView) findViewById(R.id.tenDayForecastListView);
        tenDayLV.setAdapter(tenBase);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
