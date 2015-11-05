package com.hswie.educaremobile.jsonparse;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by hswie on 08.10.2015.
 */
public class JsonHelper {

    public static final String HOSTNAME = "http://serwer1552055.home.pl/EduCare/index.php";
    public static final String TAG_MOD = "mod";
    public static final String MOD_LOGIN = "LogIn";
    public static final String MOD_GET_RESIDENTS = "getResidentsList";
    public static final String MOD_GET_CARERS = "getCarersList";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_DISPLAYNAME = "displayname";
    public static final String TAG_ONLINETEST = "onlineTestTime";


    private static final String TAG = "JsonHelper";

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jarray = null;
    static String json = "";

    public static String makeHttpRequest(String loginUrl, String method, List<NameValuePair> params) {
        // making HTTP Request
        try {
            // check for request method
            if(method == "POST"){
                Log.d(TAG, "POST");
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(loginUrl);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.d(TAG, " " + httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }else
                // check for request method
                if(method == "GET"){
                    Log.d(TAG, "GET");
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    loginUrl += "?" + paramString;
                    HttpGet httpGet = new HttpGet(loginUrl);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String  line = null;

            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
//            if (json.startsWith("["))
//                json = removeBrackets(json);
            //Log.d(TAG, "resultFromAPI:" +  json);
        } catch (IOException e) {
            Log.d("Buffer Error","Error Converting Reesult "+e.toString());
        }

        // try parse the string to JSON Object


        // return JSON String
        return json;
    }

    public static JSONObject getJSONObjectFromString(String json){

        try {
            jObj = new JSONObject(json);
            Log.d(TAG, "JObject: " + jObj
            );
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error Parsing data" + e.toString());
        }

        // return JSON String
        return jObj;
    }


    public static JSONArray getJSONArrayFromString(String json){

        try {
            jarray = new JSONArray( json);
            Log.d(TAG, "JArray: " + jarray);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jarray;
    }


}
