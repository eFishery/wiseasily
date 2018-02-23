package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 23/01/18.
 */

public class PoolBroadcastWifiOff extends BroadcastReceiver {

    private final Context mContext;
    private ConnectWifiFail connectWifiFail;
    private final WifiManager mWifiManager;
    private boolean canListenWifiOff = false;

    public PoolBroadcastWifiOff(@NonNull Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void startListen( @NonNull ConnectWifiFail callback){
        this.connectWifiFail = callback;
        mContext.registerReceiver(this, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
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
        if(intent!=null && intent.getAction()!=null){
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
                Log.d("Connect Wifi", "wifienable "+ String.valueOf(mWifiManager.isWifiEnabled()));
                Log.d("Connect Wifi", "CHANGED_ACTION"+ String.valueOf(state));
                if(mWifiManager.isWifiEnabled() && state == WifiManager.WIFI_STATE_ENABLED){
                    canListenWifiOff = true;
                }
                if (canListenWifiOff && (state == WifiManager.WIFI_STATE_DISABLING || state == WifiManager.WIFI_STATE_DISABLED)) {
                    Log.d("Connect Wifi", "wifiOff fail");
                    stopListen();
                    connectWifiFail.onWifiOff();
                }
            }
        }
    }

    public interface ConnectWifiFail {
        void onWifiOff();
    }
}
