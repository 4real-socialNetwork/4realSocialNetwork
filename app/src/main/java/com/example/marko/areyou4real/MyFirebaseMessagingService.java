package com.example.marko.areyou4real;

import android.util.Log;

import com.example.marko.areyou4real.adapter.TinyDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MYFIREBASEMESSAGINGSER";
    private String userToken = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public String getUserToken() {
        return userToken;
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notificationBody = "";
        String notificationTitle = "";
        String notificationData = "";

        try{
            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();

        }catch (NullPointerException e){
            Log.e(TAG, "onMessageReceived: "+e.getMessage() );
        }
        Log.d(TAG, "onMessageReceived: data " +notificationData);
        Log.d(TAG, "onMessageReceived: body "+ notificationBody);
        Log.d(TAG, "onMessageReceived: title "+notificationTitle);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        userToken = s;

    }
}
