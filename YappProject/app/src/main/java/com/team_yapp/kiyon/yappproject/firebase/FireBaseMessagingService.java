package com.team_yapp.kiyon.yappproject.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.team_yapp.kiyon.yappproject.R;
import com.team_yapp.kiyon.yappproject.view.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // 메시지의 두가지 타입
//        1. notification 키를 설정 할때 - 백그라운드 일때 FCM이 앱을 대신  알림을 표시하고 포그라운드일때는 앱의 onMessageReceived() 에서 알림을 처리한다.
//        2. data 키를 설정 했을때 - 백그라운드/포그라운드 상관없이 앱의 onMessageReceived() 가 실행되어 알림을 처리한다.

        if (remoteMessage.getData().size() > 0) { // data payload
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (true) {
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("content");
                sendNotificationFromServer(title, body);
                // 메시지를 받았을 경우 데이터 갱신을 위해 브로드캐시트 등록
                broadcaster.sendBroadcast(new Intent("DataChange"));
            } else {
                handleNow();
            }
        }

        if (remoteMessage.getNotification() != null) { // notification payload
            sendNotificationFromFirebase(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");

    }

//    * data payload 일경우(서버에서 fcm을 발송할 경우)
    private void sendNotificationFromServer(String title, String body) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String channelId = "Default notifycation channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Default notifycation channel";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

    }


//    * notification payload 일경우(파베에서 fcm을 발송할 경우)

    private void sendNotificationFromFirebase(String message) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String channelId = "Default notifycation channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DITO")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Default notifycation channel";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());

    }

}
