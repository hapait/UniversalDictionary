package com.mcc.dictionary.application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseMessaging.getInstance().subscribeToTopic("dictionary");
    }
}
