package wiseasily.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 04/01/18.
 */

public class WifiUtil {


    public WifiUtil() {
    }

    boolean isSsidWifiConfigEqualsSsidConfig(String ssidConfig, String ssidWifiConfig) {
        return ssidConfig != null && !ssidConfig.isEmpty() && ssidWifiConfig != null && !ssidWifiConfig.isEmpty() && ssidConfig.equals(ssidWifiConfig);
    }

    public String getConfigFormatSSID(@NonNull String ssid){
        if(ssid!=null && !ssid.isEmpty()){
            return  "\""+ ssid +"\"";
        }
        return  "\"\"";
    }

    String removeQuotes(@NonNull String value){
        if(value!=null && !value.isEmpty()){
            return value.replaceAll("^\"|\"$", "");
        }
        return "";
    }

    int getNetIdOfScanResultInWifiConfig(@NonNull String ssid,@NonNull List<WifiConfiguration> wifiConfigurations) {
        if(ssid!=null && !ssid.isEmpty() && wifiConfigurations!=null && !wifiConfigurations.isEmpty()){
            String ssidConfig = getConfigFormatSSID(ssid);
            for (WifiConfiguration wifiConfiguration : wifiConfigurations){
                if(isSsidWifiConfigEqualsSsidConfig(ssidConfig, wifiConfiguration.SSID)){
                    return wifiConfiguration.networkId;
                }
            }
        }
        return -1;
    }

    public int getNetId(@NonNull String ssid, @NonNull WifiManager mWifiManager ){
        if(ssid!=null && !ssid.isEmpty() && mWifiManager!=null){
            int netId = getNetIdOfScanResultInWifiConfig(ssid, mWifiManager.getConfiguredNetworks());
            if(netId==-1){
                netId = addNetwork(ssid, mWifiManager);
            }
            return netId;
        }
        return -1;
    }

    int addNetwork(@NonNull String ssid,@NonNull WifiManager mWifiManager) {
        if(ssid!=null && !ssid.isEmpty() && mWifiManager!=null){
            int netId;
            WifiConfiguration config = new WifiConfiguration();
            //clear alloweds
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();

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
        return -1;
    }

    public boolean isScanResultsContainsSsid(@NonNull String ssid, @NonNull List<ScanResult> scanResults){
        if(ssid!=null && !ssid.isEmpty() && scanResults!=null && !scanResults.isEmpty()){
            for(ScanResult scanResult : scanResults){
                Log.d("Connect Wifi", "Pool AP Finding "+ scanResult.SSID + ssid);
                if( scanResult.SSID.equals(ssid)){
                    return true;
                }
            }
        }
        return false;
    }


    boolean isWifiConnectedToAP(@NonNull String APSsid,@NonNull Context context){
        if(APSsid!=null && !APSsid.isEmpty() && context!=null){
            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(mWifiManager!=null){
                String ssidCurrent = mWifiManager.getConnectionInfo().getSSID();
                if(ssidCurrent!=null){
                    Log.d("Connect Wifi","Active Network " + ssidCurrent);
                    return getConfigFormatSSID(APSsid).equals(ssidCurrent);
                }
            }
        }
        return false;
    }

    public boolean forgetCurrentNetwork(@NonNull Context context){
        if(context!=null){
            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                int networkId = mWifiManager.getConnectionInfo().getNetworkId();
                mWifiManager.removeNetwork(networkId);
                return mWifiManager.saveConfiguration();
            }
        }
        return false;
    }

    public String getCurrentWifi(@NonNull Context context){
        if(context!=null){
            WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                return removeQuotes(mWifiManager.getConnectionInfo().getSSID());
            }
        }
        return "";
    }
}
