package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.TeamTask;

import java.io.Serializable;

public class WebTeamTask implements Serializable {
	private int id;
	private String title;
	private String content;
	private String starttime;
	private String endtime;
	private String clocktime;
	private String projectId;
	private String state;
	private String isdelete;
	private String teamId;
	private String selfId;

	public WebTeamTask(TeamTask teamTask){
		this.title = teamTask.getTitle();
		this.content = teamTask.getContent();
		this.starttime = teamTask.getStarttime();
		this.endtime = teamTask.getEndtime();
		this.clocktime = teamTask.getClocktime();
		this.projectId = teamTask.getProjectId();
		this.state = teamTask.getState();
		this.isdelete = teamTask.getIsdelete();
		this.teamId = teamTask.getTeamId();
		this.selfId = teamTask.getSelfId();
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	public int getId() {
		return id;
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

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	
	public WebTeamTask(){}

}
