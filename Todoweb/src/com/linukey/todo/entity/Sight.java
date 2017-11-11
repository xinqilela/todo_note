package com.linukey.todo.entity;

import java.io.Serializable;

public class Sight extends TaskClassify implements Serializable {
	public Sight(int id, String title, String content, String sightId, String userId, String isdelete) {
		setId(id);
		setTitle(title);
		setContent(content);
		setUserId(userId);
		setSelfId(sightId);
		this.setIsdelete(isdelete);
	}

	public Sight(String title, String content, String sightId, String userId, String isdelete) {
		setTitle(title);
		setContent(content);
		setUserId(userId);
		setSelfId(sightId);
		this.setIsdelete(isdelete);
	}

	public Sight() {}
	
}
