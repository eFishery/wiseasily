package wiseasily;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcast extends BroadcastReceiver  {

    private final WifiManager mWifiManager;
    private final Context mContext;
    private Activity mActivity;
    private SourceCallback.CompleteDataCallback<List<ScanResult>> startScanCallback;
    private SourceCallback.SuccessCallback isWifiConnectCallback;
    private SourceCallback.SuccessCallback isWifiReadyToActionCallback;
    private SourceCallback.CompleteDataCallback<List<ScanResult>> getScanResultCallback;

    public PoolBroadcast(@NonNull Activity activity) {
        this.mActivity = activity;
        this.mContext = mActivity.getApplicationContext();
        mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void getScanResult(SourceCallback.CompleteDataCallback<List<ScanResult>> callback){
        this.getScanResultCallback = callback;
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    private void initScan(){
        mWifiManager.startScan();
    }

    public void startScan(SourceCallback.CompleteDataCallback<List<ScanResult>> callback){
        this.startScanCallback = callback;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContext.registerReceiver(this, intentFilter);
        initScan();
    }

    public void isWifiTryToConnect(SourceCallback.SuccessCallback callback){
        this.isWifiConnectCallback = callback;
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
    }

    public void isWifiConnected(SourceCallback.SuccessCallback callback){
        this.isWifiReadyToActionCallback = callback;
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null){
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                if(startScanCallback!=null){
                    if(mActivity!=null && !mActivity.isFinishing()){
                        startScanCallback.onSuccess(mWifiManager.getScanResults());
                        initScan();
                    }else {
                        mContext.unregisterReceiver(this);
                    }
                }else if(getScanResultCallback!=null){
                    getScanResultCallback.onSuccess(mWifiManager.getScanResults());
                    mContext.unregisterReceiver(this);
                }
            }else if(intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
                if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                    if(startScanCallback!=null ){
                        if(mActivity!=null && !mActivity.isFinishing()){
                            startScanCallback.onSuccess(mWifiManager.getScanResults());
                            initScan();
                        }else {
                            mContext.unregisterReceiver(this);
                        }
                    }else if(isWifiConnectCallback!=null){
                        isWifiConnectCallback.onSuccess();
                        mContext.unregisterReceiver(this);
                    }
                }
            }else if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
                if(startScanCallback!=null ){
                    if(mActivity!=null && !mActivity.isFinishing()){
                        startScanCallback.onSuccess(mWifiManager.getScanResults());
                        initScan();
                    }else {
                        mContext.unregisterReceiver(this);
                    }
                }
            }else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                if(isWifiReadyToActionCallback!=null){
                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager != null) {
                        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            isWifiReadyToActionCallback.onSuccess();
                            mContext.unregisterReceiver(this);
                        }
                    }
                }
            }
        }
    }
}
