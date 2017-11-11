package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.Goal;

import java.io.Serializable;

public class WebGoal extends WebTaskClassify implements Serializable {

	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public WebGoal(int id, String title, String content, String state, String goalId, String userId
	, String isdelete) {
		setId(id);
		setTitle(title);
		setContent(content);
		setUserId(userId);
		setSelfId(goalId);
		this.state = state;
		setIsdelete(isdelete);
	}

	public WebGoal(Goal goal){
		setTitle(goal.getTitle());
		setContent(goal.getContent());
		setUserId(goal.getUserId());
		setSelfId(goal.getSelfId());
		this.state = goal.getState();
		setIsdelete(goal.getIsdelete());
	}

	public WebGoal() {}

}