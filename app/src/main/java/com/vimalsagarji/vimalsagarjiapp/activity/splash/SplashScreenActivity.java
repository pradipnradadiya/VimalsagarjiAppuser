package com.vimalsagarji.vimalsagarjiapp.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.vimalsagarji.vimalsagarjiapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SHOW_TIME = 3000;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash_screen);
        new BackgroundSplashTask().execute();
    }

    private class BackgroundSplashTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(SplashScreenActivity.this, SecondSplashScreenActivity.class);
            startActivity(intent);
            finish();


        }
    }
}
