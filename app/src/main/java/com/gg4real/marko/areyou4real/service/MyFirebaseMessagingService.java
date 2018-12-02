package com.gg4real.marko.areyou4real.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.gg4real.marko.areyou4real.EventChatRoom;
import com.gg4real.marko.areyou4real.GroupChatRoom;
import com.gg4real.marko.areyou4real.InsideEvent;
import com.gg4real.marko.areyou4real.OtherUserProfile;
import com.gg4real.marko.areyou4real.R;
import com.gg4real.marko.areyou4real.adapter.TinyDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    private static final String CHANNEL_ID = "Notification channel";
    private static final String CHANNEL_DESCRIPTION = "Notifikacija o novom eventu";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    String userDocId = new String();
    private TinyDB tinyDB;


    public MyFirebaseMessagingService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel();
        }
        tinyDB = new TinyDB(this);
        userDocId = tinyDB.getString("USERDOCREF");

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_DESCRIPTION,
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println(TAG + "onMessagerecieved called");
        String notificationTitle = "";
        String notificationMessage = "";
        try{
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationMessage = remoteMessage.getNotification().getBody();
        }catch (NullPointerException e){
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
        }
        System.out.println(TAG + " onMessageReceived: Message Received: \n" +
                "Title: " + notificationTitle + "\n" +
                "Message: " + notificationMessage);

        String dataType = remoteMessage.getData().get("data_type");
        if(dataType.equals("event_joining")){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String messageId = remoteMessage.getData().get("messageId");
            String extraId = remoteMessage.getData().get("extraId");
            String extraName = "EVENT_ID";

            sendMessageNotification(title, message, messageId, extraId, extraName);
        }
        if (dataType.equals("event_request")){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String messageId = remoteMessage.getData().get("messageId");
            String extraId = remoteMessage.getData().get("extraId");
            String extraName = "EVENT_ID";

            sendMessageNotification(title, message, messageId, extraId, extraName);
        }
        if (dataType.equals("friend_request")){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String messageId = remoteMessage.getData().get("messageId");
            String extraId = remoteMessage.getData().get("extraId");
            String extraName = "otherUserDocRef";

            sendFriendRequest(title, message, messageId, extraId, extraName);
        }
        if (dataType.equals("event_message")){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String messageId = remoteMessage.getData().get("messageId");
            String extraId = remoteMessage.getData().get("extraId");
            String extraName = "EVENTID";

            sendEventMessage(title, message, messageId, extraId, extraName);
        }
        if (dataType.equals("group_message")){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String messageId = remoteMessage.getData().get("messageId");
            String extraId = remoteMessage.getData().get("extraId");
            String extraName = "GROUPID";

            sendGroupMessage(title, message, messageId, extraId, extraName);
        }
    }
    private void sendGroupMessage(String title, String message, String messageId, String extraId, String extraName){

        int notificationId = buildNotificationId(messageId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent pendingIntent = new Intent(this, GroupChatRoom.class);
        pendingIntent.putExtra(extraName, extraId);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.drawable.logo_transparent_two)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());
    }

    private void sendEventMessage(String title, String message, String messageId, String extraId, String extraName){

        int notificationId = buildNotificationId(messageId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent pendingIntent = new Intent(this, EventChatRoom.class);
        pendingIntent.putExtra(extraName, extraId);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.drawable.logo_transparent_two)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());
    }


    private void sendFriendRequest(String title, String message, String messageId, String extraId, String extraName){

        int notificationId = buildNotificationId(messageId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent pendingIntent = new Intent(this, OtherUserProfile.class);
        pendingIntent.putExtra(extraName, extraId);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.drawable.logo_transparent_two)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());
    }

    private void sendMessageNotification(String title, String message, String messageId, String extraId, String extraName){

        int notificationId = buildNotificationId(messageId);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent pendingIntent = new Intent(this, InsideEvent.class);
        pendingIntent.putExtra(extraName, extraId);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setSmallIcon(R.drawable.logo_transparent_two)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());
    }


    private int buildNotificationId(String id){
        Log.d(TAG, "buildNotificationId: building a notification id.");

        int notificationId = 0;
        for(int i = 0; i < 9; i++){
            notificationId = notificationId + id.charAt(0);
        }
        Log.d(TAG, "buildNotificationId: id: " + id);
        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
        return notificationId;
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        System.out.println(TAG + " OnSucces succesfull, generating new token");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                System.out.println(TAG + " OnSucces succesfull, generating new token");

                String deviceToken = instanceIdResult.getToken();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    userRef.document(userDocId).update("userToken",deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "tokenUpdated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}


