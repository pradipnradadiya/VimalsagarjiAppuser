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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.JSONParser1;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtToday;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class ThisWeekThoughtsFragment extends Fragment {
    public ThisWeekThoughtsFragment() {

    }

    private ListView listView1;
    private CustomAdapter adapter1;
    private List<ThoughtToday> list = new ArrayList<>();
//    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;

    private EditText InputBox;
    private List<ThoughtToday> listfilterdata = new ArrayList<>();
    private final String WeekSearchThought = "http://www.grapes-solutions.com/vimalsagarji/thought/searchallthoughtsbycidweek/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thisweek_thoughts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressbar= (ProgressBar) getActivity().findViewById(R.id.progressbar);
        listView1 = (ListView) getActivity().findViewById(R.id.list1);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        if (CommonMethod.isInternetConnected(getActivity())) {
            GetWeekThought getWeekThought = new GetWeekThought();
            getWeekThought.execute();
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
                    new SearchWeekThought().execute();
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


//        listView.setListener(this);
        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (adapter1 == null) {
                    return;
                }

                if (adapter1.getCount() == 0) {
                    return;
                }

                int l = firstVisibleItem + visibleItemCount;
                System.out.println("firstVisibleItem : " + firstVisibleItem + "  visibleItemCount " + visibleItemCount + "  totalItemCount " + totalItemCount);
//                if(l >= totalItemCount && !isLoading){
//                    onLoadData();
//                    isLoading = true;
//                }
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ThoughtToday ats = (AllThoughts) parent.getItemAtPosition(position);
                ThoughtToday week = (ThoughtToday) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ThoughtsDetailActivity.class);
                intent.putExtra("click_action","");
                intent.putExtra("thoughtid", week.getId());
                intent.putExtra("listTitle", week.getTitle());
                intent.putExtra("listDescription", week.getDescription());
                intent.putExtra("listDate", week.getDate());
                intent.putExtra("view", week.getView());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        Log.e("call", "------------------");

    }

    private void loadData() {
        new LoadGetWeekThought().execute();
    }

    private class GetWeekThought extends AsyncTask<String, Void, String> {
        String responseJSON1 = "";

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
                responseJSON1 = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/thought/getallthoughtsbycidweek/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
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
                        ThoughtToday thoughtToday = new ThoughtToday();
                        thoughtToday.setId(id);
                        thoughtToday.setTitle(title);
                        thoughtToday.setDescription(description);
                        thoughtToday.setDate(dayOfTheWeek + ", " + fulldate);
                        thoughtToday.setView(view);
                        list.add(thoughtToday);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

/*
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView1 != null) {
                    adapter1 = new CustomAdapter(getActivity(), list);
                    if (adapter1.getCount() != 0) {
                        listView1.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView1.setAdapter(adapter1);
                    } else {
                        listView1.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    private class LoadGetWeekThought extends AsyncTask<String, Void, String> {
        String responseJSON1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON1 = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/thought/getallthoughtsbycidweek/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
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
                        ThoughtToday thoughtToday = new ThoughtToday();
                        thoughtToday.setId(id);
                        thoughtToday.setTitle(title);
                        thoughtToday.setDescription(description);
                        thoughtToday.setDate(dayOfTheWeek + ", " + fulldate);
                        thoughtToday.setView(view);
                        list.add(thoughtToday);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (getActivity() != null) {
                if (listView1 != null) {
                    adapter1 = new CustomAdapter(getActivity(), list);
                    if (adapter1.getCount() != 0) {
                        listView1.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView1.setAdapter(adapter1);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView1.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdapter extends ArrayAdapter<ThoughtToday> {

        final List<ThoughtToday> items;
        final Context context;
        final int resource;

        public CustomAdapter(Context context, List<ThoughtToday> items) {
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
            ThoughtToday ats = items.get(position);
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
    public class SearchWeekThought extends AsyncTask<String, String, String> {

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
                JSONObject json = jsonParser.makeHttpRequest(WeekSearchThought, "POST", params);
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
                        ThoughtToday thoughtToday = new ThoughtToday();
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
            if (getActivity() != null) {
                if (listView1 != null) {
                    adapter1 = new CustomAdapter(getActivity(), listfilterdata);
                    if (adapter1.getCount() != 0) {
                        listView1.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView1.setAdapter(adapter1);
                    } else {
                        listView1.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Search \n Found");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }
}
