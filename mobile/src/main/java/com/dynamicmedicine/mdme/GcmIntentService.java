package com.dynamicmedicine.mdme;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * MDme Android application
 * Author:: Matt Hamada (maito:ermacaz@gmail.com)
 * Created on:: 7/26/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            }
            else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = extras.getString("message");
                int id = Integer.parseInt(extras.getString("appointment_id"));
                NotificationTypes type = NotificationTypes.valueOf(
                        (extras.getString("type")).toUpperCase());
                switch (type) {
                    case DELAY:
                        sendDelayNotification(message, id);
                        break;
                    case READY:
                        sendReadyNotification(message);
                        break;

                }
                sendNotification(message);
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmReceiver.completeWakefulIntent(intent);
    }

    private void sendReadyNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_stat_gcm);
        mBuilder.setContentTitle("Appointment Ready");
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000 });
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        mBuilder.setContentText(msg);


        //mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    // shows notification of delayed appointment
    // TODO should bring user to appointment activity or a confirm/deny delay activity
    private void sendDelayNotification(String msg, int appointmentId) {
//        mNotificationManager = (NotificationManager)
//                this.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent extrasIntent = new Intent(GcmIntentService.this, AppointmentShowActivity.class);
//        extrasIntent.putExtra("appointment_id", appointmentId);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                extrasIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_stat_gcm)
//                        .setContentTitle("Appointment Delayed")
//                        .setContentText(msg);
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_stat_gcm);
        mBuilder.setContentTitle("GCM Notification");
        mBuilder .setStyle(new NotificationCompat
                .BigTextStyle()
                .bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private enum NotificationTypes {
        DELAY,
        READY
    }


}
