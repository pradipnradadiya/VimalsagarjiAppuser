package com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.model.JSONParser1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment.TodayVideoFragment.listViewvideo;
import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.VideoCategoryItem.video_cat_id;

/**
 * Created by BHARAT on 01/02/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthVideoFragment extends Fragment {
    public ThisMonthVideoFragment() {

    }

    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    final static String TAG = AllVideoFragment.class.getSimpleName();
    private final String urls = "http://www.grapes-solutions.com/vimalsagarji/video/getvideobycategorymonth/?page=1&psize=1000&cid=";
    private static String URL = "";//http://www.grapes-solutions.com/vimalsagarji/video/getvideobycategorymonth/?page=1&psize=100&cid=" + video_cat_id;
    private final String ImgURL = "http://www.grapes-solutions.com/vimalsagarji/static/videoimage/";
    private final String VideoPath = "http://www.grapes-solutions.com/vimalsagarji/static/videos/";

    private final ArrayList<String> listid = new ArrayList<>();
    private final ArrayList<String> listcatid = new ArrayList<>();
    private ArrayList<String> listVideoName = new ArrayList<String>();
    private final ArrayList<String> listDate = new ArrayList<String>();
    private final ArrayList<String> listVideo = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private final ArrayList<String> listview = new ArrayList<String>();
    TextView txtAudio;
    String strImageUrl = "";
    static Bitmap bitmap = null;
    private View view;
    private KProgressHUD loadingProgressDialog;


    private CustomAdpter customAdpter;
    private TextView txt_nodata_today;
    private EditText InputBox;
    private final String MonthSearchVideo = "http://www.grapes-solutions.com/vimalsagarji/video/searchallvideosbycidthismonth/?page=1&psize=1000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_thismonth_video, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("cid", "---------------------------------" + video_cat_id);
        URL = urls + video_cat_id;
        listViewvideo = (ListView) getActivity().findViewById(R.id.thismonth_video);

        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);

        InputBox = (EditText) getActivity().findViewById(R.id.etText);
        ImageView imsearch = (ImageView) getActivity().findViewById(R.id.imgSerch);
        activity_main_swipe_refresh_layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);

        activity_main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });
        InputBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        imsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (video_cat_id.equalsIgnoreCase("e_alliamgeid")) {
                    Log.e("response", "---------------" + "evemnt");
                    if (CommonMethod.isInternetConnected(getActivity())) {
                        new SearchMonthTodayEventVideo().execute();
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                    }

                } else if (video_cat_id.equalsIgnoreCase("bypeopleidid")) {
                    Log.e("response", "---------------" + "bypeople");
                    if (CommonMethod.isInternetConnected(getActivity())) {
                        new SearchMonthTodayByPeople().execute();
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                    }

                } else {


                    if (CommonMethod.isInternetConnected(getActivity())) {
                        new SearchMonthVideo().execute();
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
            }
        });

        if (video_cat_id.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("response", "---------------" + "evemnt");
            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
                new MonthEventVideo().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }

        } else if (video_cat_id.equalsIgnoreCase("bypeopleidid")) {
            Log.e("response", "---------------" + "bypeople");
            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
                new MonthByPeopleVideo().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
        } else {

            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute();

            } else {
                final Snackbar snackbar = Snackbar
                        .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
    }

    private void loadData() {
        customAdpter.clear();
        new LoadJsonTask().execute();
    }

    private class AllEventVideo extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(URL);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "eid";
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("Title");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = "cid";
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String[] vi = video.split(",");
                        String vidio = "http://www.grapes-solutions.com/vimalsagarji/static/eventvideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String[] parray = photo.split(",");
                        String img = "http://www.grapes-solutions.com/vimalsagarji/static/eventimage/" + parray[0];

                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
                        String view = object.getString("View");
                        listview.add(view);
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

                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }


            }

        }

    }

    private class JsonTask extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(URL);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("VideoName");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = object.getString("CategoryID");
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = VideoPath + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = ImgURL + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
                        String view=object.getString("View");
                        listview.add(view);
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
                        listDate.add(dayOfTheWeek + ", " + fulldate);
//                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();

            }
            listViewvideo = (ListView) getActivity().findViewById(R.id.thismonth_video);
            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            }

        }


    }

    private class LoadJsonTask extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(URL);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("VideoName");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = object.getString("CategoryID");
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = VideoPath + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = ImgURL + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
                        String view=object.getString("View");
                        listview.add(view);
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
                        listDate.add(dayOfTheWeek + ", " + fulldate);
//                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listViewvideo = (ListView) getActivity().findViewById(R.id.thismonth_video);
            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                    activity_main_swipe_refresh_layout.setRefreshing(false);
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            }

        }


    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.video_category_list_item, items);
            this.context = context;
            this.resource = R.layout.video_category_list_item;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtVideoName = (TextView) convertView.findViewById(R.id.txtVideoName);
                holder.txtVideoDate = (TextView) convertView.findViewById(R.id.txtVideoDate);
                holder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVideo);
                holder.imgPlayVideo = (ImageView) convertView.findViewById(R.id.imgPlayVideo);
                holder.imgPlayPush = (ImageView) convertView.findViewById(R.id.imgPlayPush);
                holder.imgPlayVideo1 = (ImageView) convertView.findViewById(R.id.imgPlayVideo1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(listview.get(position));
            holder.txtVideoName.setText(items.get(position));
            holder.txtVideoDate.setText(listDate.get(position));
//            Picasso.with(getActivity()).load(listIcon.get(position)).placeholder(R.drawable.loader).error(R.drawable.no_image).into(holder.imgVideo);


            if (listIcon != null) {
                Picasso.with(getActivity()).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).error(R.drawable.no_image).into(holder.imgVideo);
            } else {
                Picasso.with(getActivity()).load(R.drawable.no_image);
            }


            holder.imgPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strVideo = listVideo.get(position);
                    Log.e("videofile", "------------------" + strVideo);
                    String videoname = listVideoName.get(position);
                    Log.e("videoname", "------------------" + videoname);
                    String id = listid.get(position);
                    Log.e("id", "------------------" + id);
                    String catid = listcatid.get(position);
                    Log.e("catid", "------------------" + catid);
                    String photo = listIcon.get(position);
                    Log.e("photo", "------------------" + photo);
                    String date = listDate.get(position);
                    Log.e("date", "------------------" + date);

                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra("click_action","");
                    intent.putExtra("video", strVideo);
                    intent.putExtra("videoname", videoname);
                    intent.putExtra("id", id);
                    intent.putExtra("catid", catid);
                    intent.putExtra("photo", photo);
                    intent.putExtra("date", date);
                    intent.putExtra("view", listview.get(position));


                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });


            return convertView;

        }

        private class ViewHolder {
            TextView txtVideoName, txtVideoDate,txt_views;
            ImageView imgVideo, imgPlayVideo, imgPlayPush, imgPlayVideo1;

        }
    }

    public class SearchMonthVideo extends AsyncTask<String, String, String> {

        String status;
        public LayoutInflater inflater = null;
        private static final String TAG_SUCCESS = "success";

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings({"ResourceType", "deprecation"})
        @Override
        protected String doInBackground(String... param) {
            try {
                JSONParser1 jsonParser = new JSONParser1();
                //    String count = param[0];

                String searchitem = InputBox.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", video_cat_id));
                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest(MonthSearchVideo, "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listVideoName = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("VideoName");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = object.getString("CategoryID");
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = VideoPath + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = ImgURL + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
                        String view=object.getString("View");
                        listview.add(view);
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


                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String name = object.getString("Name");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            InformationCategory informationCategory = new InformationCategory();

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setText("No Search\n Found");
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private class MonthEventVideo extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/event/geteventsbycategorymonth/?page=1&psize=1000");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "eid";
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("Title");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = "cid";
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = "http://www.grapes-solutions.com/vimalsagarji/static/eventvideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.grapes-solutions.com/vimalsagarji/static/eventimage/" + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
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

                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String view=object.getString("View");
                        listview.add(view);
                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }


            }

        }

    }

    private class MonthByPeopleVideo extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/bypeople/getallapppostsmonth/?page=1&psize=1000");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return responJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "bid";
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("Title");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = "cid";
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = "http://www.grapes-solutions.com/vimalsagarji/static/bypeoplevideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleimage/" + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String view=object.getString("View");
                        listview.add(view);
                        String date = object.getString("Date");
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

                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }


            }

        }

    }

    public class SearchMonthTodayEventVideo extends AsyncTask<String, String, String> {

        String status;
        public LayoutInflater inflater = null;
        private static final String TAG_SUCCESS = "success";

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings({"ResourceType", "deprecation"})
        @Override
        protected String doInBackground(String... param) {
            try {
                JSONParser1 jsonParser = new JSONParser1();
                //    String count = param[0];

                String searchitem = InputBox.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest("http://www.grapes-solutions.com/vimalsagarji/event/searcheventsbycategorymonth/?searchterm=vimal&page=1&psize=100", "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listVideoName = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = "eid";
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("Title");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = "cid";
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = "http://www.grapes-solutions.com/vimalsagarji/static/eventvideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.grapes-solutions.com/vimalsagarji/static/eventaudio/" + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String date = object.getString("Date");
                        String view=object.getString("View");
                        listview.add(view);

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


                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String name = object.getString("Name");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            InformationCategory informationCategory = new InformationCategory();

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setText("No Search\n Found");
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    public class SearchMonthTodayByPeople extends AsyncTask<String, String, String> {

        String status;
        public LayoutInflater inflater = null;
        private static final String TAG_SUCCESS = "success";

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings({"ResourceType", "deprecation"})
        @Override
        protected String doInBackground(String... param) {
            try {
                JSONParser1 jsonParser = new JSONParser1();
                //    String count = param[0];

                String searchitem = InputBox.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest("http://www.grapes-solutions.com/vimalsagarji/bypeople/searchallpoststhismonth/?page=1&psize=1000", "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listVideoName = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = "bid";
                        Log.e("id", "------------------------" + id);
                        listid.add(id);
                        String videoname = object.getString("Title");
                        Log.e("videoname", "------------------------" + videoname);
                        listVideoName.add(videoname);
                        String catid = "cid";
                        listcatid.add(catid);
                        String video = object.getString("Video");
                        String vidio = "http://www.grapes-solutions.com/vimalsagarji/static/bypeoplevideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleimage/" + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String view=object.getString("View");
                        listview.add(view);
                        String date = object.getString("Date");

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


                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        String name = object.getString("Name");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            InformationCategory informationCategory = new InformationCategory();

            if (listViewvideo != null) {
                customAdpter = new CustomAdpter(getActivity(), listVideoName);
                if (customAdpter.getCount() != 0) {
                    listViewvideo.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listViewvideo.setAdapter(customAdpter);
                } else {
                    listViewvideo.setVisibility(View.GONE);
                    txt_nodata_today.setText("No Search\n Found");
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}

