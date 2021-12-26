package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import static lk.ac.mrt.cse.cs4472.social_distance_reminder.application.SocialDistanceReminderApplication.COVID_CONTACTED_CHANNEL;

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
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.NotificationModel;

public class FirebaseCloudMessagingService extends FirebaseMessagingService{

    private SQLiteRepository sqLiteRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        sqLiteRepository = DBHelper.getInstance(this);
    }

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
        Log.d("NotificationModel", "NotificationModel: " + remoteMessage);

        // by default a notification will only appear when the app is closed or in the background
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String covid_positive_date = remoteMessage.getData().get("date");

        Log.d("NotificationModel", "NotificationModel: " + title + " " + body + " " + covid_positive_date);

        Notification notification = new NotificationCompat.Builder(this, COVID_CONTACTED_CHANNEL)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true) // to make the notification go away when we tap on it
                .build();

        // send the notification through the channel so it will get displayed
        NotificationManagerCompat.from(this).notify(1, notification);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(body);

        sqLiteRepository.saveCovidContactedNotificationDetails(notificationModel);

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
}
