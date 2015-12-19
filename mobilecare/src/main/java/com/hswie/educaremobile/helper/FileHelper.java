package com.hswie.educaremobile.helper;


import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * Created by hswie on 11/7/2015.
 */
public class FileHelper {

    private static final String TAG ="FileHelper";
    public static final int RESIDENT_CACHE = 1;
    public static final int CARER_CACHE = 2;


    private HttpURLConnection connection = null;
    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;

    String boundary =  "*****";
    String twoHyphens = "--";
    String lineEnd = "\r\n";

    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;
    private int serverResponseCode;
    private String serverResponseMessage;

    public void uploadFile(File file, String userType) {


        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(JsonHelper.HOSTNAME);
            connection = (HttpURLConnection) url.openConnection();


            // Allow Inputs &amp; Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("userType", userType);
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            outputStream = new DataOutputStream( connection.getOutputStream() );


            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + file.getAbsolutePath() +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = connection.getResponseCode();
            serverResponseMessage = connection.getResponseMessage();
            Log.d(TAG, serverResponseMessage);

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            Log.d(TAG, "UploadFileSuccessfull");
        }
        catch (Exception ex)
        {
            //Exception handling
        }
    }


    public static boolean checkPhotoCache(Context context, String personID, int personType){

        String filePath= "";

        if (personType == RESIDENT_CACHE) {
            filePath = context.getFilesDir() + "/resident_" + personID + ".jpg";
        }
        else if (personType == CARER_CACHE) {
            filePath = context.getFilesDir() + "/carer_" + personID + ".jpg";
        }

        File file = new File(filePath);
        if(file.exists())
            return true;
        else
            return false;
    }

}
