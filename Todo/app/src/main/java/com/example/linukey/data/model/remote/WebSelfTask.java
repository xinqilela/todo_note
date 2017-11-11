package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.SelfTask;

import java.io.Serializable;

public class WebSelfTask implements Serializable {
	private int id;
	private String title;
	private String content;
	private String starttime;
	private String endtime;
	private String clocktime;
	private String projectId;
	private String goalId;
	private String sightId;
	private String userId;
	private String state;
	private String isdelete;
	private String istmp;
	private String selfId;

	public int getId() {
		return id;
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
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

	public String getClocktime() {
		return clocktime;
	}

	public void setClocktime(String clocktime) {
		this.clocktime = clocktime;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getGoalId() {
		return goalId;
	}

	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}

	public String getSightId() {
		return sightId;
	}

	public void setSightId(String sightId) {
		this.sightId = sightId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getIstmp() {
		return istmp;
	}

	public void setIstmp(String istmp) {
		this.istmp = istmp;
	}

	public WebSelfTask(SelfTask selfTask){
		this.title = selfTask.getTitle();
		this.content = selfTask.getContent();
		this.starttime = selfTask.getStarttime();
		this.endtime = selfTask.getEndtime();
		this.clocktime = selfTask.getClocktime();
		this.projectId = selfTask.getProjectId();
		this.goalId = selfTask.getGoalId();
		this.sightId = selfTask.getSightId();
		this.userId = selfTask.getUserId();
		this.state = selfTask.getState();
		this.isdelete = selfTask.getIsdelete();
		this.istmp = selfTask.getIstmp();
		this.selfId = selfTask.getSelfId();
	}

	public WebSelfTask() {}


}
