package com.shra1.batteryagain.dtos;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.shra1.batteryagain.room.MRoom;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Entity
public class BatteryEntry {
    @PrimaryKey(autoGenerate = true)
    long Id;
    int BatteryLevel;
    long EntryOnEpoch;

    public BatteryEntry(int batteryLevel, long entryOnEpoch) {

        BatteryLevel = batteryLevel;
        EntryOnEpoch = entryOnEpoch;
    }

    public BatteryEntry() {

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getBatteryLevel() {
        return BatteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        BatteryLevel = batteryLevel;
    }

    public long getEntryOnEpoch() {
        return EntryOnEpoch;
    }

    public void setEntryOnEpoch(long entryOnEpoch) {
        EntryOnEpoch = entryOnEpoch;
    }

    public static class DBCommands {
        public static void insertBatteryEntry(BatteryEntry e, MRoom db) {
            Observable.fromCallable(() -> db.batteryEntryDao().insertBatteryEntry(e)).subscribe();


        }

        public static void getAllBatteryEntries(MRoom db, GetAllBatteryEntriesCallback c) {
            c.onStart();
            //<editor-fold desc="RX CODE">
            Observable.fromCallable(new Callable<List<BatteryEntry>>() {
                @Override
                public List<BatteryEntry> call() throws Exception {
                    return db.batteryEntryDao().getAllBatteryEntries();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<BatteryEntry>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<BatteryEntry> batteryEntries) {
                            c.onComplete(batteryEntries);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            //</editor-fold>

        }

        public static void getBatteryEntriesFor(MRoom db, GetBatteryEntriesForCallback c, DateTime dt) {
            c.onStart();
            Observable.fromCallable(new Callable<List<BatteryEntry>>() {
                @Override
                public List<BatteryEntry> call() throws Exception {
                    DateTime from = dt;
                    from = from.withHourOfDay(00);
                    from = from.withMinuteOfHour(00);
                    from = from.withSecondOfMinute(00);
                    from = from.withMillisOfSecond(00);

                    DateTime to = dt;
                    to = to.withHourOfDay(23);
                    to = to.withMinuteOfHour(59);
                    to = to.withSecondOfMinute(59);
                    to = to.withMillisOfSecond(999);


                    return db.batteryEntryDao().getBatteryEntries(from.getMillis(), to.getMillis());
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<BatteryEntry>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<BatteryEntry> batteryEntries) {
                            c.onComplete(batteryEntries);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        public interface GetAllBatteryEntriesCallback {
            public void onStart();

            public void onComplete(List<BatteryEntry> allBatteryEntries);
        }

        public interface GetBatteryEntriesForCallback {
            public void onStart();

            public void onComplete(List<BatteryEntry> allBatteryEntries);
        }
    }
}
