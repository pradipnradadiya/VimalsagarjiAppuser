package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private TextView txt_privacy_policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.privacy_policy));
        toolbar.setTitleTextColor(0xFFFFFFFF);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left);


        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        txt_privacy_policy= (TextView) findViewById(R.id.txt_privacy_policy);
    }
}
