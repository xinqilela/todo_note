package com.example.linukey.data.model.remote;

import com.example.linukey.data.model.local.Project;

import java.io.Serializable;

public class WebProject extends WebTaskClassify implements Serializable {
	
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

    public WebProject(Project project){
        this.setTitle(project.getTitle());
        this.setContent(project.getContent());
        this.setUserId(project.getUserId());
        this.setSelfId(project.getSelfId());
        this.setType(project.getType());
        this.setState(project.getState());
		this.setIsdelete(project.getIsdelete());
    }

    public WebProject(){ }

	@Override
	public String toString() {
		return super.toString() + "WebProject{" +
				"state='" + state + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
