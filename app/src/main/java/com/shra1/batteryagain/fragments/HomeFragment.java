package com.shra1.batteryagain.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.shra1.batteryagain.R;
import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.dtos.BatteryEntry;
import com.shra1.batteryagain.room.MRoom;
import com.shra1.batteryagain.services.BgService;
import com.shra1.batteryagain.utils.Utils;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private static HomeFragment INSTANCE = null;
    LinearLayout llHomeFragment;
    Context mCtx;

    DateTime dateTime;

    private ImageButton ibDSLLeft;
    private ShraTextView stvDSLDate;
    private ImageButton ibDSLRight;
    private XYPlot plot;


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
                Number[] a = new Number[allBatteryEntries.size()];
                int i = 0;
                for (BatteryEntry e : allBatteryEntries) {
                    a[i] = e.getBatteryLevel();
                    i++;
                }
                LineAndPointFormatter series1Format =
                        new LineAndPointFormatter(mCtx, R.xml.line_point_formatter_with_labels);
                /*LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);*/
                /*series1Format.setInterpolationParams(
                        new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));*/
                XYSeries xySeries = new SimpleXYSeries(Arrays.asList(a), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Battery");
                plot.addSeries(xySeries, series1Format);
                plot.redraw();

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
        plot = (XYPlot) v.findViewById(R.id.plot);

    }
}
