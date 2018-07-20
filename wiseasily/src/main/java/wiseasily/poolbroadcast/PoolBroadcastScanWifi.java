package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.efishery.putrabangga.wiseasily.BuildConfig;

import java.util.ArrayList;

import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastScanWifi extends BroadcastReceiver  {

    private static final int INTERVAL_IMMEDIATE = 0;
    private long mLastScanTime;

    private final Context mContext;
    private final WifiManager mWifiManager;
    private SourceCallback.WisEasilyScanCallback scanCallback;
    private int mScanInterval; //millis

    private Handler mScanHandler = new Handler();
    private Runnable mScanRunnable = new Runnable() {
        @Override
        public void run() {
            initScan();
        }
    };

    public PoolBroadcastScanWifi(Context mContext, int mScanInterval, SourceCallback.WisEasilyScanCallback scanCallback) {
        this.mContext = mContext;
        this.mScanInterval = mScanInterval;
        this.scanCallback = scanCallback;
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public PoolBroadcastScanWifi(@NonNull Context context, @NonNull SourceCallback.WisEasilyScanCallback callback) {
        this(context, INTERVAL_IMMEDIATE, callback);
    }

    public void startScanning(boolean getInstantResult){
        if (mWifiManager != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
            intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            mContext.registerReceiver(this, intentFilter);
            scan();
            mLastScanTime = System.currentTimeMillis();
            if(getInstantResult){
                scanCallback.onAPChanged(mWifiManager.getScanResults());
            }
        }
    }

    private void initScan() {
        long scanTime = System.currentTimeMillis();

        long scanDelay = scanTime - mLastScanTime;
        //Log.d(TAG, "scan delay: " + scanDelay);
        if (mScanInterval == INTERVAL_IMMEDIATE || scanDelay >= mScanInterval) {
            scan();
            mLastScanTime = scanTime;
        } else {
            mScanHandler.removeCallbacks(mScanRunnable);
            mScanHandler.postDelayed(mScanRunnable, mScanInterval - scanDelay);
        }
    }

    public void stopScanning(){
        try  {
            mScanHandler.removeCallbacks(mScanRunnable);
            mContext.unregisterReceiver(this);
        }
        catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void changeScanInterval(int scanInterval) {
        if (scanInterval < 0) {
            throw new IllegalArgumentException("mScanInterval cannot be negative");
        }
        mScanInterval = scanInterval;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) || intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION) || intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION))){
            initScan();
            if(mWifiManager!=null){
                if(mWifiManager.getScanResults()!=null && !mWifiManager.getScanResults().isEmpty()){
                    scanCallback.onAPChanged(mWifiManager.getScanResults());
                }else {
                    scanCallback.onAPChanged(new ArrayList<>());
                }
            }

        }
    }

    private void scan() {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
    }
}
