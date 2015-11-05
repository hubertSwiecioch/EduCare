package com.hswie.educaremobile.api.pojo;

import java.io.Serializable;

/**
 * Created by hswie on 11/5/2015.
 */
public class Carer implements Serializable {

    private String fullName;
    private String onlineTest;
    private String ID;

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
}
