package com.example.linukey.data.model.remote;

import android.content.Intent;

import com.example.linukey.data.model.local.User;

import java.io.Serializable;

public class WebUser implements Serializable {
	private int id;
	private String username;
	private String password;
	private String email;
	private String phonenumber;
	private String usergroup;
	private String userId;
	private String imgpath;

	public WebUser(String username, String password, String email, String phonenumber, String usergroup, String userId) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phonenumber = phonenumber;
		this.usergroup = usergroup;
		this.userId = userId;
	}

	public WebUser(User user){
		this.id = Integer.parseInt(user.getWebId());
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.phonenumber = user.getPhonenumber();
		this.usergroup = user.getUsergroup();
		this.userId = user.getUserId();
	}
	
	public WebUser(){}
	
	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
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

}
