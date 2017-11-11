package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.TeamJoinInfo;

/**
 * Created by linukey on 17-2-28.
 */

public class WebTeamJoinInfo {
    public WebTeamJoinInfo(TeamJoinInfo teamJoinInfo){
        this.teamId = teamJoinInfo.getTeamId();
        this.fromUserName = teamJoinInfo.getFromUserName();
        this.toUserName = teamJoinInfo.getToUserName();
        this.isdelete = teamJoinInfo.getIsdelete();
        this.execType = teamJoinInfo.getExecType();
        this.teamName = teamJoinInfo.getTeamName();
        this.content = teamJoinInfo.getContent();
        this.fromUserId = teamJoinInfo.getFromUserId();
        this.toUserId = teamJoinInfo.getToUserId();
        this.selfId = teamJoinInfo.getSelfId();
    }

    public WebTeamJoinInfo(){}

    private String teamId; //目标小组Id
    private String fromUserName; //申请人用户名
    private String toUserName; //小组组长用户名
    private String isdelete; //是否删除
    private String execType; //操作类型
    private String teamName; //小组名
    private String content; //申请信息
    private String fromUserId;
    private String toUserId;
    private String selfId;

    public String getSelfId() {
        return selfId;
    }

    public void setSelfId(String selfId) {
        this.selfId = selfId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }


    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}
