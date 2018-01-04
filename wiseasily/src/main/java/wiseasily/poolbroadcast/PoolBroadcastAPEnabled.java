package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastAPEnabled extends BroadcastReceiver  {

    private final Context mContext;
    private final WifiManager mWifiManager;
    private SourceCallback.SuccessCallback isSuplicantCompletedCallback;

    public PoolBroadcastAPEnabled(@NonNull Context context, String ssid, @NonNull SourceCallback.SuccessCallback callback) {
        this.mContext = context;
        this.isSuplicantCompletedCallback = callback;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager!=null){
            int netId = WifiUtil.getNetId(ssid, mWifiManager);
            mWifiManager.enableNetwork(netId, true);
        }
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }

    private void stopListen(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
            if(mWifiManager.getConnectionInfo().getSupplicantState() == SupplicantState.COMPLETED){
                isSuplicantCompletedCallback.onSuccess();
                stopListen();
            }
        }
    }
}
