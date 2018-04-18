package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.CustomImageAdapter;
import com.vimalsagarji.vimalsagarjiapp.Gallery_gridview_full_image;
import com.vimalsagarji.vimalsagarjiapp.ImageItemSplash;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.Splash_Activity2;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GalleryCategory extends AppCompatActivity {

    static final String TAG = GalleryCategory.class.getSimpleName();
    private static final String URL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/gallery/getallimages/?page=1&psize=1000";
    private static final String ImgURL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/static/Gallery/";
    private final ArrayList<String> listIcon = new ArrayList<String>();
    String strImageUrl = "";
    static Bitmap bitmap = null;
    private GridView gridView;
    //    private KProgressHUD loadingProgressDialog;
    private RelativeLayout rel_gallary;
    private String cid, catname;
    private TextView txt_nodata_today;
    public static ArrayList<ImageItemSplash> itemSplashArrayList = new ArrayList<>();
    private CustomImageAdapter customImageAdapter;
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
        setContentView(R.layout.content_gallery_category);
        customImageAdapter = new CustomImageAdapter(GalleryCategory.this, itemSplashArrayList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        gridView = (GridView) findViewById(R.id.gallery_gridview);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        rel_gallary = (RelativeLayout) findViewById(R.id.rel_gallary);
        ImageView imgarrorback = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
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
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        cid = getIntent().getStringExtra("cid");
        catname = getIntent().getStringExtra("catname");
        txt_title.setText(catname);

       /* if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute(URL, ImgURL);
        }*/

        if (cid.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("event click", "-----------");
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
                new EventImage().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
        } else if (cid.equalsIgnoreCase("bypeopleidid")) {
            Log.e("bypeople click", "-----------");
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
                new ByPeopleImage().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL, ImgURL);
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

    private class JsonTask extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/gallery/getallimagesbycid/?cid=" + cid + "&page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String cid = jsonObject1.getString("CID");
                        String photo = ImgURL + jsonObject1.getString("Photo");
                        String date = jsonObject1.getString("Date");
                        listIcon.add(photo);
                        itemSplashArrayList.add(new ImageItemSplash(photo, photo));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          /*  if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);


            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }
            }

        }
    }

    private class ByPeopleImage extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/bypeople/getallappposts/?page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.getString("Photo").equalsIgnoreCase("null")) {

                        } else {

                            String photo = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/static/bypeopleimage/" + jsonObject1.getString("Photo");
                            itemSplashArrayList.add(new ImageItemSplash(photo, photo));
                            listIcon.add(photo);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressbar.setVisibility(View.GONE);
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/

            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }
            }

        }
    }

    private class EventImage extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/event/geteventsbycategoryyear/?page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    loadingProgressDialog.dismiss();
                    progressbar.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.getString("Photo").equalsIgnoreCase("null")) {

                        } else {
                            String photo = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/static/eventimage/" + jsonObject1.getString("Photo");
                            String[] photoarray = photo.split(",");
                            Log.e("photo array 0", "--------------" + photoarray[0]);
                            itemSplashArrayList.add(new ImageItemSplash(photoarray[0], photoarray[0]));
                            listIcon.add(photoarray[0]);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            progressbar.setVisibility(View.GONE);

            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
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
            super(context, R.layout.custom_gallery_gridview, items);
            this.context = context;
            this.resource = R.layout.custom_gallery_gridview;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(GalleryCategory.this)
                    .load(items.get(position)
                            .replaceAll(" ", "%20"))
                    .placeholder(R.drawable.loader)
                    .resize(0, 200)
                    .error(R.drawable.no_image)
                    .into(holder.grid_img);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(GalleryCategory.this, Splash_Activity2.class);
                    intent.putExtra("imagePath", listIcon.get(position));
                    String pos = String.valueOf(position);
                    intent.putExtra("position", pos);
                    intent.putExtra("cid", cid);
                    Log.e("position", "------------" + position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });


            return convertView;

        }

        private class ViewHolder {
            ImageView grid_img;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
//        listIcon.clear();

    }

}

