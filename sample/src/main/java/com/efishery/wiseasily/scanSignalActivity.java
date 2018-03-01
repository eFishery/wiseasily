package com.efishery.wiseasily;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.efishery.putrabangga.wifi.R;
import com.efishery.wiseasily.servicelisten.DAOScanResult;
import com.efishery.wiseasily.servicelisten.ScanService;
import com.efishery.wiseasily.util.FilterUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wiseasily.WisEasily;
import wiseasily.util.ScanFilter;

public class scanSignalActivity extends AppCompatActivity{

    private WifiAdapter adapter;
    private ScanFilter scanFilter;
    private WisEasily wisEasily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ListView listView = findViewById(R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        adapter = new WifiAdapter(this);
        listView.setAdapter(adapter);

        Set<String> ssidsInclude = new HashSet<>();
        ssidsInclude.add("efishery_00052");
        scanFilter = new ScanFilter(null, null, ssidsInclude, null, null, null);
        adapter.replaceData(getScanResultsFilter(ScanService.getScanResults()));
        wisEasily = new WisEasily(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wisEasily.enableWifi(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DAOScanResult DAOScanResult){
        List<ScanResult> scanResults = DAOScanResult.getScanResults();
        adapter.replaceData(getScanResultsFilter(scanResults));
    }

    private List<ScanResult> getScanResultsFilter(List<ScanResult> scanResults) {
        return FilterUtil.filterScanResult(scanFilter, scanResults);
    }
}
