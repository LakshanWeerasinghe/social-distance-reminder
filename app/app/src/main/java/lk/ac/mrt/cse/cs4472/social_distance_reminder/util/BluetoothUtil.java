package lk.ac.mrt.cse.cs4472.social_distance_reminder.util;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import lk.ac.mrt.cse.cs4472.social_distance_reminder.exception.BluetoothNotSupportException;

public class BluetoothUtil {

    private static final int REQUEST_BLUETOOTH_ENABLE = 1;
    private static BluetoothAdapter bluetoothAdapter;
    private static BroadcastReceiver broadcastReceiver;

    public BluetoothUtil(){

    }

    public static BluetoothAdapter getBluetoothAdapter() throws BluetoothNotSupportException {
        if(bluetoothAdapter == null){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter == null){
                throw new BluetoothNotSupportException("Bluetooth not supported in the device");
            }
        }
        return bluetoothAdapter;
    }

    public static boolean isBluetoothEnabled(Context context){
        try {
            bluetoothAdapter = getBluetoothAdapter();
        }
        catch (BluetoothNotSupportException exception){
            new AlertDialog.Builder(context)
                    .setTitle("Bluetooth Service")
                    .setMessage(exception.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            return false;
        }

        if(!bluetoothAdapter.isEnabled()){
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(enableBluetoothIntent);
        }

        return true;
    }

}
