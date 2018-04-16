package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;


public class GurujiVisionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_eng, txt_hnd, txt_guj, txt_description;

    private LinearLayout lin_eng,lin_hnd,lin_guj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_guruji);
        bindId();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Guruji Vision");
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

    private void bindId() {
        txt_eng = (TextView) findViewById(R.id.txt_eng);
        txt_hnd = (TextView) findViewById(R.id.txt_hnd);
        txt_guj = (TextView) findViewById(R.id.txt_guj);
        txt_description = (TextView) findViewById(R.id.txt_description);

        lin_eng= (LinearLayout) findViewById(R.id.lin_eng);
        lin_hnd= (LinearLayout) findViewById(R.id.lin_hnd);
        lin_guj= (LinearLayout) findViewById(R.id.lin_guj);

        txt_eng.setOnClickListener(this);
        txt_hnd.setOnClickListener(this);
        txt_guj.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_eng:
                txt_description.setText(getResources().getString(R.string.about_guruji_vision_eng));
                txt_eng.setTextColor(Color.WHITE);
                txt_hnd.setTextColor(Color.BLACK);
                txt_guj.setTextColor(Color.BLACK);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeone);
                break;

            case R.id.txt_hnd:
                txt_description.setText(getResources().getString(R.string.about_guruji_vision_hnd));
                txt_eng.setTextColor(Color.BLACK);
                txt_hnd.setTextColor(Color.WHITE);
                txt_guj.setTextColor(Color.BLACK);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeone);
                break;

            case R.id.txt_guj:
                txt_description.setText(getResources().getString(R.string.about_guruji_vision_guj));
                txt_eng.setTextColor(Color.BLACK);
                txt_hnd.setTextColor(Color.BLACK);
                txt_guj.setTextColor(Color.WHITE);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
                break;

        }

    }

}
