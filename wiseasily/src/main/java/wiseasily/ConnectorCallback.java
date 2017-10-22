package wiseasily;

import android.net.wifi.ScanResult;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by efishery on 28/11/16.
 */

public interface ConnectorCallback {

    interface ConnectWifiCallback {
        void onWifiConnected();
        void onProgress(String message);
        void onWifiFail(int errorCode);
    }

    interface FinishConnectWifiCallback {
        void onWifiFinishConnected();
    }

    interface enableWifiCallback {
        void onWifiEnabled();

        void onWifiFail(int errorCode);
    }

    interface scanWifiCallback {
        void onWifiScanned(List<ScanResult> scanResults);

        void onWifiFail(int errorCode);
    }

    interface scanSignalWifiCallback {
        void onWifiSignalScanned(ArrayList<String> feeders);

        void onWifiFail(int errorCode);
    }

    interface timeOutCallback {
        void onStartCountDown(CountDownTimer countDownTimer);
        void onOutTime();
    }
}
