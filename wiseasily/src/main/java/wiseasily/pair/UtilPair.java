package wiseasily.pair;

import android.net.wifi.SupplicantState;
import android.util.Log;

import java.util.ArrayList;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 15/02/18.
 */

public class UtilPair {
    public UtilPair() {
    }

    public Pair<SupplicantState, String> getStateSsidPair(SupplicantState supplicantState, String ssid){
        return new Pair<>(supplicantState, ssid);
    }
    public boolean containsPair(ArrayList<Pair> pairs, Pair pair){
        if(pairs!=null && !pairs.isEmpty() && pair!=null){
            for(Pair pairStateSsid : pairs){
                if(pairStateSsid.equals(pair)){
                    return true;
                }
            }
        }
        return false;
    }
}
