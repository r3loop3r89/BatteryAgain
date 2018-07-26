package com.shra1.batteryagain.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shra1.batteryagain.services.BgService;

public class ServiceRetainerReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, BgService.class);

        context.startService(i);

    }
}
