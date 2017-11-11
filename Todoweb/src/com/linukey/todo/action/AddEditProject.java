package com.linukey.todo.action;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.struts2.json.annotations.JSON;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.ProjectDao;
import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Project;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditProject extends ActionSupport {
	private String from; //判断请求来自web客户端还是app
	private String client_project; //app端保存项目的时候，对Project对象的序列化
	private ClientResult clientResult = null; //对Android端结果的封装
	private String userId;
	private List<Project> listProjects; //web前端请求所有项目数据
	private String projectType; //判断请求的项目是个人项目还是团队项目
	private Project project; //web前端保存的项目
	private String opType; //操作类型
	private String projectId;
	private String id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public List<Project> getListProjects() {
		return listProjects;
	}
	public void setListProjects(List<Project> listProjects) {
		this.listProjects = listProjects;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getClient_project() {
		return client_project;
	}
	public void setClient_project(String client_project) {
		this.client_project = client_project;
	}
	public ClientResult getClientResult() {
		return clientResult;
	}
	public void setClientResult(ClientResult clientResult) {
		this.clientResult = clientResult;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 保存项目
	 */
	public String saveProject(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			Project project = new Gson().fromJson(client_project, Project.class);
			int id = new ProjectDao().saveProject(project);
			if(id == -1){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端保存失败!");
			}
			//获取最新的状态，刷新客户端
			List<Project> projects = new ProjectDao().getProjects(userId);
			if(projects == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonProjects = new Gson().toJson(projects);
			clientResult.setObject(jsonProjects);
			return "json";
		}else{
			String userId = (String)ActionContext.getContext().getSession().get("userId");
			project.setUserId(userId);
			project.setSelfId(UUID.randomUUID().toString());
			project.setState("noComplete");
			project.setType(opType);
			project.setIsdelete("0");
			new ProjectDao().saveProject(project);
			return "json";
		}
	}
	
	/**
	 * 更新项目
	 * @return
	 */
	public String updateProject(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			ProjectDao projectDao = new ProjectDao();
			Project p = new Gson().fromJson(client_project, Project.class);
			System.out.println(p.getTitle());
			if(opType.equals("edit")){
				if(!projectDao.updateProject(p)){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端项目更新失败!");
				}
			}else if(opType.equals("delete")){
				if(!projectDao.deleteProject(p.getSelfId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端项目删除失败!");
				}
			}else if(opType.equals("complete")){
				if(!projectDao.completeProject(p.getSelfId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端项目完成失败!");
				}
			}
			//获取最新的状态，刷新客户端
			List<Project> projects = new ProjectDao().getProjects(userId);
			if(projects == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonProjects = new Gson().toJson(projects);
			clientResult.setObject(jsonProjects);
			return "json";
		}else{
			ProjectDao projectDao = new ProjectDao();
			Project p = projectDao.getProjectByProjectId(projectId);
			if(opType.equals("edit")){	
				p.setTitle(project.getTitle());
				p.setContent(project.getContent());
				projectDao.updateProject(p);
			}else if(opType.equals("getProject")){
				project = p;
			}else if(opType.equals("complete")){
				projectDao.completeProject(projectId);
			}else if(opType.equals("delete")){
				projectDao.deleteProject(projectId);
			}
			return "web";
		}
	}
	
	/**
	 * 获取所有项目
	 * @return
	 */
	public String obtainProjects(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<Project> projects = new ProjectDao().getProjects(userId);
			if(projects == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonProjects = new Gson().toJson(projects);
			clientResult.setObject(jsonProjects);
			return "json";
		}else{
			userId = (String)ActionContext.getContext().getSession().get("userId");
			listProjects = new ArrayList<Project>();
			if(projectType.equals("self")){
				List<Project> projects = new ProjectDao().getProjects(userId, projectType);
				if(projects != null){
					for(Project project : projects){
						if(project.getIsdelete().equals("0") && project.getState().equals("noComplete"))
							listProjects.add(project);
					}
				}
				return "web";
			}else{
				List<Project> projects = new ProjectDao().getProjects(userId);
				if(projects != null){
					for(Project project : projects){
						if(project.getIsdelete().equals("0") && project.getState().equals("noComplete"))
							listProjects.add(project);
					}
				}
				return "web";
			}
		}
	}
}
