package com.example.forgroundservicetest;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class app extends Application {
    public static final String CHANNEL_ID = "NotificationService";
    public static final int NOTIFICATION_ID = 101;
    @Override
    public void onCreate() {
        super.onCreate();
    }


}
