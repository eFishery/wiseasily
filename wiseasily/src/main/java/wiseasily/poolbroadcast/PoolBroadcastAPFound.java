package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastAPFound extends BroadcastReceiver  {

    private final Context mContext;
    private final String ssid;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    private SourceCallback.APFoundCallback apFoundCallback;
    private final WifiManager mWifiManager;

    public PoolBroadcastAPFound(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;

        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
    }

    public void startListen( @NonNull SourceCallback.APFoundCallback callback){
        this.apFoundCallback = callback;
        if(mWifiManager!=null){
            mWifiManager.setWifiEnabled(true);
            mWifiManager.startScan();
            Log.d("Connect Wifi", "AP Find startListen");
            mContext.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            poolBroadcastWifiOff.startListen(new PoolBroadcastWifiOff.ConnectWifiFail() {
                @Override
                public void onWifiOff() {
                    stopListen();
                    callback.onFail();
                }
            });
        }
    }

    public void stopListen(){
        try  {
            mContext.unregisterReceiver(this);
        }
        catch (IllegalArgumentException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
            stopListenAll();
            Log.d("Connect Wifi", "Pool AP Found "+ mWifiManager.getScanResults().toString());
            if(!WifiUtil.isScanResultsContainsSsid(ssid, mWifiManager.getScanResults())){
                apFoundCallback.onAPNotFound();
            }else {
                apFoundCallback.onAPFound();
            }
        }
    }

    private void stopListenAll() {
        if(poolBroadcastWifiOff!=null){
            poolBroadcastWifiOff.stopListen();
        }
        stopListen();
    }
}
