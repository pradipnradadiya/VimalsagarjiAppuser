package com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
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

import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.VideoCategoryItem.video_cat_id;


/**
 * Created by BHARAT on 01/02/2016.
 */
@SuppressWarnings("ALL")
public class TodayVideoFragment extends Fragment {
    public TodayVideoFragment() {

    }

    final static String TAG = AllVideoFragment.class.getSimpleName();
    private final String urls = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/video/getvideobycategorytoday/?page=1&psize=1000&cid=";
    private static String URL = "";// "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/video/getvideobycategorytoday/?page=1&psize=100&cid=" + video_cat_id;
    private static final String ImgURL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/videoimage/";
    private static final String VideoPath = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/videos/";
    private final ArrayList<String> listid = new ArrayList<>();
    private final ArrayList<String> listcatid = new ArrayList<>();
    private List<String> listVideoName = new ArrayList<>();
    private final ArrayList<String> listDate = new ArrayList<String>();
    private final ArrayList<String> listVideo = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private final ArrayList<String> listview = new ArrayList<String>();
    private final ArrayList<String> listflag = new ArrayList<String>();
    TextView txtAudio;
    String strImageUrl = "";
    static Bitmap bitmap = null;
    private View view;
    //    private KProgressHUD loadingProgressDialog;
    private CustomAdpter customAdpter;
    @SuppressLint("StaticFieldLeak")
    public static ListView listViewvideo;
    private TextView txt_nodata_today;
    private EditText InputBox;
    private final String TodaySearchVideo = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/video/searchallvideosbycidtoday/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;
    Sharedpreferance sharedpreferance;

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
        sharedpreferance = new Sharedpreferance(getActivity());
        Log.e("today cid", "---------------------------------" + video_cat_id);
        URL = urls + video_cat_id;
        listViewvideo = (ListView) getActivity().findViewById(R.id.thismonth_video);
        ImageView imgBack = (ImageView) getActivity().findViewById(R.id.imgarrorback);
        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
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


        if (video_cat_id.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("response", "---------------" + "evemnt");
            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    new TodayEventVideo().execute("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/event/geteventsbycategorytoday/?page=1&psize=1000");
                } else {
                    new TodayEventVideo().execute("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/event/geteventsbycategorytoday/?page=1&psize=1000" + "&uid=" + sharedpreferance.getId());
                }
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
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    new TodayByPeopleVideo().execute("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/bypeople/getallapppoststoday/?page=1&psize=1000");
                } else {
                    new TodayByPeopleVideo().execute("http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/bypeople/getallapppoststoday/?page=1&psize=1000"+"&uid="+sharedpreferance.getId());
                }
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

//            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
//                JsonTask jsonTask = new JsonTask();
//                jsonTask.execute();
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
        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            new LoadJsonTask().execute(URL);
        } else {
            new LoadJsonTask().execute(URL + "&uid=" + sharedpreferance.getId());
        }
    }

    private class JsonTask extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(params[0]);

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
                    Log.e("length", "-----------" + jsonArray.length());
//                    listVideoName.add("pradip");
//                    listVideoName.add("parth");
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
                        String view = object.getString("View");
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
//                        String name = object.getString("Name");

                        String flag=object.getString("is_viewed");
                        listflag.add(flag);

                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listViewvideo != null) {
                    customAdpter = new CustomAdpter(getActivity(), listVideoName);
                    Log.e("listvideo size", "--------------" + listVideoName);
                    if (customAdpter.getCount() != 0) {
                        listViewvideo.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listViewvideo.setAdapter(customAdpter);
//                    customAdpter.notifyDataSetChanged();
                    } else {
                        listViewvideo.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);

                    }


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
                responJSON = CommonMethod.getStringResponse(params[0]);

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
                        String view = object.getString("View");
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

                        String flag=object.getString("is_viewed");
                        listflag.add(flag);
//                        String name = object.getString("Name");
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {

                if (listViewvideo != null) {
                    customAdpter = new CustomAdpter(getActivity(), listVideoName);
                    if (customAdpter.getCount() != 0) {
                        listViewvideo.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listViewvideo.setAdapter(customAdpter);
//                    customAdpter.notifyDataSetChanged();
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listViewvideo.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);

                    }

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
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(CommonMethod.decodeEmoji(listview.get(position)));
            holder.txtVideoName.setText(CommonMethod.decodeEmoji(items.get(position)));
            holder.txtVideoDate.setText(CommonMethod.decodeEmoji(listDate.get(position)));
            Log.e("Image item path", "-------------------------------" + listIcon.get(position));

//            Picasso.with(getActivity()).load(listIcon.get(position)).placeholder(R.drawable.loader).error(R.drawable.no_image).into(holder.imgVideo);
            if (listIcon != null) {
//                Picasso.with(getActivity()).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0,200).error(R.drawable.no_image).into(holder.imgVideo);

                Glide.with(getActivity()).load(listIcon.get(position)
                        .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.imgVideo);


            } else {
                Picasso.with(getActivity()).load(R.drawable.no_image);
            }


//            holder.imgVideo.setImageBitmap(bitmap);

            holder.imgPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listflag.set(position,"true");
                    customAdpter.notifyDataSetChanged();


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
                    intent.putExtra("click_action", "");
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

            if (listflag.get(position).equalsIgnoreCase("true")){
                holder.img_new.setVisibility(View.GONE);
            }
            else{
                holder.img_new.setVisibility(View.VISIBLE);
            }




            return convertView;

        }

        private class ViewHolder {
            TextView txtVideoName, txtVideoDate, txt_views;
            ImageView imgVideo, imgPlayVideo, imgPlayPush, imgPlayVideo1, img_new;

        }
    }

    private class TodayEventVideo extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
            /*loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(params[0]);

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
                        String vidio = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/eventvideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/eventimage/" + photo;
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

                        String flag=object.getString("is_viewed");
                        listflag.add(flag);

                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

/*

            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/

            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
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

    }

    private class TodayByPeopleVideo extends AsyncTask<String, Void, String> {
        String responJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
            /*loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responJSON = CommonMethod.getStringResponse(params[0]);

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
                        String vidio = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/bypeoplevideo/" + video;
                        Log.e("vidio", "------------------------" + vidio);
                        listVideo.add(vidio.replaceAll(" ", "%20"));
                        String photo = object.getString("Photo");
                        String img = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji_qa/static/bypeopleimage/" + photo;
                        Log.e("img", "------------------------" + img);
                        listIcon.add(img.replaceAll(" ", "%20"));
                        String view = object.getString("View");
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

                        String flag=object.getString("is_viewed");
                        listflag.add(flag);
                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            progressbar.setVisibility(View.GONE);
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            if (getActivity() != null) {
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

    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        listViewvideo.setVisibility(View.GONE);
        if (video_cat_id.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("response", "---------------" + "evemnt");
            listVideoName.clear();
            if (CommonMethod.isInternetConnected(getActivity())) {
//                new TodayEventVideo().execute();
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
//                new TodayByPeopleVideo().execute();
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
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    JsonTask jsonTask = new JsonTask();
                    jsonTask.execute(URL);
                } else {
                    JsonTask jsonTask = new JsonTask();
                    jsonTask.execute(URL + "&uid=" + sharedpreferance.getId());
                }
            }
        }
    }

}

