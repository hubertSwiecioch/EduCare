package com.hswie.educaremobile.api.pojo;

import java.io.Serializable;

/**
 * Created by hswie on 11/5/2015.
 */
public class Family implements Serializable {

    private String fullName;
    private String residentID;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getResidentID() {
        return residentID;
    }

    public void setResidentID(String residentID) {
        this.residentID = residentID;
    }
}
