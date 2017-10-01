package com.efishery.putrabangga.wifimodul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

import timber.log.Timber;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 9/2/17.
 */

public class ScanNetwork {

    private final Context context;
    private final WifiManager wifiManager;
    private ConnectorCallback.scanWifiCallback callback;

    public ScanNetwork(Context context){
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void scan(ConnectorCallback.scanWifiCallback callback){
        this.callback = callback;

        List<ScanResult> scanResults = wifiManager.getScanResults();
        if(scanResults.isEmpty()){
            IntentFilter i = new IntentFilter();
            i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(broadcastScanReceiver,i);
        }else {
            callback.onWifiScanned(scanResults);
        }
    }

    public void alwaysScan(ConnectorCallback.scanWifiCallback callback){
        this.callback = callback;

        List<ScanResult> scanResults = wifiManager.getScanResults();
        if(!scanResults.isEmpty()){
            callback.onWifiScanned(scanResults);
        }
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        i.addAction(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(broadcastAlwasysScanReceiver,i);
    }

    public void stopScan(){
        if(broadcastAlwasysScanReceivercontext!=null){
            broadcastAlwasysScanReceivercontext.unregisterReceiver(broadcastAlwasysScanReceiver);
        }
    }

    private BroadcastReceiver broadcastScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d("onReceiveBroadcast: " + intent.toString());
            context.unregisterReceiver(broadcastScanReceiver);
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };

    private Context broadcastAlwasysScanReceivercontext=null;

    private BroadcastReceiver broadcastAlwasysScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastAlwasysScanReceivercontext = context;
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };
}
