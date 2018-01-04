package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastAPFound extends BroadcastReceiver  {

    private final Context mContext;
    private final String ssid;
    private SourceCallback.APFoundCallback apFoundCallback;
    private final WifiManager mWifiManager;

    public PoolBroadcastAPFound(@NonNull Context context, @NonNull String ssid, @NonNull SourceCallback.APFoundCallback callback) {
        this.mContext = context;
        this.ssid = ssid;
        this.apFoundCallback = callback;

        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager!=null){
            mWifiManager.setWifiEnabled(true);
            mWifiManager.startScan();
            mContext.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        }
    }

    private void stopListen(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
            if(!WifiUtil.isScanResultsContainsSsid(ssid, mWifiManager.getScanResults())){
                apFoundCallback.onAPNotFound();
            }else {
                stopListen();
                apFoundCallback.onAPFound();
            }
        }
    }
}
