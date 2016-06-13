package com.example.alex.pluggedin.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.alex.pluggedin.constants.Constants.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(MY_TAG, "From: " + remoteMessage.getFrom());
        Log.d(MY_TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
