package wiseasily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 22/02/18.
 */

public class ConnectivityUtil {
    public static boolean isConnectedToAP(String APSsid, Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mConnectivityManager!=null){
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if(activeNetwork!=null) {
                Log.d("Connect Wifi", "Active Network " + activeNetwork.toString());
                return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().equals(WifiUtil.getConfigFormatSSID(APSsid));
            }else {
                Log.d("Connect Wifi", "Active Network null");
                return false;
            }
        }else {
            return false;
        }
    }

    public static boolean isConnectedToAPContainsChar(String containtChar, Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mConnectivityManager!=null){
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if(activeNetwork!=null) {
                Log.d("Connect Wifi", "Active Network " + activeNetwork.toString());
                return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.getExtraInfo().contains(containtChar);
            }else {
                Log.d("Connect Wifi", "Active Network null");
                return false;
            }
        }else {
            return false;
        }
    }


    public static int currentConnection(Context context){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(mConnectivityManager!=null){
            NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
            if(activeNetwork!=null) {
                Log.d("Connect Wifi", "Active Network " + activeNetwork.toString());
                if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                    return ConnectionData.WIFI;
                }else {
                    return ConnectionData.MOBILE;
                }
            }else {
                Log.d("Connect Wifi", "Active Network null");
                return ConnectionData.EMPTY;
            }
        }else {
            return ConnectionData.EMPTY;
        }
    }
}
