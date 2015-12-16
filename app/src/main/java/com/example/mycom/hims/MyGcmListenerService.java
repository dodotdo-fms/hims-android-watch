package com.example.mycom.hims;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mycom.hims.voice_messaging.MessageSendActivity;
import com.example.mycom.hims.voice_messaging.ReceiveMessageActivity;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e(TAG, "rece: ");
        String message = data.getString("message");
        String type = data.getString("type");
        Log.e(TAG, "From: " + data.getString("message_format"));
        Log.e(TAG, "type: " + type);
        Log.e(TAG, "Message: " + message);
//        Intent intent = new Intent(getApplicationContext(), MessageSendActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        switch (type){
            case "message" :
                receiveMessage(data);
                break;

            default:
                return;
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
//        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void receiveMessage(Bundle bundle){
        Intent intent = new Intent();
        intent.putExtra("data", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(MessageSendActivity.isOnSendActivity){
            Log.e("ass","asd");
            Log.e("chanenne1", MessageSendActivity.chanelId+"A");
            Log.e("chanenne1", bundle.get("channel_id")+"A");
            if(MessageSendActivity.chanelId.equals(bundle.get("channel_id"))) {
                Log.e("ass1","asd");
                intent.setClass(getApplicationContext(), MessageSendActivity.class);
            }else {
                Log.e("ass2","asd");
                intent.setClass(getApplicationContext(), ReceiveMessageActivity.class);
            }
        }else {
            Log.e("ass3","asd");
            intent.setClass(getApplicationContext(), ReceiveMessageActivity.class);
        }
        startActivity(intent);

    }

}
