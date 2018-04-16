package com.vimalsagarji.vimalsagarjiapp.fragment.questionanswer_fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.EventDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.JSONParser1;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtToday;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


@SuppressWarnings("ALL")
public class TodayQuetionAnswerFragment extends Fragment {

    public TodayQuetionAnswerFragment() {

    }

    private CustomAdpter adpter;
    //    private KProgressHUD loadingProgressDialog;
    private Sharedpreferance sharedpreferance;
    private ArrayList<String> listQuestion = new ArrayList<String>();
    private final ArrayList<String> listAnswer = new ArrayList<String>();
    private final ArrayList<String> listUserID = new ArrayList<String>();
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listDate = new ArrayList<String>();
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listview = new ArrayList<String>();
    private String strAskQuestion;
    private ListView listView;
    private Button btn_askQuestion;
    private final String postQue = "http://www.grapes-solutions.com/vimalsagarji/questionanswer/askques/";

    private Dialog dialog;
    private TextView txt_nodata_today;
    private EditText InputBox;
    List<ThoughtToday> listfilterdata = new ArrayList<>();
    private final String TodaySearchQuestion = "http://www.grapes-solutions.com/vimalsagarji/questionanswer/searchallappquestoday/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    String approve = "";
    private ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_question_answer, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listview_today);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);

        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
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
                    new SearchTodayQuestion().execute();
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

        btn_askQuestion = (Button) getActivity().findViewById(R.id.btn_askQuestion);
        sharedpreferance = new Sharedpreferance(getActivity());
        if (CommonMethod.isInternetConnected(getActivity())) {
//            GetTodayQuestion getTodayQuestion = new GetTodayQuestion();
//            getTodayQuestion.execute();
            new CheckUserApprove().execute();
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AllQuestionDetail.class);
                Log.e("Question", listQuestion.get(position));
                Log.e("Answer", listAnswer.get(position));
