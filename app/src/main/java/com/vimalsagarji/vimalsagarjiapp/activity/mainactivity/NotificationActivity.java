package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.MainActivity;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.NotificationListAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.NotificationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Grapes-Pradip on 02-Oct-17.
 */

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recycleview_notification;
    LinearLayoutManager linearLayoutManager;
    //    NotificationListAdapter notificationListAdapter;
//    private KProgressHUD loadingProgressDialog;
    private ImageView img_nodata;
    private ProgressBar progressbar;
    private int page_count = 1;
    private int psize = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    NotificationListAdapter notificationListAdapter;
    ArrayList<NotificationItem> notificationItems;
    private TextView txt_title;
    private ImageView imgarrorback,img_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        imgarrorback= (ImageView) findViewById(R.id.imgarrorback);
        img_search= (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Latest Posts");

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        notificationItems = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        fndId();
        if (CommonMethod.isInternetConnected(NotificationActivity.this)) {
            new GetNotificationList().execute();
        } else {
            Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
        recycleview_notification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (flag_scroll) {
//                                                                 Log.e("flag-Scroll", flag_scroll + "");
                } else {
                    if (loading) {
                        Log.e("flag-Loading", loading + "");
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        Log.e("flag-Loading_second_if", loading + "");
                        if (CommonMethod.isInternetConnected(NotificationActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetNotificationList().execute();
                        } else {
                            //internet not connected
                        }
                        loading = true;


                    }

                }
            }

        });
    }


    private void fndId() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        recycleview_notification = (RecyclerView) findViewById(R.id.recycleview_notification);
        recycleview_notification.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
    }
/*
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
*/
    private class GetNotificationList extends AsyncTask<String, Void, String> {

        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(NotificationActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait..")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            responseString = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/notificationcount/countnotification/?page=" + page_count + "&psize=20");
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    if (jsonArray.length() < 20 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String table = jsonObject1.getString("Table");
                        Log.e("table", "--------------" + table);
                        String id = jsonObject1.getString("ID");
                        String title = jsonObject1.getString("Title");
                        String description = jsonObject1.getString("Description");
                        String date = jsonObject1.getString("Date");
                        if (description.equalsIgnoreCase("null")) {
                            description = "Description not available";
                        } else {

                        }

                        Date dt = CommonMethod.convert_date(date);
                        Log.e("Convert date is", "------------------" + dt);
                        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dt);//Thursday
                        String stringMonth = (String) android.text.format.DateFormat.format("MMM", dt); //Jun
                        String intMonth = (String) android.text.format.DateFormat.format("MM", dt); //06
                        String year = (String) android.text.format.DateFormat.format("yyyy", dt); //2013
                        String day = (String) android.text.format.DateFormat.format("dd", dt); //20

                        Log.e("dayOfTheWeek", "-----------------" + dayOfTheWeek);
                        Log.e("stringMonth", "-----------------" + stringMonth);
                        Log.e("intMonth", "-----------------" + intMonth);
                        Log.e("year", "-----------------" + year);
                        Log.e("day", "-----------------" + day);

                        String fulldate = day + "/" + intMonth + "/" + year;

                        String[] time = date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        notificationItems.add(new NotificationItem(table, id, title, description, fulldate + ", " + time[1]));
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressbar.setVisibility(View.GONE);
        /*    if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/
            if (recycleview_notification != null) {
                notificationListAdapter = new NotificationListAdapter(NotificationActivity.this, notificationItems);
                if (notificationListAdapter.getItemCount() != 0) {
                    recycleview_notification.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recycleview_notification.setAdapter(notificationListAdapter);

                } else {
                    recycleview_notification.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
