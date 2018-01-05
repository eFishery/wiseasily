package wiseasily;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import wiseasily.poolbroadcast.PoolBroadcastScanWifi;
import wiseasily.source.SourceCallback;
import wiseasily.util.ScanFilter;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 19/11/17.
 */

class ScanWifi implements SourceCallback.WisEasilyScanCallback {
    private final Context mContext;
    private PoolBroadcastScanWifi poolBroadcastScanWifi;
    private SourceCallback.WisEasilyScanCallback callback;
    private ScanFilter scanFilter;

    ScanWifi(@NonNull Context context) {
        this.mContext = context;
    }

    void start(@NonNull SourceCallback.WisEasilyScanCallback callback){
        this.callback = callback;
        poolBroadcastScanWifi = new PoolBroadcastScanWifi(mContext, this);
        poolBroadcastScanWifi.startScanning(true);
    }

    void stop(){
        if(poolBroadcastScanWifi!=null){
            poolBroadcastScanWifi.stopScanning();
        }
    }

    void changeScanInterval(int scanInterval) {
        if (scanInterval < 0) {
            throw new IllegalArgumentException("mScanInterval cannot be negative");
        }
        if(poolBroadcastScanWifi == null){
            throw new NullPointerException("callback cannot be null");
        }
        poolBroadcastScanWifi.changeScanInterval(scanInterval);
    }

    void changeFilter(@NonNull ScanFilter scanFilter) {
        this.scanFilter = scanFilter;
    }

    @Override
    public void onAPChanged(List<ScanResult> scanResults) {
        if(callback!=null){
            List<ScanResult> scanResultsFilter = new ArrayList<>();
            for(ScanResult scanResult : scanResults){
                if(scanFilter==null || scanFilter.matchesStart(scanResult)){
                    scanResultsFilter.add(scanResult);
                }
            }
            callback.onAPChanged(scanResultsFilter);
        }
    }
}
