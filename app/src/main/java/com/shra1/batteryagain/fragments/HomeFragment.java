package com.shra1.batteryagain.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shra1.batteryagain.R;
import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.dtos.BatteryEntry;
import com.shra1.batteryagain.room.MRoom;
import com.shra1.batteryagain.services.BgService;
import com.shra1.batteryagain.utils.Utils;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.joda.time.DateTime;

import java.util.List;

public class HomeFragment extends Fragment {

    private static HomeFragment INSTANCE = null;
    LinearLayout llHomeFragment;
    Context mCtx;

    DateTime dateTime;
    ValueLineChart lineChart;
    private ImageButton ibDSLLeft;
    private ShraTextView stvDSLDate;
    private ImageButton ibDSLRight;

    public static HomeFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HomeFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCtx = container.getContext();
        View v = inflater.inflate(R.layout.home_fragment, container, false);
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
                ValueLineSeries series= new ValueLineSeries();
                for (BatteryEntry e:
                     ) {

                }
                DateTime lable = new DateTime()
                series.addPoint(new ValueLinePoint());

                lineChart.addSeries();
            }
        }, dateTime);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Utils.isMyServiceRunning(mCtx, BgService.class)) {
        } else {
            Snackbar.make(
                    getActivity().findViewById(R.id.llHomeFragment)
                    , "Service is not running", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Run", v1 -> {
                        Intent i = new Intent(mCtx, BgService.class);
                        mCtx.startService(i);
                        Toast.makeText(mCtx, "Service running", Toast.LENGTH_SHORT).show();
                    }).show();
        }
    }

    private void initViews(View v) {
        llHomeFragment = (LinearLayout) getActivity().getWindow().getDecorView().findViewById(R.id.llHomeFragment);

        ibDSLLeft = (ImageButton) v.findViewById(R.id.ibDSLLeft);
        stvDSLDate = (ShraTextView) v.findViewById(R.id.stvDSLDate);
        ibDSLRight = (ImageButton) v.findViewById(R.id.ibDSLRight);
        lineChart = (ValueLineChart) v.findViewById(R.id.lineChart);
    }
}
