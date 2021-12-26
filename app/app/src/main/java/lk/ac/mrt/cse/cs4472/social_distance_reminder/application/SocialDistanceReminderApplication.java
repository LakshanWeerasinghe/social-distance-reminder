package lk.ac.mrt.cse.cs4472.social_distance_reminder.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;

public class SocialDistanceReminderApplication extends Application {

    public static final String HIGH_RISK_CHANNEL = "highRiskChannel";
    public static final String MILD_RISK_CHANNEL = "mildRiskChannel";
    public static final String COVID_CONTACTED_CHANNEL = "covidContactedChannel";
    private static final String TAG = "SocialDistanceReminderApplication";
    private static SocialDistanceReminderApplication instance;
    private static Context appContext;

    public static SocialDistanceReminderApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        appContext = mAppContext;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate() {
        Log.d(TAG, "application creating begin");
        super.onCreate();

        createNotificationChannels();

        instance = this;
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SQLiteRepository sqLiteRepository = DBHelper.getInstance(this);
        User user = sqLiteRepository.getUserDetails();
        if (user == null) {
            sqLiteRepository.initializeTables();
        }

        Log.d(TAG, "application creating end");
    }

    private void createNotificationChannels() {
        // Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelHighRisk = new NotificationChannel(
                    HIGH_RISK_CHANNEL,
                    "Channel High Risk",
                    NotificationManager.IMPORTANCE_HIGH // importance level
            );
            channelHighRisk.setDescription("Risk High Channel");
            channelHighRisk.enableVibration(true);
            channelHighRisk.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            channelHighRisk.enableLights(true);
            channelHighRisk.setLightColor(Color.RED);

            NotificationChannel channelMildRisk = new NotificationChannel(
                    MILD_RISK_CHANNEL,
                    "Channel Mild Risk",
                    NotificationManager.IMPORTANCE_LOW // importance level
            );
            channelMildRisk.setDescription("Risk Mild Channel");
            channelMildRisk.setSound(null, null);
            channelHighRisk.enableVibration(false);
            channelHighRisk.setVibrationPattern(null);
            channelHighRisk.enableLights(false);

            NotificationChannel channelCovidContacted = new NotificationChannel(
                    COVID_CONTACTED_CHANNEL,
                    "Channel Covid Contacted",
                    NotificationManager.IMPORTANCE_HIGH // importance level
            );
            channelCovidContacted.setDescription("Covid Contacted Channel");
            channelCovidContacted.setSound(null, null);
            channelCovidContacted.enableVibration(false);
            channelCovidContacted.setVibrationPattern(null);
            channelCovidContacted.enableLights(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelHighRisk);
            manager.createNotificationChannel(channelMildRisk);
            manager.createNotificationChannel(channelCovidContacted);
        }
    }
}
