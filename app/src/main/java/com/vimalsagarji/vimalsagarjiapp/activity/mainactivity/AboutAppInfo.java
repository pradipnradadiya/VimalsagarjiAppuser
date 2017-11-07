package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vimalsagarji.vimalsagarjiapp.R;

/**
 * Created by Grapes-Pradip on 02-Oct-17.
 */

public class AboutAppInfo extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About Us");
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
//        View count = menu.findItem(R.id.action_notification).getActionView();
//        notifCount = (Button) count.findViewById(R.id.notif_count);
//        notifCount.setText(String.valueOf(mNotifCount));


        return true;
    }

    //    private void setNotifCount(int count){
//        mNotifCount = count;
//        invalidateOptionsMenu();
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
//            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
