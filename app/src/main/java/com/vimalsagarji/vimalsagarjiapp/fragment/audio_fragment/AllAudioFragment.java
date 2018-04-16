package com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.AudioDetail;
import com.vimalsagarji.vimalsagarjiapp.activity.EventsAllDisplay;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.JSONParser1;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthAudio;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.AudioCategoryitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by BHARAT on 23/01/2016.
 */
public class AllAudioFragment extends Fragment {

    public AllAudioFragment() {

    }

    CustomAdpter customAdpter;
    SwipeRefreshLayout activity_main_swipe_refresh_layout;
    final static String URL = "http://www.grapes-solutions.com/vimalsagarji/audio/getaudiobycategoryid/?page=1&psize=1000";
    String ImgURL = "http://www.grapes-solutions.com/vimalsagarji/static/audioimage/";
    static String AudioPath = "http://www.grapes-solutions.com/vimalsagarji/static/audios/";
    ArrayList<ThisMonthAudio> arrayList = new ArrayList<>();
    private static String strCid = "";
    private View view;
    TextView audioImagname;
//    KProgressHUD loadingProgressDialog;
    TextView txt_nodata_today;
    private EditText InputBox;
    private ImageView imsearch;
    String MonthSearchAudio = "http://www.grapes-solutions.com/vimalsagarji/audio/searchallaudio/?page=1&psize=1000";
    ListView listView;
//    ArrayList<ArrayList> audioarraylist=new ArrayList<>();

    ArrayList<String> audiolistarraylist = new ArrayList<>();
    String[] aArray;
    private ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_thismonth_audio, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AudioCategoryitem audioCategoryitem = (AudioCategoryitem) getActivity();
        strCid = audioCategoryitem.getMydata();
        listView = (ListView) getActivity().findViewById(R.id.thismonth_audio);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);

        progressbar= (ProgressBar) getActivity().findViewById(R.id.progressbar);
        InputBox = (EditText) getActivity().findViewById(R.id.etText);
        imsearch = (ImageView) getActivity().findViewById(R.id.imgSerch);
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

                if (strCid.equalsIgnoreCase("e_alliamgeid")) {
                    if (CommonMethod.isInternetConnected(getActivity())) {
//                        new SearchMonthEventAudio().execute();
                    } else {
                        Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }
                } else if (strCid.equalsIgnoreCase("bypeopleidid")) {
                    if (CommonMethod.isInternetConnected(getActivity())) {
//                        new SearchAllByPeople().execute();
                    } else {
                        Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }

                } else {

                    if (CommonMethod.isInternetConnected(getActivity())) {
//                        new SearchMonthAudio().execute();
                    } else {
                        Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }
                }


            }
        });


        if (strCid.equalsIgnoreCase("e_alliamgeid")) {
            activity_main_swipe_refresh_layout.setRefreshing(false);
            activity_main_swipe_refresh_layout.setEnabled(false);
            new GetMonthEventAudio().execute();
        } else if (strCid.equalsIgnoreCase("bypeopleidid")) {
            activity_main_swipe_refresh_layout.setRefreshing(false);
            activity_main_swipe_refresh_layout.setEnabled(false);
            if (CommonMethod.isInternetConnected(getActivity())) {
                new GetAllByPeople().execute();
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        } else {

            if (CommonMethod.isInternetConnected(getActivity())) {
                GetMonthAudio monthAudio = new GetMonthAudio();
                monthAudio.execute();
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        }


        audioImagname = (TextView) getActivity().findViewById(R.id.audioImagname);
    }

    private void loadData() {
        LoadGetMonthAudio loadGetMonthAudio = new LoadGetMonthAudio();
        loadGetMonthAudio.execute();
    }

    private class GetMonthAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(URL + "&cid=" + strCid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String AudioName = object.getString("AudioName");
                        String CategoryID = object.getString("CategoryID");
                        String Audio = AudioPath + object.getString("Audio");
                        String Photo = ImgURL + object.getString("Photo");
                        String Duration = object.getString("Duration");
                        String Date = object.getString("Date");
                        String view = object.getString("View");


                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
           progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
                        txt_nodata_today.setText("No Data");

                    }

                }
            }

        }


    }

    private class GetMonthEventAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
           progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/event/geteventsbycategoryyear/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    audiolistarraylist = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "eid" + String.valueOf(i);
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = object.getString("Audio");
                        String Photo = "http://www.grapes-solutions.com/vimalsagarji/static/eventimage/" + object.getString("Photo");
                        Log.e("photo split", "--------------" + Photo);
                        String[] parray = Photo.split(",");
                        String audiolist = object.getString("Audio");
                        /*if (audiolist.equalsIgnoreCase("")) {
                            audiolistarraylist.clear();
                        } else {
                            String[] au = audiolist.split(",");
                            for (int j = 0; j < au.length; j++) {
                                audiolistarraylist.add(au[j]);
                            }
                        }*/
