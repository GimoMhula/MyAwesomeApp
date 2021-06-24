package dev.visum.demoapp.services;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.security.KeyStore;

import dev.visum.demoapp.broadcastreceivers.DoNotStopInternetService;
import dev.visum.demoapp.broadcastreceivers.NetworkChangeReceiver;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;

public class MyInternetCheckAvailabilityService extends Service
{
    private JobScheduler jobScheduler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (jobScheduler == null) {
            createJob();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (jobScheduler == null && !isMyJobServiceRunning(MyInternetJobService.class)) {
            createJob();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isMyJobServiceRunning(Class<?> serviceClass) {
        JobScheduler manager = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo service : manager.getAllPendingJobs()) {
            if (serviceClass.getName().equals(service.getService().getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void createJob() {
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName jobService = new ComponentName(getPackageName(), MyInternetCheckAvailabilityService.class.getName());
        JobInfo jobInfo = new JobInfo.Builder(Constants.getInstance().MYJOBID,jobService).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY). build();
        jobScheduler.schedule(jobInfo);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Intent broadcastIntent = new Intent(this, DoNotStopInternetService.class);
        sendBroadcast(broadcastIntent);
    }
}
