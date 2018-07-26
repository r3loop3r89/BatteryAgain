package com.shra1.batteryagain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shra1.batteryagain.R;
import com.shra1.batteryagain.customviews.ShraTextView;
import com.shra1.batteryagain.dtos.BatteryEntry;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.text.DecimalFormat;
import java.util.List;

public class BatteryDetailsListAdapter extends BaseAdapter {
    Context mCtx;
    List<BatteryEntry> l;

    public BatteryDetailsListAdapter(Context mCtx, List<BatteryEntry> l) {
        this.mCtx = mCtx;
        this.l = l;
    }


    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public Object getItem(int position) {
        return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        BDLAViewHolder h;
        if (v == null) {
            v = LayoutInflater.from(mCtx).inflate(R.layout.battery_details_list_item_layout, parent, false);
            h = new BDLAViewHolder(v);
            v.setTag(h);
        } else {
            h = (BDLAViewHolder) v.getTag();
        }
        BatteryEntry d = (BatteryEntry) getItem(position);
        h.tvBDLILSrNo.setText("" + d.getId());
        h.tvBDLILBatteryLevel.setText("" + d.getBatteryLevel() + " %");
        DateTime dt = new DateTime(d.getEntryOnEpoch());
        h.tvBDLILEntryTime.setText(dt.toString("hh:mm:ss aa"));

        try {
            DateTime pdt = new DateTime(l.get(position - 1).getEntryOnEpoch());
            Period period = new Period(pdt.getMillis(), dt.getMillis());
            DecimalFormat df = new DecimalFormat("00");
            h.tvBDLILEntryDifference.setText(
                    df.format(period.getHours()) + ":" + df.format(period.getMinutes()) + ":" + df.format(period.getSeconds())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    static class BDLAViewHolder {
        private ShraTextView tvBDLILSrNo;
        private ShraTextView tvBDLILEntryTime;
        private ShraTextView tvBDLILBatteryLevel;
        private ShraTextView tvBDLILEntryDifference;

        public BDLAViewHolder(View v) {
            tvBDLILSrNo = (ShraTextView) v.findViewById(R.id.tvBDLILSrNo);
            tvBDLILEntryTime = (ShraTextView) v.findViewById(R.id.tvBDLILEntryTime);
            tvBDLILBatteryLevel = (ShraTextView) v.findViewById(R.id.tvBDLILBatteryLevel);
            tvBDLILEntryDifference = (ShraTextView) v.findViewById(R.id.tvBDLILEntryDifference);
        }
    }
}
