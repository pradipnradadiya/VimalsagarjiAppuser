package com.vimalsagarji.vimalsagarjiapp.categoryactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.EventActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.VideoCategoryItem;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class VideoCategory extends AppCompatActivity {
    private Toolbar toolbar_video;
    private TextView grid_txtTitle;
    private static final String TAG = VideoCategory.class.getSimpleName();
    private static final String URL = Constant.GET_ALL_VIDEO_CATEGORY;
    private static final String img = Constant.ImgURL;
    private final ArrayList<String> listId = new ArrayList<>();
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private static final String categoryName = "videocategory";
    private static final String ImgURL = img.replace("audioimage", categoryName);
    private String strImageUrl = "";
    static Bitmap bitmap = null;
    private KProgressHUD loadingProgressDialog;
    private RelativeLayout rel;
    private TextView txt_nodata_today;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_video_category);
        toolbar_video = (Toolbar) findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar_video);
        ImageView imgarrorback = (ImageView) toolbar_video.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar_video.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar_video.findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoCategory.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        rel = (RelativeLayout) findViewById(R.id.rel);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        TextView txt_title = (TextView) toolbar_video.findViewById(R.id.txt_title);
        txt_title.setText("Video");
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        if (CommonMethod.isInternetConnected(VideoCategory.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }

    }

    private class JsonTask extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(VideoCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(URL);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------------" + s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        listId.add(id);
                        String strName = object.getString("Name");
                        listName.add(strName);
                        Log.d(TAG, "list Name data:" + listName);
                        String strCategoryIcon = object.getString("CategoryIcon");
                        listIcon.add(ImgURL + strCategoryIcon);
                        Log.d(TAG, "list Icon Image:" + listIcon);
                        strImageUrl = ImgURL + strCategoryIcon;
                    }

                    listId.add("e_alliamgeid");
                    listName.add("Event");
                    listIcon.add("http://theme.behsamanco.com/images/avatarpack/26.png");

                    listId.add("bypeopleidid");
                    listName.add("ByPeople");
                    listIcon.add("http://theme.behsamanco.com/images/avatarpack/26.png");

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
            GridView gridView = (GridView) findViewById(R.id.grid_video);
            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(VideoCategory.this, listName);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            grid_txtTitle = (TextView) findViewById(R.id.grid_txtTitle);
                            String strAudioTitle = grid_txtTitle.getText().toString();
                            Intent intent = new Intent(VideoCategory.this, VideoCategoryItem.class);
                            intent.putExtra("listTitle", strAudioTitle);
                            intent.putExtra("v_cid", listId.get(position));
                            Log.e("id", "----------------------" + listId.get(position));
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            } else {
                gridView.setVisibility(View.GONE);
            }
        }
    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.custom_video_gridview, items);
            this.context = context;
            this.resource = R.layout.custom_video_gridview;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.grid_txtTitle.setText(items.get(position));

            Picasso.with(VideoCategory.this).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).error(R.drawable.no_image).into(holder.grid_img);
            return convertView;

        }


        private class ViewHolder {
            TextView grid_txtTitle;
            ImageView grid_img;

        }
    }
}

