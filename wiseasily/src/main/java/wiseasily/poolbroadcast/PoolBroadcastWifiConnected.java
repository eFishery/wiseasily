package wiseasily.poolbroadcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */


public class PoolBroadcastWifiConnected {

    private final Context mContext;
    private final ConnectivityManager mConnectivityManager;
    private final String ssid;
    private SourceCallback.ConnectCallback isConnectivityAction;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    private final PoolBroadcastSuplicantOff poolBroadcastSuplicantOff;
    private Handler mHandler;

    private final Runnable mOutOfTime = new Runnable() {
        @Override
        public void run() {
            if (mConnectivityManager != null) {
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
                        isConnectivityAction.onFail();
                    }
                }else {
                    isConnectivityAction.onFail();
                }
            }else {
                isConnectivityAction.onFail();
            }
        }
    };

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid) {
        this.mContext = context;
        this.ssid = ssid;
        mConnectivityManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
        poolBroadcastSuplicantOff = new PoolBroadcastSuplicantOff(mContext);
        mHandler = new Handler();

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
//
//    public void stopListen(){
//        try  {
//            mContext.unregisterReceiver(this);
//        }
//        catch (IllegalArgumentException e) {
//            // Check wether we are in debug mode
//            if (BuildConfig.DEBUG) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction()!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
//            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
//            if(activeNetwork!=null){
//                Log.d("Connect Wifi","Active Network " + activeNetwork.toString());
//            }
//            if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().equals(WifiUtil.getConfigFormatSSID(ssid))) {
//                stopListenAll();
//                isConnectivityAction.onSuccess();
//            }else {
//                isConnectivityAction.onFail();
//            }
//        }
//    }

    private void stopListenAll() {
        if(poolBroadcastWifiOff!=null){
            poolBroadcastWifiOff.stopListen();
        }
//        if(poolBroadcastSuplicantOff!=null){
//            poolBroadcastSuplicantOff.stopListen();
//        }
//        stopListen();
    }


    private void forceToUseWifiWithoutInternet(){
        //bind to current thread
        Log.d("Connect Wifi", "Force to Connect ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            if (mConnectivityManager != null) {
                mHandler.postDelayed(mOutOfTime, 5000);
                mConnectivityManager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d("Connect Wifi", "Network onAvailable");
                        stopListenAll();
                        boolean successForceConnect;
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            successForceConnect = ConnectivityManager.setProcessDefaultNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                            callbackConnManager(successForceConnect);
                        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            successForceConnect = mConnectivityManager.bindProcessToNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                            callbackConnManager(successForceConnect);
                        }
                    }
                });
            }else {
                isConnectivityAction.onFail();
            }
        }else {
            isConnectivityAction.onSuccess();
        }
    }


    private void callbackConnManager(boolean successForceConnect) {
        Log.d("Connect Wifi", "Force to Connect " + String.valueOf(successForceConnect));
        if(successForceConnect){
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if(activeNetwork!=null){
                Log.d("Connect Wifi","Active Network " + activeNetwork.toString());
            }
            if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().equals(WifiUtil.getConfigFormatSSID(ssid))) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        isConnectivityAction.onSuccess();
                    }
                });
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        isConnectivityAction.onFail();
                    }
                });
            }
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    isConnectivityAction.onFail();
                }
            });
        }
    }
}
