package com.vimalsagarji.vimalsagarjiapp.activity.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

@SuppressWarnings("ALL")
public class ThirdSpalshScreenActivity extends ActivityManagePermission {
    Intent intent;
    private Sharedpreferance sharedpreferance;
    private ProgressBar progress;
    private TextView txt_content,txt_timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_third_spalsh_screen);

        sharedpreferance = new Sharedpreferance(ThirdSpalshScreenActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
        txt_content = findViewById(R.id.txt_content);
        txt_timer = findViewById(R.id.txt_timer);

        new ContentGet().execute();
    }

    private void askforPermission() {

        askCompactPermissions(new String[]{ PermissionUtils.Manifest_RECEIVE_SMS,PermissionUtils.Manifest_READ_SMS,PermissionUtils.Manifest_SEND_SMS,PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE}, new PermissionResult() {
            @Override
            public void permissionGranted() {
                Intent intent = new Intent(ThirdSpalshScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
//                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }

            @Override
            public void permissionDenied() {
                //permission denied
                askforPermission();
                //replace with your action
            }

            @Override
            public void permissionForeverDenied() {
                askforPermission();
            }

        });

    }

    private void askforLogin() {

        askCompactPermissions(new String[]{PermissionUtils.Manifest_RECEIVE_SMS,PermissionUtils.Manifest_READ_SMS}, new PermissionResult() {
            @Override
            public void permissionGranted() {
                Intent intent = new Intent(ThirdSpalshScreenActivity.this, ActivityHomeMain.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }

            @Override
            public void permissionDenied() {
                //permission denied
                askforPermission();
                //replace with your action
            }

            @Override
            public void permissionForeverDenied() {
                askforPermission();
            }
        });
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

    private final CountDownTimer timer = new CountDownTimer(16000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("time","----------"+millisUntilFinished / 1000);
            txt_timer.setText(String.valueOf(millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {
            txt_timer.setText("");
            if (sharedpreferance.getId().equalsIgnoreCase("")) {

                askforPermission();
            }

            else {
                if (CommonMethod.isInternetConnected(ThirdSpalshScreenActivity.this)) {

                    new AllreadyRegisterUser().execute(sharedpreferance.getEmail(), sharedpreferance.getMobile(), sharedpreferance.getToken());
                } else {

                    Toast.makeText(ThirdSpalshScreenActivity.this, R.string.internet, Toast.LENGTH_LONG).show();

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
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"aluser/checkuser", nameValuePairs, ThirdSpalshScreenActivity.this);

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
                        String Address = object.getString("Address");
                        sharedpreferance.saveId(uid);
                        sharedpreferance.saveEmail(email);
                        sharedpreferance.saveMobile(mobile);
                        sharedpreferance.saveFirst_name(Address);

                        Log.e("email", "---------------" + sharedpreferance.getEmail());
                        Log.e("uid", "---------------" + sharedpreferance.getId());


                        askforLogin();

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

    private class ContentGet extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"quote/viewQuote");
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

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");

                        JSONObject object = jsonArray.getJSONObject(0);

                        Log.e("quote","-------------"+object.getString("title"));

                        txt_content.setText(CommonMethod.decodeEmoji(object.getString("title")));

                        Log.e("array", "-----------------success");


                    progress.setVisibility(View.GONE);

                } else {
                    progress.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
