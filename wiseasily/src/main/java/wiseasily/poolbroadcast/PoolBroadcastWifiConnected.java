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

    private final ConnectivityManager mConnectivityManager;
    private final String ssid;
    private final Context mContext;
    private SourceCallback.ConnectCallback isConnectivityAction;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    public static final String NETWORK_AVAILABILITY_ACTION = "com.wiseasily.NETWORK_AVAILABILITY_ACTION";

    public PoolBroadcastWifiConnected(@NonNull Context context, @NonNull String ssid) {
        mContext = context;
        this.ssid = ssid;
        mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(context);

    }

    public void startListen(@NonNull SourceCallback.ConnectCallback callback){
        this.isConnectivityAction = callback;
        Log.d("Connect Wifi", "Wifi Connected startListen");
        poolBroadcastWifiOff.startListen(new PoolBroadcastWifiOff.ConnectWifiFail() {
            @Override
            public void onWifiOff() {
                callback.onFail();
            }
        });
        mContext.registerReceiver(this, new IntentFilter(NETWORK_AVAILABILITY_ACTION));
        forceToUseWifiWithoutInternet();
    }

    private void stopListenAll() {
        if(poolBroadcastWifiOff!=null){
            poolBroadcastWifiOff.stopListen();
        }
        stopListen();
    }

    public void stopListen(){
        try  {
            mContext.unregisterReceiver(this);
        }
        catch (IllegalArgumentException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }


    private void forceToUseWifiWithoutInternet(){
        Log.d("Connect Wifi", "Force to Connect ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            if (mConnectivityManager != null) {
                mConnectivityManager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d("Connect Wifi", "Network onAvailable");
                        boolean successForceConnect;
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                            successForceConnect = ConnectivityManager.setProcessDefaultNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            successForceConnect = mConnectivityManager.bindProcessToNetwork(network);
                            mConnectivityManager.unregisterNetworkCallback(this);
                        }else {
                            successForceConnect = true;
                        }
                        Log.d("Connect Wifi", "Network onAvailable "+successForceConnect);
                        mContext.sendBroadcast(getNetworkAvailabilityIntent(successForceConnect));
                    }
                });
            }else {
                mContext.sendBroadcast(getNetworkAvailabilityIntent(false));
            }
        }else {
            mContext.sendBroadcast(getNetworkAvailabilityIntent(true));
        }
    }

    @NonNull
    private Intent getNetworkAvailabilityIntent(boolean isNetworkAvailable) {
        Intent intent = new Intent(NETWORK_AVAILABILITY_ACTION);
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, !isNetworkAvailable);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null && intent.getAction().equals(NETWORK_AVAILABILITY_ACTION)){
            Log.d("Connect Wifi", "callbackWifiConnected intent="+ intent.toString()+" extras="+intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false));
            stopListenAll();
            if (mConnectivityManager != null && !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
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
                    Log.d("Connect Wifi", "activeNetwork null");
                    isConnectivityAction.onFail();
                }
            }else {
                Log.d("Connect Wifi", "mConnectivityManager null");
                isConnectivityAction.onFail();
            }
        }
    }
}
