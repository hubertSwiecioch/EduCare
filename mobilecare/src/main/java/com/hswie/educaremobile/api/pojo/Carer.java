package com.hswie.educaremobile.api.pojo;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by hswie on 11/5/2015.
 */
public class Carer implements Serializable {

    private String fullName;
    private String onlineTest;
    private String ID;
    private String photo;
    private String photoCache;
    private byte[] photoByte;
    private String TAG = "Carer";


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
}
