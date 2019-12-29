package org.iiitdharwad.navigation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseNotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        createNotificationChannel();
        if(remoteMessage.getData().isEmpty()) {
            putNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }
        else {
            putNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("description"));
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        /**
         * Called if InstanceID token is updated. This may occur if the security of
         * the previous token had been compromised. Note that this is called when the InstanceID token
         * is initially generated so this is where you would retrieve the token.
         */
    }

    protected void createNotificationChannel(){
        CharSequence name = "Announcements";
        String description = "Notifications for Announcements";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void putNotification(String title, String description){
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, "1");
        notifyBuilder.setSmallIcon(R.drawable.iiit);
        notifyBuilder.setContentTitle(title);
        notifyBuilder.setContentText(description);
        notifyBuilder.setAutoCancel(true);

        Intent onNotification = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, onNotification, 0);
        notifyBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, notifyBuilder.build());
    }


}
