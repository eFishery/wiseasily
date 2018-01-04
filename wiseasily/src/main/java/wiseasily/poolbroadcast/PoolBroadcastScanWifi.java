package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastScanWifi extends BroadcastReceiver  {

    private final Context mContext;
    private final WifiManager mWifiManager;
    private SourceCallback.WisEasilyScanCallback scanCallback;

    public PoolBroadcastScanWifi(@NonNull Context context, @NonNull SourceCallback.WisEasilyScanCallback callback) {
        this.mContext = context;
        this.scanCallback = callback;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void startScanning(boolean getInstantResult){
        if (mWifiManager != null) {
            reScan();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            mContext.registerReceiver(this, intentFilter);
            if(getInstantResult){
                scanCallback.onAPChanged(mWifiManager.getScanResults());
            }
        }
    }

    public void stopScanning(){
        mContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) || intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) || intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION))){
            scanCallback.onAPChanged(mWifiManager.getScanResults());
            reScan();

        }
    }

    private void reScan() {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
    }
}
