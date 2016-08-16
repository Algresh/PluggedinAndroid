package ru.tulupov.alex.pluggedin.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.alex.pluggedin.R;
import ru.tulupov.alex.pluggedin.activities.ShowArticleActivity;
import ru.tulupov.alex.pluggedin.activities.ShowReviewActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import static ru.tulupov.alex.pluggedin.constants.Constants.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    protected static int NOTIFY_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(MY_TAG, "From: " + remoteMessage.getFrom());
        Log.d(MY_TAG, "DATA: " + remoteMessage.getData().toString());

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        boolean permissionSend = sharedPreferences
                .getBoolean(APP_PREFERENCES_SENT_NOTIFY_PERMISSION, true);

        if (permissionSend) {
            boolean permissionSound = sharedPreferences
                    .getBoolean(APP_PREFERENCES_SOUND_NOTIFY_PERMISSION, false);

            try {
                sendNotification(remoteMessage.getData(), permissionSound);
            } catch (Exception e) {
                Log.d(MY_TAG, "NOTIFICATION: " + remoteMessage.getNotification());
            }
        }
    }

    private void sendNotification(Map data, boolean permissionSound) throws Exception {
        String message = (String) data.get("message");
        Integer type = Integer.parseInt((String) data.get("type"));
        Integer id = Integer.parseInt((String) data.get("id"));


        Intent intent;
        if(type != TYPE_REVIEW) {
            intent= new Intent(this, ShowArticleActivity.class);
            intent.putExtra(ID, id);
        } else {
            intent= new Intent(this, ShowReviewActivity.class);
            intent.putExtra(ID_REVIEW, id);
        }

        String[] arrStrings = getResources().getStringArray(R.array.type_titles);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.message_text)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentTitle(arrStrings[type])
                .setContentText(message)
                .setContentIntent(pendingIntent);

        if (permissionSound) {
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(defaultSoundUri);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NOTIFY_ID = NOTIFY_ID < 1 ? NOTIFY_ID + 1 : 0;
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }
}
