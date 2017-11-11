package com.linukey.todo.entity;

import java.io.Serializable;

public class Team implements Serializable {
	private int id;
	private String teamname;
	private String content;
	private String leaderId;
	private String teamId;
	private String isdelete;
	private String leaderName;

	
	
	public Team(int id, String teamname, String content, String leaderId, 
			String teamId, String isdelete, String leaderName) {
		super();
		this.id = id;
		this.teamname = teamname;
		this.content = content;
		this.leaderId = leaderId;
		this.teamId = teamId;
		this.isdelete = isdelete;
		this.leaderName = leaderName;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public Team(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}
