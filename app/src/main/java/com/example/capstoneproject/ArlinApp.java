package com.example.capstoneproject;

import android.app.Application;

import com.firebase.client.Firebase;

public class ArlinApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
