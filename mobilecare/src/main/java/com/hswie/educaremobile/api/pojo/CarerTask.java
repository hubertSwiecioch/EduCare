package com.hswie.educaremobile.api.pojo;

import java.io.Serializable;

/**
 * Created by hswie on 11/12/2015.
 */
public class CarerTask implements Serializable {

    private String id;
    private String header;
    private String description;
    private long date;
    private String targetCarerID;
    private String targetResidentID;
    private boolean is_done;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTargetCarerID() {
        return targetCarerID;
    }

    public void setTargetCarerID(String targetCarerID) {
        this.targetCarerID = targetCarerID;
    }

    public String getTargetResidentID() {
        return targetResidentID;
    }

    public void setTargetResidentID(String targetResidentID) {
        this.targetResidentID = targetResidentID;
    }

    public boolean getIs_done() {
        return is_done;
    }

    public void setIs_done(boolean is_confirmed) {
        this.is_done = is_confirmed;
    }
}
