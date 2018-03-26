package wiseasily.source;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 11/5/17.
 */

public interface SourceCallback {

    interface WisEasilyCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    interface WisEasilyScanCallback {
        void onAPChanged(List<ScanResult> scanResults);
    }

    interface WisEasilyWifiDisable {
        void onDisabled();
    }

    interface APFoundCallback {
        void onAPFound();
        void onAPNotFound();
    }

    interface SuccessCallback {
        void onSuccess();
    }

    interface ConnectCallback {
        void onSuccess();
        void onFail();
    }
}