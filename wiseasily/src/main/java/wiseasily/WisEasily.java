package wiseasily;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.R;

import java.util.List;
import java.util.Locale;

import static wiseasily.EnableNetwork.isScanContainsSsid;

/**
 * Bismillahirrahmanirrahim
 * Created by Innovation, eFishery  on 4/20/17.
 */

public class WisEasily {
    private final WifiManager wifiManager;
    private final Context context;

    private String networkSSID;

    private ConnectorCallback.ConnectWifiCallback fcCallback;

    public WisEasily(Context context) {
        this.context = context;

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void connect(String ssid, final ConnectorCallback.ConnectWifiCallback fcCallback) {
        if(ssid == null || ssid.isEmpty()){
            fcCallback.onWifiFail(0);
        }else {
            this.networkSSID = ssid;
            fcCallback.onProgress(String.format(Locale.ENGLISH, context.getString(R.string.connecting), networkSSID));
            Log.d("wifiConnector","Connect");
            if(wifiManager.isWifiEnabled()){
                if(isAlreadyConnectToSSID()){
                    fcCallback.onWifiConnected();
                }else {
                    if(isScanContainsSsid(networkSSID, wifiManager.getScanResults())){
                        scanToEnable(fcCallback);
                    }else {
                        enable(fcCallback);
                    }
                }
            }else {
                enableWifi(true);
                scanToEnable(fcCallback);
            }
        }
    }

    private void scanToEnable(final ConnectorCallback.ConnectWifiCallback fcCallback) {
        new ScanNetwork(context).scan(new ConnectorCallback.scanWifiCallback() {
            @Override
            public void onWifiScanned(List<ScanResult> scanResults) {
                enable(fcCallback);
            }

            @Override
            public void onWifiFail(int errorCode) {
                finishConnect(() -> fcCallback.onWifiFail(errorCode));
            }
        });
    }

    private void enable(final ConnectorCallback.ConnectWifiCallback fcCallback) {
        new EnableNetwork(context).enable(networkSSID, new ConnectorCallback.enableWifiCallback() {
            @Override
            public void onWifiEnabled() {
                fcCallback.onWifiConnected();
            }

            @Override
            public void onWifiFail(int errorCode) {
                finishConnect(() -> fcCallback.onWifiFail(errorCode));
            }
        });
    }

    private void enableWifi(boolean enable) {
        wifiManager.setWifiEnabled(enable);
    }

    private boolean isAlreadyConnectToSSID(){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return (wifiInfo.getNetworkId() != -1 && wifiInfo.getSSID().contains(networkSSID));
    }

    private void finishConnect(final ConnectorCallback.FinishConnectWifiCallback callback){
        enableWifi(false);
        callback.onWifiFinishConnected();
    }
}
