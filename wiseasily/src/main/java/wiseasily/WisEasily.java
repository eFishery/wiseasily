package wiseasily;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import wiseasily.source.SourceCallback;


/**
 * Bismillahirrahmanirrahim
 * Created by Innovation, eFishery  on 4/20/17.
 */

public class WisEasily extends WifiConection{

    public WisEasily(Activity activity) {
        super(activity);
    }

    public void connect(@NonNull String ssid, final SourceCallback.WisEasilyCallback callback) {
        if(ssid.isEmpty()){
            callback.onError("SSID Cannot be Empty");
        }else {
            connectWifi(ssid, callback);
        }
    }

    public boolean isWifiEnable(){
        return isWifiMEnable();
    }

    public void enable(boolean enable, SourceCallback.SuccessCallback callback){
        enableWifi(enable, callback);
    }

    public void scan(SourceCallback.WisEasilyScanCallback callback) {
        scanWifi(callback);
    }
}
