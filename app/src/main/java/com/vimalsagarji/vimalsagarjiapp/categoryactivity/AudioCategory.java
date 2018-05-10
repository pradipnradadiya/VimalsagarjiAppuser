package com.vimalsagarji.vimalsagarjiapp.categoryactivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AudioAllBypeopleActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AudioAllEventActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.AudioCategoryitem;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AudioCategory extends AppCompatActivity {
    private Toolbar toolbar_home;
    private TextView grid_txtTitle;
    //ImageView imgarrorback,imghomeicon;
//    private KProgressHUD loadingProgressDialog;
    private static final String TAG = AudioCategory.class.getSimpleName();
    private static final String URL = Constant.GET_ALL_CATEGORY_AUDIO;
    private static final String ImgURL = Constant.GET_AUDIO_IMAGE_URL;
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private final ArrayList<String> listID = new ArrayList<String>();
    private String strImageUrl = "";
    private String strName;
    String title = "";
    private RelativeLayout rel_audio;
    private GridView gridView;
    private TextView txt_nodata_today;
    private ProgressBar progressbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_audio_category);
        toolbar_home = (Toolbar) findViewById(R.id.toolbar_audio);
        setSupportActionBar(toolbar_home);
        TextView txt_title = (TextView) toolbar_home.findViewById(R.id.txt_title);
        txt_title.setText("Audio");
        ImageView imgarrorback = (ImageView) toolbar_home.findViewById(R.id.imgarrorback);
        ImageView img_search = (ImageView) toolbar_home.findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioCategory.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        ImageView imgHome = (ImageView) toolbar_home.findViewById(R.id.imgHome);
        rel_audio = (RelativeLayout) findViewById(R.id.rel_audio);
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

        if (CommonMethod.isInternetConnected(AudioCategory.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel_audio, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(AudioCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();*/
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
            Log.e("respone", "------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strName = object.getString("Name");
                        listName.add(strName);
                        Log.d(TAG, "list Name data:" + listName);
                        String strCategoryIcon = object.getString("CategoryIcon");
                        listIcon.add(ImgURL + strCategoryIcon);
                        Log.d(TAG, "list Icon Image:" + listIcon);
                        String strID = object.getString("ID");
                        listID.add(strID);
                        strImageUrl = ImgURL + strCategoryIcon;

                    }
                    listID.add("e_alliamgeid");
                    listName.add("Event");
                    listIcon.add("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/Gallery/event.png");

                    listID.add("bypeopleidid");
                    listName.add("ByPeople");
                    listIcon.add("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/Gallery/bypeople.png");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            gridView = (GridView) findViewById(R.id.grid_audio);
            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(AudioCategory.this, listName);
                customAdpter.notifyDataSetChanged();
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if (listID.get(position).equalsIgnoreCase("e_alliamgeid")) {
                                Intent intent = new Intent(AudioCategory.this, AudioAllEventActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else if (listID.get(position).equalsIgnoreCase("bypeopleidid")) {
                                Intent intent = new Intent(AudioCategory.this, AudioAllBypeopleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                grid_txtTitle = (TextView) findViewById(R.id.grid_txtTitle);
                                String strAudioTitle = listName.get(position);
                                Log.e(TAG, "Audio Category Title" + strAudioTitle);
                                String strListId = listID.get(position);
                                Log.e(TAG, "ListID is:" + strListId);
                                //(String) ((TextView)findViewById(R.id.grid_txtTitle)).getText();


                                Intent intent = new Intent(AudioCategory.this, AudioCategoryitem.class);
                                intent.putExtra("listTitle", strAudioTitle);
                                intent.putExtra("listId", strListId);
                                intent.putExtra("listCategoryId", strListId);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            } else {
//                Toast.makeText(AudioCategory.this, "Gridview is null", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.custom_audio_gridview, items);
            this.context = context;
            this.resource = R.layout.custom_audio_gridview;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // holder.txt_ID.setText(items.get(position));
            holder.grid_txtTitle.setText(CommonMethod.decodeEmoji(items.get(position)));

//            Picasso.with(AudioCategory.this).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0, 200).error(R.drawable.no_image).into(holder.grid_img);

            Glide.with(AudioCategory.this).load(listIcon.get(position)
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.grid_img);

            return convertView;

        }

        private class ViewHolder {
            TextView grid_txtTitle;
            ImageView grid_img;

        }
    }
}
