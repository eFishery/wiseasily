package com.efishery.wiseasily;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.efishery.putrabangga.wifi.R;

import java.util.ArrayList;
import java.util.List;

import static com.efishery.wiseasily.SignalUtil.getSignal;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 21/11/17.
 */

public class WifiAdapter extends BaseAdapter {

    private Context context;
    private List<ScanResult> scanResults;

    public WifiAdapter(Context context) {
        this.context = context;
        this.scanResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.wifi_scan, parent, false);
            holder = new ViewHolder();
            holder.ssid = (TextView) convertView.findViewById(R.id.ssid);
            holder.rssi = (TextView) convertView.findViewById(R.id.signal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scan = scanResults.get(position);
        holder.ssid.setText(scan.SSID);
        holder.rssi.setText(getSignal(scan.level));
        return convertView;
    }

    public void replaceData(List<ScanResult> data) {
        this.scanResults.clear();
        this.scanResults.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView ssid;
        TextView rssi;
        TextView distance;
    }

}