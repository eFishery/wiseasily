package wiseasily;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import wiseasily.poolbroadcast.PoolBroadcastAPEnabled;
import wiseasily.poolbroadcast.PoolBroadcastAPFound;
import wiseasily.poolbroadcast.PoolBroadcastWifiConnected;
import wiseasily.source.SourceCallback;
import wiseasily.util.ConnectionData;
import wiseasily.util.ConnectivityUtil;
import wiseasily.util.WifiUtil;

import static wiseasily.util.ConnectivityUtil.currentConnection;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

class ConnectWifi {
    private final Context mContext;
    private final WifiManager mWifiManager;
    private final WifiUtil wifiUtil;
    private final ConnectivityUtil connectivityUtil;
    private int prevConnection;
    private String ssidPrev;

    ConnectWifi(@NonNull Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiUtil = new WifiUtil();
        connectivityUtil = new ConnectivityUtil();
    }

    void start(@NonNull String ssid,@NonNull final SourceCallback.WisEasilyCallback callback) {
        if (ssid.isEmpty()) {
            callback.onError("SSID Cannot be Empty");
        } else {
            saveCurrentConnection(ssid);
            if(connectivityUtil.isConnectedToAP(ssid, mContext)){
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
                        backToPrevNetwork();
                        Log.d("Connect Wifi", "BroadcastAPEnabled onFail");
                        callback.onError("Can Not Connect To Wifi");
                    }
                });
            }

            @Override
            public void onAPNotFound() {
                backToPrevNetwork();
                Log.d("Connect Wifi", "BroadcastAPFound onAPNotFound");
                callback.onError("Not Found Wifi");
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

    private void saveCurrentConnection(String ssid) {
        prevConnection = currentConnection(mContext);
        if(prevConnection == ConnectionData.WIFI){
            if(connectivityUtil.isConnectedToAP(ssid, mContext)){
                Log.d("Connect Wifi", "connect to: "+ ssid);
                prevConnection = ConnectionData.EMPTY;
            }else {
                ssidPrev =wifiUtil.getCurrentWifi(mContext);
            }
        }
    }

    public void backToPrevNetwork(){
        if(prevConnection ==ConnectionData.EMPTY){
            disconnectedToAP();
            boolean success = wifiUtil.forgetCurrentNetwork(mContext);
            Log.d("Connect Wifi", "forgetCurrentSssid: ");
        }else if(prevConnection == ConnectionData.MOBILE){
            enableWifi(false);
            Log.d("Connect Wifi", "enable Mobile: ");
        }else {
            Log.d("Connect Wifi", "enable ssidprev :"+ ssidPrev);
            ProcessListenConnection(ssidPrev, new SourceCallback.WisEasilyCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }
    }
}
