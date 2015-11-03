package com.hswie.educaremobile.jsonparse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class JSONParser {

	public static final String TAG = "JSONParser";
	static InputStream is = null;
	static JSONObject jObj = null;
	static JSONArray jarray = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}
	
	 public JSONArray getJSONArrayFromUrl(String url) {
		 
		 StringBuilder builder = new StringBuilder();
		 HttpClient client = new DefaultHttpClient();
         HttpGet httpGet = new HttpGet(url);
         
         try {
             HttpResponse response = client.execute(httpGet);
             StatusLine statusLine = response.getStatusLine();
             int statusCode = statusLine.getStatusCode();
             if (statusCode == 200) {
               HttpEntity entity = response.getEntity();
               InputStream content = entity.getContent();
               BufferedReader reader = new BufferedReader(new InputStreamReader(content));
               String line;
               while ((line = reader.readLine()) != null) {
                 builder.append(line);
               }
             } else {
               Log.e("==>", "Failed to download file");
             }
           } catch (ClientProtocolException e) {
             e.printStackTrace();
           } catch (IOException e) {
             e.printStackTrace();
           }
         
         // Parse String to JSON object
         try {
             jarray = new JSONArray( builder.toString());
         } catch (JSONException e) {
             Log.e("JSON Parser", "Error parsing data " + e.toString());
         }
		return jarray;
	 }
	
	public JSONObject getJSONFromUrl(String url) {
		// making HTTP Request
		try {
			// Construct the client and the HTTP request.
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			// Execute the POST request and store the response locally.
			HttpResponse httpResponse = httpClient.execute(httpPost);

			// Extract data from the response.
			HttpEntity httpEntity = httpResponse.getEntity();

			// Open an inputStream with the data content.
			is = httpEntity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Create a BufferedReader to parse through the inputStream.
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			// Declare a string builder to help with the parsing.
			StringBuilder sb = new StringBuilder();
			// Declare a string to store the JSON object data in string form.
			String line = null;

			// Build the string until null.
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			// Close the input stream.
			is.close();
			// Convert the string builder data to an actual string.
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try to parse the string to a JSON Object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error Parsing data" + e.toString());
		}

		// return JSON String
		return jObj;
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public static JSONObject makeHttpRequest(String loginUrl, String method, List<NameValuePair> params) {
		// making HTTP Request
		try {
			// check for request method
			if(method == "POST"){
				Log.d(TAG, "POST");
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(loginUrl);
				Log.d(TAG, " " + params);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				
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
				BufferedReader  reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String  line = null;
			
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (IOException e) {
				Log.d("Buffer Error","Error Converting Reesult "+e.toString());
			}
		
		// try parse the string to JSON Object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error Parsing data" + e.toString());
		}

		// return JSON String
		return jObj;
	}

	public static JSONObject getJSONfromURL(String url) {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
 
        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
 
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
 
        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
 
        try {
 
            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
 
        return jArray;
    }
}