package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ArgumentConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.models.UserConfig;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.service.BeaconService;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.ui.dashboard.DashboardFragment;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.util.Util;

public class HomeActivity extends AppCompatActivity implements NavigationHost,
        HomeActionInterface, HomePermissionHandlerInterface {

    private static final String TAG = "HomeActivity";
    private static final int PERMISSION_FINE_LOCATION_REQUEST_CODE = 200;
    private static final int PERMISSION_BLUETOOTH_REQUEST_CODE = 201;

    private SQLiteRepository sqLiteRepository;
    private String deviceUUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdc_home_activity);

        sqLiteRepository = DBHelper.getInstance(this);

        UserConfig userConfig = sqLiteRepository.getUserConfigs();
        deviceUUID = sqLiteRepository.getUserDetails().getDeviceUUID();

        Bundle bundle = new Bundle();
        bundle.putInt(ArgumentConstants.BUNDLE_ARG_USER_CONFIG_ID, userConfig.getId());
        boolean startBeaconService = false;

        if (userConfig.getEnableBeaconService()) {
            boolean isBluetoothPermissionGranted = checkPermission(Manifest.permission.BLUETOOTH);
            boolean isLocationPermissionGranted = checkPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (isBluetoothPermissionGranted && isLocationPermissionGranted) {
                bundle.putBoolean(ArgumentConstants.BUNDLE_ARG_ENABLE_BEACON_SERVICE, true);

                startBeaconService = true;
            } else {
                bundle.putBoolean(ArgumentConstants.BUNDLE_ARG_ENABLE_BEACON_SERVICE, false);
                sqLiteRepository.updateUserConfig(userConfig.getId(),
                        false, null);
                if (!isBluetoothPermissionGranted) {
                    requestPermission(Manifest.permission.BLUETOOTH, PERMISSION_BLUETOOTH_REQUEST_CODE);
                }
                if (!isLocationPermissionGranted) {
                    requestPermission(Manifest.permission.BLUETOOTH,
                            PERMISSION_FINE_LOCATION_REQUEST_CODE);
                }
            }
        }

        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, dashboardFragment)
                    .commit();
        }

        if(startBeaconService){
            startBeaconService(true);
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
        startBeaconService(enable);
        sqLiteRepository.updateUserConfig(id, enable, null);
    }

    private void startBeaconService(boolean enable){
        if(deviceUUID == null){
            deviceUUID = sqLiteRepository.getUserDetails().getDeviceUUID();
        }
        Intent beaconServiceIntent = new Intent(this, BeaconService.class);
        beaconServiceIntent.putExtra(
                ArgumentConstants.EXTRA_START_BEACON_SERVICE_ENABLE, enable);
        beaconServiceIntent.putExtra(
                ArgumentConstants.EXTRA_START_BEACON_SERVICE_DEVICE_UUID, deviceUUID);

        ContextCompat.startForegroundService(this, beaconServiceIntent);
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION_REQUEST_CODE:
                handlePermissionRequest(requestCode, permissions[0], grantResults);
            case PERMISSION_BLUETOOTH_REQUEST_CODE:
                handlePermissionRequest(requestCode, permissions[0], grantResults);
                break;
        }
    }

    private void handlePermissionRequest(int requestCode, String permission, int[] grantResults){
        if (grantResults.length > 0) {

            boolean isPermissionGranted = grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED;

            if (!isPermissionGranted)

                Util.showToast(this, "You should grant "
                        + permission + " permission to use this feature");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    showMessageOKCancel("You need to allow access to the permission",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{permission}, requestCode);
                                }
                            });
                }
            }

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean checkPermissionsAndRequest() {
        boolean isBluetoothPermissionGranted = checkPermission(Manifest.permission.BLUETOOTH);
        boolean isLocationPermissionGranted = checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(!isBluetoothPermissionGranted || !isLocationPermissionGranted){
            if (!isBluetoothPermissionGranted) {
                requestPermission(Manifest.permission.BLUETOOTH, PERMISSION_BLUETOOTH_REQUEST_CODE);
            }
            if (!isLocationPermissionGranted) {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        PERMISSION_FINE_LOCATION_REQUEST_CODE);
            }
            return false;
        }
        return true;
    }
}
