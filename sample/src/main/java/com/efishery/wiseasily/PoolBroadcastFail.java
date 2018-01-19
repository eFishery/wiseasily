package com.efishery.wiseasily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastFail extends BroadcastReceiver  {

    private final Context mContext;
    private final WifiManager mWifiManager;
    private final ConnectivityManager mConnectivityManager;
    private messageCallback messageCallback;

    public PoolBroadcastFail(@NonNull Context context, @NonNull messageCallback callback) {
        this.mContext = context;
        this.messageCallback = callback;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mContext.registerReceiver(this, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }

    private void stopListen(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentString = intent.toString();
        if(intent.getAction()!=null){
            intentString = intentString + "\n" + intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                intentString = intentString + "\n" + state;
            }
            if(intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
                String supplicantState = mWifiManager.getConnectionInfo().getSupplicantState().toString();
                intentString = intentString + "\n" + supplicantState;
            }
            if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                intentString = intentString + "\n" + activeNetwork.toString();
            }
        }
        messageCallback.message(intentString);
    }

    interface messageCallback {
        void message(String s);
    }
}
