package com.efishery.wiseasily.util;

import android.net.wifi.WifiManager;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 21/11/17.
 */

public class SignalUtil {
    public static String getSignal(int level){

        int signal = WifiManager.calculateSignalLevel(level,3);
        switch (signal) {
            case 0:  return "Weak";
            case 1:  return "Intermediate";
            case 2:  return "Strong";
            default: return "Weak";
        }
    }
}
