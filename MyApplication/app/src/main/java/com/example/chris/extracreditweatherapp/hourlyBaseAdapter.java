package com.example.chris.extracreditweatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 10/21/15.
 */
public class hourlyBaseAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<HourlyForecast> forecasts;
    private static final int ID_CONSTANT = 0x01000000;


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
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hourlylayout,parent,false);
        }

        HourlyForecast item = (HourlyForecast) getItem(position);


        ((TextView)convertView.findViewById(R.id.timeTextView)).setText(item.getTime());
        ((TextView)convertView.findViewById(R.id.temperatureTextView)).setText(item.getTemperature());
        ((ImageView)convertView.findViewById(R.id.iconImageView)).setImageResource(item.getIcon());

        return convertView;
    }
}