//                intent.putExtra("Question", listQuestion.get(position));
//                intent.putExtra("Answer", listAnswer.get(position));
//                intent.putExtra("view", listview.get(position));
                intent.putExtra("qid", listID.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btn_askQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, "Please register after ask question. ", Snackbar.LENGTH_SHORT).show();
                } else {


                    if (approve.equalsIgnoreCase("1")) {
                        dialog = new Dialog(getActivity());
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_askquestion_dialog);
                        dialog.setCancelable(false);
                        final EditText etAskQuestion = (EditText) dialog.findViewById(R.id.etAskQuestion);
                        Button btnPost = (Button) dialog.findViewById(R.id.btnPost);
                        final String strUid = sharedpreferance.getId();
                        strAskQuestion = etAskQuestion.getText().toString();
                        dialog.show();
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnPost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CommonMethod.isInternetConnected(getActivity())) {
                                    String txtpost = etAskQuestion.getText().toString();
                                    if (TextUtils.isEmpty(txtpost)) {
                                        etAskQuestion.setError("Please enter ask question.");
                                        etAskQuestion.requestFocus();
                                    } else {
                                        new postData().execute(txtpost);

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

                            }
                        });
                    } else {
//                        Toast.makeText(getActivity(), "You are not approved user.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadData() {
        adpter.clear();
        new LoadGetTodayQuestion().execute();
    }


    //Method to show the snackbar
    private void showSnackbar(View v) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, "Please register after ask question.", Snackbar.LENGTH_LONG);

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        //Customizing colors
        snackbar.setActionTextColor(Color.WHITE);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);

        //Displaying snackbar
        snackbar.show();
    }

    private class GetTodayQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/questionanswer/viewallappquestoday/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
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
                        listID.add(id);
                        String Question = object.getString("Question");
                        String Answer = object.getString("Answer");
                        String Date = object.getString("Date");
                        String uid = object.getString("UserID");
                        String name = object.getString("Name");
                        String view = object.getString("View");
                        listview.add(view);
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
                        listQuestion.add(Question);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        listAnswer.add(Answer);
                        listUserID.add(uid);
                        listName.add(name);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

       /*     if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listQuestion);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    TEmptyView.init(TViewUtil.EmptyViewBuilder.getInstance(getActivity())
//                            .setShowText(true)
//                            .setEmptyText("No Data")
//                            .setEmptyTextSize(20)
//                            .setShowButton(false)
//                            .setShowIcon(true)
//                    );
//                    TViewUtil.setEmptyView(listView);


                    }


                }

            }
        }
    }

    private class LoadGetTodayQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/questionanswer/viewallappquestoday/?page=1&psize=1000");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
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
                        listID.add(id);
                        String Question = object.getString("Question");
                        String Answer = object.getString("Answer");
                        String Date = object.getString("Date");
                        String uid = object.getString("UserID");
                        String name = object.getString("Name");
                        String view = object.getString("View");
                        listview.add(view);
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
                        listQuestion.add(Question);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        listAnswer.add(Answer);
                        listUserID.add(uid);
                        listName.add(name);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

         /*   if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/
         progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listQuestion);
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

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.question_answer_list_item, items);
            this.context = context;
            this.resource = R.layout.question_answer_list_item;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
                holder.txtAnswer = (TextView) convertView.findViewById(R.id.txtAnswer);
                holder.txt_date = (TextView) convertView.findViewById(R.id.txt_datess);
                holder.txt_postby = (TextView) convertView.findViewById(R.id.txt_postby);
                //makeTextViewResizable(holder.txtAnswer, 2,"Read More",true);
                //  holder.btnReadMore=(Button)convertView.findViewById(R.id.btnReadMore);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(listview.get(position));
            holder.txtQuestion.setText("Q: "+items.get(position));
            holder.txtAnswer.setText("A: "+listAnswer.get(position));
            holder.txt_date.setText(listDate.get(position));
            holder.txt_postby.setText("Question By:" + listName.get(position));
            return convertView;

        }


        private class ViewHolder {
            TextView txtQuestion, txtAnswer, txt_date, txt_postby, txt_views;
            //  Button btnReadMore;

        }


    }

    private class postData extends AsyncTask<String, Void, String> {

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
                ArrayList<NameValuePair> Pairs = new ArrayList<>();
                Pairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
                Pairs.add(new BasicNameValuePair("Question", params[0]));
                responseJSON = CommonMethod.postStringResponse(postQue, Pairs, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("respone question ask", "---------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Question asked successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            loadingProgressDialog.dismiss();
            progressbar.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    public class SearchTodayQuestion extends AsyncTask<String, String, String> {

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
                JSONObject json = jsonParser.makeHttpRequest(TodaySearchQuestion, "POST", params);
                // JSONObject json = JSONParser.getJsonFromUrl(url);
                Log.d("Create Response", json.toString());
                status = json.optString(TAG_SUCCESS);
                for (int i = 0; i < json.length(); i++) {
                    listQuestion = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("data");
                    System.out.println("JsonArray is" + jsonArray.length());
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        listID.add(id);
                        String Question = object.getString("Question");
                        String Answer = object.getString("Answer");
                        String Date = object.getString("Date");
                        String uid = object.getString("UserID");
                        String view = object.getString("View");
                        listview.add(view);


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
                        listQuestion.add(Question);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        listAnswer.add(Answer);
                        listUserID.add(uid);


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
                    adpter = new CustomAdpter(getActivity(), listQuestion);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
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

    //Check user approve or not
    private class CheckUserApprove extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/userregistration/checkuserapproveornot/?uid=" + sharedpreferance.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        approve = jsonObject1.getString("Is_Active");
                    }

                } else {
//                    Toast.makeText(getActivity(), "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        listQuestion.clear();
        if (CommonMethod.isInternetConnected(getActivity())) {
            GetTodayQuestion getTodayQuestion = new GetTodayQuestion();
            getTodayQuestion.execute();
        }
    }


}
