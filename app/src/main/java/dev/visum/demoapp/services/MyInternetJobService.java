

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

import com.google.android.material.snackbar.Snackbar;

import java.security.KeyStore;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.visum.demoapp.R;
import dev.visum.demoapp.broadcastreceivers.DoNotStopInternetService;
import dev.visum.demoapp.broadcastreceivers.NetworkChangeReceiver;
import dev.visum.demoapp.data.api.GetDataService;
import dev.visum.demoapp.data.api.MozCarbonAPI;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.model.AddSaleModel;
import dev.visum.demoapp.model.NotificationType;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.services.offlineSales.OfflineSalesTasks;
import dev.visum.demoapp.utils.Tools;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Response;

public class MyInternetJobService extends JobService {
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (networkChangeReceiver == null) {
            new ConnectionStateMonitor().enable(getApplicationContext());
            registerNetworkBroadcast();
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

    class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {

        final NetworkRequest networkRequest;
        final MyInternetJobService myInternetJobService = new MyInternetJobService();


        public ConnectionStateMonitor() {
            networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build();
        }

        public void enable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerNetworkCallback(networkRequest, this);
        }

        @Override
        public void onAvailable(Network network) {
            // Do what you need to do here
            Log.d("Network ", "Something");

            OfflineSalesTasks.getInstance().sendNewSales(MyInternetJobService.this);
        }
    }
}
