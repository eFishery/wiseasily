package wiseasily.pair;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.efishery.putrabangga.wiseasily.BuildConfig;
import com.logentries.logger.AndroidLogger;

import java.io.IOException;
import java.util.ArrayList;

import wiseasily.poolbroadcast.PoolBroadcastWifiOff;
import wiseasily.util.Device;
import wiseasily.util.WifiUtil;

public class ServiceStateMachine extends Service {
    private BroadcastReceiver broadcastReceiver;
    private PoolBroadcastWifiOff poolBroadcastWifiOff;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ssid = intent.getStringExtra("SSID");
        Pair<SupplicantState, String> machineState = (Pair<SupplicantState, String>) intent.getSerializableExtra("machinestate");

        GenerateStateMachineWifi generateStateMachineWifi = new GenerateStateMachineWifi(ssid);
        generateStateMachineWifi.setStateMachine(machineState.getSupplicantState(), machineState.getSsid());

        IntentFilter theFilter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        ArrayList<Pair> successfulConnectStateMachine = new ArrayList<>();
        successfulConnectStateMachine.add(machineState);

        poolBroadcastWifiOff = new PoolBroadcastWifiOff(this);
        poolBroadcastWifiOff.startListen(new PoolBroadcastWifiOff.ConnectWifiFail() {
            @Override
            public void onWifiOff() {
                stopReceiveStateMachine();
            }
        });

        this.broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                SupplicantState supplicantStateCurrent = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                String ssidCurrent = mWifiManager.getConnectionInfo().getSSID();
                Log.d("Connect Wifi Service", "AP Enabled SpState" + supplicantStateCurrent);
                Log.d("Connect Wifi Service", "AP Enabled ssidCurrent"+ ssidCurrent);
                if(supplicantStateCurrent == SupplicantState.COMPLETED){
                    stopReceiveStateMachine();
                    if(new WifiUtil().getConfigFormatSSID(ssid).equals(ssidCurrent)){
                        AndroidLogger logger = getLogger(ServiceStateMachine.this);
                        if(logger!=null){
                            String deviceInfo = new Device().getDeviceInfo();
                            String message = "DeviceInfo : " + deviceInfo + "SSID : " + ssid + " StateMachine : " + successfulConnectStateMachine.toString();
                            Log.d("Connect Wifi Service", "Send Log "+ message);
                            logger.log(message);
                        }
                    }
                }else {
                    Pair<SupplicantState, String> stateSuplicantSssid = new UtilPair().getStateSsidPair(supplicantStateCurrent, ssidCurrent);
                    ArrayList<Pair> machineState = generateStateMachineWifi.getStateMachine();
//                    if(!new UtilPair().containsPair(machineState, stateSuplicantSssid)){
//                        onDestroy();
//                    }else {
//                        successfulConnectStateMachine.add(stateSuplicantSssid);
//                    }
                    successfulConnectStateMachine.add(stateSuplicantSssid);
                    generateStateMachineWifi.setStateMachine(supplicantStateCurrent, ssidCurrent);
                }
            }
        };

        this.registerReceiver(this.broadcastReceiver, theFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    private void stopReceiveStateMachine() {
        try  {
            this.unregisterReceiver(this.broadcastReceiver);
            if(poolBroadcastWifiOff!=null){
                poolBroadcastWifiOff.stopListen();
            }
        }
        catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private AndroidLogger getLogger(Context context) {
        String tokenLogentries = "4723c537-602f-48f6-9e78-1b8f046b8097";
        try {
            return AndroidLogger.createInstance(
                    context,
                    true,
                    false,
                    false,
                    null,
                    0,
                    tokenLogentries,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
