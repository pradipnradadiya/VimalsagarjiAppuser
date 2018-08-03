package com.vimalsagarji.vimalsagarjiapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.VisharMessageActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.HomeSliderAdapter;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.EventCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fcm.Config;
import com.vimalsagarji.vimalsagarjiapp.model.SliderItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionAllActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;
import com.vimalsagarji.vimalsagarjiapp.utils.DashboardCirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHomeMain extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rel_info;
    private RelativeLayout rel_event;
    private RelativeLayout rel_audio;
    private RelativeLayout rel_video;
    private RelativeLayout rel_thought;
    private RelativeLayout rel_gallery;
    private RelativeLayout rel_qa;
    private RelativeLayout rel_comp;
    private RelativeLayout rel_bypeople;

    private ViewPager viewpager_slider;
    private DashboardCirclePageIndicator indicator;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private final ArrayList<SliderItem> itemSplashArrayList = new ArrayList<>();
    private HomeSliderAdapter customImageAdapter;

    private View headerLayout;
    private LinearLayout lin_alert;
    private LinearLayout lin_vichar;
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
    private CircleImageView img_youtube, img_facebook;
    private boolean doubleBackToExitPressedOnce = false;
    Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Sharedpreferance sharedpreferance;
    ToggleButton pushonoff;
    private Dialog dialog;

    private ImageView img_hide_show;
    private int flag = 0;

    private DrawerLayout drawer_layout;
    private ImageView img_menu_drawer, img_search, img_notification;

    ImageView img_slide;
    String currentVersion, playStoreVersion;

    //Count posts
    private TextView txt_latestposts_count, txt_d_latest_posts, txt_d_infomation, txt_d_event, txt_d_thought, txt_d_audio, txt_d_video, txt_d_qa, txt_d_bypeople;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

       /* //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/


        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
            Log.e("version", "------------------" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



//        new FetchAppVersionFromGooglePlayStore().execute();



      /*  String newVersion = null;
        try {
            newVersion = Jsoup
                    .connect(
                            "https://play.google.com/store/apps/details?id="
                                    + getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent(
                            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("new Version", newVersion);
*/
        /*

        VersionChecker versionChecker = new VersionChecker();
        try {
            String latestVersion = versionChecker.execute().get();
            Log.e("latest version","------------"+latestVersion);

            if (currentVersion.equalsIgnoreCase(latestVersion)){

            }else{

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Alert")
                        .setMessage("this app update version available in play store.")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

*/
        sharedpreferance = new Sharedpreferance(ActivityHomeMain.this);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        findID();
        idClick();

