package dev.visum.demoapp.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dev.visum.demoapp.services.MyInternetCheckAvailabilityService;
import dev.visum.demoapp.utils.Tools;

public class DoNotStopInternetService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(DoNotStopInternetService.class.getSimpleName(), "Continue internet service!");
        Tools.scheduleJob(context);
        // context.startService(new Intent(context, MyInternetCheckAvailabilityService.class));
    }
}