//                        Log.e("photo", "----------------" + Photo);
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, parray[0], Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    CustomAdpter customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }


    }

    private class LoadGetMonthAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(URL + "&cid=" + strCid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String AudioName = object.getString("AudioName");
                        String CategoryID = object.getString("CategoryID");
                        String Audio = AudioPath + object.getString("Audio");
                        String Photo = ImgURL + object.getString("Photo");
                        String Duration = object.getString("Duration");
                        String Date = object.getString("Date");
                        String view = object.getString("View");


                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
                    }


                }
            }

        }


    }

    public class CustomAdpter extends ArrayAdapter<ThisMonthAudio> {

        List<ThisMonthAudio> items;
        Context context;
        int resource;

        public CustomAdpter(Context context, int resource, List<ThisMonthAudio> items) {
            super(context, resource, items);
            this.context = context;
            this.resource = resource;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.btnComment = (Button) convertView.findViewById(R.id.btnComment);
                holder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
                holder.btnLikeClick = (Button) convertView.findViewById(R.id.btnLikeClick);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtAudioName = (TextView) convertView.findViewById(R.id.txtAudioName);
                holder.txtAudioName = (TextView) convertView.findViewById(R.id.txtAudioName);
                holder.txtAudioDate = (TextView) convertView.findViewById(R.id.txtAudioDate);
                holder.imgAudio = (ImageView) convertView.findViewById(R.id.imgAudio);
                holder.imgPlayAudio = (ImageView) convertView.findViewById(R.id.imgPlayAudio);
                holder.imgPlayPush = (ImageView) convertView.findViewById(R.id.imgPlayPush);
                holder.imgPlayAudio1 = (ImageView) convertView.findViewById(R.id.imgPlayAudio1);


                holder.imgPlayAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("play audio", "------------------");

                        if (items.get(position).getID().equalsIgnoreCase("eid" + String.valueOf(position))) {
                            Intent intent = new Intent(getActivity(), EventsAllDisplay.class);
                            if (items.get(position).getAudio().equalsIgnoreCase("")) {
                                Log.e("audio play lisy list", "-------------" + audiolistarraylist);

                                audiolistarraylist = new ArrayList<>();
                                intent.putExtra("mylist", audiolistarraylist);
                                intent.putExtra("status", "a");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                audiolistarraylist.clear();
                            } else {
                                audiolistarraylist = new ArrayList<>();
                                String[] au = items.get(position).getAudio().split(",");
                                for (int j = 0; j < au.length; j++) {
                                    audiolistarraylist.add(au[j]);
                                }
                                Log.e("audio play lisy list", "-------------" + audiolistarraylist);

                                intent.putExtra("mylist", audiolistarraylist);
                                intent.putExtra("status", "a");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                        } else if (items.get(position).getID().equalsIgnoreCase("bid"+String.valueOf(position))) {

                            if (items.get(position).getAudio().equalsIgnoreCase("")) {

                                Toast.makeText(context, "This posts audio not avalable.", Toast.LENGTH_SHORT).show();
                                
                            } else {

                                Intent i = new Intent(getActivity(), AudioDetail.class);
                                i.putExtra("click_action", "");
                                i.putExtra("id", items.get(position).getID());
                                i.putExtra("AudioName", items.get(position).getAudioName());
                                i.putExtra("CategoryID", items.get(position).getCategoryID());
                                i.putExtra("Audio", "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleaudio/" + items.get(position).getAudio());
                                i.putExtra("Photo", items.get(position).getPhoto());
                                i.putExtra("Duration", items.get(position).getDuration());
                                i.putExtra("Date", items.get(position).getDate());
                                i.putExtra("view", items.get(position).getView());
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }

                        } else {
                            Intent i = new Intent(getActivity(), AudioDetail.class);
                            i.putExtra("click_action", "");
                            i.putExtra("id", items.get(position).getID());
                            i.putExtra("AudioName", items.get(position).getAudioName());
                            i.putExtra("CategoryID", items.get(position).getCategoryID());
                            i.putExtra("Audio", items.get(position).getAudio());
                            i.putExtra("Photo", items.get(position).getPhoto());
                            i.putExtra("Duration", items.get(position).getDuration());
                            i.putExtra("Date", items.get(position).getDate());
                            i.putExtra("view", items.get(position).getView());
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }

                });


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(items.get(position).getView());
            holder.txtAudioName.setText(items.get(position).getAudioName());
            holder.txtAudioDate.setText(items.get(position).getDate());
            Picasso.with(getActivity()).load(items.get(position).getPhoto().replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0, 200).error(R.drawable.no_image).into(holder.imgAudio);
            Log.e("image path", "-----------------" + items.get(position).getPhoto().replaceAll(" ", "%20"));
            holder.imgAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }


        private class ViewHolder {
            TextView txtAudioName, txtAudioDate, txtDuration, txt_views;
            Button btnLike, btnComment, btnLikeClick;
            ImageView imgAudio, imgPlayAudio, imgPlayPush, imgPlayAudio1;

        }
    }

    /*public class SearchMonthAudio extends AsyncTask<String, String, String> {

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
                params.add(new BasicNameValuePair("cid", strCid));
                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest(MonthSearchAudio, "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {

                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        String AudioName = object.getString("AudioName");
                        String CategoryID = object.getString("CategoryID");
                        String Audio = AudioPath + object.getString("Audio");
                        String Photo = ImgURL + object.getString("Photo");
                        String Duration = object.getString("Duration");
                        String Date = object.getString("Date");
                        String view = object.getString("View");


                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);
                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Search\n Found");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }*/

   /* public class SearchMonthEventAudio extends AsyncTask<String, String, String> {

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
                JSONObject json = jsonParser.makeHttpRequest("http://www.grapes-solutions.com/vimalsagarji/event/searcheventsbycategoryyear/?page=1&psize=1000", "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                audiolistarraylist = new ArrayList<>();
                for (int i = 0; i < json.length(); i++) {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {

                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = "eid";
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = "http://www.grapes-solutions.com/vimalsagarji/static/eventaudio/" + object.getString("Audio");
                        String audiolist = object.getString("Audio");
                        String[] au = audiolist.split(",");
                        for (int k = 0; i < au.length; i++) {
                            audiolistarraylist.add(au[i]);
                        }
                        String Photo = "http://www.grapes-solutions.com/vimalsagarji/static/eventimage/" + object.getString("Photo").split(",");
                        Log.e("photo split", "----------------" + Photo);
                        String[] parray = Photo.split(",");
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");

                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, parray[0], Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);
            if (getActivity() != null) {
                if (listView != null) {
                    CustomAdpter customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);
                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Search\n Found");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }*/

    private class GetAllByPeople extends AsyncTask<String, Void, String> {
        String responseJSON = "";

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
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/bypeople/getallappposts/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "bid"+String.valueOf(i);
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = object.getString("Audio");
                        String Photo = "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleimage/" + object.getString("Photo");
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
           progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    CustomAdpter customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }


    }

    /*public class SearchAllByPeople extends AsyncTask<String, String, String> {

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
                JSONObject json = jsonParser.makeHttpRequest("http://www.grapes-solutions.com/vimalsagarji/bypeople/searchallposts/?page=1&psize=1000", "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {

                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = "bid";
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleaudio/" + object.getString("Audio");
                        String Photo = "http://www.grapes-solutions.com/vimalsagarji/static/bypeopleimage/" + object.getString("Photo");
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");

                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            if (getActivity() != null) {
                if (listView != null) {
                    CustomAdpter customAdpter = new CustomAdpter(getActivity(), R.layout.audiocategorylistviewsubelement, arrayList);
                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Search\n Found");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }*/
}
