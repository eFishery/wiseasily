package com.efishery.wiseasily.util;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import wiseasily.util.ScanFilter;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 05/01/18.
 */

public class FilterUtil {

    public static List<ScanResult> filterScanResult(@Nullable ScanFilter scanFilter, @NonNull List<ScanResult> scanResults) {
        if(scanFilter == null){
            return scanResults;
        }else {
            List<ScanResult> scanResultsFilter = new ArrayList<>();
            for(ScanResult scanResult : scanResults){
                if(scanFilter.matchesStart(scanResult)){
                    scanResultsFilter.add(scanResult);
                }
            }
            return scanResultsFilter;
        }
    }
}
