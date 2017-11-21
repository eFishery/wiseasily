package com.efishery.wiseasily;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.efishery.putrabangga.wifi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import wiseasily.WisEasily;
import wiseasily.source.SourceCallback;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.switchWifi)
    Switch switchWifi;
    @BindView(R.id.connectWifi)
    Button connectWifi;
    @BindView(R.id.ssid)
    EditText ssid;
    private WisEasily wisEasily;

    @OnCheckedChanged(R.id.switchWifi)
    void onGenderSelected(CompoundButton button, final boolean checked){
        wisEasily.enable(checked, new SourceCallback.SuccessCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onOutTime() {
                switchWifi.setChecked(!checked);
            }
        });
    }

    @OnClick(R.id.connectWifi) void connect() {
        Log.d("MainActivity","try: "+"Connect");
        wisEasily.connect(ssid.getText().toString(), new SourceCallback.WisEasilyCallback() {
            @Override
            public void onSuccess() {
                Log.d("MainActivity","Success: "+"Connect");
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("MainActivity","onError: "+"errorMessage");
            }
        });
    }

    @OnClick(R.id.scanWifis) void scan() {
        Intent scanIntent = new Intent(MainActivity.this, scanActivity.class);
        startActivity(scanIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        wisEasily = new WisEasily(this);
        switchWifi.setChecked(wisEasily.isWifiEnable());
    }
}
