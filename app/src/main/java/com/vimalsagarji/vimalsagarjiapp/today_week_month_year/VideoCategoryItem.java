package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Intent;
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
import com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.AllVideoFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.ThisMonthVideoFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.ThisWeekVideoFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.TodayAllVideoFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.TodayVideoFragment;

import static com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.TodayVideoFragment.listViewvideo;

@SuppressWarnings("ALL")
public class VideoCategoryItem extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_today;
    private TextView txt_thisweek;
    private TextView txt_thismonth;
    private TextView txt_all;
    private FrameLayout framecontent;
    private View view_today;
    private View view_week;
    private View view_month;
    private View view_all;
    public static String video_cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_information_category1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String video_cat_Title = intent.getStringExtra("listTitle");
        video_cat_id = intent.getStringExtra("v_cid");
        Log.e("cid", "---------------------------------" + video_cat_id);

        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        imgHome.setVisibility(View.GONE);
        txt_title.setText(video_cat_Title);
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
        etText.setHint("Search Video");

        LinearLayout lin_main = (LinearLayout) findViewById(R.id.lin_main);
        if (video_cat_id.equalsIgnoreCase("e_alliamgeid")) {
            lin_main.setVisibility(View.GONE);
            openAllInfromation();

        } else if (video_cat_id.equalsIgnoreCase("bypeopleidid")) {
            lin_main.setVisibility(View.GONE);
            openAllInfromation();
        } else {
            openTodayInfromation();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_search:
                Intent intent = new Intent(VideoCategoryItem.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.imgarrorback:
                listViewvideo.setAdapter(null);
//                Intent intent = new Intent(VideoCategoryItem.this, VideoCategory.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.imgHome:
//                Intent intent1 = new Intent(VideoCategoryItem.this, VideoCategory.class);
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
        view_today.setVisibility(View.VISIBLE);
        view_month.setVisibility(View.GONE);
        view_week.setVisibility(View.GONE);
        view_all.setVisibility(View.GONE);

        Fragment fr = null;
        fr = new TodayVideoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openWeekInfromation() {
        view_today.setVisibility(View.GONE);
        view_month.setVisibility(View.GONE);
        view_week.setVisibility(View.VISIBLE);
        view_all.setVisibility(View.GONE);
        Fragment fr = null;
        fr = new ThisWeekVideoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openMonthInfromation() {
        view_today.setVisibility(View.GONE);
        view_month.setVisibility(View.VISIBLE);
        view_week.setVisibility(View.GONE);
        view_all.setVisibility(View.GONE);
        Fragment fr = null;
        fr = new ThisMonthVideoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAllInfromation() {
        view_today.setVisibility(View.GONE);
        view_month.setVisibility(View.GONE);
        view_week.setVisibility(View.GONE);
        view_all.setVisibility(View.VISIBLE);
        Fragment fr = null;
        fr = new AllVideoFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listViewvideo.setAdapter(null);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
