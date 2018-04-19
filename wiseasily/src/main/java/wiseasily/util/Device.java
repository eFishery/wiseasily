package wiseasily.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/04/18.
 */

public class Device {
    public Device() {
    }

    public String getDeviceInfo(){

        String osVersion = "OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        String osApiLevel = "OS API Level: " + android.os.Build.VERSION.SDK_INT;
        String device = "Device: " + android.os.Build.DEVICE;
        String model = "Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        String serialNumber =  "SN: " + android.os.Build.SERIAL;

        return "{" + osVersion + ", "
                + osApiLevel + ", "
                + device + ", "
                + model + ", "
                + serialNumber + "}";
    }
}
