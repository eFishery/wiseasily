package com.efishery.wiseasily;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.efishery.putrabangga.wifi.R;
import com.efishery.wiseasily.util.PermissionUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import wiseasily.WisEasily;
import wiseasily.source.SourceCallback;
import wiseasily.util.ConnectionData;

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
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    private WisEasily wisEasily;
    String message = "";
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private int REQUEST_LOCATION = 33;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @OnCheckedChanged(R.id.switchWifi)
    void onGenderSelected(CompoundButton button, final boolean checked) {
        boolean success = wisEasily.enableWifi(checked);
    }

    @OnClick(R.id.connectWifi)
    void connect() {
        process.setText(String.format("try to connect %s", ssid.getText().toString()));
        connectSSID();
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
//        new PoolBroadcastFail(this, this);
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

    @OnClick(R.id.currentConnection)
    public void onViewClicked() {

        if(wisEasily.getCurrentConnection() == ConnectionData.WIFI){
            if(wisEasily.isWifiConnectedToSsid(ssid.getText().toString())){
                boolean success = wisEasily.forgetCurrentSssid(ssid.getText().toString());
                Log.d("Connect Wifi", "forgetCurrentSssid: "+ ssid.getText().toString() + success);
            }
        }
    }

    private void connectSSID() {
        initLocationParameters();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(mainLayout, "Location Needed for this function",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Granted", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(MainActivity.this, PERMISSIONS_LOCATION,
                                            REQUEST_LOCATION);
                        }
                    })
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }

    private void initLocationParameters() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest);
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    tryToConnect();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d("MainActivity_Location", e.toString());
                            } catch (ClassCastException e) {
                                Log.d("MainActivity_Location", e.toString());
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.d("MainActivity_Location", "LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
                            break;
                    }
                }
            }
        });
    }

    private void tryToConnect() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }else {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Snackbar.make(mainLayout, "Permission Location Granted",
                        Snackbar.LENGTH_SHORT)
                        .show();
                Log.d("MainActivity", "onRequestPermissionsResult");
                connectSSID();
            } else {
                Snackbar.make(mainLayout, "Permission Location Not Granted",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        tryToConnect();
                        break;
                    case Activity.RESULT_CANCELED:
                        Snackbar.make(mainLayout, "Location Require",
                                Snackbar.LENGTH_SHORT)
                                .show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }
}
