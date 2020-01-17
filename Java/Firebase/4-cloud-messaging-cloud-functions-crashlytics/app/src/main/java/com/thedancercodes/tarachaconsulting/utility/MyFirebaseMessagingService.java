package com.thedancercodes.tarachaconsulting.utility;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";

    /**
     * Receive messages through the onMessageReceived() method.
     *
     * @param remoteMessage - This is the message object that is received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /* Testing an inbound cloud message */

        // String Variables
        String notificationBody="";
        String notificationTitle="";
        String notificationData="";

        try {

            // Get Notification Data, Title & Body
            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();

        } catch (NullPointerException e){ // In case messages don't contain data or notification.
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage());
        }
        
        // Log Outputs to display the inbound cloud message
        Log.d(TAG, "onMessageReceived: data: " + notificationData );
        Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
        Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
