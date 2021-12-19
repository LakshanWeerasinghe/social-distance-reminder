package lk.ac.mrt.cse.cs4472.social_distance_reminder.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;

public class SocialDistanceReminderApplication extends Application {

    private static final String TAG = "SocialDistanceReminderApplication";
    private static SocialDistanceReminderApplication instance;
    private static Context appContext;

    public static SocialDistanceReminderApplication getInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return appContext;
    }

    public void setAppContext(Context mAppContext){
        appContext = mAppContext;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onCreate() {
        Log.d(TAG, "application creating begin");
        super.onCreate();

        instance = this;
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SQLiteRepository sqLiteRepository = DBHelper.getInstance(this);
        User user = sqLiteRepository.getUserDetails();
        if(user == null){
            sqLiteRepository.initializeTables();
        }

        Log.d(TAG, "application creating end");
    }
}
