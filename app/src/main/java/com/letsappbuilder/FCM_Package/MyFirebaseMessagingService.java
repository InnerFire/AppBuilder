package com.letsappbuilder.FCM_Package;


import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.letsappbuilder.FCM_Chat.Constants;
import com.letsappbuilder.FCM_Chat.NotificationHandler;

import org.json.JSONObject;

/**
 * Created by Belal on 03/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
          //  Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                JSONObject data = json.getJSONObject("data");
                String message = data.getString("message");
                String title = data.getString("title");
                String id = data.getString("id");
                sendNotification(message, title, id);

            } catch (Exception e) {
              //  Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }
    private void sendNotification(String message, String title, String id) {
        //Creating a broadcast intent
        Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
        //Adding notification data to the intent
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("name", title);
        pushNotification.putExtra("id", id);

        //We will create this class to handle notifications
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chatroom to add the new message
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            notificationHandler.showNotificationMessage(title, message);
        }
    }

}