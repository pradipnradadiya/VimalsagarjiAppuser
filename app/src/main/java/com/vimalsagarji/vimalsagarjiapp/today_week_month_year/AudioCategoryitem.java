package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment.AllAudioFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment.ThisMonthAudioFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment.ThisWeekAudioFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment.TodayAudioFragment;

@SuppressWarnings("ALL")
public class AudioCategoryitem extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_today;
    private TextView txt_thisweek;
    private TextView txt_thismonth;
    private TextView txt_all;
    private FrameLayout framecontent;
    private View view_today;
    private View view_week;
    private View view_month;
    private View view_all;
    private String strAudioTitle;
    private String strCategoryID;
    private String audiostrid;
    LinearLayout lin_today, lin_week, lin_month, lin_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_information_category1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbar);
        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);

        imgHome.setVisibility(View.GONE);
        EditText etText = (EditText) findViewById(R.id.etText);
        txt_today = (TextView) findViewById(R.id.txt_today);
        txt_thisweek = (TextView) findViewById(R.id.txt_thisweek);
        txt_thismonth = (TextView) findViewById(R.id.txt_thismonth);
        txt_all = (TextView) findViewById(R.id.txt_all);
        framecontent = (FrameLayout) findViewById(R.id.framecontent);
        view_today = findViewById(R.id.view_today);
        view_week = findViewById(R.id.view_week);
        view_month = findViewById(R.id.view_month);
        view_all = findViewById(R.id.view_all);
        imgBack.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        txt_today.setOnClickListener(this);
        txt_thisweek.setOnClickListener(this);
        txt_thismonth.setOnClickListener(this);
        txt_all.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_search.setVisibility(View.GONE);

        Intent intent = getIntent();
        strAudioTitle = intent.getStringExtra("listTitle");
        Log.e("title", "--------------------------------" + strAudioTitle);
        txt_title.setText(CommonMethod.decodeEmoji(strAudioTitle));
        audiostrid = intent.getStringExtra("listId");
        Log.e("sid", "--------------------------------" + audiostrid);
        strCategoryID = intent.getStringExtra("listCategoryId");
        Log.e("cid", "--------------------------------" + strCategoryID);
        etText.setHint("Search Audio");

        lin_today = (LinearLayout) findViewById(R.id.lin_today);
        lin_week = (LinearLayout) findViewById(R.id.lin_week);
        lin_month = (LinearLayout) findViewById(R.id.lin_month);
        lin_all = (LinearLayout) findViewById(R.id.lin_all);

        LinearLayout lin_main= (LinearLayout) findViewById(R.id.lin_main);
        if (strCategoryID.equalsIgnoreCase("e_alliamgeid")) {
            lin_main.setVisibility(View.GONE);
            openAllInfromation();
        }else if (strCategoryID.equalsIgnoreCase("bypeopleidid")){
            lin_main.setVisibility(View.GONE);
            openAllInfromation();
        }
        else {
            openAllInfromation();
        }


    }

    public String getMydata() {
        return strCategoryID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_search:
                Intent intent = new Intent(AudioCategoryitem.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.imgarrorback:
//                Intent intent = new Intent(AudioCategoryitem.this, MainActivity.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.imgHome:
//                Intent intent1 = new Intent(AudioCategoryitem.this, MainActivity.class);
//                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.txt_today:
                openTodayInfromation();
                break;
            case R.id.txt_thisweek:
                openWeekInfromation();
                break;
            case R.id.txt_thismonth:
                openMonthInfromation();
                break;
            case R.id.txt_all:
                openAllInfromation();
            default:
                break;
        }

    }

    private void openTodayInfromation() {

        txt_today.setTextColor(Color.WHITE);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.BLACK);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);

        Fragment fr = null;
        fr = new TodayAudioFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openWeekInfromation() {
        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.WHITE);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.BLACK);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);
        Fragment fr = null;
        fr = new ThisWeekAudioFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openMonthInfromation() {
        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.WHITE);
        txt_all.setTextColor(Color.BLACK);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);
        Fragment fr = null;
        fr = new ThisMonthAudioFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAllInfromation() {
        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.WHITE);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        Fragment fr = null;
        fr = new AllAudioFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
