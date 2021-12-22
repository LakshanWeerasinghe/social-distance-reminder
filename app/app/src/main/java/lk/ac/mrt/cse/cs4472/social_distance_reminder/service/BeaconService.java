package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import static lk.ac.mrt.cse.cs4472.social_distance_reminder.application.SocialDistanceReminderApplication.HIGH_RISK_CHANNEL;
import static lk.ac.mrt.cse.cs4472.social_distance_reminder.application.SocialDistanceReminderApplication.MILD_RISK_CHANNEL;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.InternalBeaconConsumer;
import org.altbeacon.beacon.Region;

import java.util.Collections;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.R;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.constants.ArgumentConstants;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.DBHelper;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.db.SQLiteRepository;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.notification.CustomNotificationManager;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.util.BluetoothUtil;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.util.Util;

public class BeaconService extends Service implements InternalBeaconConsumer {

    private static final String TAG = "BeaconService";
    private static SQLiteRepository sqLiteRepository;

    private BeaconManager beaconManager;

    private NotificationManagerCompat notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sqLiteRepository = DBHelper.getInstance(this);
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "start beacon service");
        super.onStartCommand(intent, flags, startId);

        boolean startBeaconService = intent.getBooleanExtra(
                ArgumentConstants.EXTRA_START_BEACON_SERVICE_ENABLE, true);

        if(startBeaconService){
            startForeground(Util.getForegroundServiceId(),
                    CustomNotificationManager.createBeaconServiceNotification(
                            "Social Distance Reminder",
                            "Background Running...",
                            getApplicationContext()
                    ));

            String userUUID = intent.getStringExtra(
                    ArgumentConstants.EXTRA_START_BEACON_SERVICE_DEVICE_UUID);

            if(BluetoothUtil.isBluetoothEnabled(this)){
                scanBeacons();
                transmitBeacons(userUUID);
            }
        }
        else {
            stopForeground(true);
        }


        Log.d(TAG, "end of beacon service start command");
        return START_STICKY;
    }

    private void scanBeacons() {
        Log.d(TAG, "start scanning beacons");

        if (beaconManager == null) {
            beaconManager = BeaconManager.getInstanceForApplication(this);

            // To detect proprietary beacons, you must add a line like below corresponding to your beacon
            // type.  Do a web search for "setBeaconLayout" to get the proper expression.
            beaconManager.getBeaconParsers().add(new BeaconParser()
                    .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
            beaconManager.setEnableScheduledScanJobs(false);
            beaconManager.setBackgroundBetweenScanPeriod(0);
            beaconManager.setBackgroundScanPeriod(6000);
            beaconManager.bindInternal(this);
        }

    }

    private void transmitBeacons(String deviceUUID) {
        Log.d(TAG, "start transmitting beacons");

        Log.i(TAG, "DeviceUUID: " + deviceUUID);

        Beacon beacon = new Beacon.Builder()
                .setId1(deviceUUID)
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Collections.singletonList(0L))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT);
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(),
                beaconParser);
        beaconTransmitter.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

            @Override
            public void onStartFailure(int errorCode) {
                Log.e(TAG, "Advertisement start failed with code: " + errorCode);
            }

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i(TAG, "Advertisement start succeeded");
            }
        });


        Log.d(TAG, "end transmitting beacons");
    }


    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "receive beacon signals");

        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(
                (beacons, region) -> {
                    for (Beacon beacon : beacons) {
                        Log.i(TAG, "I see a beacon");
                        int riskLevel = Util.getRiskLevel(beacon.getDistance());
                        if(riskLevel != 0){
                            Log.d(TAG, Integer.valueOf(riskLevel).toString());
                            sqLiteRepository.saveSocialDistancingViolation(
                                    beacon.getId1().toString(), riskLevel);
                            if (riskLevel == 1) {
                                // high risk
                                Log.d("RISK", "high risk");
                                sendOnHighRiskChannel();
                            } else if (riskLevel == 2) {
                                // mild risk
                                Log.d("RISK", "mild risk");
                                sendOnMildRiskChannel();
                            } else {
                                // low risk
                                Log.d("RISK", "low risk");
                            }
                        }
                    }
                });
        try {
            beaconManager.setForegroundScanPeriod(6000);
            beaconManager.setForegroundBetweenScanPeriod(6000);
            beaconManager.updateScanPeriods();
            beaconManager.startRangingBeacons(new Region("myRangingUniqueId",
                    null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void sendOnHighRiskChannel() {
        Notification notification = new NotificationCompat.Builder(this, HIGH_RISK_CHANNEL)
                .setSmallIcon(R.drawable.bluetooth)
                .setContentTitle("HIGH RISK")
                .setContentText("Move to a less crowded place")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // have this if you support android version less than Oreo
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1500, notification);
    }

    private void sendOnMildRiskChannel() {
        Notification notification = new NotificationCompat.Builder(this, MILD_RISK_CHANNEL)
                .setSmallIcon(R.drawable.bluetooth)
                .setContentTitle("MILD RISK")
                .setContentText("Move to a less crowded place")
                .setPriority(NotificationCompat.PRIORITY_LOW) // have this if you support android version less than Oreo
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1500, notification);
    }

}
