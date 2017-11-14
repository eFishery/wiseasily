package wiseasily;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import java.util.List;

import wiseasily.source.SourceCallback;


/**
 * Bismillahirrahmanirrahim
 * Created by Innovation, eFishery  on 4/20/17.
 */

public class WisEasily {
    private final Activity activity;
    private final WifiManager mWifiManager;

    public WisEasily(Activity activity) {
        Context mContext = activity.getApplicationContext();
        this.activity = activity;
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void connect(@NonNull String ssid, final SourceCallback.WisEasilyCallback callback) {
        if(ssid.isEmpty()){
            callback.onError("SSID Cannot be Empty");
        }else {
            PoolBroadcast poolBroadcast = new PoolBroadcast(activity);
            poolBroadcast.getScanResult(new SourceCallback.CompleteDataCallback<List<ScanResult>>() {
                @Override
                public void onSuccess(List<ScanResult> data) {
                    if(!isScanResultsContainsSsid(ssid, data)){
                        callback.onError("Not Found Wifi");
                    }else {
                        int netId = getNetIdOfScanResultInWifiConfig(ssid);
                        if(netId==-1){
                            WifiConfiguration config = new WifiConfiguration();
                            config.SSID = getConfigFormatSSID(ssid);
                            config.status = WifiConfiguration.Status.ENABLED;
                            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

                            netId = mWifiManager.addNetwork(config);
                        }
                        mWifiManager.enableNetwork(netId, true);
                        poolBroadcast.isWifiTryToConnect(new SourceCallback.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                poolBroadcast.isWifiConnected(new SourceCallback.SuccessCallback() {
                                    @Override
                                    public void onSuccess() {
                                        callback.onSuccess();
                                    }

                                    @Override
                                    public void onOutTime() {
                                        callback.onError("Cannot Connect To Network");
                                    }
                                });
                            }

                            @Override
                            public void onOutTime() {
                                callback.onError("Cannot Connect To Network");
                            }
                        });
                    }
                }

                @Override
                public void onOutTime() {
                    callback.onError("Cannot Connect To Network");
                }
            });
        }
    }

    public void scanNetwork(Activity activity, SourceCallback.CompleteDataCallback<List<ScanResult>> callback) {
        new PoolBroadcast(activity).startScan(callback);
    }

    private boolean isScanResultsContainsSsid(@NonNull String ssid, @NonNull List<ScanResult> scanResults){
        boolean scanContainsSsid = false;
        for(ScanResult scanResult : scanResults){
            if( scanResult.SSID.equals(ssid)){
                scanContainsSsid = true;
                break;
            }
        }
        return scanContainsSsid;
    }

    private int getNetIdOfScanResultInWifiConfig(String ssid) {
        String ssidConfig = getConfigFormatSSID(ssid);
        if (mWifiManager != null) {
            for (WifiConfiguration wifiConfiguration : mWifiManager.getConfiguredNetworks()){
                if(wifiConfiguration.SSID.equals(ssidConfig)){
                    return wifiConfiguration.networkId;
                }
            }
        }
        return -1;
    }

    private String getConfigFormatSSID(String ssid){
        return  "\""+ ssid +"\"";
    }
}
