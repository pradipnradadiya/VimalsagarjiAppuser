package com.vimalsagarji.vimalsagarjiapp.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pradip-PC on 10/21/2016.
 */

@SuppressWarnings("ALL")
public class AllQuestionDetail extends AppCompatActivity {
    ImageView imgEvent;
    private ImageView imgBack;
    private ImageView imgHome;
    private TextView txt_question;
    private TextView txt_answer;
    private String view;
    private String qid;
    private String que;
    private String ans;

    KProgressHUD loadingProgressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail);
        txt_question = (TextView) findViewById(R.id.txt_question);
        txt_answer = (TextView) findViewById(R.id.txt_answer);
        imgBack = (ImageView) findViewById(R.id.imgarrorback);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHome.setVisibility(View.GONE);
        Intent intent = getIntent();
        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Question Detail");

        Log.e("Question", "-----------------------" + intent.getStringExtra("Question"));
        Log.e("Answer", "-------------------------" + intent.getStringExtra("Answer"));

//        String que = intent.getStringExtra("Question");
//        String ans = intent.getStringExtra("Answer");
//        view = intent.getStringExtra("view");
        qid = intent.getStringExtra("qid");

        new viewQuestionAnswer().execute();


        if (CommonMethod.isInternetConnected(AllQuestionDetail.this)) {
            new countView().execute();
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intentBack=new Intent(getApplicationContext(),QuestionAnswerActivity.class);
//                startActivity(intentBack);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intentHome);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    private class countView extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/countviews/question/?qid=" + qid + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
        }

    }

    private class viewQuestionAnswer extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AllQuestionDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/questionanswer/viewquesansbyid/?qid=" + qid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        jsonObject1.getString("ID");
                        que = jsonObject1.getString("Question");
                        ans = jsonObject1.getString("Answer");
                        jsonObject1.getString("Date");
                        jsonObject1.getString("Is_Approved");
                        jsonObject1.getString("UserID");
                        jsonObject1.getString("View");
                        txt_answer.setText(ans);
                        txt_question.setText("Question. :" + que);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
        }

    }
}
