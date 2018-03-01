package wiseasily;

import android.Manifest;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import java.util.List;

import wiseasily.source.SourceCallback;
import wiseasily.util.ScanFilter;

import static wiseasily.util.ConnectivityUtil.currentConnection;
import static wiseasily.util.ConnectivityUtil.isConnectedToAP;
import static wiseasily.util.ConnectivityUtil.isConnectedToAPContainsChar;


/**
 * Bismillahirrahmanirrahim
 * Created by Innovation, eFishery  on 4/20/17.
 */

public class WisEasily {

    @NonNull
    private final ConnectWifi connectWifi;
    @NonNull
    private final ScanWifi scanWifi;
    private final WifiDisabled wifiDisabled;
    private final WifiManager mWifiManager;
    private final Context context;

    public WisEasily(@NonNull Context context) {
        wifiDisabled = new WifiDisabled(context);
        connectWifi = new ConnectWifi(context);
        scanWifi = new ScanWifi(context);
        this.context = context;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET})
    public void connect(@NonNull String ssid, @NonNull final SourceCallback.WisEasilyCallback callback) {
        connectWifi.start(ssid, callback);
    }

    public List<ScanResult> getWifiResult() {
        return mWifiManager.getScanResults();
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void scan(@NonNull SourceCallback.WisEasilyScanCallback callback) {
        scanWifi.start(callback);
    }

    public void scanChangeInterval(int interval) {
        scanWifi.changeScanInterval(interval);
    }

    public void scanChangeFilter(ScanFilter scanFilter) {
        scanWifi.changeFilter(scanFilter);
    }

    public void stopScan() {
        scanWifi.stop();
    }

    public void startIsWifiDisable(@NonNull SourceCallback.WisEasilyWifiDisable callback){
        wifiDisabled.start(callback::onDisabled);
    }

    public boolean isWifiEnable(){
        return connectWifi.isWifiMEnable();
    }

    public boolean enableWifi(boolean enable){
        return connectWifi.enableWifi(enable);
    }

    public int getCurrentConnection(){
        return currentConnection(context);
    }

    public boolean disconnectedToSsid(){
        return connectWifi.disconnectedToAP();
    }

    public boolean isWifiConnectedToSsid(String ssid){
        return isConnectedToAP(ssid, context);
    }

    public boolean isWifiConnectedToSsidContainsChar(String character){
        return isConnectedToAPContainsChar(character, context);
    }
}
