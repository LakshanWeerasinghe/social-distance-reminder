package lk.ac.mrt.cse.cs4472.social_distance_reminder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.HomeActivity;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.SignupActivity;

public class LauncherActivity extends AppCompatActivity {

    private final boolean firstLaunch = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent nextActivity;

        if (firstLaunch) {
            nextActivity = new Intent(LauncherActivity.this, SignupActivity.class);
        } else {
            nextActivity = new Intent(LauncherActivity.this, HomeActivity.class);
        }
        startActivity(nextActivity);
        finish();
    }
}
