package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */


public class PoolBroadcastWifiConnected extends BroadcastReceiver {

    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
    private final String ssid;
    private SourceCallback.ConnectCallback isConnectivityAction;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    private Handler mHandler;
    boolean successForceConnect = true;
    private boolean hasbeenForceConnect = false;

    private final Runnable mOutOfTime = new Runnable() {
        @Override
        public void run() {
            hasbeenForceConnect = true;
            callbackWifiConnected();
        }
    };
    private int count=0;

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;
        mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
        mHandler = new Handler();
        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    public void startListen(@NonNull SourceCallback.ConnectCallback callback){
        this.isConnectivityAction = callback;
        Log.d("Connect Wifi", "Wifi Connected startListen");
//        mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        poolBroadcastSuplicantOff.startListen(new PoolBroadcastSuplicantOff.ConnectWifiFail() {
//            @Override
//            public void onSuplicantOff() {
//                stopListen();
//                callback.onFail();
//            }
//        });
        poolBroadcastWifiOff.startListen(new PoolBroadcastWifiOff.ConnectWifiFail() {
            @Override
            public void onWifiOff() {
                callback.onFail();
            }
        });
        forceToUseWifiWithoutInternet();
    }

    public void stopListen(){
        try  {
            mContext.unregisterReceiver(this);
        }
        catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            if(hasbeenForceConnect){
                callbackWifiConnected();
            }
        }
    }

    private void callbackWifiConnected() {
        stopListenAll();
        mHandler.removeCallbacks(mOutOfTime);
        if (mConnectivityManager != null && successForceConnect) {
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                Log.d("Connect Wifi","Active Network " + activeNetwork.toString());
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                    if(activeNetwork.getExtraInfo().equals(WifiUtil.getConfigFormatSSID(ssid))){
                        isConnectivityAction.onSuccess();
                    }else {
                        isConnectivityAction.onFail();
                    }
                }else {
                    count++;
                    if(count>2){
                        isConnectivityAction.onFail();
                    }
                }
            }else {
                isConnectivityAction.onFail();
            }
        }else {
            isConnectivityAction.onFail();
        }
    }

    private void stopListenAll() {
        if(poolBroadcastWifiOff!=null){
            poolBroadcastWifiOff.stopListen();
        }
//        if(poolBroadcastSuplicantOff!=null){
//            poolBroadcastSuplicantOff.stopListen();
//        }
        stopListen();
    }


    private void forceToUseWifiWithoutInternet(){
        //bind to current thread
        Log.d("Connect Wifi", "Force to Connect ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            if (mConnectivityManager != null) {
                mHandler.postDelayed(mOutOfTime, 30000);
                mConnectivityManager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        hasbeenForceConnect = true;
                        Log.d("Connect Wifi", "Network onAvailable");
                        mHandler.removeCallbacks(mOutOfTime);
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            successForceConnect = ConnectivityManager.setProcessDefaultNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            successForceConnect = mConnectivityManager.bindProcessToNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                        }else {
                            successForceConnect = true;
                        }
                    }
                });
            }else {
                successForceConnect = false;
            }
        }else {
            successForceConnect = true;
        }
    }
}
