package com.votum.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {
        // This is where you start your service
        aContext.startService(new Intent(aContext, MyService.class));
    }
}