package com.vimalsagarji.vimalsagarjiapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthVideo;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressWarnings("ALL")
public class AudioDetail extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    private TextView txt_like;
    private TextView txt_comment;
    private String status;
    private Sharedpreferance sharedpreferance;
    private List<ThisMonthVideo> listComments = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private JcPlayerView jcPlayerView;
    private String approve = "";
    private String id;
    private String view;
    EditText et_event;
    TextView txt_title;
    private final String Imagepath = "http://www.grapes-solutions.com/vimalsagarji/static/audioimage/";
    private final String AudioPath = "http://www.grapes-solutions.com/vimalsagarji/static/audios/";
    TextView txtDate;
    String click_action;

    private final ArrayList<String> listname = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jcPlayerView.kill();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_playing_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sharedpreferance = new Sharedpreferance(AudioDetail.this);
        jcPlayerView = (JcPlayerView) findViewById(R.id.jcplayerview_audio);
        et_event = (EditText) findViewById(R.id.et_event);
        txt_title = (TextView) findViewById(R.id.txt_title);

        txt_title.setText("\t Audio Detail");
        txtDate = (TextView) findViewById(R.id.txtDate);

        ImageView imgHomeBack = (ImageView) findViewById(R.id.imgarrorback);
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jcPlayerView.kill();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        imgHome.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);

        //Like Comment Id
        LinearLayout lin_like = (LinearLayout) findViewById(R.id.lin_like);
        LinearLayout lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        ImageView img_like = (ImageView) findViewById(R.id.img_like);
        ImageView img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like_event);
        txt_comment = (TextView) findViewById(R.id.txt_comment_event);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        click_action = intent.getExtras().getString("click_action");
        Log.e("id", "------------------" + id);

        if (id.equalsIgnoreCase("eid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String audioname = intent.getExtras().getString("AudioName");
            et_event.setText(audioname);
            Log.e("audioname", "------------------" + audioname);
            String category_id = intent.getExtras().getString("CategoryID");
            Log.e("category_id", "------------------" + category_id);
            String audio = intent.getExtras().getString("Audio");
            Log.e("audio", "------------------" + audio);
            String photo = intent.getExtras().getString("Photo");
            Log.e("photo", "------------------" + photo);
            String duration = intent.getExtras().getString("Duration");
            Log.e("duration", "------------------" + duration);
            String date = intent.getExtras().getString("Date");
            Log.e("date", "------------------" + date);
            view = intent.getStringExtra("view");
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                assert audio != null;
                jcPlayerView.playAudio(audio.replaceAll(" ", "%20"), audioname);
            }
            txtDate.setText(date);

        } else if (id.equalsIgnoreCase("bid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String audioname = intent.getExtras().getString("AudioName");
            et_event.setText(audioname);
            Log.e("audioname", "------------------" + audioname);
            String category_id = intent.getExtras().getString("CategoryID");
            Log.e("category_id", "------------------" + category_id);
            String audio = intent.getExtras().getString("Audio");
            Log.e("audio", "------------------" + audio);
            String photo = intent.getExtras().getString("Photo");
            Log.e("photo", "------------------" + photo);
            String duration = intent.getExtras().getString("Duration");
            Log.e("duration", "------------------" + duration);
            String date = intent.getExtras().getString("Date");
            Log.e("date", "------------------" + date);
            view = intent.getStringExtra("view");
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                assert audio != null;
                jcPlayerView.playAudio(audio.replaceAll(" ", "%20"), audioname);
            }
            txtDate.setText(date);
        } else {
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
//                new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
//                new CheckLike().execute(sharedpreferance.getId(), id);
//                new AudioDetailId().execute();
//                new CountComment().execute(id);
            }
        }

        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                        if (status.equalsIgnoreCase("0")) {
                            new LikeComment().execute(id);
                            new getLikeCount().execute(id);
                            new CheckLike().execute(sharedpreferance.getId(), id);
                        } else {
                            Toast.makeText(getApplicationContext(), "Already liked this information.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(v, R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

        });

        lin_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AudioDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_bypeople_comment);

                if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                    new CommentList().execute(id);


                } else {
                    Snackbar.make(v, R.string.internet, Snackbar.LENGTH_SHORT).show();
                }

                ImageView imgback1 = (ImageView) dialog.findViewById(R.id.imgback);
                imgback1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button btnPost = (Button) dialog.findViewById(R.id.btnPost);
                final EditText etComment = (EditText) dialog.findViewById(R.id.etComment);
                RelativeLayout rl_layout = (RelativeLayout) dialog.findViewById(R.id.layout_byPeople);

                dialog.show();
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            Toast.makeText(AudioDetail.this, R.string.notregister, Toast.LENGTH_SHORT).show();

                        }else {
                            String strComment = etComment.getText().toString();
                            if (TextUtils.isEmpty(strComment)) {
                                etComment.setError("Please enter your comment.");
                                etComment.requestFocus();
                            } else {
                                if (approve.equalsIgnoreCase("1")) {
                                    new CommentPost().execute(etComment.getText().toString());
                                    etComment.setText("");
                                } else {
                                    Toast.makeText(AudioDetail.this, R.string.notregister, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                });

                rl_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                    }
                });
            }
        });


    }


    //Comment List
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setLabel("Please Wait")
//                    .setCancellable(true);
//            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/getallappcomments/?page=1&psize=1000", nameValuePairs, AudioDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listComments = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        loadingProgressDialog.dismiss();
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("AudioID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");
                        String name = object.getString("Name");

                        ThisMonthVideo thisMonthVideo = new ThisMonthVideo();
                        thisMonthVideo.setID(strID);
                        thisMonthVideo.setVideoID(strInformationID);
                        thisMonthVideo.setComment(strComment);
                        thisMonthVideo.setIs_Approved(strIs_Approved);
                        thisMonthVideo.setUserID(strUserID);
                        thisMonthVideo.setDate(strDate);
                        listComments.add(thisMonthVideo);
                        listname.add(name);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (loadingProgressDialog != null) {
//                loadingProgressDialog.dismiss();
//            }
            listView = (ListView) dialog.findViewById(R.id.listbyPeopleComment);
            TextView txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nocomment);
            if (listView != null) {
                CustomAdpter adpter = new CustomAdpter(getApplicationContext(), listComments);
                if (adpter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(adpter);
                } else {
                    listView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            }
        }
    }

  /*  private class CountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/getallappcomments/?page=1&psize=1000", nameValuePairs, AudioDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listComments = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("AudioID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");

                        ThisMonthVideo thisMonthVideo = new ThisMonthVideo();
                        thisMonthVideo.setID(strID);
                        thisMonthVideo.setVideoID(strInformationID);
                        thisMonthVideo.setComment(strComment);
                        thisMonthVideo.setIs_Approved(strIs_Approved);
                        thisMonthVideo.setUserID(strUserID);
                        thisMonthVideo.setDate(strDate);
                        listComments.add(thisMonthVideo);
                        int count = listComments.size();
                        String c = String.valueOf(count);
                        txt_comment.setText(c);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

        }
    }*/

    //Custom Adapter
    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<ThisMonthVideo> {

        final List<ThisMonthVideo> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<ThisMonthVideo> items) {
            super(context, R.layout.custom_allthought_dialog_listitem, items);
            this.context = context;
            this.resource = R.layout.custom_allthought_dialog_listitem;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txtCommentUserName = (TextView) convertView.findViewById(R.id.txtCommentUserName);
                holder.txtCommentDescription = (TextView) convertView.findViewById(R.id.txtCommentDescription);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ThisMonthVideo monthComments = items.get(position);

            if (listname.get(position).equalsIgnoreCase("null")) {
                holder.txtCommentUserName.setText("Admin");
                holder.txtCommentDescription.setText(monthComments.getComment());
            } else {
                holder.txtCommentUserName.setText(listname.get(position));
                holder.txtCommentDescription.setText(monthComments.getComment());
            }
            return convertView;
        }

        private class ViewHolder {
            TextView txtCommentUserName, txtCommentDescription;

        }


    }

    //Comment Post
    private class CommentPost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", id));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
            responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/comment/", nameValuePairs, AudioDetail.this);

            return responseJSON;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AudioDetail.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(AudioDetail.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //Like
    private class LikeComment extends AsyncTask<String, Void, String> {
        String responeJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responeJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/audiolike/", nameValuePairs, AudioDetail.this);
            return responeJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            status="1";

            loadingProgressDialog.dismiss();
        }
    }

    //Count Like
    private class getLikeCount extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));

            responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/countlikes/", nameValuePairs, AudioDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Log.e("count like", "------------------" + jsonArray.getString(0));
                String count = jsonArray.getString(0);
                if (count.equalsIgnoreCase("")) {
                    txt_like.setText("0");
                } else {
                    txt_like.setText(count);
                    status="1";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Check Like
    private class CheckLike extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("aid", params[1]));

            responseJSON = CommonMethod.postStringResponse("http://www.grapes-solutions.com/vimalsagarji/audio/checklike/", nameValuePairs, AudioDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
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
//                    Toast.makeText(AudioDetail.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class countView extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/countviews/audio/?aid=" + id + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
        }

    }

    private class AudioDetailId extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getaudiobyidforadmin + "?aid=" + id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    String commentcount = jsonObject.getString("commentcount");
                    Log.e("like", "-----------------" + commentcount);
                    txt_comment.setText(commentcount);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    txt_like.setText(likecount);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String audioname = jsonObject1.getString("AudioName");
                        String cid = jsonObject1.getString("CategoryID");
                        String audio = jsonObject1.getString("Audio").replaceAll(" ", "%20");
                        String photo = jsonObject1.getString("Photo").replaceAll(" ", "%20");
                        String duration = jsonObject1.getString("Duration");
                        String date = jsonObject1.getString("Date");
                        view = jsonObject1.getString("View");


                        String[] string = date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

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

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + " " + string[1];


                        et_event.setText(audioname);
                        Log.e("audioname", "------------------" + audioname);
                        String audios = AudioPath + audio;
                        Log.e("audio", "------------------" + audio);
                        String photos = Imagepath + photo;
                        Log.e("photo", "------------------" + photo);
                        Log.e("duration", "------------------" + duration);
                        String dates = fulldate;
                        Log.e("date", "------------------" + date);

                        if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                            assert audios != null;
                            jcPlayerView.playAudio(audios.replaceAll(" ", "%20"), audioname);
                        }
                        txtDate.setText(dates);
                        new countView().execute();


//                        txt_title.setText(audioname);
//                        txt_date.setText(fulldate);
//                        txt_views.setText(view);
//                        Picasso.with(AudioDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.audioimage + jsonObject1.getString("Photo").replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(img_photo);
//                        jcplayer_audio.playAudio(CommonURL.AudioPath + CommonAPI_Name.audios + jsonObject1.getString("Audio").replaceAll(" ", "%20"), jsonObject1.getString("AudioName"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
//                new CountLike().execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        if (id.equalsIgnoreCase("eid")) {

        } else if (id.equalsIgnoreCase("bid")) {

        } else {
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                } else {
                    new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
                    new CheckLike().execute(sharedpreferance.getId(), id);
                }
                new AudioDetailId().execute();
//                new CountComment().execute(id);
            }
        }
    }

}


