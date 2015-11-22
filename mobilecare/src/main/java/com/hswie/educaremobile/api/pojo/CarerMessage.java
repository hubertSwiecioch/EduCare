package com.hswie.educaremobile.api.pojo;

import java.io.Serializable;

/**
 * Created by hswie on 11/22/2015.
 */
public class CarerMessage implements Serializable {

    private String id;
    private String title;
    private String message;
    private long sendDate;
    private String sentBy;
    private String sentTo;
    private boolean isRead;

    public CarerMessage() {
    }

    public CarerMessage(String title, String message, long sendDate,String sentTo, String sentBy, boolean isRead) {
        this.title = title;
        this.message = message;
        this.sendDate = sendDate;
        this.sentTo = sentTo;
        this.sentBy = sentBy;
        this.isRead = isRead;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getSendDate() {
        return sendDate;
    }

    public String getSentBy() {
        return sentBy;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public void setIsRead(int read) {
        if (read == 0)
            this.isRead = false;
        else
            this.isRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (id.equals(((CarerMessage) o).getId()))
            return true;

        return false;
    }
}
