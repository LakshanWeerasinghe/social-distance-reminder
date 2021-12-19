package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;

public class FirebaseCloudMessagingService extends FirebaseMessagingService{
    // called every time it receives a registration token from the firebase cloud messaging sdk
    // here you should upload the token to an online storage
    // generally it will not give you a new token unless the app was reinstalled or if the user clears the app data from the settings
    @Override
    public void onNewToken(@NonNull String s) {
        // s represents the token value
        Log.d("Token", s);
        super.onNewToken(s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token", s);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("DeviceTokens").document().set(tokenData);
    }

    // this method is called every time it receives a notification from firebase
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("Notification", "Notification: " + remoteMessage);

        // by default a notification will only appear when the app is closed or in the background
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Log.d("Notification", "Notification: " + title + " " + body);

        // purpose of the channel is to establish a link between our app and the android device
        // we use the channel to set the behaviour of our notifications
        final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // with importance high, the notification appear as a pop up
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MyNotificationChannel", NotificationManager.IMPORTANCE_HIGH);
            // we need to add the channel to the android device
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true) // to make the notification go away when we tap on it
                .build();

        // send the notification through the channel so it will get displayed
        NotificationManagerCompat.from(this).notify(1, notification);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
}
