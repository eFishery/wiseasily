package wiseasily;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import wiseasily.poolbroadcast.PoolBroadcastAPEnabled;
import wiseasily.poolbroadcast.PoolBroadcastAPFound;
import wiseasily.poolbroadcast.PoolBroadcastWifiConnected;
import wiseasily.source.SourceCallback;

import static wiseasily.util.ConnectivityUtil.isConnectedToAP;
import static wiseasily.util.WifiUtil.isWifiConnectedToAP;

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
        if (ssid.isEmpty()) {
            callback.onError("SSID Cannot be Empty");
        } else {
            if(isConnectedToAP(ssid, mContext)){
                callback.onSuccess();
            }else {
                ProcessListenConnection(ssid, callback);
            }
        }
    }

    private void ProcessListenConnection(@NonNull String ssid, @NonNull SourceCallback.WisEasilyCallback callback) {
        PoolBroadcastAPFound poolBroadcastAPFound = new PoolBroadcastAPFound(mContext, ssid);
        poolBroadcastAPFound.startListen(new SourceCallback.APFoundCallback() {
            @Override
            public void onAPFound() {
                Log.d("Connect Wifi", "onAPFound");
                PoolBroadcastAPEnabled poolBroadcastAPEnabled = new PoolBroadcastAPEnabled(mContext, ssid);
                poolBroadcastAPEnabled.startListen(new SourceCallback.ConnectCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Connect Wifi", "poolBroadcastAPEnabled");
                        PoolBroadcastWifiConnected poolBroadcastWifiConnected = new PoolBroadcastWifiConnected(mContext, ssid);
                        Log.d("Connect Wifi", "poolBroadcastWifiConnected");
                        poolBroadcastWifiConnected.startListen(new SourceCallback.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                callback.onSuccess();
                            }
                        });
                    }

                    @Override
                    public void onFail() {
                        Log.d("Connect Wifi", "BroadcastAPEnabled onFail");
                        callback.onError("Can Not Connect To Wifi");
                    }
                });
            }

            @Override
            public void onAPNotFound() {
                Log.d("Connect Wifi", "BroadcastAPFound onAPNotFound");
                callback.onError("Not Found Wifi");
            }

            @Override
            public void onFail() {
                Log.d("Connect Wifi", "BroadcastAPFound onFail");
                callback.onError("Can Not Connect To Wifi");
            }
        });
    }

    private void poolbroadcastWifiConnected(@NonNull String ssid, @NonNull SourceCallback.WisEasilyCallback callback) {
        PoolBroadcastWifiConnected poolBroadcastWifiConnected = new PoolBroadcastWifiConnected(mContext, ssid);
        Log.d("Connect Wifi", "poolBroadcastWifiConnected");
        poolBroadcastWifiConnected.startListen(new SourceCallback.SuccessCallback() {
            @Override
            public void onSuccess() {
                Log.d("Connect Wifi", "poolBroadcastWifiConnected");
                callback.onSuccess();
            }
        });
    }

    boolean enableWifi(boolean enable){
        return mWifiManager.setWifiEnabled(enable);
    }

    boolean disconnectedToAP(){
        return mWifiManager.disconnect();
    }

    boolean isWifiMEnable(){
        return mWifiManager.isWifiEnabled();
    }
}
