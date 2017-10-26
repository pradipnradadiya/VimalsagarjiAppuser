package com.vimalsagarji.vimalsagarjiapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;

@SuppressWarnings("ALL")
public class CommonMethod {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getStringResponse(String url) {
        String result = "";
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer;
        URL jsonurl;
        try {
            jsonurl = new URL(url);
            Log.e("URL", jsonurl.toString());
            stringBuffer = new StringBuffer("");
            URLConnection connection = jsonurl.openConnection();
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(50000);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postStringResponse(String url, ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs, Context ctx) {
        HttpClient httpclient = new DefaultHttpClient();


        HttpPost httppost = new HttpPost(url);
        Log.e("url postStringResponse", "" + url);

        String responseStr = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(response.getEntity());

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        }

        return responseStr;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static Date convert_date(String date) {
        Date mDate = null;
        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static String giveDate(String time) {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
}
