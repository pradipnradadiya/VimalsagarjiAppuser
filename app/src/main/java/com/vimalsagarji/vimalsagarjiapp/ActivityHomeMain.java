package com.vimalsagarji.vimalsagarjiapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.vimalsagarji.vimalsagarjiapp.adpter.HomeSliderAdapter;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fcm.Config;
import com.vimalsagarji.vimalsagarjiapp.model.SliderItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;
import com.vimalsagarji.vimalsagarjiapp.utils.DashboardCirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private final ArrayList<SliderItem> itemSplashArrayList = new ArrayList<>();
    private HomeSliderAdapter customImageAdapter;

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
    private CircleImageView img_youtube, img_facebook;
    private boolean doubleBackToExitPressedOnce = false;
    Intent intent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Sharedpreferance sharedpreferance;
    ToggleButton pushonoff;
    private Dialog dialog;

    static Button notifCount;
    static int mNotifCount = 0;
    private ImageView img_back;
    private ImageView img_hide_show;
    private int flag = 0;

    private DrawerLayout drawer_layout;
    private ImageView img_menu_drawer, img_search, img_notification;

    ImageView img_slide;

//    private final String url = "http://ecx.images-amazon.com/images/I/71Pe-ft8QsL._UL1500_.jpg";
//    private final String url1 = "http://www.wamanharipethesons.com/portalrepository/catalogs/default/WHPS288.248_0_r.jpg";
//    private final String url2 = "http://www.southjewellery.com/wp-content/uploads/2013/09/grt_jewellers_gold_necklace.jpg";
//    private final String url3 = "http://baggout.tiles.large.new1.s3-ap-southeast-1.amazonaws.com/MK-Jewellers-Traditional-Gold-Design-Beautiful-Immitation-Necklace-Jewellery-Set-82650665-e5a6b7bb-0afa-46b5-8639-573cad42dd0b_0.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
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

        if (CommonMethod.isInternetConnected(ActivityHomeMain.this)) {
            new LoadSlideImage().execute();
        }else{
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

        img_slide= (ImageView) findViewById(R.id.img_slide);
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
            case R.id.rel_info:
                intent = new Intent(ActivityHomeMain.this, InformationCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.rel_event:
                intent = new Intent(ActivityHomeMain.this, EventActivity.class);
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
                intent = new Intent(ActivityHomeMain.this, EventActivity.class);
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
                onBackPressed();
                break;

            case R.id.txt_aboutguruji:
                intent = new Intent(ActivityHomeMain.this, GurujiVisionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.txt_aboutmission:
                intent = new Intent(ActivityHomeMain.this, GurujiMissionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.txt_appinfo:
                intent = new Intent(ActivityHomeMain.this, AboutAppInfo.class);
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
        }else{
            img_slide.setVisibility(View.VISIBLE);
        }
//
    }

    private class LoadSlideImage extends AsyncTask<String, Void, String> {
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            try {
                response = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/gallery/getImages");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        itemSplashArrayList.add(new SliderItem("http://"+jsonObject1.getString("url")));
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
}
