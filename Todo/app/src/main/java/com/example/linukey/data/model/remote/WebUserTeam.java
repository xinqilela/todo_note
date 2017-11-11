package com.example.linukey.data.model.remote;

import java.io.Serializable;

public class WebUserTeam  implements Serializable {
	private int id;
	private String userId;
	private String teamId;
	
	public WebUserTeam(int id, String userId, String teamId) {
		super();
		this.id = id;
		this.userId = userId;
		this.teamId = teamId;
	}
	
	public WebUserTeam(String userId, String teamId) {
		super();
		this.userId = userId;
		this.teamId = teamId;
	}
	
	public WebUserTeam(){}

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
