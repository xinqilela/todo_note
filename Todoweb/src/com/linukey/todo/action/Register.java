package com.linukey.todo.action;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.User;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionSupport;

public class Register extends ActionSupport {

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

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getClient_user() {
		return client_user;
	}

	public void setClient_user(String client_user) {
		this.client_user = client_user;
	}

	public ClientResult getClientResult() {
		return clientResult;
	}

	public void setClientResult(ClientResult clientResult) {
		this.clientResult = clientResult;
	}

	private String username;
	private String userId;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private String password;
	private String repassword;
	private String email;
	private String phonenumber;
	private String from;
	private String client_user;
	private ClientResult clientResult;

	public String registAction() {
		if (from != null && from.equals(TodoHelper.From.get("client"))) {
			clientResult = new ClientResult();
			clientResult.setResult(true);
			User user = new Gson().fromJson(client_user, User.class);
			int id = new UserDao().saveUser(user.getUsername(), user.getPassword(), user.getEmail(),
					user.getPhonenumber());
			if (id == -2) {
				clientResult.setResult(false);
				clientResult.setMessage("用户名已存在!");
			}else if(id == -1){
				clientResult.setResult(false);
				clientResult.setMessage("web端保存用户信息失败!");
			}
			clientResult.setObject(id);
			return "json";
		} else {
			int id = new UserDao().saveUser(username, password, email, phonenumber);
			if (id == -2 || id == -1) {
				addFieldError("user", "用户名已存在!");
				return INPUT;
			}
		}
		return SUCCESS;
	}
	
	//通过用户Id来获取用户名
	public String getUserNameByUserID(){
		if(from != null && from.equals(new TodoHelper().From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			String username = new UserDao().getUserNameByUserId(userId);
			if(username == null){
				clientResult.setResult(false);
				clientResult.setMessage("没有查找到此用户!");
			}
			clientResult.setObject(username);
			return "json";
		}
		return SUCCESS;
	}

	@Override
	public void validate() {
		if (from == null) {
			if (username.trim().isEmpty())
				addFieldError("username", "用户名不能为空!");
			else if (password.trim().isEmpty())
				addFieldError("password", "密码不能为空!");
			else if (repassword.trim().isEmpty())
				addFieldError("repassword", "请再次输入密码!");
			else if (!repassword.equals(password))
				addFieldError("password", "两次输入密码不一致!");
			else if (email.trim().isEmpty())
				addFieldError("email", "请输入邮箱!");
			else if (phonenumber.trim().isEmpty())
				addFieldError("phonenumber", "请输入手机号码!");
		}
	}
}
