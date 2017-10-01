package com.efishery.putrabangga.wifimodul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Bismillahirrahmanirrahim
 * Created by Innovation, eFishery  on 4/20/17.
 */

public class WifiConnector {
    private final WifiManager wifiManager;
    private final Context context;

    private String networkSSID;

    private ConnectorCallback.ConnectWifiCallback fcCallback;
    private String ssidBefore;
    private boolean wasEnabled;

    public WifiConnector(String SSID, Context context) {
        this.networkSSID = SSID;
        this.context = context;

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void connect(final ConnectorCallback.ConnectWifiCallback fcCallback) {
        Log.d("wifiConnector","Connect");
        enableWifi(true);
        this.fcCallback = fcCallback;


        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo.getNetworkId() != -1 && wifiInfo.getSSID().contains(networkSSID)){
            fcCallback.onWifiConnected();
        }else {
            Log.d("wifiConnector","not connect"+wifiInfo);
            ssidBefore = wifiInfo.getSSID();
            Log.d("wifiConnector","ssidBefore"+ssidBefore);
            if(wifiManager.getScanResults().isEmpty()){
                IntentFilter i = new IntentFilter();
                i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                context.registerReceiver(broadcastScanReceiver,i);
            }else {
                new EnableNetwork(context).enable(networkSSID, new ConnectorCallback.enableWifiCallback() {
                    @Override
                    public void onWifiEnabled() {
                        fcCallback.onWifiConnected();
                    }

                    @Override
                    public void onWifiFail(int errorCode) {
                        fcCallback.onWifiFail(errorCode);
                    }
                });
            }
        }
    }

    private BroadcastReceiver broadcastScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(broadcastScanReceiver);
            new EnableNetwork(context).enable(networkSSID, new ConnectorCallback.enableWifiCallback() {
                @Override
                public void onWifiEnabled() {
                    fcCallback.onWifiConnected();
                }

                @Override
                public void onWifiFail(int errorCode) {
                    fcCallback.onWifiFail(errorCode);
                }
            });
        }
    };
    private BroadcastReceiver broadcastScanReceiverBefore = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(broadcastScanReceiverBefore);
            new EnableNetwork(context).enable(ssidBefore, new ConnectorCallback.enableWifiCallback() {
                @Override
                public void onWifiEnabled() {
                    fcCallback.onWifiConnected();
                }

                @Override
                public void onWifiFail(int errorCode) {
                    fcCallback.onWifiFail(errorCode);
                }
            });
        }
    };

    private void enableWifi(boolean enable) {
        wasEnabled = wifiManager.isWifiEnabled();
        wifiManager.setWifiEnabled(enable);
        if(enable){
            while(!wifiManager.isWifiEnabled()){
                Log.d("wifiConnector","wifiIsDisable");
            }
        }else {
            while(wifiManager.isWifiEnabled()){
                Log.d("wifiConnector","wifiIsEnable");
            }
        }
    }
    public void finishConnect(final ConnectorCallback.FinishConnectWifiCallback callback){

        if(wasEnabled){
            Log.d("wifiConnector","ssidBefore "+ ssidBefore);
            Log.d("wifiConnector","ssidBefore "+ ssidBefore.replaceAll("^\"|\"$", ""));
            new EnableNetwork(context).enable(ssidBefore.replaceAll("^\"|\"$", ""), new ConnectorCallback.enableWifiCallback() {
                @Override
                public void onWifiEnabled() {
                    callback.onWifiFinishConnected();
                }

                @Override
                public void onWifiFail(int errorCode) {
                }
            });

        }else {
            wifiManager.disconnect();
        }
    }
}
