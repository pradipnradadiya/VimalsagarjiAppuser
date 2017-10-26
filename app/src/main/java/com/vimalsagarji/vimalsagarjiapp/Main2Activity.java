package com.vimalsagarji.vimalsagarjiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;


@SuppressWarnings("ALL")
public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private View headerLayout;
    private LinearLayout lin_info;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;
    LinearLayout lin_user;
    private boolean doubleBackToExitPressedOnce = false;
    private Intent intent;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void findID() {
        lin_info = (LinearLayout) headerLayout.findViewById(R.id.lin_info);
        lin_event = (LinearLayout) headerLayout.findViewById(R.id.lin_event);
        lin_audio = (LinearLayout) headerLayout.findViewById(R.id.lin_audio);
        lin_video = (LinearLayout) headerLayout.findViewById(R.id.lin_video);
        lin_thought = (LinearLayout) headerLayout.findViewById(R.id.lin_thought);
        lin_gallery = (LinearLayout) headerLayout.findViewById(R.id.lin_gallery);
        lin_qa = (LinearLayout) headerLayout.findViewById(R.id.lin_qa);
        lin_comp = (LinearLayout) headerLayout.findViewById(R.id.lin_comp);
        lin_op = (LinearLayout) headerLayout.findViewById(R.id.lin_op);
        lin_bypeople = (LinearLayout) headerLayout.findViewById(R.id.lin_bypeople);
    }

    private void idClick() {
        lin_info.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
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

            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                intent = new Intent(Main2Activity.this, InformationCategory.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_event:
                intent = new Intent(Main2Activity.this, EventActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_audio:
                intent = new Intent(Main2Activity.this, AudioCategory.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_video:
                intent = new Intent(Main2Activity.this, VideoCategory.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_thought:
                intent = new Intent(Main2Activity.this, ThoughtsActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_gallery:
                intent = new Intent(Main2Activity.this, Gallery_All_Category.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_qa:
                intent = new Intent(Main2Activity.this, QuestionAnswerActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_comp:
                intent = new Intent(Main2Activity.this, CompetitionActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_op:
                intent = new Intent(Main2Activity.this, OpinionPoll.class);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.lin_bypeople:
                intent = new Intent(Main2Activity.this, ByPeople.class);
                startActivity(intent);
                onBackPressed();
                break;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            return true;
        }
        /*if (id==R.id.action_notification){
            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }




}
