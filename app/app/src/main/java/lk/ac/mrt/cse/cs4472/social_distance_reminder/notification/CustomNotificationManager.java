package lk.ac.mrt.cse.cs4472.social_distance_reminder.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.NotificationConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActivity;

public class CustomNotificationManager {

    private static final String TAG = "CustomNotificationManager";
    @SuppressLint("StaticFieldLeak")
    private static CustomNotificationManager customNotificationManager;
    private NotificationManager notificationManager;

    private boolean isBeaconNotificationChannelCreated;

    private final Context context;

    private CustomNotificationManager(Context context) {
        this.context = context;
    }

    public static synchronized CustomNotificationManager getInstance(Context context) {
        if (customNotificationManager == null || customNotificationManager.context != context) {
            customNotificationManager = new CustomNotificationManager(context);
        }
        return customNotificationManager;
    }

    public static Notification createBeaconServiceNotification(String textTitle,
                                                               String textContent, Context context) {
        try {
            return getInstance(context).showBeaconServiceNotification(textTitle,textContent);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("LongLogTag")
    private Notification showBeaconServiceNotification(String textTitle, String textContent) throws Exception {
        Log.d(TAG, "start to show beacon service notification");

        this.createNotificationChannelForBeaconService();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this.context, NotificationConstants.BEACON_SERVICE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);

        Log.d(TAG, "notified beacon service notification");
        return notificationBuilder.build();
    }

    @SuppressLint("LongLogTag")
    public void createNotificationChannelForBeaconService() {
        Log.d(TAG, "begin creating notification channel for beacon service");

        if (isBeaconNotificationChannelCreated) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NotificationConstants.BEACON_SERVICE_NOTIFICATION_CHANNEL_ID,
                    NotificationConstants.BEACON_SERVICE_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = this.createNotificationManager();
            if(manager != null){
                manager.createNotificationChannel(notificationChannel);
                isBeaconNotificationChannelCreated = true;
            }
            else {
                Log.e(TAG, "creating notification channel for beacon service failed");
            }
        }

        Log.d(TAG, "end creating notification channel for beacon service");
    }

    private NotificationManager createNotificationManager() {

        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    private PendingIntent getPendingIntent() {
        //TODO: Pending activity can change by if conditions or parameters
        Intent intent = new Intent(this.context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this.context, 0, intent, 0);
    }
}
