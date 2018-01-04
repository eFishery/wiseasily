package wiseasily.util;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

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
}
