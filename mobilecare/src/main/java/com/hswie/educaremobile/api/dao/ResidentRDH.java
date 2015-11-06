package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Resident;
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
public class ResidentRDH {

    public static final String TAG = "ResidentRDH";

    public ArrayList<Resident> getAllResidents(){
        ArrayList<Resident> residents = new ArrayList<Resident>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_RESIDENTS));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    residents.add(parseResident(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getResidentMessages " +  "JSONException e = " + e.getMessage());
                }
            }
        }

        return residents;
    }


    public Resident parseResident(JSONObject obj) {
        Resident resident = new Resident();
        try {
            resident.setID((obj.getString(DatabaseColumns.COL_FIRST_NAME)));
            resident.setBirthDate((obj.getString(DatabaseColumns.COL_BIRTH_DATE)));
            resident.setDateOfAdoption((obj.getString(DatabaseColumns.COL_DATE_OF_ADOPTION)));
            resident.setFirstName((obj.getString(DatabaseColumns.COL_FIRST_NAME)));
            resident.setLastName((obj.getString(DatabaseColumns.COL_LAST_NAME)));
            resident.setAddress((obj.getString(DatabaseColumns.COL_ADDRESS)));
            resident.setCity((obj.getString(DatabaseColumns.COL_CITY)));
            resident.setPhoto((obj.getString(DatabaseColumns.COL_PHOTO)));

        } catch (JSONException e) {
            Log.d(TAG,"parseCarerMessage " +  "JSONException e = " + e.getMessage());
        }

        return resident;
    }
}
