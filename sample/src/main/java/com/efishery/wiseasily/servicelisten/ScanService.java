package com.efishery.wiseasily.servicelisten;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wiseasily.WisEasily;
import wiseasily.source.SourceCallback;
import wiseasily.util.ScanFilter;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 04/01/18.
 */

public class ScanService extends Service implements SourceCallback.WisEasilyWifiDisable, SourceCallback.WisEasilyScanCallback {


    
    private WisEasily wisEasily;

    public static List<ScanResult> getScanResults() {
        return scanResults;
    }

    private static List<ScanResult> scanResults = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        wisEasily = new WisEasily(this);
        wisEasily.startIsWifiDisable(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Set<Integer> channels = new HashSet<>();
        channels.add(7);
        wisEasily.scan(this);
        ScanFilter scanFilter = new ScanFilter(null, "efishery", null, null, channels, null);
        wisEasily.scanChangeFilter(scanFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wisEasily.stopScan();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDisabled() {
        stopSelf();
    }

    @Override
    public void onAPChanged(List<ScanResult> scanResults) {
        ScanService.scanResults = new ArrayList<>(scanResults);
        EventBus.getDefault().post(new DAOScanResult(scanResults));
    }
}
