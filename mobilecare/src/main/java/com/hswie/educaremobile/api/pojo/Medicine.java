package com.hswie.educaremobile.api.pojo;

import java.io.Serializable;

/**
 * Created by hswie on 11/22/2015.
 */
public class Medicine implements Serializable {

    private String id;
    private String name;
    private String dose;
    private String residentID;
    private String carerID;
    private long startDate;
    private long endDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getResidentID() {
        return residentID;
    }

    public void setResidentID(String residentID) {
        this.residentID = residentID;
    }

    public String getCarerID() {
        return carerID;
    }

    public void setCarerID(String carerID) {
        this.carerID = carerID;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
