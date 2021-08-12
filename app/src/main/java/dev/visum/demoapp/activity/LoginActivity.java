package dev.visum.demoapp.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import dev.visum.demoapp.R;
import dev.visum.demoapp.data.local.KeyStoreLocal;
import dev.visum.demoapp.fragment.LoginFragment;
import dev.visum.demoapp.model.BaseActivity;
import dev.visum.demoapp.services.MyInternetCheckAvailabilityService;
import dev.visum.demoapp.services.MyInternetJobService;
import dev.visum.demoapp.utils.Constants;
import dev.visum.demoapp.utils.Tools;

public class LoginActivity extends BaseActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Intent mServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);
         runJobService();
         // runService();
        enableMyLocation();
    }

    private void runJobService() {
        if (!Tools.isJobServiceOn(this)) {
            ComponentName serviceComponent = new ComponentName(this, MyInternetJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(Constants.getInstance().MYJOBID, serviceComponent);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            //builder.setPeriodic(500);
            builder.setMinimumLatency(10 * 1000); // Wait at least 30s
            builder.setOverrideDeadline(40 * 1000); // Maximum delay 60s
            builder.setPersisted(true);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());

            /*
            Job myJob = mDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(JOB_TAG)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(600, 600))
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

    mDispatcher.schedule(myJob);
            * */
        }
    }

    private void runService() {
        if (!isMyServiceRunning(MyInternetCheckAvailabilityService.class)) {
            mServiceIntent = new Intent(LoginActivity.this, MyInternetCheckAvailabilityService.class);
            startService(mServiceIntent);

            // bindService(mServiceIntent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startApp() {
        if (KeyStoreLocal.getInstance(this).getUser() != null || (KeyStoreLocal.getInstance(this).getToken() != null && KeyStoreLocal.getInstance(this).getUserId() != null)) {
            startActivity(MainActivity.class, null, null);
        } else {
            FragmentTransaction fragmentTransaction = getInstance().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, new LoginFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            Context context;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Permissões");
            alertDialog.setMessage("É necessário dar permissão para determinarmos a localização do utilizador no momento da venda!");
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (mServiceIntent != null) {
            stopService(mServiceIntent);
        }
        super.onDestroy();
    }

}
