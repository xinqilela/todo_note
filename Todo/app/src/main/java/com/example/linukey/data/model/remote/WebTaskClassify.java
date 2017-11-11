package com.example.linukey.data.model.remote;

import java.io.Serializable;

public abstract class WebTaskClassify implements Serializable {
	int id;
    private String title;
    private String content;
    private String selfId;
    private String userId;
    private String isdelete;

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "WebTaskClassify{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", selfId='" + selfId + '\'' +
                ", userId='" + userId + '\'' +
                ", isdelete='" + isdelete + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
