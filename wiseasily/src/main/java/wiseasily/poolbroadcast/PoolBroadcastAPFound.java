package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
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
    private int totalScan = 3;
    private int timeoutSecond = 25;
    private final WifiUtil wifiUtil;
    private SourceCallback.APFoundCallback apFoundCallback;
    private final WifiManager mWifiManager;
    private Handler mHandler;
    private int count = 0;
    private final Runnable mOutOfTime = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mOutOfTime);
            if(apFoundCallback!=null){
                if(!wifiUtil.isScanResultsContainsSsid(ssid, mWifiManager.getScanResults())){
                    apFoundCallback.onAPNotFound();
                }else {
                    apFoundCallback.onAPNotFound();
                }
            }
        }
    };

    public PoolBroadcastAPFound(@NonNull Context context, int timeoutSecond, int totalScan, @NonNull String ssid) {
        if(timeoutSecond!=0){
            this.timeoutSecond = timeoutSecond;
        }
        if(totalScan!=0){
            this.totalScan = totalScan;
        }
        wifiUtil = new WifiUtil();
        this.mContext = context;
        this.ssid = ssid;
        mHandler = new Handler(Looper.getMainLooper());
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
    }

    public PoolBroadcastAPFound(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;
        mHandler = new Handler(Looper.getMainLooper());
        wifiUtil = new WifiUtil();
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
    }

    public void startListen( @NonNull SourceCallback.APFoundCallback callback){
        if(this.ssid!=null && !this.ssid.isEmpty()){
            this.apFoundCallback = callback;
            if(mWifiManager!=null){
                mWifiManager.setWifiEnabled(true);
                mWifiManager.startScan();
                Log.d("Connect Wifi", "AP Find startListen");
                mContext.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                poolBroadcastWifiOff.startListen(() -> {
                    stopListen();
                    callback.onAPNotFound();
                });
                postDelay();
            }
        }else {
            callback.onAPNotFound();
        }
    }

    private void postDelay() {
        mHandler.postDelayed(mOutOfTime, timeoutSecond*1000);
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
            mHandler.removeCallbacks(mOutOfTime);
            Log.d("Connect Wifi", "Pool AP Found "+ mWifiManager.getScanResults().toString());
            if(!wifiUtil.isScanResultsContainsSsid(ssid, mWifiManager.getScanResults())){
                count++;
                if(count>totalScan){
                    count=0;
                    apFoundCallback.onAPNotFound();
                }else {
                    postDelay();
                }
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
