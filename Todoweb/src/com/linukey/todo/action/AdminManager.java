package com.linukey.todo.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.User;
import com.opensymphony.xwork2.ActionSupport;

public class AdminManager extends ActionSupport {

	private List<User> users;
	private int pageNow = 1;
	private int pageSize = 10; 
	private String searchUser;
	private String judgeSearch;
	private List<User> resultUsers;
	private Map map = new HashMap();
	private String deleteIds;
	private String toRoots;

	public String getToRoots() {
		return toRoots;
	}

	public void setToRoots(String toRoots) {
		this.toRoots = toRoots;
	}

	public String getDeleteIds() {
		return deleteIds;
	}

	public void setDeleteIds(String deleteIds) {
		this.deleteIds = deleteIds;
	}

	public String getJudgeSearch() {
		return judgeSearch;
	}

	public void setJudgeSearch(String judgeSearch) {
		this.judgeSearch = judgeSearch;
	}
	
	public String getSearchUser() {
		return searchUser;
	}

	public void setSearchUser(String searchUser) {
		this.searchUser = searchUser;
	}

	public List<User> getResultUsers() {
		return resultUsers;
	}

	public void setResultUsers(List<User> resultUsers) {
		this.resultUsers = resultUsers;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getSearchuser() {
		return searchUser;
	}

	public void setSearchuser(String searchuser) {
		this.searchUser = searchuser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 初始化管理员后台访问
	 * 
	 * @return
	 */
	public String initAdmin() {
		getUsersByPage();
		return SUCCESS;
	}

	/**
	 * 分页获得用户信息
	 * @return
	 */
	public String getUsersByPage(){	
		if(pageNow < 1){
			pageNow = 1;
		}
		users = new UserDao().queryUserInfoByPage((pageNow-1)*pageSize, pageSize);
		if(users == null || users.size() < 1){
			pageNow--;
			users = new UserDao().queryUserInfoByPage((pageNow-1)*pageSize, pageSize);
		}
		return SUCCESS;
	}
	
	/**
	 * 通过用户名查找用户
	 * @return
	 */
	public String searchUserByName(){
		resultUsers = new UserDao().searchUserByName(searchUser);
		if(resultUsers.size() == 0)
			resultUsers = null;
		judgeSearch = "1";
		return SUCCESS;
	}
	
	/**
	 * 通过id删除用户
	 * @return
	 */
	public String deleteUserById(){
		String[] ids = deleteIds.split(",");
		for(int i = 0; i < ids.length; i++){
			User user = new User();
			user.setId(Integer.parseInt(ids[i]));
			new UserDao().deleteUserById(user);
		}  
		getUsersByPage();
		
		return SUCCESS;
	}
	
	/**
	 * 通过id提权
	 * @return
	 */
	public String toRootById(){
		String[] ids = toRoots.split(",");
		for(int i = 0; i < ids.length; i++){
			new UserDao().toRootById(Integer.parseInt(ids[i]));
		}
		getUsersByPage();
		
		return SUCCESS;
	}
}
