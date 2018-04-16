package com.vimalsagarji.vimalsagarjiapp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AboutAppGuruji;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AboutAppInfo;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.GurujiMissionActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.GurujiVisionActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.NotificationActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SettingActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fcm.Config;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private View headerLayout;
    private LinearLayout lin_alert;
    private LinearLayout lin_home;
    private LinearLayout lin_info;
    private LinearLayout lin_about;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;
    private LinearLayout lin_user;
    private LinearLayout lin_setting;
    private LinearLayout lin_show_hide;
    private TextView txt_aboutinfo, txt_aboutguruji, txt_aboutmission, txt_appinfo;
    private Dialog dialog;
    private CircleImageView img_youtube, img_facebook;
    private boolean doubleBackToExitPressedOnce = false;
    Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Sharedpreferance sharedpreferance;
    ToggleButton pushonoff;

    static Button notifCount;
    static int mNotifCount = 0;
    private ImageView img_back;
    private ImageView img_hide_show;
    private int flag = 0;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferance = new Sharedpreferance(MainActivity.this);
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
        //Push Notification
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_SHORT).show();

                    Log.e("message", "---------------" + message);
                }
            }
        };

        displayFirebaseRegId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_main2);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        openDesktop();
        findID();
        idClick();
//        setNotifCount(50);
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase reg id: ", regId);

        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e("Firebase reg id: ", regId);
        else
            Log.e("Firebase Reg Id is not received yet!", "");
    }

    private void findID() {
        img_youtube = (CircleImageView) headerLayout.findViewById(R.id.img_youtube);
        img_facebook = (CircleImageView) headerLayout.findViewById(R.id.img_facebook);
        lin_alert = (LinearLayout) headerLayout.findViewById(R.id.lin_alert);
        lin_info = (LinearLayout) headerLayout.findViewById(R.id.lin_info);
        lin_about = (LinearLayout) headerLayout.findViewById(R.id.lin_about);
        lin_event = (LinearLayout) headerLayout.findViewById(R.id.lin_event);
        lin_audio = (LinearLayout) headerLayout.findViewById(R.id.lin_audio);
        lin_video = (LinearLayout) headerLayout.findViewById(R.id.lin_video);
        lin_thought = (LinearLayout) headerLayout.findViewById(R.id.lin_thought);
        lin_gallery = (LinearLayout) headerLayout.findViewById(R.id.lin_gallery);
        lin_qa = (LinearLayout) headerLayout.findViewById(R.id.lin_qa);
        lin_comp = (LinearLayout) headerLayout.findViewById(R.id.lin_comp);
        lin_op = (LinearLayout) headerLayout.findViewById(R.id.lin_op);
        lin_bypeople = (LinearLayout) headerLayout.findViewById(R.id.lin_bypeople);
        lin_setting = (LinearLayout) headerLayout.findViewById(R.id.lin_setting);
        lin_show_hide = (LinearLayout) headerLayout.findViewById(R.id.lin_show_hide);
        txt_aboutinfo = (TextView) headerLayout.findViewById(R.id.txt_aboutinfo);
        txt_aboutguruji = (TextView) headerLayout.findViewById(R.id.txt_aboutguruji);
        txt_aboutmission = (TextView) headerLayout.findViewById(R.id.txt_aboutmission);
        txt_appinfo = (TextView) headerLayout.findViewById(R.id.txt_appinfo);
        img_hide_show= (ImageView) headerLayout.findViewById(R.id.img_adds);

    }

    private void idClick() {
        lin_alert.setOnClickListener(this);
        lin_info.setOnClickListener(this);
        lin_about.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
        lin_setting.setOnClickListener(this);
        img_youtube.setOnClickListener(this);
        img_facebook.setOnClickListener(this);
        txt_aboutinfo.setOnClickListener(this);
        txt_aboutguruji.setOnClickListener(this);
        txt_aboutmission.setOnClickListener(this);
        txt_appinfo.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lin_alert:
                intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                intent = new Intent(MainActivity.this, InformationCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_about:
                Log.e("lin_about", "------------------" + "click");
                if (flag == 0) {
                    flag = 1;
                    img_hide_show.setImageResource(R.drawable.ic_remove_black_24dp);
                    lin_show_hide.setVisibility(View.VISIBLE);

                } else {
                    flag = 0;
                    img_hide_show.setImageResource(R.drawable.ic_add_black_24dp);
                    lin_show_hide.setVisibility(View.GONE);
                }

                break;
            case R.id.lin_event:
                Log.e("information", "------------------" + "click");
                intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_audio:
                Log.e("lin_event", "------------------" + "click");
                intent = new Intent(MainActivity.this, AudioCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_video:
                Log.e("lin_video", "------------------" + "click");
                intent = new Intent(MainActivity.this, VideoCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_thought:
                Log.e("lin_video", "------------------" + "click");
                intent = new Intent(MainActivity.this, ThoughtsActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_gallery:
                Log.e("lin_gallery", "------------------" + "click");
                intent = new Intent(MainActivity.this, Gallery_All_Category.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_qa:
                Log.e("lin_qa", "------------------" + "click");
                intent = new Intent(MainActivity.this, QuestionAnswerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_comp:
                Log.e("lin_comp", "------------------" + "click");
                intent = new Intent(MainActivity.this, CompetitionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_op:
                Log.e("lin_op", "------------------" + "click");
                intent = new Intent(MainActivity.this, OpinionPoll.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_bypeople:
                Log.e("lin_bypeople", "------------------" + "click");
                intent = new Intent(MainActivity.this, ByPeople.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;
            case R.id.lin_setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();

                /*onBackPressed();
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_setting);
                dialog.show();
                pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
                img_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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
                            Toast.makeText(MainActivity.this, "Push notification on.", Toast.LENGTH_SHORT).show();
                        } else {
                            // The toggle is disabled
                            sharedpreferance.savePushNotification("pushoff");
                            pushonoff.setChecked(false);
                            Toast.makeText(MainActivity.this, "Push notification off.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                break;


            case R.id.txt_aboutinfo:
                intent = new Intent(MainActivity.this, AboutAppGuruji.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.txt_aboutguruji:
                intent = new Intent(MainActivity.this, GurujiVisionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.txt_aboutmission:
                intent = new Intent(MainActivity.this, GurujiMissionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.txt_appinfo:
                intent = new Intent(MainActivity.this, AboutAppInfo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;


            case R.id.img_youtube:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCBl49_j0js41gkXG6lhcUmQ"));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCBl49_j0js41gkXG6lhcUmQ"));
                }
                onBackPressed();
                break;

            case R.id.img_facebook:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/munivimalsagarji?ref=br_rs"));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/munivimalsagarji?ref=br_rs"));
                }
//                openFB("100006434383261");
                onBackPressed();
                break;


        }
    }

    public void openFB(String facebookId) {
        try {
            String facebookScheme = "fb://profile/" + facebookId;
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
            startActivity(facebookIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (ActivityNotFoundException e) {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=" + facebookId));
            startActivity(facebookIntent);
        }
    }

    public void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + profile + "/posts")));
        }
    }

    private void openDesktop() {
        Fragment fr = null;
        fr = new DesktopFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_search) {
//            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        if (id == R.id.action_notification) {
//            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
//        dialog.show();
        pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
        if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
            pushonoff.setChecked(true);
        } else {
            pushonoff.setChecked(false);
        }
//
    }

}
