package com.cbo.cbomobilereporting.databaseHelper.Location;

public class mLocation {

    String Latitude="";
    String Longitude="";
    String Adresss="";
    String StartTime="";
    String PauseTime="";
    String StopTime="";


    public mLocation(String latitude, String longitude, String adresss, String startTime, String pauseTime, String stopTime) {
        Latitude = latitude;
        Longitude = longitude;
        Adresss = adresss;
        StartTime = startTime;
        PauseTime = pauseTime;
        StopTime = stopTime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAdresss() {
        return Adresss;
    }

    public void setAdresss(String adresss) {
        Adresss = adresss;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getPauseTime() {
        return PauseTime;
    }

    public void setPauseTime(String pauseTime) {
        PauseTime = pauseTime;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }
}
