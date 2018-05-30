package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionCategoryAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionQuestionAllAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionItem;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestion;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class CompetitionQuestionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerCompetitionQuestionAllAdapter recyclerCompetitionCategoryAdapter;
    private ProgressBar progress_load;
    private ArrayList<CompetitionQuestionItem> competitionItemArrayList = new ArrayList<>();
    private TextView txt_nodata;
    private TextView txt_timer;
    private String cid, minute;

    //Timer
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;

    private Button btn_submit;
    Sharedpreferance sharedpreferance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_questionall);
        sharedpreferance = new Sharedpreferance(CompetitionQuestionActivity.this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        minute = intent.getStringExtra("minute");
        bindID();

        new GetAllCompetitionQuestion().execute();


        int time = Integer.parseInt(minute) * 60;
        totalTimeCountInMilliseconds = time * 1000;      // time count for 3 minutes = 180 seconds
        timeBlinkInMilliseconds = 30 * 1000;


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (competitionItemArrayList.size() == recyclerCompetitionCategoryAdapter.qidlist.size()) {
                    Log.e("qid", "----------------------" + recyclerCompetitionCategoryAdapter.qidlist);
                    Log.e("answerlist", "----------------------" + recyclerCompetitionCategoryAdapter.answerlist);

                    new PostQuestionData().execute();

                } else {
                    Toast.makeText(CompetitionQuestionActivity.this, "Please fill all the question", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void bindID() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompetitionQuestionActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_competition);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        txt_timer = (TextView) findViewById(R.id.txt_timer);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    @SuppressLint("StaticFieldLeak")
    private class GetAllCompetitionQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllCompetionQuestionActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));

            responseJSON = CommonMethod.postStringResponse(CommonURL.Main_url + "competition/getquestionbycid", nameValuePairs, CompetitionQuestionActivity.this);

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);

                    countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
                        // 500 means, onTick function will be called at every 500 milliseconds

                        @Override
                        public void onTick(long leftTimeInMilliseconds) {
                            long seconds = leftTimeInMilliseconds / 1000;
                            txt_timer.setText("Time Left: " + String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                            // format the textview to show the easily readable format
                        }

                        @Override
                        public void onFinish() {
                            // this function will be called when the timecount is finished
                            txt_timer.setText("Time up!");
                            txt_timer.setVisibility(View.VISIBLE);
                        }

                    }.start();


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String question = jsonObject1.getString("question");
                        String answer = jsonObject1.getString("answer");

                        String questiontype = jsonObject1.getString("qtype");
                        String competition_id = jsonObject1.getString("competition_id");

//                        String qtype = null;
//                        if (questiontype.equalsIgnoreCase("Radio")){
//                            qtype="Option";
//                        }else {
//                            qtype = jsonObject1.getString("QType");
//                        }

                        String categoryid = jsonObject1.getString("answer");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("options");
                        ArrayList<String> optionArrayList = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            optionArrayList.add(jsonArray1.getString(j));
                            Log.e("option", "---------------------" + optionArrayList);
                        }

                        String listString = "";

                        for (String str : optionArrayList) {
                            listString += str + ",";
                        }

                        competitionItemArrayList.add(new CompetitionQuestionItem(id, question, questiontype, competition_id, answer, listString));


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerView != null) {
                recyclerCompetitionCategoryAdapter = new RecyclerCompetitionQuestionAllAdapter(CompetitionQuestionActivity.this, competitionItemArrayList);
                if (recyclerCompetitionCategoryAdapter.getItemCount() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_nodata.setVisibility(View.GONE);
                    recyclerView.setAdapter(recyclerCompetitionCategoryAdapter);
                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }


        }
    }


    private class PostQuestionData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompetitionQuestionActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
                nameValuePairs.add(new BasicNameValuePair("cid", cid));


                for (int i = 0; i < recyclerCompetitionCategoryAdapter.qidlist.size(); i++) {

                    nameValuePairs.add(new BasicNameValuePair("qid["+i+"]",recyclerCompetitionCategoryAdapter.qidlist.get(i)));
                    nameValuePairs.add(new BasicNameValuePair("Answer["+i+"]",recyclerCompetitionCategoryAdapter.answerlist.get(i)));

                }

                nameValuePairs.add(new BasicNameValuePair("time", "10:15 am"));


                responseJSON = CommonMethod.postStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/competition/answer/", nameValuePairs, CompetitionQuestionActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "--------------------" + s);

            Toast.makeText(CompetitionQuestionActivity.this, "Thank you! Your answer submitted successfully!", Toast.LENGTH_SHORT).show();
            
            /*try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                jsonObject.getString("Answer");
                Toast.makeText(CompetitionQuestionActivity.this, "Thank you! Your answer submitted successfully!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
//                progressbar.setVisibility(View.GONE);


            } catch (JSONException e) {
                e.printStackTrace();
            }*/

//            progressbar.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CompetitionQuestionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CompetitionQuestionActivity.this);
        }
        builder.setTitle("Alert")
                .setMessage("Are you sure you want to exit this competition?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
