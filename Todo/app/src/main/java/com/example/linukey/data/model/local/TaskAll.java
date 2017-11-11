package com.example.linukey.data.model.local;

/**
 * Created by linukey on 17-3-9.
 */

public class TaskAll {
    private String title;
    private String starttime;
    private String endtime;
    private String selfId;
    private String type;

    public TaskAll(String title, String starttime, String endtime, String selfId, String type){
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
        this.selfId = selfId;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
