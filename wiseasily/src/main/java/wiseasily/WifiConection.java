package wiseasily;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import java.util.List;

import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

public class WifiConection extends PoolBroadcast{
    private final WifiManager mWifiManager;
    private final ConnectivityManager mConnectivityManager;
    private final Activity mActivity;

    public WifiConection(Activity activity) {
        super(activity.getApplicationContext());
        this.mActivity = activity;
        mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    protected void connectWifi(@NonNull String ssid, final SourceCallback.WisEasilyCallback callback) {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
        isScanResultsAvailableAction(new SourceCallback.SuccessCallback() {
            @Override
            public void onSuccess() {
                closePoolBroadcast();
                List<ScanResult> scanResults = mWifiManager.getScanResults();
                if(!isScanResultsContainsSsid(ssid, scanResults)){
                    callback.onError("Not Found Wifi");
                }else {
                    int netId = getNetIdOfScanResultInWifiConfig(ssid, mWifiManager.getConfiguredNetworks());
                    if(netId==-1){
                        netId = addNetwork(ssid);
                    }
                    mWifiManager.enableNetwork(netId, true);
                    isSuplicantComplete(new SourceCallback.SuccessCallback() {
                        @Override
                        public void onSuccess() {
                            if(mWifiManager.getConnectionInfo().getSupplicantState() == SupplicantState.COMPLETED){
                                closePoolBroadcast();
                                isConnectivityAction(new SourceCallback.SuccessCallback() {
                                    @Override
                                    public void onSuccess() {
                                        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                                        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().equals(getConfigFormatSSID(ssid))) {
                                            closePoolBroadcast();
                                            callback.onSuccess();
                                        }
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

            @Override
            public void onOutTime() {
                callback.onError("Cannot Connect To Network");
            }
        });
    }

    protected void scanWifi(SourceCallback.CompleteDataCallback<List<ScanResult>> callback){
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
        startScan(new SourceCallback.SuccessCallback() {
            @Override
            public void onSuccess() {
                if(mActivity!=null && !mActivity.isFinishing()){
                    mWifiManager.startScan();
                    callback.onSuccess(mWifiManager.getScanResults());
                }else {
                    closePoolBroadcast();
                }
            }

            @Override
            public void onOutTime() {
                callback.onOutTime();
            }
        });
    }

    protected void enableWifi(boolean enable, SourceCallback.SuccessCallback callback){
        mWifiManager.setWifiEnabled(enable);
        isConnectivityAction(new SourceCallback.SuccessCallback() {
            @Override
            public void onSuccess() {
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if(enable){
                    if(activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                        callback.onSuccess();
                    }
                }else {
                    if(activeNetwork == null || activeNetwork.getType() != ConnectivityManager.TYPE_WIFI){
                        callback.onSuccess();
                    }
                }
            }

            @Override
            public void onOutTime() {
                callback.onOutTime();
            }
        });
    }

    protected boolean isWifiMEnable(){
        return mWifiManager.isWifiEnabled();
    }

    private int addNetwork(@NonNull String ssid) {
        int netId;WifiConfiguration config = new WifiConfiguration();
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
        return netId;
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

    private int getNetIdOfScanResultInWifiConfig(String ssid, List<WifiConfiguration> wifiConfigurations) {
        String ssidConfig = getConfigFormatSSID(ssid);
        if (wifiConfigurations!=null) {
            for (WifiConfiguration wifiConfiguration : wifiConfigurations){
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
