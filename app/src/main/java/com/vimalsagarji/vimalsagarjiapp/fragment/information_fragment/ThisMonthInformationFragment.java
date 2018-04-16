package com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
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
 * Created by Pradip on 11/03/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthInformationFragment extends Fragment {
    public ThisMonthInformationFragment() {
    }

    private List<InformationCategory> listfilterdata = new ArrayList<>();
//    private KProgressHUD loadingProgressDialog;
    private static final String TAG = ThisMonthInformationFragment.class.getSimpleName();
    private List<InformationCategory> listThisMonthitem = new ArrayList<>();
    private static final String URL = Constant.GET_THISMONTH_INFORMATION_CATEGORY;
    private ListView listView;
    private TextView txt_nodata_today;
    private EditText InputBox;
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;

    private CustomAdpter adpter;
    private ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thismonth_information, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressbar= (ProgressBar) getActivity().findViewById(R.id.progressbar);
        InputBox = (EditText) getActivity().findViewById(R.id.etText);
        ImageView imsearch = (ImageView) getActivity().findViewById(R.id.imgSerch);
        activity_main_swipe_refresh_layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);

        activity_main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

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
        imsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"REsponse",Toast.LENGTH_SHORT).show();
                if (CommonMethod.isInternetConnected(getActivity())) {
                    new SearchMonthInformation().execute();
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
        listView = (ListView) getActivity().findViewById(R.id.list);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InformationCategory informationCategory = (InformationCategory) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), InformationDetailActivity.class);
                intent.putExtra("click_action", "");
                intent.putExtra("listTitle", informationCategory.getTitle());
                intent.putExtra("listDescription", informationCategory.getDescription());
                intent.putExtra("listDate", informationCategory.getDay() + "," + informationCategory.getDate());
                intent.putExtra("listID", informationCategory.getID());
                intent.putExtra("time", informationCategory.getTime());
                intent.putExtra("view", informationCategory.getView());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }


    private class JsonTask extends AsyncTask<String, String, String> {
        final String status = "";

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
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                //  for (int i = 0; i < jsonObject.length(); i++)
                //  {
                listfilterdata = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object = jsonArray.getJSONObject(j);
                    String strID = object.getString("ID");
                    String strTitle = object.getString("Title");
                    String strDescription = object.getString("Description");
                    String strAddress = object.getString("Address");
                    String strDate = object.getString("Date");
                    String view = object.getString("View");
                    Log.e("date", "----------------------------" + strDate);

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


                    InformationCategory category = new InformationCategory();
                    category.setID(strID);
                    category.setTitle(strTitle);
                    category.setDescription(strDescription);
                    category.setAddress(strAddress);
                    category.setDate(fulldate);
                    category.setDay(dayOfTheWeek);
                    category.setView(view);
                    listThisMonthitem.add(category);
                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listThisMonthitem);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No information found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<InformationCategory> {

        final List<InformationCategory> items;
        final List<InformationCategory> filterdata;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<InformationCategory> items) {
            super(context, R.layout.thismonth_list_item, items);
            this.context = context;
            this.resource = R.layout.thismonth_list_item;
            this.items = items;
            this.filterdata = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txt_Title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.txt_Description = (TextView) convertView.findViewById(R.id.txtDescription);
                holder.txt_Date = (TextView) convertView.findViewById(R.id.txtDate);
                holder.txt_Address = (TextView) convertView.findViewById(R.id.txt_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            InformationCategory inCategory = items.get(position);
            holder.txt_Title.setMaxLines((int) 1.5);
            holder.txt_Title.setText(inCategory.getTitle());
            holder.txt_Description.setText(inCategory.getDescription());
            holder.txt_Date.setText(inCategory.getDay() + ", " + inCategory.getDate());
            holder.txt_Address.setText(inCategory.getAddress());
            holder.txt_views.setText(inCategory.getView());

            InformationCategory icategory = filterdata.get(position);
            holder.txt_Title.setMaxLines((int) 1.5);
            holder.txt_Title.setText(icategory.getTitle());
            holder.txt_Description.setText(icategory.getDescription());
            holder.txt_views.setText(icategory.getView());
            holder.txt_Address.setText(icategory.getAddress());
            holder.txt_Date.setText(icategory.getDay() + "," + icategory.getDate());
            return convertView;
        }

        private class ViewHolder {
            TextView txt_ID, txt_Title, txt_Description, txt_Address, txt_Date,txt_views;
        }

    }


    @SuppressWarnings("deprecation")
    public class SearchMonthInformation extends AsyncTask<String, String, String> {

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
                String url = Constant.SEARCHMONTHINFORMATION;
                Log.d(TAG, "Allinformation URL:" + url);
                String searchitem = InputBox.getText().toString();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("searchterm", searchitem));
                System.out.println("InputBox Value " + searchitem);
                JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
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

                        InformationCategory informationCategory = new InformationCategory();
                        informationCategory.setID(strID);
                        informationCategory.setTitle(strTitle);
                        informationCategory.setDescription(strDescription);
                        informationCategory.setAddress(strAddress);
                        informationCategory.setDate(fulldate);
                        informationCategory.setDay(dayOfTheWeek);
                        informationCategory.setTime(time[1]);
                        informationCategory.setView(view);
                        listfilterdata.add(informationCategory);
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
//            Toast.makeText(getActivity(), "Response"+listfilterdata, Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listfilterdata);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
                        txt_nodata_today.setText("No Search\n Found");
                    }
                }
            }
        }
    }

    private class LoadJsonTask extends AsyncTask<String, String, String> {
        final String status = "";

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
                //  for (int i = 0; i < jsonObject.length(); i++)
                //  {
                listfilterdata = new ArrayList<>();
                listThisMonthitem = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object = jsonArray.getJSONObject(j);
                    String strID = object.getString("ID");
                    String strTitle = object.getString("Title");
                    String strDescription = object.getString("Description");
                    String strAddress = object.getString("Address");
                    String strDate = object.getString("Date");
                    String view = object.getString("View");
                    Log.e("date", "----------------------------" + strDate);

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


                    InformationCategory category = new InformationCategory();
                    category.setID(strID);
                    category.setTitle(strTitle);
                    category.setDescription(strDescription);
                    category.setAddress(strAddress);
                    category.setDate(fulldate);
                    category.setDay(dayOfTheWeek);
                    category.setView(view);
                    listThisMonthitem.add(category);
                }
                //  }

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listThisMonthitem);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void loadData() {
        LoadJsonTask jsonTask = new LoadJsonTask();
        jsonTask.execute(URL);


    }


}

