package com.mcc.dictionary.notification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mcc.dictionary.data.constants.AppConstants;
import com.mcc.dictionary.activity.MainActivity;
import java.util.Map;
import com.mcc.dictionary.R;
import com.mcc.dictionary.data.preference.AppPreference;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > AppConstants.VALUE_ZERO) {
            Map<String, String> params = remoteMessage.getData();

            if (AppPreference.getInstance(MyFirebaseMessagingService.this).isNotificationOn()) {
                sendNotification(params.get("title"), params.get("message"));
            }
        }
    }

    private void sendNotification(String title, String message) {

        Intent intent = null;
        if (title != null && !title.isEmpty()) {
            intent = new Intent(this, MainActivity.class);
        }

        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, AppConstants.VALUE_ZERO, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_static_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000, 1000})
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }
}
