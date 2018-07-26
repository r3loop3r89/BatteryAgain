package com.shra1.batteryagain.dtos.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.shra1.batteryagain.dtos.BatteryEntry;

import java.util.List;

@Dao
public interface BatteryEntryDao {

    @Insert
    public long insertBatteryEntry(BatteryEntry e);

    @Query("SELECT * FROM BatteryEntry")
    List<BatteryEntry> getAllBatteryEntries();

    @Query("SELECT * FROM BatteryEntry where EntryOnEpoch > :fromMillis and EntryOnEpoch < :toMillis")
    List<BatteryEntry> getBatteryEntries(long fromMillis, long toMillis);
}
