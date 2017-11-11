package com.linukey.todo.entity;

import java.io.Serializable;

public class UserTeam implements Serializable {
	private int id;
	private String userId;
	private String teamId;
	private String isdelete;
	
	public UserTeam(int id, String userId, String teamId) {
		super();
		this.id = id;
		this.userId = userId;
		this.teamId = teamId;
	}
	
	public UserTeam(String userId, String teamId) {
		super();
		this.userId = userId;
		this.teamId = teamId;
	}
	
	public UserTeam(){}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}
