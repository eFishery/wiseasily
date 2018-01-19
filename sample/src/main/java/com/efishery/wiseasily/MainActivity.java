package com.efishery.wiseasily;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.efishery.putrabangga.wifi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import wiseasily.WisEasily;
import wiseasily.source.SourceCallback;

public class MainActivity extends AppCompatActivity implements SourceCallback.SuccessCallback, PoolBroadcastFail.messageCallback {


    @BindView(R.id.switchWifi)
    Switch switchWifi;
    @BindView(R.id.connectWifi)
    Button connectWifi;
    @BindView(R.id.ssid)
    EditText ssid;
    @BindView(R.id.process)
    TextView process;
    @BindView(R.id.pool)
    TextView pool;
    private WisEasily wisEasily;
    String message = "";

    @OnCheckedChanged(R.id.switchWifi)
    void onGenderSelected(CompoundButton button, final boolean checked) {
        wisEasily.enable(checked, () -> {

        });
    }

    @OnClick(R.id.connectWifi)
    void connect() {
        process.setText(String.format("try to connect %s", ssid.getText().toString()));
        wisEasily.connect(ssid.getText().toString(), new SourceCallback.WisEasilyCallback() {
            @Override
            public void onSuccess() {
                process.setText(String.format("success connect to %s", ssid.getText().toString()));
            }

            @Override
            public void onError(String errorMessage) {
                process.setText(errorMessage);
            }
        });
    }

    @OnClick(R.id.scanWifi)
    void scanWifi() {
        Intent scanWifiIntent = new Intent(MainActivity.this, scanWifiActivity.class);
        startActivity(scanWifiIntent);
    }

    @OnClick(R.id.scanWifiSignal)
    void scanWifiSignal() {
        Intent scanWifiSignalIntent = new Intent(MainActivity.this, scanSignalActivity.class);
        startActivity(scanWifiSignalIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pool.setMovementMethod(new ScrollingMovementMethod());
        wisEasily = new WisEasily(this);
        switchWifi.setChecked(wisEasily.isWifiEnable());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        wisEasily.enable(false, this);
        new PoolBroadcastFail(this, this);
    }

    @Override
    public void onSuccess() {
        Log.d("MainActivity", "onSuccess TurnOff");
        process.setText("WIfi Turn Off");
    }

    @Override
    public void message(String s) {
        message = message + s + "\n";
        pool.setText(message);
    }

    @OnClick(R.id.clear)
    public void onViewClicked() {
        message = "";
        pool.setText(message);
    }
}
