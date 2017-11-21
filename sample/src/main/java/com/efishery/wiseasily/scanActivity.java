package com.efishery.wiseasily;

import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.efishery.putrabangga.wifi.R;

import java.util.List;

import wiseasily.WisEasily;
import wiseasily.source.SourceCallback;

public class scanActivity extends AppCompatActivity implements SourceCallback.CompleteDataCallback<List<ScanResult>> {

    private WisEasily wisEasily;
    private WifiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        wisEasily = new WisEasily(this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        adapter = new WifiAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        wisEasily.scan(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSuccess(List<ScanResult> scanResults) {
        adapter.replaceData(scanResults);
    }

    @Override
    public void onOutTime() {

    }
}
