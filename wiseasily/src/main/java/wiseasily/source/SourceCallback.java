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

    interface SuccessCallback {
        void onSuccess();
        void onOutTime();
    }

    interface CompleteDataCallback<T> {
        void onSuccess(T data);
        void onOutTime();
    }
}