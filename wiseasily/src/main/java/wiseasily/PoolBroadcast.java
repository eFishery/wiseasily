package wiseasily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcast extends BroadcastReceiver  {

    private final Context mContext;
    private SourceCallback.SuccessCallback startScanCallback;
    private SourceCallback.SuccessCallback isSuplicantCompletedCallback;
    private SourceCallback.SuccessCallback isConnectivityAction;
    private SourceCallback.SuccessCallback getScanResultCallback;
    private CountDownTimer countDownTimer;

    public PoolBroadcast(@NonNull Context context) {
        this.mContext = context;
        goAsync();
    }

    public void closePoolBroadcast(){
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        mContext.unregisterReceiver(this);
    }

    public void isScanResultsAvailableAction(SourceCallback.SuccessCallback callback){
        this.getScanResultCallback = callback;
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void startScan(SourceCallback.SuccessCallback callback){
        this.startScanCallback = callback;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContext.registerReceiver(this, intentFilter);
    }

    public void isSuplicantComplete(SourceCallback.SuccessCallback callback){
        this.isSuplicantCompletedCallback = callback;
        timeOut(callback);
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }

    public void isConnectivityAction(SourceCallback.SuccessCallback callback){
        this.isConnectivityAction = callback;
        timeOut(callback);
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null){
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                if(startScanCallback!=null){
                    startScanCallback.onSuccess();
                }else if(getScanResultCallback!=null){
                    getScanResultCallback.onSuccess();
                }
            }else if(intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
                if(startScanCallback!=null ){
                    startScanCallback.onSuccess();
                }else if(isSuplicantCompletedCallback !=null){
                    isSuplicantCompletedCallback.onSuccess();
                }
            }else if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
                if(startScanCallback!=null ){
                    startScanCallback.onSuccess();
                }
            }else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                if(isConnectivityAction !=null){
                    isConnectivityAction.onSuccess();
                }
            }
        }
    }


    //In BroadcastReceiver, if pass 10 second and nothing return broadcast, it will be close.
    private void timeOut(SourceCallback.SuccessCallback callback){
        countDownTimer = new CountDownTimer(10*1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                closePoolBroadcast();
                callback.onOutTime();
            }
        }.start();
    }
}
