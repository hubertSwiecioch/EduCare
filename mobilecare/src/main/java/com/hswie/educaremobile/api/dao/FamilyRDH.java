package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.Family;
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
public class FamilyRDH {

    public static final String TAG = "FamilyRDH";

    public ArrayList<Family> getAllFamilies(){
        ArrayList<Family> families = new ArrayList<Family>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_FAMILIES));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    families.add(parseFamily(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getFamilies " +  "JSONException e = " + e.getMessage());
                }
            }
        }

        return families;
    }

    public void addFamily(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }


        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_REGISTER_FAMILY));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_FAMILY_USERNAME, params.get(0)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_FAMILY_PASSWORD, params.get(1)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_FAMILY_FULLNAME, params.get(2)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_RESIDENT_ID, params.get(3)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_PHONENUMBER, params.get(4)));


        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public void removeFamily(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }


        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_REMOVE_FAMILY));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_ID, params.get(0)));



        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public Family parseFamily(JSONObject obj) {
        Family family = new Family();
        try {
            family.setID((obj.getString(DatabaseColumns.COL_ID)));
            family.setUsername((obj.getString(DatabaseColumns.COL_FAMILY_USERNAME)));
            family.setFullName((obj.getString(DatabaseColumns.COL_FAMILY_FULLNAME)));
            family.setPassword((obj.getString(DatabaseColumns.COL_FAMILY_PASSWORD)));
            family.setResidentID((obj.getString(DatabaseColumns.COL_RESIDENT_ID)));
            family.setPhoneNumber((obj.getString(DatabaseColumns.COL_PHONENUMBER)));



        } catch (JSONException e) {
            Log.d(TAG,"parseFamily " +  "JSONException e = " + e.getMessage());
        }

        return family;
    }

    public ArrayList<Family> getResidentFamily(String residentID) {
        ArrayList<Family> families = new ArrayList<Family>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_CURRENT_RESIDENT_FAMILY));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_RESIDENT_ID,  residentID));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    families.add(parseFamily(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getResidentMessages " +  "JSONException e = " + e.getMessage());
                }
            }
        }

        return families;
    }
}
