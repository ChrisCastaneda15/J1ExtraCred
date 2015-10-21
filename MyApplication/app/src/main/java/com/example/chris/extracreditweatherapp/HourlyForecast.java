package com.example.chris.extracreditweatherapp;

/**
 * Created by Chris on 10/21/15.
 */
public class TenDayForeCast {
    String temperature;
    String time;
    int icon;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
