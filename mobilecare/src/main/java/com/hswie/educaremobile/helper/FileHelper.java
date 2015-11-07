package com.hswie.educaremobile.helper;


import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by hswie on 11/7/2015.
 */
public class FileHelper {

    private static final String TAG ="FileHelper";


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

    public void uploadFile(File file) {


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
}
