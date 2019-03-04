package com.orbitview.salesmanagement.controller;

import android.app.Application;

import com.orbitview.salesmanagement.model.Employee;

/**
 * Created by hamnaro on 07/11/18.
 */

public class AppController  extends Application{
    public Employee employee;
    public String userName;
    public double latitude;
    public double longitude;
    public String currentPlace;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
