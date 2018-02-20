package wiseasily;

import android.content.Context;
import android.support.annotation.NonNull;

import wiseasily.poolbroadcast.PoolWifiDisabled;
import wiseasily.source.SourceCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

class WifiDisabled {
    private final Context mContext;

    WifiDisabled(Context context) {
        this.mContext = context;
    }

    void start(@NonNull final SourceCallback.SuccessCallback callback) {
        new PoolWifiDisabled(mContext, callback);
    }
}
