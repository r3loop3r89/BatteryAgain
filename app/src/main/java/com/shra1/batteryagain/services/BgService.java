package com.shra1.batteryagain.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.shra1.batteryagain.dtos.BatteryEntry;
import com.shra1.batteryagain.room.MRoom;
import com.shra1.batteryagain.utils.Constants;
import com.shra1.batteryagain.utils.LongLog;
import com.shra1.batteryagain.utils.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.shra1.batteryagain.utils.Utils.TAG;

public class BgService extends Service {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    Context mCtx;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        scheduler.scheduleAtFixedRate(() -> {
            mCtx = getApplicationContext();
            int BatteryLevel = Utils.getBatteryPercentage(mCtx);
            BatteryEntry batteryEntry = new BatteryEntry(BatteryLevel, System.currentTimeMillis());
            BatteryEntry.DBCommands.insertBatteryEntry(batteryEntry, MRoom.getInstance(mCtx));
        }, 0, Constants.INTERVAL, TimeUnit.SECONDS);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LongLog.d(TAG, "Service destroyed");
        Intent intent = new Intent("UNKILLABLE");
        sendBroadcast(intent);
    }
}
