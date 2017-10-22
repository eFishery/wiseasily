package wiseasily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 9/2/17.
 */

public class WifiSignalNetwork {

    private final Context context;
    private final WifiManager wifiManager;
    private ConnectorCallback.scanWifiCallback callback;

    public WifiSignalNetwork(Context context){

        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    public void signal(ConnectorCallback.scanWifiCallback callback){
        this.callback = callback;

        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(broadcastSignalReceiver,i);
    }

    private BroadcastReceiver broadcastSignalReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(broadcastSignalReceiver);
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };
}
