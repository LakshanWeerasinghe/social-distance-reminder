package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.service.BeaconService;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.dashboard.DashboardFragment;

public class HomeActivity extends AppCompatActivity implements NavigationHost, HomeActionInterface{

    private SQLiteRepository sqLiteRepository;
    private String deviceId;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdc_home_activity);

        id = getIntent().getIntExtra("id", -1);

        sqLiteRepository = DBHelper.getInstance(this);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new DashboardFragment())
                    .commit();
        }

        UserConfig userConfig = sqLiteRepository.getUserConfigs();
        if(userConfig.getEnableBeaconService()){
            Intent beaconServiceIntent = new Intent(this, BeaconService.class);
            beaconServiceIntent.putExtra("enable", true);

            ContextCompat.startForegroundService(this, beaconServiceIntent);
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void changeBeaconServiceState(Integer id, Boolean enable) {
        Intent beaconServiceIntent = new Intent(this, BeaconService.class);
        beaconServiceIntent.putExtra("enable", enable);

        ContextCompat.startForegroundService(this, beaconServiceIntent);

        sqLiteRepository.updateUserConfig(id, enable, null);
    }
}
