package wiseasily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.List;

import static wiseasily.EnableNetwork.timeOut;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 9/2/17.
 */

class ScanNetwork {

    private final Context context;
    private final WifiManager wifiManager;
    private ConnectorCallback.scanWifiCallback callback;
    private CountDownTimer broadcastAlwaysScanReceiverCountDownTimer;
    private CountDownTimer broadcastScanReceiverCountDownTimer;
    private CountDownTimer broadcastScanWifiReceiverCountDownTimer;

    ScanNetwork(Context context){
        this.context = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    void scan(ConnectorCallback.scanWifiCallback callback){
        this.callback = callback;
        wifiManager.startScan();
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(broadcastScanReceiver,i);
        timeOut(15, new ConnectorCallback.timeOutCallback() {
            @Override
            public void onStartCountDown(CountDownTimer countDownTimer) {
                broadcastScanReceiverCountDownTimer = countDownTimer;
            }

            @Override
            public void onOutTime() {
                context.unregisterReceiver(broadcastScanReceiver);
                List<ScanResult> scanResults = wifiManager.getScanResults();
                callback.onWifiScanned(scanResults);
            }
        });
    }

    public void scanWifi(ConnectorCallback.scanWifiCallback callback){
        Log.d("scanWifi","scanWifi");
        this.callback = callback;
        wifiManager.startScan();
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(broadcastScanWifiReceiver,i);
        timeOut(15, new ConnectorCallback.timeOutCallback() {
            @Override
            public void onStartCountDown(CountDownTimer countDownTimer) {
                broadcastScanWifiReceiverCountDownTimer = countDownTimer;
            }

            @Override
            public void onOutTime() {
                Log.d("scanWifi","scanWifi Timeout");
                context.unregisterReceiver(broadcastScanWifiReceiver);
                List<ScanResult> scanResults = wifiManager.getScanResults();
                callback.onWifiScanned(scanResults);
            }
        });
    }

    public void alwaysScan(ConnectorCallback.scanWifiCallback callback){
        this.callback = callback;
        wifiManager.startScan();
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        i.addAction(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(broadcastAlwasysScanReceiver,i);
        timeOut(15, new ConnectorCallback.timeOutCallback() {
            @Override
            public void onStartCountDown(CountDownTimer countDownTimer) {
                broadcastAlwaysScanReceiverCountDownTimer = countDownTimer;
            }

            @Override
            public void onOutTime() {
                context.unregisterReceiver(broadcastAlwasysScanReceiver);
                callback.onWifiFail(0);
            }
        });
    }

    public void stopScan(){
        if(broadcastAlwasysScanReceivercontext!=null && broadcastAlwaysScanReceiverCountDownTimer !=null){
            broadcastAlwaysScanReceiverCountDownTimer.cancel();
            broadcastAlwasysScanReceivercontext.unregisterReceiver(broadcastAlwasysScanReceiver);
        }
    }

    private BroadcastReceiver broadcastScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastScanReceiverCountDownTimer.cancel();
            context.unregisterReceiver(broadcastScanReceiver);
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };

    private BroadcastReceiver broadcastScanWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastScanWifiReceiverCountDownTimer.cancel();
            context.unregisterReceiver(broadcastScanWifiReceiver);
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };
    private Context broadcastAlwasysScanReceivercontext=null;

    private BroadcastReceiver broadcastAlwasysScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastAlwaysScanReceiverCountDownTimer.cancel();
            broadcastAlwasysScanReceivercontext = context;
            callback.onWifiScanned(wifiManager.getScanResults());
        }
    };
}
