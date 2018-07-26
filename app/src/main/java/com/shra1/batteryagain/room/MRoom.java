package com.shra1.batteryagain.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.shra1.batteryagain.dtos.BatteryEntry;
import com.shra1.batteryagain.dtos.daos.BatteryEntryDao;

@Database(entities = {BatteryEntry.class}, version = 1, exportSchema = false)
public abstract class MRoom extends RoomDatabase {

    private static MRoom INSTANCE = null;
    public abstract BatteryEntryDao batteryEntryDao();

    public static MRoom getInstance(Context mCtx) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(mCtx, MRoom.class, "shra1").build();
        }
        return INSTANCE;
    }
}
