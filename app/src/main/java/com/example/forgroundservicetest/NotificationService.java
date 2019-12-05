package com.example.forgroundservicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaCas;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.MediaSessionManager;

import static com.example.forgroundservicetest.app.CHANNEL_ID;
import static com.example.forgroundservicetest.app.NOTIFICATION_ID;

public class NotificationService extends Service {
    private  NotificationManager notificationManager;
    MediaPlayer mediaPlayer;
    private static final String TAG = "NotificationService";
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder notificationBuilder = createNotificationBuilder();
        startForeground(NOTIFICATION_ID,notificationBuilder.build());
        mediaPlayer.start();
        return START_NOT_STICKY;
    }

    private NotificationCompat.Builder createNotificationBuilder() {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(),NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //intent for play noti
        Intent intent1 = new Intent(this,MainActivity.class);
        intent1.putExtra("data","play");
        intent1.setAction("play");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),107,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(),107,intent1,PendingIntent.FLAG_NO_CREATE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.capture);
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("Demo Title")
                .setContentText("Here is the body")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_android)
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_skip_previous_black_24dp,"previous",null)
                .addAction(R.drawable.ic_play_arrow_black_24dp,"play",null)
                .addAction(R.drawable.ic_skip_next_black_24dp,"next",null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle());
        return builder;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID
                    , "NotificationService"
                    , NotificationManager.IMPORTANCE_HIGH);
                    channel.enableLights(true);
                    channel.enableVibration(true);
                    channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    channel.setLightColor(Color.RED);

            notificationManager.createNotificationChannel(channel);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
