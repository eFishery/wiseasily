package wiseasily;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import java.util.List;
import wiseasily.source.SourceCallback;
import wiseasily.util.ScanFilter;


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

    public WisEasily(@NonNull Context context) {
        wifiDisabled = new WifiDisabled(context);
        connectWifi = new ConnectWifi(context);
        scanWifi = new ScanWifi(context);
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void connect(@NonNull String ssid,@NonNull final SourceCallback.WisEasilyCallback callback) {
        if(ssid.isEmpty()){
            callback.onError("SSID Cannot be Empty");
        }else {
            connectWifi.start(ssid, callback);
        }
    }

    public List<ScanResult> getWifiResult() {
        return mWifiManager.getScanResults();
    }

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

    public void enable(boolean enable,@NonNull SourceCallback.SuccessCallback callback){
        connectWifi.enableWifi(enable, callback);
    }
}
