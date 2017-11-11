package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.Team;

import java.io.Serializable;

public class WebTeam  implements Serializable {
	private int id;
	private String teamname;
	private String content;
	private String leaderId;
	private String teamId;
	private String isdelete;
	private String leaderName;

	public WebTeam(Team team){
		this.teamname = team.getTeamname();
		this.content = team.getContent();
		this.leaderId = team.getLeaderId();
		this.teamId = team.getTeamId();
		this.isdelete = team.getIsdelete();
		this.leaderName = team.getLeaderName();
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public WebTeam(){}

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
