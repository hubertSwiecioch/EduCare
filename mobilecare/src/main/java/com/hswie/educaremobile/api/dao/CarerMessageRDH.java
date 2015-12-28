package com.hswie.educaremobile.api.dao;

import android.util.Log;

import com.hswie.educaremobile.api.DatabaseColumns;
import com.hswie.educaremobile.api.pojo.Carer;
import com.hswie.educaremobile.api.pojo.CarerMessage;
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
public class CarerMessageRDH {

    public static final String TAG = "CarerMessageRDH";

    public ArrayList<CarerMessage> getCarerMessages(String carerID){
        ArrayList<CarerMessage> messages = new ArrayList<CarerMessage>();
        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_GET_CARER_MESSAGES));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CARER_ID, carerID));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);

        JSONArray jsonArray = JsonHelper.getJSONArrayFromString(JSONString);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    messages.add(parseMessage(obj));
                } catch (JSONException e) {
                    Log.d(TAG, "getCarerMessages " + "JSONException e = " + e.getMessage());
                }
            }
        }

        return messages;
    }

    public CarerMessage parseMessage(JSONObject obj) {
        CarerMessage message = new CarerMessage();
        try {
            message.setId((obj.getString(DatabaseColumns.COL_ID)));
            message.setMessage((obj.getString(DatabaseColumns.COL_CONTENT)));
            message.setSendDate((Long.parseLong(obj.getString(DatabaseColumns.COL_SEND_DATE))));
            message.setIsRead((Integer.parseInt(obj.getString(DatabaseColumns.COL_IS_READ))));
            message.setSentBy((obj.getString(DatabaseColumns.COL_SENDER_ID)));
            message.setTitle((obj.getString(DatabaseColumns.COL_TITLE)));
            message.setSentTo((obj.getString(DatabaseColumns.COL_TARGET_ID)));






        } catch (JSONException e) {
            Log.d(TAG,"parseCarerTask " +  "JSONException e = " + e.getMessage());
        }

        return message;
    }

    public void addCarerMessage(ArrayList<String> params){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();

        for (String param:params) {

            Log.d(TAG, "RDH param: "  + param);
        }


        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_ADD_CARER_MESSAGE));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_CONTENTS, params.get(0)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_SEND_DATE, params.get(1)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_SENDER_ID, params.get(2)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_TITLE, params.get(3)));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_TARGET_ID, params.get(4)));




        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);

    }

    public void setIsRead(String messageID){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();



        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_SET_MESSAGE_IS_READ));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MESSAGE_ID, messageID));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_IS_READ, Integer.toString(1)));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);
    }

    public void removeMessage(String messageID){

        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
        Log.d(TAG, "params:" + messageID);

        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MOD, JsonHelper.MOD_REMOVE_MESSAGE));
        paramss.add(new BasicNameValuePair(JsonHelper.TAG_MESSAGE_ID, messageID));

        String JSONString = JsonHelper.makeHttpRequest(JsonHelper.HOSTNAME, "POST", paramss);
        Log.d(TAG, "JSONString: "  + JSONString);
    }
}
