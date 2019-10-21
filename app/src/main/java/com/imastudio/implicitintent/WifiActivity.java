package com.imastudio.implicitintent;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiActivity extends AppCompatActivity {

    @BindView(R.id.sw_wifi)
    Switch swWifi;
    @BindView(R.id.tv_status)
    TextView tvStatus;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        ButterKnife.bind(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            swWifi.setChecked(true);
            tvStatus.setText("Wifi is Active");
        }else {
            swWifi.setChecked(false);
            tvStatus.setText("Wifi is Off");
        }

        swWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wifiManager.setWifiEnabled(true);
                    tvStatus.setText("Wifi Active");
                }else {
                    wifiManager.setWifiEnabled(false);
                    tvStatus.setText("Wifi Off");
                }
            }
        });
    }
}