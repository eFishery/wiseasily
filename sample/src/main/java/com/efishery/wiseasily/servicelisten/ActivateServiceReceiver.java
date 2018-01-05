package com.efishery.wiseasily.servicelisten;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * @author Jonas Sevcik
 */
public class ActivateServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) || WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            Log.d("ActiveServiceReceiver", "WIFI_STATE_CHANGED_ACTION");
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null && (wifiManager.isWifiEnabled())) {
                Log.d("ActiveServiceReceiver", "wifiEnable");
                context.startService(new Intent(context, ScanService.class));
            }
        }
    }
}
