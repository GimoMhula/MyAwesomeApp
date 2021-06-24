

package dev.visum.demoapp.services;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.security.KeyStore;

import dev.visum.demoapp.broadcastreceivers.DoNotStopInternetService;
import dev.visum.demoapp.broadcastreceivers.NetworkChangeReceiver;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.utils.Tools;

public class MyInternetJobService extends JobService {
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (networkChangeReceiver == null) {
            new ConnectionStateMonitor().enable(getApplicationContext());
            registerNetworkBroadcast();
        } else {
            sendNewSales();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }

        Intent broadcastIntent = new Intent(this, DoNotStopInternetService.class);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private void registerNetworkBroadcast() {
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.setPriority(100);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void sendNewSales() {
        Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
        if (Tools.isConnected(this)) {
            System.out.println("Sales: \n" + KeyStoreLocal.getInstance(this).getOfflineSales().toString());
        } else {
            System.out.println("Offline!");
        }
    }

    class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {

        final NetworkRequest networkRequest;


        public ConnectionStateMonitor() {
            networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
        }

        public void enable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerNetworkCallback(networkRequest, this);

            sendNewSales();
        }

        @Override
        public void onAvailable(Network network) {
            // Do what you need to do here
            Log.d("Network ", "Something");

            // wait 5s

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendNewSales();
        }

        private void sendNewSales() {
            if (Tools.isConnected(MyInternetJobService.this)) {
                System.out.println("Sales: \n" + KeyStoreLocal.getInstance(MyInternetJobService.this).getOfflineSales().toString());
            } else {
                System.out.println("Offline!");
            }
        }
    }
}
