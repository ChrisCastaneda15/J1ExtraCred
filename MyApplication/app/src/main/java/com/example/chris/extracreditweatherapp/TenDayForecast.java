package com.example.chris.extracreditweatherapp;

/**
 * Created by Chris on 10/21/15.
 */
public class TenDayForecast {
    String date;
    String temperature;
    int icon;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public TenDayForecast(String date, String temperature, int icon) {
        this.date = date;
        this.temperature = temperature;
        this.icon = icon;
    }
}
