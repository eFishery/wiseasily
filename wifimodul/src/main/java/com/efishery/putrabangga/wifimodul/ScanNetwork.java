package com.efishery.putrabangga.wifimodul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;

import java.util.List;

import static com.efishery.putrabangga.wifimodul.EnableNetwork.timeOut;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 9/2/17.
 */

public class ScanNetwork {

    private final Context context;
    private final WifiManager wifiManager;
    private ConnectorCallback.scanWifiCallback callback;
    private CountDownTimer broadcastAlwasysScanReceiverCountDownTimer;

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
        timeOut(30, new ConnectorCallback.timeOutCallback() {
            @Override
            public void onStartCountDown(CountDownTimer countDownTimer) {
                broadcastAlwasysScanReceiverCountDownTimer = countDownTimer;
            }

            @Override
            public void onOutTime() {
                context.unregisterReceiver(broadcastAlwasysScanReceiver);
            }
        });
    }

    public void stopScan(){
        if(broadcastAlwasysScanReceivercontext!=null && broadcastAlwasysScanReceiverCountDownTimer!=null){
            broadcastAlwasysScanReceiverCountDownTimer.cancel();
            broadcastAlwasysScanReceivercontext.unregisterReceiver(broadcastAlwasysScanReceiver);
        }
    }

    private BroadcastReceiver broadcastScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(broadcastScanReceiver);
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };

    private Context broadcastAlwasysScanReceivercontext=null;

    private BroadcastReceiver broadcastAlwasysScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastAlwasysScanReceiverCountDownTimer.cancel();
            broadcastAlwasysScanReceivercontext = context;
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };
}
