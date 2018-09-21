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
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */


public class PoolBroadcastWifiForceConnected extends BroadcastReceiver {

    private final ConnectivityManager mConnectivityManager;
    private final String ssid;
    private final Context mContext;
    private final WifiUtil wifiUtil;
    private SourceCallback.ConnectCallback isConnectivityAction;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    public static final String NETWORK_AVAILABILITY_ACTION = "com.wiseasily.NETWORK_AVAILABILITY_ACTION";

    public PoolBroadcastWifiForceConnected(@NonNull Context context, @NonNull String ssid) {
        mContext = context;
        this.ssid = ssid;
        wifiUtil = new WifiUtil();
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
        forceToUseWifiWithoutInternet();
        mContext.registerReceiver(this, new IntentFilter(NETWORK_AVAILABILITY_ACTION));
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            boolean canWriteFlag = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                canWriteFlag = Settings.System.canWrite(mContext);

                if (!canWriteFlag) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    mContext.startActivity(intent);
                }
            }

            if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && canWriteFlag)
                    || ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))) {
                NetworkRequest.Builder request = new NetworkRequest.Builder();
                request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                if (mConnectivityManager != null) {
                    mConnectivityManager.requestNetwork(request.build(), new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                mConnectivityManager.bindProcessToNetwork(network);
                            } else {
                                //This method was deprecated in API level 23
                                ConnectivityManager.setProcessDefaultNetwork(network);
                            }
                            try {
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mConnectivityManager.unregisterNetworkCallback(this);
                        }
                    });
                }else {
                    mContext.sendBroadcast(getNetworkAvailabilityIntent(false));
                }
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
            if (mConnectivityManager != null && !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true)) {
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    Log.d("Connect Wifi","Active Network " + activeNetwork.toString());
                    if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                        if(activeNetwork.getExtraInfo().equals(wifiUtil.getConfigFormatSSID(ssid))){
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
