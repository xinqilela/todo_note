package com.linukey.todo.entity;

import java.io.Serializable;

public class Project extends TaskClassify implements Serializable {
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String state;
    private String type;

    public Project(int id, String title, String content, String state, String projectId,
                   String userId, String type, String isdelete) {
        this.state = state;
        setId(id);
        setTitle(title);
        setContent(content);
        setUserId(userId);
        setSelfId(projectId);
        this.type = type;
        this.setIsdelete(isdelete);
    }

    public Project(String title, String content, String state, String projectId,
                   String userId, String type, String isdelete) {
        this.state = state;
        this.type = type;
        setTitle(title);
        setContent(content);
        setUserId(userId);
        setSelfId(projectId);
        this.setIsdelete(isdelete);
    }

    public Project(){ }
}
