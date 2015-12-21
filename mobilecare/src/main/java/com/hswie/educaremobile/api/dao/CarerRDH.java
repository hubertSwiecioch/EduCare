package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.helper.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by hswie on 11/5/2015.
 */
public class CarerRDH {

    public static final String TAG = "CarerRDH";

    public ArrayList<Carer> getAllCarers(){
        ArrayList<Carer> carers = new ArrayList<Carer>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_CARERS));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    carers.add(parseCarer(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getCarerMessages " +  "JSONException e = " + e.getMessage());
                }
            }
        }

        return carers;
    }

    public void addCarer(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }


        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_REGISTER_CARER));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_USERNAME, params.get(0)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_PASSWORD, params.get(1)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_FULLNAME, params.get(2)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_IMAGE, params.get(3)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_PHONENUMBER, params.get(4)));


        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public void updateCarer(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }

        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_UPDATE_CARER));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_ID, params.get(0)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_USERNAME, params.get(1)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_PASSWORD, params.get(2)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_FULLNAME, params.get(3)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_IMAGE, params.get(4)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_PHONENUMBER, params.get(5)));


        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public Carer parseCarer(JSONObject obj) {
        Carer carer = new Carer();
        try {
            carer.setID((obj.getString(DatabaseColumns.COL_ID)));
            carer.setFullName((obj.getString(DatabaseColumns.COL_FULL_NAME)));
            carer.setOnlineTest((obj.getString(DatabaseColumns.COL_ONLINE_TEST)));
            carer.setPhoto((obj.getString(DatabaseColumns.COL_PHOTO)));
            carer.setPhoneNumber((obj.getString(DatabaseColumns.COL_PHONENUMBER)));



        } catch (JSONException e) {
            Log.d(TAG,"parseCarerMessage " +  "JSONException e = " + e.getMessage());
        }

        return carer;
    }
}
