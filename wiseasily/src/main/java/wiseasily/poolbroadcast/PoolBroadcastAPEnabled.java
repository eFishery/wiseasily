package wiseasily.poolbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import wiseasily.pair.GenerateStateMachineWifi;
import wiseasily.pair.Pair;
import wiseasily.pair.ServiceStateMachine;
import wiseasily.pair.UtilPair;
import wiseasily.source.SourceCallback;
import wiseasily.util.WifiUtil;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public class PoolBroadcastAPEnabled extends BroadcastReceiver  {

    private final Context mContext;
    private final WifiManager mWifiManager;
    private final String ssid;
    private final PoolBroadcastWifiOff poolBroadcastWifiOff;
    private final WifiUtil wifiUtil;
    private SourceCallback.ConnectCallback isSuplicantCompletedCallback;
    private GenerateStateMachineWifi generateStateMachineWifi = null;
    private int i = 0;


    public PoolBroadcastAPEnabled(@NonNull Context context, String ssid) {
        this.mContext = context;
        this.ssid = ssid;
        wifiUtil = new WifiUtil();
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        poolBroadcastWifiOff = new PoolBroadcastWifiOff(mContext);
        generateStateMachineWifi = new GenerateStateMachineWifi(ssid);
    }

    public void startListen(@NonNull SourceCallback.ConnectCallback callback){
        this.isSuplicantCompletedCallback = callback;
        if(mWifiManager!=null){
            if(!wifiUtil.isScanResultsContainsSsid(ssid, mWifiManager.getScanResults())){
                isSuplicantCompletedCallback.onFail();
            }else {
                int netId = wifiUtil.getNetId(ssid, mWifiManager);
                if(!enableNework(ssid, mContext)){
                    mWifiManager.enableNetwork(netId, true);
                }
                Log.d("Connect Wifi", "AP Enable startListen " + ssid + " " + netId);
                mContext.registerReceiver(this, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
                poolBroadcastWifiOff.startListen(new PoolBroadcastWifiOff.ConnectWifiFail() {
                    @Override
                    public void onWifiOff() {
                        stopListen();
                        callback.onFail();
                    }
                });
            }
        }else {
            callback.onFail();
        }
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
        if(intent.getAction()!=null && intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
            SupplicantState supplicantStateCurrent = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            String ssidCurrent = mWifiManager.getConnectionInfo().getSSID();
            Log.d("Connect Wifi", "AP Enabled SpState" + supplicantStateCurrent);
            Log.d("Connect Wifi", "AP Enabled ssidCurrent"+ ssidCurrent);
            if(supplicantStateCurrent == SupplicantState.COMPLETED && wifiUtil.getConfigFormatSSID(ssid).equals(ssidCurrent)){
                stopListenAll();
                isSuplicantCompletedCallback.onSuccess();
            }else {
                if(i!=0){
                    Pair<SupplicantState, String> stateSuplicantSssid = new UtilPair().getStateSsidPair(supplicantStateCurrent, ssidCurrent);
                    ArrayList<Pair> machineState = generateStateMachineWifi.getStateMachine();
                    Log.d("Connect Wifi", "stateSuplicantSssid" + stateSuplicantSssid);
                    if(!new UtilPair().containsPair(machineState, stateSuplicantSssid)){
                        intentToServiceStateMachine(context, stateSuplicantSssid);
                        stopListenAll();
                        isSuplicantCompletedCallback.onFail();
                    }
                }
                generateStateMachineWifi.setStateMachine(supplicantStateCurrent, ssidCurrent);
                i++;
            }
        }
    }

    private void intentToServiceStateMachine(Context context, Pair pair) {
        Intent serviceIntent = new Intent(context ,ServiceStateMachine.class);
        serviceIntent.putExtra("SSID", ssid);
        serviceIntent.putExtra("machinestate", pair);
        context.startService(serviceIntent);
    }

    boolean enableNework(String ssid, Context cxt) {
        boolean state = false;
        WifiManager wm = (WifiManager) cxt.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm != null && wm.setWifiEnabled(true)) {
            List<WifiConfiguration> networks = wm.getConfiguredNetworks();
            if(networks!=null){
                for (WifiConfiguration wifiConfig : networks) {
                    if (wifiConfig.SSID.equals(ssid))
                        state = wm.enableNetwork(wifiConfig.networkId, true);
                    else
                        wm.disableNetwork(wifiConfig.networkId);
                }
            }
            wm.reconnect();
        }
        return state;
    }

    private void stopListenAll() {
        if(poolBroadcastWifiOff!=null){
            poolBroadcastWifiOff.stopListen();
        }
        stopListen();
    }
}
