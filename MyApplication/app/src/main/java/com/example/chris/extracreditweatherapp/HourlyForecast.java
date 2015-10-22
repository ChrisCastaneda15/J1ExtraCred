package com.example.chris.extracreditweatherapp;

/**
 * Created by Chris on 10/21/15.
 */
public class HourlyForecast {
    String time;
    String temperature;
    String icon;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public HourlyForecast(String time, String temperature, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
    }
}
