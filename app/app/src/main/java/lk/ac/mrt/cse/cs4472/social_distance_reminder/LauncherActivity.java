package lk.ac.mrt.cse.cs4472.social_distance_reminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.User;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.SignupActivity;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteRepository sqLiteRepository = DBHelper.getInstance(this);

        User user = sqLiteRepository.getUserDetails();

        Intent nextActivity;
        Log.d(TAG, user.toString());
        if (!user.getVerifiedUser()) {
            nextActivity = new Intent(LauncherActivity.this, SignupActivity.class);
        } else {
            nextActivity = new Intent(LauncherActivity.this, HomeActivity.class);
        }
        nextActivity.putExtra("id", user.getId());
        startActivity(nextActivity);
        finish();
    }
}
