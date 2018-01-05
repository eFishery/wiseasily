package com.efishery.wiseasily.servicelisten;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * بِسْمِ اللّهِ الرَّحْمَنِ
 * Created by putrabangga on 04/01/18.
 */

public class DAOScanResult {

    List<ScanResult> scanResults;

    public DAOScanResult(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<ScanResult> scanResults) {
        this.scanResults = scanResults;
    }

    @Override
    public String toString() {
        return "DAOScanResult{" +
                "scanResults=" + scanResults +
                '}';
    }
}
