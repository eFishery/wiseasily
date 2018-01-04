package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastWifiConnected extends BroadcastReceiver  {

    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
    private final String ssid;
    private SourceCallback.SuccessCallback isConnectivityAction;

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid, @NonNull SourceCallback.SuccessCallback callback) {
        this.mContext = context;
        this.ssid = ssid;
        this.isConnectivityAction = callback;
        mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void stopListen(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().equals(WifiUtil.getConfigFormatSSID(ssid))) {
                stopListen();
                isConnectivityAction.onSuccess();
            }
        }
    }
}
