package com.vimalsagarji.vimalsagarjiapp.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.MainActivity;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressWarnings("ALL")
public class ThirdSpalshScreenActivity extends AppCompatActivity {
    Intent intent;
    private Sharedpreferance sharedpreferance;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_third_spalsh_screen);
        sharedpreferance = new Sharedpreferance(ThirdSpalshScreenActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }


    private final CountDownTimer timer = new CountDownTimer(2000, 2000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (CommonMethod.isInternetConnected(ThirdSpalshScreenActivity.this)) {
                    new AllreadyRegisterUser().execute(sharedpreferance.getEmail(), sharedpreferance.getMobile(), sharedpreferance.getToken());
                } else {
                    Toast.makeText(ThirdSpalshScreenActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
//                    try {
//                        Thread.sleep(2000);
////
////                        finish();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        }
    };

    private class AllreadyRegisterUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("EmailID", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Phone", params[1]));
                nameValuePairs.add(new BasicNameValuePair("DeviceID", params[2]));
                responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/aluser/checkuser", nameValuePairs, ThirdSpalshScreenActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("reponse", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progress.setVisibility(View.GONE);
//                    dialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.e("array", "-----------------success");
                        String uid = object.getString("ID");
                        String email = object.getString("EmailID");
                        String mobile = object.getString("Phone");
                        sharedpreferance.saveId(uid);
                        sharedpreferance.saveEmail(email);
                        sharedpreferance.saveMobile(mobile);

                        Log.e("email", "---------------" + sharedpreferance.getEmail());
                        Log.e("uid", "---------------" + sharedpreferance.getId());

                        Intent intent = new Intent(ThirdSpalshScreenActivity.this, ActivityHomeMain.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                } else {
                    progress.setVisibility(View.GONE);
//                    Toast.makeText(ThirdSpalshScreenActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
