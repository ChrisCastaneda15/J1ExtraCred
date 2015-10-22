package com.example.chris.extracreditweatherapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

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
    String apiURL = "http://api.wunderground.com/api/5a0e5992b1f92b8d/conditions/q/FL/Orlando.json";


    int page;
    Context mContext;

    //******************* DATA *******************************
    ArrayList<HourlyForecast> myHourlyForecast = new ArrayList<>();
    ArrayList<TenDayForecast> myTenDayForecast = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        ArrayList<String> spinnerPages = new ArrayList<>();
        spinnerPages.add("Current Forecast");
        spinnerPages.add("Hourly Forecast");
        spinnerPages.add("10 day Forecast");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                spinnerPages);

        ConnectivityManager mgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mgr != null) {
            NetworkInfo networkInf = mgr.getActiveNetworkInfo();
            if (networkInf.isConnected()) {
                Log.i("Zeek", "Connected");
                if (networkInf.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.i("Zeek", "Connected to Wf-fi");
                } else if (networkInf.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Log.i("Zeek", "Connected to Mobile");
                } else {
                    Log.i("Zeek", "Not Connected to nothing");
                }
                new InternetClass(apiURL, mContext).execute();
            } else {
                Log.i("Zeek", "Not Connected");
            }

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
                        apiURL = "http://api.wunderground.com/api/5a0e5992b1f92b8d/conditions/q/FL/Orlando.json";
                        new InternetClass(apiURL, mContext).execute();
                        page = position;

                    } else if (clicked == "Hourly Forecast") {
                        iconIV.setVisibility(View.GONE);
                        temperatureTV.setVisibility(View.GONE);
                        locationTV.setVisibility(View.GONE);
                        hourlyLV.setVisibility(View.VISIBLE);
                        tenDayLV.setVisibility(View.GONE);
                        apiURL = "http://api.wunderground.com/api/5a0e5992b1f92b8d/hourly/q/FL/Orlando.json";
                        new InternetClass(apiURL, mContext).execute();
                        page = position;

                    } else {
                        iconIV.setVisibility(View.GONE);
                        temperatureTV.setVisibility(View.GONE);
                        locationTV.setVisibility(View.GONE);
                        hourlyLV.setVisibility(View.GONE);
                        tenDayLV.setVisibility(View.VISIBLE);
                        apiURL = "http://api.wunderground.com/api/5a0e5992b1f92b8d/forecast10day/q/FL/Orlando.json";
                        new InternetClass(apiURL, mContext).execute();
                        page = position;


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


    }

    class InternetClass extends AsyncTask<String, Void, String> {


        public InternetClass(String apiURL, Context mContext) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("Zeek", "Starting background Task");


            try {
                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();

                    String apiData = IOUtils.toString(inputStream);
                    inputStream.close();

                    return apiData;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.d("Zeek", "Post");

            //Log.d("Zeek", "APi Data" + s);

            switch (page){
                case 0:
                    try{
                        JSONObject outObj = new JSONObject(s);
                        JSONObject currObs = outObj.getJSONObject("current_observation");
                        JSONObject location = currObs.getJSONObject("display_location");
                        String city = location.getString("city");
                        String temp = currObs.getString("temperature_string");
                        String lastUP = currObs.getString("observation_time");

                        lastUpdated.setText(lastUP);
                        temperatureTV.setText(temp);
                        locationTV.setText(city);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try{
                        JSONObject outObj = new JSONObject(s);
                        JSONArray hourly = outObj.getJSONArray("hourly_forecast");
                        for(int i = 0; i < hourly.length(); i++){
                            JSONObject arrayObj = hourly.getJSONObject(i);
                            JSONObject FCTTIME = arrayObj.getJSONObject("FCTTIME");
                            String time = FCTTIME.getString("hour");
                            JSONObject temp = arrayObj.getJSONObject("temp");
                            String engTemp = temp.getString("english");
                            String img = arrayObj.getString("icon");

                            myHourlyForecast.add(new HourlyForecast(time,engTemp,img));
                        }
                        hourlyLV.setAdapter(new hourlyBaseAdapter(mContext, myHourlyForecast));
                    } catch (JSONException e){
                        Log.d("Zeek", "In case 1 catch");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try{
                        JSONObject outObj = new JSONObject(s);
                        JSONObject forecast = outObj.getJSONObject("forecast");
                        JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
                        JSONArray forecastDay = simpleForecast.getJSONArray("forecastday");
                        for (int i =0;i<forecastDay.length();i++){
                            JSONObject arrayObj = forecastDay.getJSONObject(i);
                            JSONObject date = arrayObj.getJSONObject("date");
                            String prettyDate = date.getString("pretty");
                            JSONObject high = arrayObj.getJSONObject("high");
                            String fahrenheit = high.getString("fahrenheit");
                            String img = arrayObj.getString("icon");
                            Log.d("Zeek", img);
                            myTenDayForecast.add(new TenDayForecast(prettyDate, fahrenheit, img));
                        }
                        tenDayLV.setAdapter(new TenDayBaseAdapter(mContext, myTenDayForecast));
                    } catch (JSONException e){
                        Log.d("Zeek", "In case 1 catch");
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }


    }

}

    /**
     * Created by Chris on 10/21/15.
     */
    public static class hourlyBaseAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<HourlyForecast> forecasts;
        private static final int ID_CONSTANT = 0x01000000;

        public hourlyBaseAdapter(Context con, ArrayList<HourlyForecast> fore) {
            mContext = con;
            forecasts = fore;
        }


        @Override
        public int getCount() {
            if(forecasts!= null){
                return  forecasts.size();
            }else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (forecasts != null && position < forecasts.size() && position >=0){
                return forecasts.get(position);
            }else{
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            if (forecasts != null ){
                return  ID_CONSTANT + position;
            }else {
                return 0;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.hourlylayout, parent, false);

            ImageView iconImageView = (ImageView)row.findViewById(R.id.iconImageView);
            TextView timeTextView = (TextView)row.findViewById(R.id.timeTextView);
            TextView hourlyTextView = (TextView)row.findViewById(R.id.hourlyTextView);

            if (Objects.equals(forecasts.get(position).icon, "clear")) {
                iconImageView.setImageResource(R.drawable.w1);
            } else if (Objects.equals(forecasts.get(position).icon, "partlycloudy")){
                iconImageView.setImageResource(R.drawable.w2);
            }else if (Objects.equals(forecasts.get(position).icon, "mostlycloudy")) {
                iconImageView.setImageResource(R.drawable.w3);
            } else if (Objects.equals(forecasts.get(position).icon, "chancerain")) {
                iconImageView.setImageResource(R.drawable.w7);
            } else {
                iconImageView.setImageResource(R.drawable.w1);
            }


            if ((Integer.parseInt(forecasts.get(position).time)) >= 12){
                String time = String.valueOf((Integer.parseInt(forecasts.get(position).time)) - 12);
                timeTextView.setText(time + "PM");
            } else {
                timeTextView.setText(forecasts.get(position).time + "AM");
            }
            hourlyTextView.setText(forecasts.get(position).temperature + "F");


            return row;
        }
    }

    /**
     * Created by Chris on 10/21/15.
     */



    public static class TenDayBaseAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<TenDayForecast> forecasts;
        private static final int ID_CONSTANT = 0x01000001;

        public TenDayBaseAdapter(Context con, ArrayList<TenDayForecast> fore) {
            mContext = con;
            forecasts = fore;
        }

        @Override
        public int getCount() {
            if(forecasts!= null){
                return  forecasts.size();
            }else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (forecasts != null && position < forecasts.size() && position >=0){
                return forecasts.get(position);
            }else{
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            if (forecasts != null ){
                return  ID_CONSTANT + position;
            }else {
                return 0;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.tendaylayout, parent, false);

            ImageView iconImageView = (ImageView)row.findViewById(R.id.iconImageView);
            TextView timeTextView = (TextView)row.findViewById(R.id.timeTextView);
            TextView hourlyTextView = (TextView)row.findViewById(R.id.hourlyTextView);

            if (Objects.equals(forecasts.get(position).icon, "clear")) {
                iconImageView.setImageResource(R.drawable.w1);
            } else if (Objects.equals(forecasts.get(position).icon, "partlycloudy")){
                iconImageView.setImageResource(R.drawable.w2);
            }else if (Objects.equals(forecasts.get(position).icon, "mostlycloudy")) {
                iconImageView.setImageResource(R.drawable.w3);
            } else if (Objects.equals(forecasts.get(position).icon, "chancerain")) {
                iconImageView.setImageResource(R.drawable.w7);
            } else {
                iconImageView.setImageResource(R.drawable.w1);
            }

            timeTextView.setText(forecasts.get(position).date);
            hourlyTextView.setText(forecasts.get(position).temperature + "F");


            return row;
        }
    }
}