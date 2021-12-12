package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.InternalBeaconConsumer;
import org.altbeacon.beacon.Region;

import java.util.Collections;
import java.util.UUID;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.notification.CustomNotificationManager;
import lk.ac.mrt.cse.cs4472.social_distance_reminder.util.Util;

public class BeaconService extends Service implements InternalBeaconConsumer {

    private static final String TAG = "BeaconService";

    @SuppressLint("StaticFieldLeak")
    private static BeaconService beaconServiceInstance;
    private BeaconManager beaconManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "start beacon service");
        super.onStartCommand(intent, flags, startId);


        startForeground(Util.getForegroundServiceId(),
                CustomNotificationManager.createBeaconServiceNotification(
                        "Social Distance Reminder",
                        "Background Running...",
                        getApplicationContext()
                ));

        scanBeacons();
        transmitBeacons();

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
            beaconManager.setBackgroundScanPeriod(60000);
            beaconManager.bindInternal(this);
        }

    }

    private void transmitBeacons() {
        Log.d(TAG, "start transmitting beacons");

        UUID deviceId =  Util.generateRandomUUID();

        Log.i(TAG, "Device Id "+ deviceId.toString());

        Beacon beacon = new Beacon.Builder()
                .setId1(deviceId.toString())
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
                Log.i(TAG, "============= Advertisement start succeeded. ===================");
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
                        Log.i(TAG, buildBeaconSeeMsg(beacon));
                    }
                });
        try {
            beaconManager.setForegroundScanPeriod(60000);
            beaconManager.setForegroundBetweenScanPeriod(60000);
            beaconManager.updateScanPeriods();
            beaconManager.startRangingBeacons(new Region("myRangingUniqueId",
                    null, null, null));
        } catch (RemoteException e) {

        }
    }


    private String buildBeaconSeeMsg(Beacon beacon){
        StringBuilder sb = new StringBuilder();
        sb.append("===================================================\n");
        sb.append("Beacon Detected ");
        sb.append("Beacon Id : ").append(beacon.getId1().toString());
        sb.append(" Distance : ").append(beacon.getDistance());
        sb.append("===================================================\n");
        return sb.toString();
    }
}
