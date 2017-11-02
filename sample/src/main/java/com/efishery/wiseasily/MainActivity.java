package com.efishery.wiseasily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.efishery.putrabangga.wifi.R;
import com.efishery.putrabangga.wifimodul.ConnectorCallback;
import com.efishery.putrabangga.wifimodul.WifiConnector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.switchWifi)
    Switch switchWifi;
    @BindView(R.id.enableWifi)
    Button enableWifi;
    @BindView(R.id.ssid)
    EditText ssid;

    @OnCheckedChanged(R.id.switchWifi)
    void onGenderSelected(CompoundButton button, boolean checked){
        if(checked){
            new WifiConnector(ssid.getText().toString(),this).finishConnect(new ConnectorCallback.FinishConnectWifiCallback() {
                @Override
                public void onWifiFinishConnected() {

                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
