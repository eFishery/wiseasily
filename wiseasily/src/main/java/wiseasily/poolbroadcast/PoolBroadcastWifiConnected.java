package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;


import wiseasily.source.SourceCallback;
import wiseasily.util.ConnectivityUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */


public class PoolBroadcastWifiConnected extends BroadcastReceiver  {

    private final Context mContext;
    private final String ssid;
    private final ConnectivityUtil connectivityUtil;
    private SourceCallback.SuccessCallback isConnectivityAction;

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;
        connectivityUtil = new ConnectivityUtil();
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
            if(android.os.Build.MODEL.equals("MITO A21 (A21)") || connectivityUtil.isConnectedToAP(ssid, context)){
                stopListen();
                isConnectivityAction.onSuccess();
            }
        }
    }
}