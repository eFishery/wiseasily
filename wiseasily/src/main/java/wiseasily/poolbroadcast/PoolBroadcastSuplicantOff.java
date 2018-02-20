package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 23/01/18.
 */

public class PoolBroadcastSuplicantOff extends BroadcastReceiver {

    private final Context mContext;
    private final WifiManager mWifiManager;
    private ConnectWifiFail connectWifiFail;

    public PoolBroadcastSuplicantOff(@NonNull Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void startListen(@NonNull ConnectWifiFail callback){
        this.connectWifiFail = callback;
        mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null && intent.getAction()!=null){
            SupplicantState supplicantState = mWifiManager.getConnectionInfo().getSupplicantState();
            String ssidCurrent = mWifiManager.getConnectionInfo().getSSID();
            Log.d("Connect Wifi","Listen Off SupplicantState " + supplicantState.toString());
            Log.d("Connect Wifi", "Listen Off ssidCurrent "+ ssidCurrent);
            if(supplicantState != SupplicantState.COMPLETED){
                Log.d("Connect Wifi", "Listen Off suplicantOFF fail");
                stopListen();
                connectWifiFail.onSuplicantOff();
            }
        }
    }

    public interface ConnectWifiFail {
        void onSuplicantOff();
    }
}
