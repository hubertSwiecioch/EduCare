package com.hswie.educaremobile.api.pojo;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hswie on 11/5/2015.
 */
public class Carer implements Serializable {

    private static final String TAG = "Carer";

    private String fullName;
    private String onlineTest;
    private String ID;
    private String photo;
    private String photoCache;
    private String phoneNumber;
    private byte[] photoByte;
    private ArrayList<CarerMessage> carerMessages;

    private ArrayList<CarerTask> carerTasks;


    public boolean contains(String query) {
        String[] queries = query.split(" ");

        if (queries.length == 2) {
            if ((fullName.toLowerCase().contains(queries[0].toLowerCase()))
                    || (fullName.toLowerCase().contains(queries[1].toLowerCase()))) {
                //Log.d(TAG, "TRUE. firstname = " + firstname + " lastname = " + lastname);
                return true;
            }
        } else if (queries.length == 1) {
            if (fullName.toLowerCase().contains(queries[0].toLowerCase())) {
                //Log.d(TAG, "TRUE. firstname = " + firstname + " lastname = " + lastname);
                return true;
            }
        }

        return false;
    }

    public ArrayList<CarerMessage> getCarerMessages() {
        return carerMessages;
    }

    public void setCarerMessages(ArrayList<CarerMessage> carerMessages) {
        this.carerMessages = carerMessages;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOnlineTest() {
        return onlineTest;
    }

    public void setOnlineTest(String onlineTest) {
        this.onlineTest = onlineTest;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public byte[] getPhotoByte() {
        return photoByte;
    }

    public void setPhotoByte(byte[] photoByte) {
        this.photoByte = photoByte;
    }

    public String getPhotoCache() {
        return photoCache;
    }

    public void setPhotoCache(String photoCache) {
        Log.d(TAG, "photoCache = " + photoCache);
        this.photoCache = photoCache;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public ArrayList<CarerTask> getCarerTasks() {
        return carerTasks;
    }

    public void setCarerTasks(ArrayList<CarerTask> carerTasks) {
        this.carerTasks = carerTasks;
    }
}
