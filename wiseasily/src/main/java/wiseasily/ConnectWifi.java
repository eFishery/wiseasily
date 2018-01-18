package wiseasily;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import wiseasily.poolbroadcast.PoolBroadcastWifiConnected;
import wiseasily.poolbroadcast.PoolBroadcastAPFound;
import wiseasily.poolbroadcast.PoolBroadcastAPEnabled;
import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

class ConnectWifi {
    private final Context mContext;
    private final WifiManager mWifiManager;

    ConnectWifi(@NonNull Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    void start(@NonNull String ssid,@NonNull final SourceCallback.WisEasilyCallback callback) {
        Handler mListenHandler = new Handler();
        Runnable mOutOfTime = new Runnable() {
            @Override
            public void run() {
                callback.onError("Out Of Time");
            }
        };
        mListenHandler.postDelayed(mOutOfTime, 30000);
        new PoolBroadcastAPFound(mContext, ssid, new SourceCallback.APFoundCallback() {
            @Override
            public void onAPFound() {
                new PoolBroadcastAPEnabled(mContext, ssid, new SourceCallback.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        new PoolBroadcastWifiConnected(mContext, ssid, new SourceCallback.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                mListenHandler.removeCallbacks(mOutOfTime);
                                callback.onSuccess();
                            }
                        });
                    }
                });
            }

            @Override
            public void onAPNotFound() {
                mListenHandler.removeCallbacks(mOutOfTime);
                callback.onError("Not Found Wifi");
            }
        });
    }

    void enableWifi(boolean enable,@NonNull SourceCallback.SuccessCallback callback){
        mWifiManager.setWifiEnabled(enable);
        callback.onSuccess();
    }

    boolean isWifiMEnable(){
        return mWifiManager.isWifiEnabled();
    }
}
