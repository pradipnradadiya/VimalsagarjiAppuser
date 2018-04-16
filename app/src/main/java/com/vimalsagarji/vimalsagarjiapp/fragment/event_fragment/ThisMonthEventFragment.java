package com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.EventDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.EventAdpter;
import com.vimalsagarji.vimalsagarjiapp.model.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.model.JSONParser1;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by BHARAT on 20/01/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthEventFragment extends Fragment {

    public ThisMonthEventFragment() {

    }

    private static final String TAG = ThisMonthEventFragment.class.getSimpleName();
    private static final String constantURL = Constant.GET_ALLYEAR_EVENT_DATA;
    private static final String strMonth = "geteventsbycategorymonth";
    private static final String URL = constantURL.replace("geteventsbycategor", strMonth);
    static String img = Constant.ImgURL;
    private final List<EventAdpter> listAllEvent = new ArrayList<>();
    String[] daysArray = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String day = "";
    int dayOfWeek = 0;
    Date date;
    private final String Photo = "http://www.grapes-solutions.com/vimalsagarji/static/eventimage/";
    private final String Audio = "http://www.grapes-solutions.com/vimalsagarji/static/eventaudio/";
    private final String Video = "http://www.grapes-solutions.com/vimalsagarji/static/eventvideo/";
    final int drawableImage = R.drawable.event;

    private TextView txt_nodata_today;
    private EditText InputBox;
    private CustomAdpter adpter;
//    private KProgressHUD loadingProgressDialog;
    private List<EventAdpter> listfilterdata = new ArrayList<>();
    private final String MonthSearch = "http://www.grapes-solutions.com/vimalsagarji/event/searcheventsbycategorymonth/?page=1&psize=1000";

    private GridView gridView;
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thismonth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressbar= (ProgressBar) getActivity().findViewById(R.id.progressbar);
        gridView = (GridView) getActivity().findViewById(R.id.grid_thisMonth);
//        gridView = (GridView) getActivity().findViewById(R.id.grid_thisMonth);
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
                if (CommonMethod.isInternetConnected(getActivity())) {
                    new SearchMonthEvent().execute();
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
        });

        if (CommonMethod.isInternetConnected(getActivity())) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute(URL);
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

    private void loadData() {
        adpter.clear();
        LoadJsonTask loadJsonTask = new LoadJsonTask();
        loadJsonTask.execute(URL);
    }

    private class JsonTask extends AsyncTask<String, String, String> {
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
            String strUrl = params[0];
            //     String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                // for (int i=0; i<jsonObject.length()  ;i++){
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        String strID = object.getString("ID");
                        String strTitle = object.getString("Title");
                        String strDescription = object.getString("Description");
                        String strAddress = object.getString("Address");
                        String strDate = object.getString("Date");
                        String strdate1 = strDate.substring(0, 10);
                        String strAudio = object.getString("Audio");
                        String strAudioImage = object.getString("AudioImage");
                        String strVideoLink = object.getString("VideoLink");
                        String strVideo = object.getString("Video");
                        String strVideoImage = object.getString("VideoImage");
                        String strPhoto = object.getString("Photo");
                        String view = object.getString("View");


                        Date dt = CommonMethod.convert_date(strDate);
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

                        String[] time = strDate.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        EventAdpter eventAdpter = new EventAdpter();
                        eventAdpter.setID(strID);
                        eventAdpter.setTitle(strTitle);
                        eventAdpter.setDescription(strDescription);
                        eventAdpter.setAddress(strAddress);
                        eventAdpter.setDate(dayOfTheWeek + ",\n " + fulldate);
                        eventAdpter.setAudio(strAudio);
                        eventAdpter.setAudioImage(strAudioImage);
                        eventAdpter.setVideoLink(strVideoLink);
                        eventAdpter.setVideo(strVideo);
                        eventAdpter.setVideoImage(strVideoImage);
                        eventAdpter.setPhoto(strPhoto);
                        eventAdpter.setView(view);
                        listAllEvent.add(eventAdpter);
                    }
                } else {
//                    Toast.makeText(getActivity(), "No Event Found in ThisMonth", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressbar.setVisibility(View.GONE);
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            if (getActivity() != null) {

                if (gridView != null) {
                    adpter = new CustomAdpter(getActivity(), listAllEvent);
                    if (adpter.getCount() != 0) {
                        gridView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        gridView.setAdapter(adpter);
                    } else {
                        gridView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No ThisMonth Event Found", Toast.LENGTH_SHORT).show();
                    }

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            EventAdpter eventAdpter = (EventAdpter) parent.getItemAtPosition(position);
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra("click_action", "");
                            intent.putExtra("listtitle", eventAdpter.getTitle());
                            intent.putExtra("listDate", eventAdpter.getDate());
                            intent.putExtra("listAddress", eventAdpter.getAddress());
                            intent.putExtra("listID", eventAdpter.getID());
                            intent.putExtra("discription", eventAdpter.getDescription());
                            Log.e("discription", "-----------------------" + eventAdpter.getDescription());
                            intent.putExtra("Audio", Audio + eventAdpter.getAudio());
                            String audio = Audio + eventAdpter.getAudio();
                            audio = audio.replace(" ", "_");
                            String video = Video + eventAdpter.getVideo();
                            video = video.replace(" ", "_");
                            String photo = Photo + eventAdpter.getPhoto();
                            photo = photo.replace(" ", "-");
                            Log.e("Audio", "-----------------------" + audio);
                            intent.putExtra("Video", video);
                            Log.e("Video", "-----------------------" + video);
                            intent.putExtra("Photo", photo);
                            Log.e("Photo", "-----------------------" + photo);
                            intent.putExtra("view", eventAdpter.getView());
                            //intent.putExtra("listImage",eventAdpter.getPhoto());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });

                } else {
                    assert gridView != null;
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), "Gridview is null", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private class LoadJsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            //     String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                // for (int i=0; i<jsonObject.length()  ;i++){
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        String strID = object.getString("ID");
                        String strTitle = object.getString("Title");
                        String strDescription = object.getString("Description");
                        String strAddress = object.getString("Address");
                        String strDate = object.getString("Date");
                        String strdate1 = strDate.substring(0, 10);
                        String strAudio = object.getString("Audio");
                        String strAudioImage = object.getString("AudioImage");
                        String strVideoLink = object.getString("VideoLink");
                        String strVideo = object.getString("Video");
                        String strVideoImage = object.getString("VideoImage");
                        String strPhoto = object.getString("Photo");
                        String view = object.getString("View");


                        Date dt = CommonMethod.convert_date(strDate);
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

                        String[] time = strDate.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        EventAdpter eventAdpter = new EventAdpter();
                        eventAdpter.setID(strID);
                        eventAdpter.setTitle(strTitle);
                        eventAdpter.setDescription(strDescription);
                        eventAdpter.setAddress(strAddress);
                        eventAdpter.setDate(dayOfTheWeek + ",\n " + fulldate);
                        eventAdpter.setAudio(strAudio);
                        eventAdpter.setAudioImage(strAudioImage);
                        eventAdpter.setVideoLink(strVideoLink);
                        eventAdpter.setVideo(strVideo);
                        eventAdpter.setVideoImage(strVideoImage);
                        eventAdpter.setPhoto(strPhoto);
                        eventAdpter.setView(view);
                        listAllEvent.add(eventAdpter);
                    }
                } else {
//                    Toast.makeText(getActivity(), "No Event Found in ThisMonth.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getActivity() != null) {
                if (gridView != null) {
                    adpter = new CustomAdpter(getActivity(), listAllEvent);
                    if (adpter.getCount() != 0) {
                        gridView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        gridView.setAdapter(adpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        gridView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No ThisMonth Event Found", Toast.LENGTH_SHORT).show();
                    }

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            EventAdpter eventAdpter = (EventAdpter) parent.getItemAtPosition(position);
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            intent.putExtra("listDate", eventAdpter.getDate());
                            intent.putExtra("listAddress", eventAdpter.getAddress());
                            intent.putExtra("listID", eventAdpter.getID());
                            intent.putExtra("discription", eventAdpter.getDescription());
                            Log.e("discription", "-----------------------" + eventAdpter.getDescription());
                            intent.putExtra("Audio", Audio + eventAdpter.getAudio());
                            String audio = Audio + eventAdpter.getAudio();
                            audio = audio.replace(" ", "_");
                            String video = Video + eventAdpter.getVideo();
                            video = video.replace(" ", "_");
                            String photo = Photo + eventAdpter.getPhoto();
                            photo = photo.replace(" ", "-");
                            Log.e("Audio", "-----------------------" + audio);
                            intent.putExtra("Video", video);
                            Log.e("Video", "-----------------------" + video);
                            intent.putExtra("Photo", photo);
                            Log.e("Photo", "-----------------------" + photo);
                            intent.putExtra("view", eventAdpter.getView());
                            //intent.putExtra("listImage",eventAdpter.getPhoto());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });

                } else {
                    assert gridView != null;
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), "Gridview is null", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<EventAdpter> {

        final List<EventAdpter> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<EventAdpter> items) {
            super(context, R.layout.custom_event_grid_layout, items);
            this.context = context;
            this.resource = R.layout.custom_event_grid_layout;
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
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_txtDate = (TextView) convertView.findViewById(R.id.grid_txtDate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            EventAdpter eventAdpterHolder = items.get(position);
            holder.txt_views.setText(eventAdpterHolder.getView());
            holder.grid_txtTitle.setText(eventAdpterHolder.getTitle());
            holder.grid_txtDate.setText(eventAdpterHolder.getDate());
            String strPhoto = eventAdpterHolder.getPhoto();

            return convertView;

        }

        private class ViewHolder {
            TextView grid_txtTitle, grid_txtDate, txt_views;


        }
    }

    @SuppressWarnings("deprecation")
    public class SearchMonthEvent extends AsyncTask<String, String, String> {

        String status;
        public LayoutInflater inflater = null;
        private static final String TAG_SUCCESS = "success";

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("ResourceType")
        @Override
        protected String doInBackground(String... param) {
            try {
                JSONParser1 jsonParser = new JSONParser1();
                //    String count = param[0];

                String searchitem = InputBox.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest(MonthSearch, "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listfilterdata = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String strID = object.getString("ID");
                        String strTitle = object.getString("Title");
                        String strDescription = object.getString("Description");
                        String strAddress = object.getString("Address");
                        String strDate = object.getString("Date");
                        String strAudio = object.getString("Audio");
                        String strAudioImage = object.getString("AudioImage");
                        String strVideoLink = object.getString("VideoLink");
                        String strVideo = object.getString("Video");
                        String strVideoImage = object.getString("VideoImage");
                        String strPhoto = object.getString("Photo");
                        String view = object.getString("View");


                        Date dt = CommonMethod.convert_date(strDate);
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

                        String[] time = strDate.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        EventAdpter eventAdpter = new EventAdpter();
                        eventAdpter.setID(strID);
                        eventAdpter.setTitle(strTitle);
                        eventAdpter.setDescription(strDescription);
                        eventAdpter.setAddress(strAddress);
                        eventAdpter.setDate(dayOfTheWeek + ", " + fulldate);
                        eventAdpter.setAudio(strAudio);
                        eventAdpter.setAudioImage(strAudioImage);
                        eventAdpter.setVideoLink(strVideoLink);
                        eventAdpter.setVideo(strVideo);
                        eventAdpter.setVideoImage(strVideoImage);
                        eventAdpter.setPhoto(strPhoto);
                        eventAdpter.setView(view);
                        listfilterdata.add(eventAdpter);
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

            if (getActivity() != null) {
                if (gridView != null) {
                    adpter = new CustomAdpter(getActivity(), listfilterdata);
                    if (adpter.getCount() != 0) {
                        gridView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        gridView.setAdapter(adpter);
                    } else {
                        txt_nodata_today.setText("No Search \n Found");
                        gridView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }
}


