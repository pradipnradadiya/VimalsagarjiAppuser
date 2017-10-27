package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

/**
 * Created by Grapes-Pradip on 26-Oct-17.
 */

public class SettingActivity extends AppCompatActivity {
    Sharedpreferance sharedpreferance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        sharedpreferance = new Sharedpreferance(SettingActivity.this);
        final ToggleButton pushonoff = (ToggleButton) findViewById(R.id.pushonoff);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
            pushonoff.setChecked(true);
        } else {
            pushonoff.setChecked(false);
        }
        pushonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    sharedpreferance.savePushNotification("pushon");
                    pushonoff.setChecked(true);
                    Toast.makeText(SettingActivity.this, "Push notification on.", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    sharedpreferance.savePushNotification("pushoff");
                    pushonoff.setChecked(false);
                    Toast.makeText(SettingActivity.this, "Push notification off.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