//        drawer_layout.setPadding(0, getStatusBarHeight(), 0, 0);
        if (CommonMethod.isInternetConnected(ActivityHomeMain.this)) {

            new LoadSlideImage().execute();
            new GetAllNotes().execute();

        } else {
            img_slide.setVisibility(View.VISIBLE);
        }
    }

    //Drawer methos navigate slider
    private void openDrawerSlider() {

        if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            drawer_layout.openDrawer(Gravity.LEFT);
        }
        if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
            drawer_layout.closeDrawer(Gravity.RIGHT);
        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase reg id: ", regId);

        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e("Firebase reg id: ", regId);
        else
            Log.e("Firebase Id is not yet!", "");
    }

    private void findID() {

        img_slide = (ImageView) findViewById(R.id.img_slide);
        viewpager_slider = (ViewPager) findViewById(R.id.viewPager);
        indicator = (DashboardCirclePageIndicator) findViewById(R.id.indicator);

        rel_info = (RelativeLayout) findViewById(R.id.rel_info);
        rel_event = (RelativeLayout) findViewById(R.id.rel_event);
        rel_audio = (RelativeLayout) findViewById(R.id.rel_audio);
        rel_video = (RelativeLayout) findViewById(R.id.rel_video);
        rel_thought = (RelativeLayout) findViewById(R.id.rel_thought);
        rel_gallery = (RelativeLayout) findViewById(R.id.rel_gallery);
        rel_qa = (RelativeLayout) findViewById(R.id.rel_qa);
        rel_comp = (RelativeLayout) findViewById(R.id.rel_comp);
        rel_bypeople = (RelativeLayout) findViewById(R.id.rel_bypeople);


        img_youtube = (CircleImageView) headerLayout.findViewById(R.id.img_youtube);
        img_facebook = (CircleImageView) headerLayout.findViewById(R.id.img_facebook);
        lin_alert = (LinearLayout) headerLayout.findViewById(R.id.lin_alert);
        lin_vichar = (LinearLayout) headerLayout.findViewById(R.id.lin_vichar);
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
        img_hide_show = (ImageView) headerLayout.findViewById(R.id.img_adds);

        txt_latestposts_count = (TextView) findViewById(R.id.txt_latestposts_count);
        txt_d_latest_posts = (TextView) headerLayout.findViewById(R.id.txt_d_latest_posts);
        txt_d_infomation = (TextView) headerLayout.findViewById(R.id.txt_d_infomation);
        txt_d_event = (TextView) headerLayout.findViewById(R.id.txt_d_event);
        txt_d_thought = (TextView) headerLayout.findViewById(R.id.txt_d_thought);
        txt_d_audio = (TextView) headerLayout.findViewById(R.id.txt_d_audio);
        txt_d_video = (TextView) headerLayout.findViewById(R.id.txt_d_video);
        txt_d_qa = (TextView) headerLayout.findViewById(R.id.txt_d_qa);
        txt_d_bypeople = (TextView) headerLayout.findViewById(R.id.txt_d_bypeople);


        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_menu_drawer = (ImageView) findViewById(R.id.img_menu_drawer);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_notification = (ImageView) findViewById(R.id.img_notification);

    }

    private void idClick() {
        rel_info.setOnClickListener(this);
        rel_event.setOnClickListener(this);

        rel_audio.setOnClickListener(this);
        rel_video.setOnClickListener(this);
        rel_thought.setOnClickListener(this);
        rel_gallery.setOnClickListener(this);
        rel_qa.setOnClickListener(this);
        rel_comp.setOnClickListener(this);
        rel_bypeople.setOnClickListener(this);

        lin_alert.setOnClickListener(this);
        lin_vichar.setOnClickListener(this);
        lin_vichar.setVisibility(View.GONE);
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
        img_menu_drawer.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_notification.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Log.e("double back click", "----------------");
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    Log.e("back click", "----------------");

                }
            }, 2000);

        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rel_info:
                intent = new Intent(ActivityHomeMain.this, InformationCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_event:
                intent = new Intent(ActivityHomeMain.this, EventCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_audio:
                intent = new Intent(ActivityHomeMain.this, AudioCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_video:
                intent = new Intent(ActivityHomeMain.this, VideoCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_thought:
                intent = new Intent(ActivityHomeMain.this, ThoughtsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_gallery:
                intent = new Intent(ActivityHomeMain.this, Gallery_All_Category.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_qa:
                intent = new Intent(ActivityHomeMain.this, QuestionAnswerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_comp:
//                intent = new Intent(ActivityHomeMain.this, CompetitionAllActivity.class);
                intent = new Intent(ActivityHomeMain.this, CompetitionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_bypeople:
                intent = new Intent(ActivityHomeMain.this, ByPeople.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.img_menu_drawer:
                openDrawerSlider();
                break;

            case R.id.img_search:
                intent = new Intent(ActivityHomeMain.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.img_notification:
                intent = new Intent(ActivityHomeMain.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.lin_alert:
                intent = new Intent(ActivityHomeMain.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_vichar:
                intent = new Intent(ActivityHomeMain.this, VisharMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, InformationCategory.class);
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
                intent = new Intent(ActivityHomeMain.this, EventCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_audio:
                Log.e("lin_event", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, AudioCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_video:
                Log.e("lin_video", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, VideoCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_thought:
                Log.e("lin_video", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, ThoughtsActivity.class);
                startActivity(intent);
                onBackPressed();
                break;

            case R.id.lin_gallery:
                Log.e("lin_gallery", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, Gallery_All_Category.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_qa:
                Log.e("lin_qa", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, QuestionAnswerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_comp:
                Log.e("lin_comp", "------------------" + "click");
//                intent = new Intent(ActivityHomeMain.this, CompetitionAllActivity.class);
                intent = new Intent(ActivityHomeMain.this, CompetitionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_op:
                Log.e("lin_op", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, OpinionPoll.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_bypeople:
                Log.e("lin_bypeople", "------------------" + "click");
                intent = new Intent(ActivityHomeMain.this, ByPeople.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_setting:
                intent = new Intent(ActivityHomeMain.this, SettingActivity.class);
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
                intent = new Intent(ActivityHomeMain.this, AboutAppGuruji.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_aboutguruji:
                intent = new Intent(ActivityHomeMain.this, GurujiVisionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_aboutmission:
                intent = new Intent(ActivityHomeMain.this, GurujiMissionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_appinfo:
                intent = new Intent(ActivityHomeMain.this, AboutAppInfo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
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
//                try {
//
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji/"));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji/"));
//                }
////                openFB("100006434383261");
//                onBackPressed();
//
//
//

             /*   try
                {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/aacharyavimalsagarsooriji"));
                    startActivity(followIntent);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {
                            Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/aacharyavimalsagarsooriji"));
                            startActivity(followIntent);
                        }
                    }, 1000 * 2);

                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji")));
                    String errorMessage = (e.getMessage()==null)?"Message is empty":e.getMessage();
                    Log.e("Unlock_ScreenActivityd" ,errorMessage);
                }*/


/*
                try
                {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Vimalsagarsuruji"));
                    startActivity(followIntent);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {
                            Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Vimalsagarsuruji"));
                            startActivity(followIntent);
                        }
                    }, 1000 * 2);

                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsuruji")));
                    String errorMessage = (e.getMessage()==null)?"Message is empty":e.getMessage();
                    Log.e("Unlock_Activity:FacebookAppNot" ,errorMessage);
                }*/

                break;

        }

    }


    public static void getOpenFacebookIntent(Context context) {
        try {
            // open in Facebook app
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/Vimalsagarsuruji"));
        } catch (Exception e) {
            // open in browser
            new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Vimalsagarsuruji"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = new Dialog(ActivityHomeMain.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
//        dialog.show();
        pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
        if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
            pushonoff.setChecked(true);
        } else {
            pushonoff.setChecked(false);
        }


        if (CommonMethod.isInternetConnected(ActivityHomeMain.this)) {
//            new LoadSlideImage().execute();
            img_slide.setVisibility(View.GONE);
        } else {
            img_slide.setVisibility(View.VISIBLE);
        }

        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            txt_latestposts_count.setVisibility(View.GONE);
            txt_d_latest_posts.setVisibility(View.GONE);
            txt_d_infomation.setVisibility(View.GONE);
            txt_d_event.setVisibility(View.GONE);
            txt_d_thought.setVisibility(View.GONE);
            txt_d_audio.setVisibility(View.GONE);
            txt_d_video.setVisibility(View.GONE);
            txt_d_qa.setVisibility(View.GONE);
            txt_d_bypeople.setVisibility(View.GONE);
        } else {
            new checkCount().execute();
        }
//
    }

    private class LoadSlideImage extends AsyncTask<String, Void, String> {
        String response = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHomeMain.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {


                response = CommonMethod.getStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/gallery/getallbanner");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                new CheckVersion().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("response", "---------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        itemSplashArrayList.add(new SliderItem("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/static/bannerimage/" + jsonObject1.getString("image")));
                    }


                }
            } catch (JSONException e) {

                e.printStackTrace();
            }


            customImageAdapter = new HomeSliderAdapter(ActivityHomeMain.this, itemSplashArrayList);

            viewpager_slider.setAdapter(customImageAdapter);
            indicator.setViewPager(viewpager_slider);

            Log.e("image length", "" + itemSplashArrayList.size());

            if (itemSplashArrayList.size() > 1) {
                NUM_PAGES = itemSplashArrayList.size();
                final android.os.Handler handler = new android.os.Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0;
                        }
                        viewpager_slider.setCurrentItem(currentPage++);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);

                    }

                }, 5000, 5000);
            }
        }
    }


    private class CheckVersion extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                responseString = CommonMethod.getStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/info/getversion");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    playStoreVersion = jsonObject.getString("message");

                    if (playStoreVersion.equalsIgnoreCase(currentVersion)) {

                    } else {

                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(ActivityHomeMain.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ActivityHomeMain.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle("Alert")

                                .setMessage("This apps latest update version available in play store, so please download it.")
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete]

                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }

                                    }
                                })
                                .setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing

                                        dialog.dismiss();

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return
                        Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.aacharyavimalsagarsuriji.nayisochsahidisha" + "&hl=en")
                                .timeout(10000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                                .select("div[itemprop=softwareVersion]")
                                .first()
                                .ownText();

            } catch (Exception e) {
                return "";
            }
        }

        protected void onPostExecute(String string) {
            String newVersion = string;
            Log.e("new Version", newVersion);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class checkCount extends AsyncTask<String, Void, String> {

        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "countviews/getuserviewcount", nameValuePairs, ActivityHomeMain.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONObject object = jsonObject.getJSONObject("data");
                    int audio = object.getInt("audio");
                    int bypeople = object.getInt("bypeople");
                    int event = object.getInt("event");
                    int info = object.getInt("info");
                    int thought = object.getInt("thought");
                    int video = object.getInt("video");
                    int qa = object.getInt("qa");

                    int total = audio + bypeople + event + info + thought + video + qa;
                    if (total == 0) {
                        txt_d_latest_posts.setVisibility(View.GONE);
                        txt_latestposts_count.setVisibility(View.GONE);
                    } else {
                        txt_d_latest_posts.setVisibility(View.VISIBLE);
                        txt_latestposts_count.setVisibility(View.VISIBLE);
                        txt_d_latest_posts.setText(String.valueOf(total));
                        txt_latestposts_count.setText(String.valueOf(total));
                    }

                    if (audio == 0) {
                        txt_d_audio.setVisibility(View.GONE);
                    } else {
                        txt_d_audio.setVisibility(View.VISIBLE);
                        txt_d_audio.setText(String.valueOf(audio));
                    }

                    if (bypeople == 0) {
                        txt_d_bypeople.setVisibility(View.GONE);

                    } else {
                        txt_d_bypeople.setVisibility(View.VISIBLE);
                        txt_d_bypeople.setText(String.valueOf(bypeople));

                    }

                    if (event == 0) {
                        txt_d_event.setVisibility(View.GONE);
                    } else {
                        txt_d_event.setVisibility(View.VISIBLE);
                        txt_d_event.setText(String.valueOf(event));
                    }

                    if (info == 0) {
                        txt_d_infomation.setVisibility(View.GONE);
                    } else {
                        txt_d_infomation.setVisibility(View.VISIBLE);
                        txt_d_infomation.setText(String.valueOf(info));
                    }

                    if (thought == 0) {
                        txt_d_thought.setVisibility(View.GONE);
                    } else {
                        txt_d_thought.setVisibility(View.VISIBLE);
                        txt_d_thought.setText(String.valueOf(thought));
                    }

                    if (video == 0) {
                        txt_d_video.setVisibility(View.GONE);
                    } else {
                        txt_d_video.setVisibility(View.VISIBLE);
                        txt_d_video.setText(String.valueOf(video));

                    }

                    if (qa == 0) {
                        txt_d_qa.setVisibility(View.GONE);
                    } else {
                        txt_d_qa.setVisibility(View.VISIBLE);
                        txt_d_qa.setText(String.valueOf(qa));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllNotes extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + "competition/getcompetitionnote");
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

//                    usersItems=new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String date = jsonObject1.getString("date");
                        String time = jsonObject1.getString("time");


                        final Dialog dialog = new Dialog(ActivityHomeMain.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_alert);

                        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close_popup);
                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


                        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                        TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
                        TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
                        TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);

                        txt_title.setText(CommonMethod.decodeEmoji(title));
                        txt_description.setText(CommonMethod.decodeEmoji(description));

                        String[] datesarray = CommonMethod.decodeEmoji(date).split("-");

                        txt_date.setText(CommonMethod.decodeEmoji(datesarray[2] + "-" + datesarray[1] + "-" + datesarray[0]));

                        txt_time.setText(CommonMethod.decodeEmoji(time));

                        dialog.show();
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        dialog.getWindow().setGravity(Gravity.CENTER);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
