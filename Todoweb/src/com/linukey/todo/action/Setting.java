package com.linukey.todo.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.User;
import com.linukey.todo.util.Document;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Setting extends ActionSupport {
	private String title;
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String from;
	private String client_user;
	private String userId;
	private ClientResult clientResult = null;

	private String username;
	private String email;
	private String phonenumber;

	private Map map = new HashMap();

	@JSON(name = "ajaxValues")
	public Map getMap() {
		return map;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public void setMap(Map map) {
		this.map = map;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	/**
	 * 获得当前登录的用户信息
	 * 
	 * @return
	 */
	public String getUserinfo() {
		User user = new UserDao().getUserInfo();
		if (user != null) {
			username = user.getUsername();
			email = user.getEmail();
			phonenumber = user.getPhonenumber();
			return SUCCESS;
		}

		return INPUT;
	}

	/**
	 * 更新用户个人信息
	 * 
	 * @return
	 */
	public String updateUserInfo() {
		if (from != null && from.equals(TodoHelper.From.get("client"))) {
			clientResult = new ClientResult();
			clientResult.setResult(true);
			User user = new Gson().fromJson(client_user, User.class);
			int num = new UserDao().updateUserInfo(user);
			if(num == -1){
				clientResult.setResult(false);
				clientResult.setMessage("web端修改用户信息失败!");
			}else if(num == -2){
				clientResult.setResult(false);
				clientResult.setMessage("用户名已存在!");
			}
			return "json";
		} else {
			User user = new UserDao().getUserInfo();
			if (user != null) {
				user.setUsername(username);
				user.setEmail(email);
				user.setPhonenumber(phonenumber);
				int num = new UserDao().updateUserInfo(user);
				if (num == -1) {
					return INPUT;
				}else if(num == -2){
					return INPUT;
				}
				map.put("alter", username);
				return SUCCESS;
			}
		}
		return INPUT;
	}

	public String obtainUserLogoUrl(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			String imagePath = new UserDao().getUserLogoUrlByUserId(userId);
			clientResult = new ClientResult();
			clientResult.setResult(true);
			clientResult.setObject(imagePath);
			return "json";
		}
		return "json";
	}
	
	/**
	 * 上传用户头像
	 * 
	 * @return
	 */
	public String upLoadImg() {
		//如果是web前端传来的
		if(userId == null && username == null){
			userId = (String)ActionContext.getContext().getSession().get("userId");
			username = new UserDao().getUsername();
		}
		String[] types = uploadContentType.split("/");
		String newName = username + "_" + "Logo";
		String goalPath = ServletActionContext.getRequest().getRealPath("/img") + "//" + newName + "." + types[1];
		String savePath = "/Todoweb/img/" + newName + "." + types[1];
	
		try {
			FileOutputStream fos = new FileOutputStream(goalPath);
			FileInputStream fis = new FileInputStream(getUpload());
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setUploadFileName(newName);
		new UserDao().setUserImgPath(userId, savePath);
		ActionContext.getContext().getSession().put("userlogo", savePath);
		map.put("userlogo", savePath);

		return "web";
	}

	/**
	 * 验证更新用户信息输入
	 */
	public void validateupdateUserInfo() {
		if (username.trim().isEmpty())
			addFieldError("username", "用户名不能为空!");
		else if (email.trim().isEmpty())
			addFieldError("email", "邮箱不能为空!");
		else if (phonenumber.trim().isEmpty())
			addFieldError("phonenumber", "手机号码不能为空!");
	}

}
