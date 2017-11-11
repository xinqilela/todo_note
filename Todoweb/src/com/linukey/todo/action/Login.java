package com.linukey.todo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.*;
import com.linukey.todo.util.HibernateSessionFactory;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionSupport;

public class Login extends ActionSupport {
	private String username;
	private String password;
	private String usergroup;
	private ClientResult clientResult;
	private String from;
	private String result = null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public ClientResult getClientResult() {
		return clientResult;
	}

	public void setClientResult(ClientResult clientResult) {
		this.clientResult = clientResult;
	}

	public String getUsergroup() {
		return usergroup;
	}

	public void setUsergroup(String usergroup) {
		this.usergroup = usergroup;
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

	/**
	 * 登录
	 * 
	 * @return
	 * @throws ScriptException 
	 */
	public String loginAction() throws ScriptException {
		if (from != null && from.equals(TodoHelper.From.get("client"))) {
			clientResult = new ClientResult();
			clientResult.setResult(true);
			username = new Gson().fromJson(username, String.class);
			password = new Gson().fromJson(password, String.class);
			String usergroup = "normal";
			if (!new UserDao().checkinput(username, password, usergroup)) {
				clientResult.setResult(false);
				clientResult.setMessage("用户名或密码错误");
			}
			User user = null;
			List<User> users = new UserDao().searchUserByName(username);
			if(users.size() > 0){
				user = users.get(0);
			}
			result = new Gson().toJson(clientResult);
			result += "***";
			result += new Gson().toJson(user);
			return "json";
		} else {
			if (usergroup != null && usergroup.equals("普通用户")) {
				usergroup = TodoHelper.UserGroup.get("normal");
			} else if (usergroup != null && usergroup.equals("管理员")) {
				usergroup = TodoHelper.UserGroup.get("root");
			}
			if (!new UserDao().checkinput(username, password, usergroup)) {
				addFieldError("username", "用户名或密码错误!");
				return INPUT;
			}
		}
		
		return SUCCESS;
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	public String logout() {
		new UserDao().logout();
		return SUCCESS;
	}

	public void validateLoginAction() {
		if (username.trim().isEmpty())
			addFieldError("username", "用户名不能为空!");
		else if (password.trim().isEmpty())
			addFieldError("password", "密码不能为空!");
	}
}
