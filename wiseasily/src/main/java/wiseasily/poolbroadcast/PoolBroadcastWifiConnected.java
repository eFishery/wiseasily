package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;


import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

import static wiseasily.util.ConnectivityUtil.isConnectedToAP;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */


public class PoolBroadcastWifiConnected extends BroadcastReceiver  {

    private final Context mContext;
    private final String ssid;
    private SourceCallback.SuccessCallback isConnectivityAction;

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;
    }

    public void startListen(@NonNull SourceCallback.SuccessCallback callback){
        this.isConnectivityAction = callback;
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void stopListen(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            if(isConnectedToAP(ssid, context)){
                stopListen();
                isConnectivityAction.onSuccess();
            }
        }
    }
}