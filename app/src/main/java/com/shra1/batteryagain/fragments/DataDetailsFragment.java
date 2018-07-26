package com.shra1.batteryagain.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.shra1.batteryagain.R;
import com.shra1.batteryagain.adapters.BatteryDetailsListAdapter;
import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.dtos.BatteryEntry;
import com.shra1.batteryagain.room.MRoom;

import org.joda.time.DateTime;

import java.util.List;

public class DataDetailsFragment extends Fragment {

    private static DataDetailsFragment INSTANCE = null;
    ListView lvDataDetailsList;
    Context mCtx;
    DateTime dateTime;
    private ImageButton ibDSLLeft;
    private ShraTextView stvDSLDate;
    private ImageButton ibDSLRight;

    public static DataDetailsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataDetailsFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCtx = container.getContext();
        View v = inflater.inflate(R.layout.data_detail_fragment, container, false);

        initViews(v);

        dateTime = new DateTime();

        setDateNLoadData();

        ibDSLLeft.setOnClickListener(ib -> {
            dateTime = dateTime.minusDays(1);
            setDateNLoadData();
        });

        ibDSLRight.setOnClickListener(ib -> {
            dateTime = dateTime.plusDays(1);
            setDateNLoadData();
        });
        stvDSLDate.setOnClickListener(stv -> {
            dateTime = new DateTime();
            setDateNLoadData();
        });


        return v;
    }

    private void setDateNLoadData() {
        stvDSLDate.setText(dateTime.toString("dd-MMM-yyyy"));
        BatteryEntry.DBCommands.getBatteryEntriesFor(MRoom.getInstance(mCtx), new BatteryEntry.DBCommands.GetBatteryEntriesForCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(List<BatteryEntry> allBatteryEntries) {

                BatteryDetailsListAdapter adapter
                        = new BatteryDetailsListAdapter(mCtx, allBatteryEntries);
                lvDataDetailsList.setAdapter(adapter);

            }
        }, dateTime);
    }

    private void initViews(View v) {
        lvDataDetailsList = (ListView) v.findViewById(R.id.lvDataDetailsList);
        ibDSLLeft = (ImageButton) v.findViewById(R.id.ibDSLLeft);
        stvDSLDate = (ShraTextView) v.findViewById(R.id.stvDSLDate);
        ibDSLRight = (ImageButton) v.findViewById(R.id.ibDSLRight);

    }
}
