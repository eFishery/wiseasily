package wiseasily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 04/01/18.
 */

public class WifiUtil {


    public static int getNetId(@NonNull String ssid,@NonNull WifiManager mWifiManager ){
        int netId = getNetIdOfScanResultInWifiConfig(ssid, mWifiManager.getConfiguredNetworks());
        if(netId==-1){
            netId = addNetwork(ssid, mWifiManager);
        }
        return netId;
    }

    private static int addNetwork(@NonNull String ssid,@NonNull WifiManager mWifiManager) {
        int netId;
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
        return netId;
    }

    public static boolean isScanResultsContainsSsid(@NonNull String ssid, @NonNull List<ScanResult> scanResults){
        boolean scanContainsSsid = false;
        for(ScanResult scanResult : scanResults){
            if( scanResult.SSID.equals(ssid)){
                scanContainsSsid = true;
                break;
            }
        }
        return scanContainsSsid;
    }

    private static int getNetIdOfScanResultInWifiConfig(String ssid, List<WifiConfiguration> wifiConfigurations) {
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

    public static String getConfigFormatSSID(String ssid){
        return  "\""+ ssid +"\"";
    }


    public static boolean isWifiConnectedToAP(String APSsid, Context context){
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager!=null){
            String ssidCurrent = mWifiManager.getConnectionInfo().getSSID();
            if(ssidCurrent!=null){
                Log.d("Connect Wifi","Active Network " + ssidCurrent);
                return getConfigFormatSSID(APSsid).equals(ssidCurrent);
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    public static boolean forgetCurrentNetwork(Context context){
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            int networkId = mWifiManager.getConnectionInfo().getNetworkId();
            mWifiManager.removeNetwork(networkId);
            return mWifiManager.saveConfiguration();
        }else {
            return false;
        }
    }
}
