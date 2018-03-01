package wiseasily.pair;

import android.net.wifi.SupplicantState;
import android.util.Log;

import java.util.ArrayList;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 15/02/18.
 */

public class UtilPair {
    public static Pair<SupplicantState, String> getStateSsidPair(SupplicantState supplicantState, String ssid){
        return new Pair<>(supplicantState, ssid);
    }
    public static boolean containsPair(ArrayList<Pair> pairs, Pair pair){
        Log.d("Connect Wifi", "pairStateSsid"+ pair +pair.getSupplicantState()+ pair.getSsid());
        for(Pair pairStateSsid : pairs){
            Log.d("Connect Wifi", "pairStateSsid"+ pairStateSsid +pairStateSsid.getSupplicantState()+ pairStateSsid.getSsid());
            if(pairStateSsid.equals(pair)){
                return true;
            }
        }
        return false;
    }
}
