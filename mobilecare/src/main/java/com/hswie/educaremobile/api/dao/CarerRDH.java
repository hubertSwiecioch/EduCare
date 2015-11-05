package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.jsonparse.JsonHelper;

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

    public Carer parseCarer(JSONObject obj) {
        Carer carer = new Carer();
        try {
            carer.setID((obj.getString(DatabaseColumns.COL_FIRST_NAME)));
            carer.setFullName((obj.getString(DatabaseColumns.COL_FULL_NAME)));
            carer.setFullName((obj.getString(DatabaseColumns.COL_ONLINE_TEST)));

        } catch (JSONException e) {
            Log.d(TAG,"parseCarerMessage " +  "JSONException e = " + e.getMessage());
        }

        return carer;
    }
}
