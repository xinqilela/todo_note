package com.linukey.todo.entity;

import java.io.Serializable;

public class User implements Serializable {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getUsergroup() {
		return usergroup;
	}

	public void setUsergroup(String usergroup) {
		this.usergroup = usergroup;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	private int id;
	private String username;
	private String password;
	private String email;
	private String phonenumber;
	private String usergroup;
	private String userId;
	private String imgpath;

	public User(int id, String username, String password, String email, String phonenumber, String usergroup,
			String userId) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phonenumber = phonenumber;
		this.usergroup = usergroup;
		this.userId = userId;
	}

	public User(String username, String password, String email, 
			String phonenumber, String usergroup, String userId, String imgpath) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phonenumber = phonenumber;
		this.usergroup = usergroup;
		this.userId = userId;
		this.imgpath = imgpath;
	}
	
	public User(){}

}
