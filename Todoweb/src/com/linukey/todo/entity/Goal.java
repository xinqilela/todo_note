package com.linukey.todo.entity;

import java.io.Serializable;

public class Goal extends TaskClassify implements Serializable {

	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Goal(int id, String title, String content, String state, String goalId, String userId, String isdelete) {
		setId(id);
		setTitle(title);
		setContent(content);
		setUserId(userId);
		setSelfId(goalId);
		this.setIsdelete(isdelete);
		this.state = state;
	}

	public Goal(String title, String content, String state, String goalId, String userId, String isdelete) {
		this.state = state;
		setTitle(title);
		setContent(content);
		setUserId(userId);
		setSelfId(goalId);
		this.setIsdelete(isdelete);
	}

	public Goal() {}

}
