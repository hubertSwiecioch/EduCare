package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.CarerTask;
import com.hswie.educaremobile.helper.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by hswie on 11/12/2015.
 */
public class CarerTasksRDH {

    public static final String TAG = "CarerTasksRDH";

    public ArrayList<CarerTask> getCarerTasks(String carerID){
        ArrayList<CarerTask> tasks = new ArrayList<CarerTask>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_CARER_TASKS));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_ID, carerID));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    tasks.add(parseTask(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getCarerMessages " + "JSONException e = " + e.getMessage());
                }
            }
        }

        return tasks;
    }

    public CarerTask parseTask(JSONObject obj) {
        CarerTask task = new CarerTask();
        try {
            task.setId((obj.getString(DatabaseColumns.COL_ID)));
            task.setDescription((obj.getString(DatabaseColumns.COL_DESCRIPTION)));
            task.setDate((Long.parseLong(obj.getString(DatabaseColumns.COL_DATE))));
            task.setHeader((obj.getString(DatabaseColumns.COL_HEADER)));
            task.setIs_done((Boolean.parseBoolean(obj.getString(DatabaseColumns.COL_IS_DONE))));
            task.setTargetCarerID((obj.getString(DatabaseColumns.COL_CARER_ID)));
            task.setTargetResidentID((obj.getString(DatabaseColumns.COL_RESIDENT_ID)));



        } catch (JSONException e) {
            Log.d(TAG,"parseCarerTask " +  "JSONException e = " + e.getMessage());
        }

        return task;
    }

    public void addCarerTask(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }


        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_ADD_TASK));



        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public void setIsDone(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }

        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_SET_TASK_IS_DONE));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_ID, params.get(0)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_ISDONE, params.get(1)));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);
    }
}
