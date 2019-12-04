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
import android.graphics.Color;
import android.media.MediaCas;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.forgroundservicetest.app.CHANNEL_ID;
import static com.example.forgroundservicetest.app.NOTIFICATION_ID;

public class NotificationService extends Service {
    private  NotificationManager notificationManager;
    MediaPlayer mediaPlayer;
    MediaSession mSession;
    private static final String TAG = "NotificationService";
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
        mSession = new MediaSession(this, "MusicService");
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: ");
                Toast.makeText(getApplicationContext(),"Play",Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("play");
        registerReceiver(broadcastReceiver,intentFilter);
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
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this,107,intent1,PendingIntent.FLAG_CANCEL_CURRENT);

        //intent for pause noti
        Intent intent2 = new Intent(this,MainActivity.class);
        intent1.putExtra("data","pause");
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this,108,intent2,PendingIntent.FLAG_NO_CREATE);

        //intent for stop noti
        Intent intent3 = new Intent(this,MainActivity.class);
        intent1.putExtra("data","pause");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(this,109,intent3,PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("Demo Title")
                .setContentText("Here is the body")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_android,"Play",pendingIntent1)
                .addAction(R.drawable.ic_android,"Pause",pendingIntent2)
                .addAction(R.drawable.ic_android,"Stop",pendingIntent3)
                .setSmallIcon(R.drawable.ic_android);
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
