package com.vimalsagarji.vimalsagarjiapp.fragment.thought_fragment;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.AllThoughts;
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
 * Created by Pradip on 20/10/2016.
 */
@SuppressWarnings("ALL")
public class AllThoughtsFragment extends Fragment {

    public AllThoughtsFragment() {

    }

    private static final String TAG = AllThoughtsFragment.class.getSimpleName();
    private final List<AllThoughts> listAllThoughts = new ArrayList<>();
    private ListView listView;
    private CustomAdapter adapter;
    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;


    private EditText InputBox;
    private List<AllThoughts> listfilterdata = new ArrayList<>();
    private final String AllSearchThought = "http://www.grapes-solutions.com/vimalsagarji/thought/searchallthoughts/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private String url;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_thoughts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        url = Constant.ALL_THOUGHTS_URL + "&psize=1000";
        listView = (ListView) getActivity().findViewById(R.id.list);
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
                    new SearchAllThought().execute();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllThoughts ats = (AllThoughts) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ThoughtsDetailActivity.class);
                intent.putExtra("click_action","");
                intent.putExtra("thoughtid", ats.getId());
                intent.putExtra("listTitle", ats.getTitle());
                intent.putExtra("listDescription", ats.getDescription());
                intent.putExtra("listDate", ats.getDate());
                intent.putExtra("view", ats.getView());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        Log.e("all thought", "-----------------------------");
        if (CommonMethod.isInternetConnected(getActivity())) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute(url);
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
        adapter.clear();
        new LoadJsonTask().execute(url);
    }


    private class JsonTask extends AsyncTask<String, String, String> {

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
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object = jsonArray.getJSONObject(j);
                    String strID = object.getString("ID");
                    String strTitle = object.getString("Title");
                    String strDescription = object.getString("Description");
                    String strDate = object.getString("Date");
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

                    AllThoughts ats = new AllThoughts();
                    ats.setId(strID);
                    ats.setTitle(strTitle);
                    ats.setDescription(strDescription);
                    ats.setDate(dayOfTheWeek + ", " + fulldate);
                    ats.setView(view);
                    listAllThoughts.add(ats);
                }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
            adapter = new CustomAdapter(getActivity(), listAllThoughts);
            if (listView != null) {
                if (adapter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                } else {
                    listView.setVisibility(View.GONE);

                    txt_nodata_today.setVisibility(View.VISIBLE);
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
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object = jsonArray.getJSONObject(j);
                    String strID = object.getString("ID");
                    String strTitle = object.getString("Title");
                    String strDescription = object.getString("Description");
                    String strDate = object.getString("Date");
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

                    AllThoughts ats = new AllThoughts();
                    ats.setId(strID);
                    ats.setTitle(strTitle);
                    ats.setDescription(strDescription);
                    ats.setDate(dayOfTheWeek + ", " + fulldate);
                    ats.setView(view);
                    listAllThoughts.add(ats);
                }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new CustomAdapter(getActivity(), listAllThoughts);
            if (listView != null) {
                if (adapter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                    activity_main_swipe_refresh_layout.setRefreshing(false);
                } else {
                    listView.setVisibility(View.GONE);

                    txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdapter extends ArrayAdapter<AllThoughts> {

        final List<AllThoughts> items;
        final Context context;
        final int resource;

        public CustomAdapter(Context context, List<AllThoughts> items) {
            super(context, R.layout.thoughts_all_list_item, items);
            this.context = context;
            this.resource = R.layout.thoughts_all_list_item;
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
                holder.txt_Title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.txt_Description = (TextView) convertView.findViewById(R.id.txtDescription);
                holder.txt_Date = (TextView) convertView.findViewById(R.id.txtDate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AllThoughts ats = items.get(position);
            holder.txt_views.setText(ats.getView());
            holder.txt_Title.setText(ats.getTitle());
            holder.txt_Description.setText(ats.getDescription());
            holder.txt_Date.setText(ats.getDate());
            return convertView;

        }

        private class ViewHolder {
            TextView txt_ID, txt_Title, txt_Description, txt_Date,txt_views;
        }


    }

    @SuppressWarnings("deprecation")
    public class SearchAllThought extends AsyncTask<String, String, String> {

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
                JSONObject json = jsonParser.makeHttpRequest(AllSearchThought, "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listfilterdata = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        String title = object.getString("Title");
                        String description = object.getString("Description");
                        String date = object.getString("Date");
                        String view = object.getString("View");

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

                        AllThoughts thoughtToday = new AllThoughts();
                        thoughtToday.setId(id);
                        thoughtToday.setTitle(title);
                        thoughtToday.setDescription(description);
                        thoughtToday.setDate(dayOfTheWeek + ", " + fulldate);
                        thoughtToday.setView(view);
                        listfilterdata.add(thoughtToday);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        protected void onPostExecute(String status) {
            super.onPostExecute(status);

            if (listView != null) {
                adapter = new CustomAdapter(getActivity(), listfilterdata);
                if (adapter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                } else {
                    listView.setVisibility(View.GONE);
                    txt_nodata_today.setText("No Search \n Found");
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
