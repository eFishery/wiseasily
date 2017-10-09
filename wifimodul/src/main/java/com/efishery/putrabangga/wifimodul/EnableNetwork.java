package com.efishery.putrabangga.wifimodul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.List;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 9/2/17.
 */

public class EnableNetwork {
    private final WifiManager wifiManager;
    private final ConnectivityManager connectivityManager;
    private final Context context;
    private ConnectorCallback.enableWifiCallback callback;
    private String networkSSID;
    public CountDownTimer broadcastWifiReceiverCountDownTimer;
    private CountDownTimer broadcastNetworkReceiverCountDownTimer;

    public EnableNetwork(Context context){

        this.context = context;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void enable(String ssid, ConnectorCallback.enableWifiCallback callback) {
        this.callback = callback;
        this.networkSSID = ssid;
        Log.d("wifiConnector","enable networkSSID "+networkSSID);

        List<ScanResult> wireless = wifiManager.getScanResults();
        Log.d("wifiConnector","wireless"+wireless);
        boolean scanContainsSsid = false;
        for(ScanResult scanResult : wireless){
            if(scanResult.SSID != null && scanResult.SSID.equals(networkSSID)){
                scanContainsSsid = true;
                break;
            }
        }
        if(!scanContainsSsid){
            Log.d("wifiConnector","!scanContainsSsid");
            callback.onWifiFail(662);
        }else {
            Log.d("wifiConnector","scanContainsSsid");
            boolean configContainsSsid = false;
            String ssidConfig = "\""+networkSSID+"\"";
            for (WifiConfiguration wifiConfiguration : wifiManager.getConfiguredNetworks()){
                Log.d("wifiConnector","wifiConfiguration"+wifiConfiguration);
                if(wifiConfiguration.SSID != null && wifiConfiguration.SSID.equals(ssidConfig)){
                    configContainsSsid = true;
                    wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                    IntentFilter i = new IntentFilter();
                    i.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                    context.registerReceiver(broadcastWifiReceiver,i);
                    break;
                }
            }
            if(!configContainsSsid){
                WifiConfiguration config = new WifiConfiguration();
                config.SSID = ssidConfig;
                config.status = WifiConfiguration.Status.ENABLED;
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

                int id=wifiManager.addNetwork(config);
                wifiManager.enableNetwork(id, true);
            }

            IntentFilter i = new IntentFilter();
            i.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            context.registerReceiver(broadcastWifiReceiver,i);
            timeOut(30, new ConnectorCallback.timeOutCallback() {

                @Override
                public void onStartCountDown(CountDownTimer countDownTimer) {
                    broadcastWifiReceiverCountDownTimer = countDownTimer;
                }

                @Override
                public void onOutTime() {
                    context.unregisterReceiver(broadcastWifiReceiver);
                }
            });
        }
    }

    private BroadcastReceiver broadcastWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d("wifiConnector","wifiInfo"+wifiInfo);
            if(wifiInfo.getSupplicantState()== SupplicantState.COMPLETED){
                broadcastWifiReceiverCountDownTimer.cancel();
                Log.d("wifiConnector","COMPLETED");
                context.unregisterReceiver(broadcastWifiReceiver);
                IntentFilter i = new IntentFilter();
                i.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(broadcastNetworkReceiver,i);
                timeOut(30, new ConnectorCallback.timeOutCallback() {

                    @Override
                    public void onStartCountDown(CountDownTimer countDownTimer) {
                        broadcastNetworkReceiverCountDownTimer = countDownTimer;
                    }

                    @Override
                    public void onOutTime() {
                        context.unregisterReceiver(broadcastNetworkReceiver);
                    }
                });
            }
        }
    };

    private BroadcastReceiver broadcastNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            Log.d("wifiConnector","activeNetwork "+activeNetwork);
            String ssidConfig = "\""+networkSSID+"\"";
            Log.d("wifiConnector","ssidConfig "+ssidConfig);
            if(activeNetwork!=null && activeNetwork.getState() == NetworkInfo.State.CONNECTED && activeNetwork.getExtraInfo().equals(ssidConfig)){
                broadcastNetworkReceiverCountDownTimer.cancel();
                Log.d("wifiConnector","activeNetwork Connected into "+ssidConfig);
                context.unregisterReceiver(broadcastNetworkReceiver);
                callback.onWifiEnabled();
            }
        }
    };

    public static void timeOut(int timeout, final ConnectorCallback.timeOutCallback callback){
        CountDownTimer countDownTimer = new CountDownTimer(timeout*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("timeOut","onTick ");
            }

            public void onFinish() {
                callback.onOutTime();
            }
        }.start();
        callback.onStartCountDown(countDownTimer);
    }
}
