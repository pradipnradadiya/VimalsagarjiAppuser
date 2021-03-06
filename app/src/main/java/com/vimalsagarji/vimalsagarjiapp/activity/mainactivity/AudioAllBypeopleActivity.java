package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.AudioAllAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.AudioAllItem;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurApiName;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurCommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AudioAllBypeopleActivity extends AppCompatActivity {
    private RecyclerView recycleviewVideo;
    private LinearLayoutManager linearLayoutManager;
    AudioAllAdapter audioAllAdapter;
    private ArrayList<AudioAllItem> audioAllItems = new ArrayList<>();
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressBar progressbar;
    //    private ImageView imgarrorback;
//    private TextView txt_title;
//    private ImageView img_search;
    private TextView txt_nodata_today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_all_activity);

        toolbarClick();

        txt_nodata_today= (TextView) findViewById(R.id.txt_nodata_today);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(AudioAllBypeopleActivity.this);
        recycleviewVideo = (RecyclerView) findViewById(R.id.recycleviewVideo);
        recycleviewVideo.setLayoutManager(linearLayoutManager);

        audioAllAdapter = new AudioAllAdapter(AudioAllBypeopleActivity.this, audioAllItems);
        recycleviewVideo.setAdapter(audioAllAdapter);

        recycleviewVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                             if (CommonMethod.isInternetConnected(AudioAllBypeopleActivity.this)) {

                                                                 Log.e("total count", "--------------------" + page_count);

                                                                 page_count++;
                                                                 new GetAllAudio().execute();
                                                             } else {
                                                                 //internet not connected
                                                             }
                                                             loading = true;


                                                         }

                                                     }
                                                 }

                                             }

        );
        if (CommonMethod.isInternetConnected(AudioAllBypeopleActivity.this)) {
            new GetAllAudio().execute();
        } else {
            //internet not connected
            Toast.makeText(this, R.string.internet, Toast.LENGTH_LONG).show();
        }


    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Audios");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetAllAudio extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            responseJson = CommonMethod.getStringResponse(AllOurCommonUrl.commonUrl + AllOurApiName.audioallbypeople + "&page=" + page_count + "&psize=15");
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("Title");
                    String audio = object.getString("Audio");
                    audioAllItems.add(new AudioAllItem(title, "bypeopleaudio/" + audio));
                }
                audioAllAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            audioAllAdapter.notifyDataSetChanged();
            progressbar.setVisibility(View.GONE);

            if (audioAllItems.isEmpty()) {

                txt_nodata_today.setVisibility(View.VISIBLE);
            }

        }


    }
}
