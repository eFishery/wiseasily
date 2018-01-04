package wiseasily;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import wiseasily.poolbroadcast.PoolBroadcastWifiConnected;
import wiseasily.poolbroadcast.PoolBroadcastAPFound;
import wiseasily.poolbroadcast.PoolBroadcastScanWifi;
import wiseasily.poolbroadcast.PoolBroadcastAPEnabled;
import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

class WifiConection {
    private final Context mContext;
    private final WifiManager mWifiManager;

    WifiConection(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    void connectWifi(@NonNull String ssid, final SourceCallback.WisEasilyCallback callback) {
        Handler mListenHandler = new Handler();
        Runnable mOutOfTime = () -> callback.onError("OutOfTime");
        mListenHandler.postDelayed(mOutOfTime, 30000);
        new PoolBroadcastAPFound(mContext, ssid, new SourceCallback.APFoundCallback() {
            @Override
            public void onAPFound() {
                new PoolBroadcastAPEnabled(mContext, ssid, () -> new PoolBroadcastWifiConnected(mContext, ssid, () -> {
                    mListenHandler.removeCallbacks(mOutOfTime);
                    callback.onSuccess();
                }));
            }

            @Override
            public void onAPNotFound() {
                mListenHandler.removeCallbacks(mOutOfTime);
                callback.onError("Not Found Wifi");
            }
        });
    }

    void scanWifi(SourceCallback.WisEasilyScanCallback callback){
        PoolBroadcastScanWifi poolBroadcastScanWifi = new PoolBroadcastScanWifi(mContext, callback);
        poolBroadcastScanWifi.startScanning(true);
    }

    void enableWifi(boolean enable, SourceCallback.SuccessCallback callback){
        mWifiManager.setWifiEnabled(enable);
        callback.onSuccess();
    }

    boolean isWifiMEnable(){
        return mWifiManager.isWifiEnabled();
    }
}
