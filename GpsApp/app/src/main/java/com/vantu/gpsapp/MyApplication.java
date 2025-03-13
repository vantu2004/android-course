package com.vantu.gpsapp;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

// Việc tạo một class kế thừa từ Application trong Android giúp quản lý trạng thái toàn cục (global state) của ứng dụng.
public class MyApplication extends Application {
    private static MyApplication singleton;
    private List<Location> myLocations;

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        myLocations = new ArrayList<>();
    }

    public List<Location> getLocation() {
        return myLocations;
    }

    public static MyApplication getInstance() {
        return singleton;
    }
}
