package lk.ac.mrt.cse.cs4472.social_distance_reminder.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.net.InetAddress;

public class NetworkManagerService extends Service {

    private static NetworkManagerService networkManagerService;

    public static synchronized NetworkManagerService getInstance(){
        if(networkManagerService == null){
            networkManagerService = new NetworkManagerService();
        }
        return networkManagerService;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isConnectedToInternetAndAvailable(){
        return isNetworkConnected() && isInternetAvailable();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
