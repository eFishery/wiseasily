package wiseasily.util;

import android.os.CountDownTimer;
import android.util.Log;

import wiseasily.ConnectorCallback;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 10/29/17.
 */

public class TimeOutUtil {

    public static void timeOut(int timeout, final ConnectorCallback.timeOutCallback callback){
        CountDownTimer countDownTimer = new CountDownTimer(timeout*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("timeOut","onTick ");
            }

            public void onFinish() {
                callback.onOutTime();
            }
        }.start();
        callback.onStartCountDown(countDownTimer);
    }
}
