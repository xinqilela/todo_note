package com.linukey.todo.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.GoalDao;
import com.linukey.todo.dao.ProjectDao;
import com.linukey.todo.dao.SelfTaskDao;
import com.linukey.todo.dao.SightDao;
import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Project;
import com.linukey.todo.entity.SelfTask;
import com.linukey.todo.entity.Sight;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditSelfTask extends ActionSupport {
	
	private String client_selftask; //从Android端传来的SelfTask对象的序列化
	private String from; //判断请求是从web前端还是Android端来的
	private ClientResult clientResult; //返回给Android端的结果
	private String userId;
	private String opType; //判断web前端的请求类型（任务删除，编辑，完成）
	private int taskId; //web前端请求操作的任务ID
	private SelfTask selftask; //web前端任务编辑请求的结果 or 保存任务时前端提交的任务内容
	private List<SelfTask> selftasks; //web前端请求此用户所有的任务
	private List<Sight> listSights; //web前端请求所有情景数据
	private List<Project> listProjects; //web前端请求所有项目数据
	private String projectType; //判断请求的项目是个人项目还是团队项目
	private List<Goal> listGoals; //web前端请求所有目标数据
	
	public List<Project> getListProjects() {
		return listProjects;
	}
	public void setListProjects(List<Project> listProjects) {
		this.listProjects = listProjects;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public List<Goal> getListGoals() {
		return listGoals;
	}
	public void setListGoals(List<Goal> listGoals) {
		this.listGoals = listGoals;
	}
	public List<Sight> getListSights() {
		return listSights;
	}
	public void setListSights(List<Sight> listSights) {
		this.listSights = listSights;
	}
	@JSON(name = "ajaxValues")
	public List<SelfTask> getSelftasks() {
		return selftasks;
	}
	public void setSelftasks(List<SelfTask> selftasks) {
		this.selftasks = selftasks;
	}
	@JSON(name = "selftask")
	public SelfTask getSelftask() {
		return selftask;
	}
	public void setSelftask(SelfTask selftask) {
		this.selftask = selftask;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ClientResult getClientResult() {
		return clientResult;
	}
	public void setClientResult(ClientResult clientResult) {
		this.clientResult = clientResult;
	}
	public String getFrom() {
		return from;
	}
	public String getClient_selftask() {
		return client_selftask;
	}
	public void setClient_selftask(String client_selftask) {
		this.client_selftask = client_selftask;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	//初始化进入用户任务页面
	public String initUserTasks(){
		return SUCCESS;
	}
	
	//保存个人任务
	public String saveSelfTask() {
		SelfTaskDao sd = new SelfTaskDao();
		if (from != null && from.equals(TodoHelper.From.get("client"))) {
			clientResult = new ClientResult();
			clientResult.setResult(true);
			SelfTask sk = new Gson().fromJson(client_selftask, SelfTask.class);
			int id = sd.saveSelfTask(sk);
			if (id == -1) {
				clientResult.setResult(false);
				clientResult.setMessage("Web端任务保存失败!");
			}
			List<SelfTask> tasks = sd.getAll(userId);
			if(tasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端个人任务获取失败!");
			}
			String jsonTasks = new Gson().toJson(tasks);
			clientResult.setObject(jsonTasks);
			return "json";
		} else {
			String userId = (String)ActionContext.getContext().getSession().get("userId");
			selftask.setUserId(userId);
			selftask.setIsdelete("0");
			selftask.setState("noComplete");
			selftask.setClocktime("0:0");
			sd.saveSelfTask(selftask);
			return "json";
		}
	}

	//更新个人任务
	public String updateSelfTask() {
		SelfTaskDao sd = new SelfTaskDao();
		if (from != null && from.equals(TodoHelper.From.get("client"))) {
			clientResult = new ClientResult();
			clientResult.setResult(true);
			SelfTask sk = new Gson().fromJson(client_selftask, SelfTask.class);
			//更新任务
			if (!sd.updateSelfTask(sk)) {
				clientResult.setResult(false);
				clientResult.setMessage("web端个人任务更新失败!");
			}
			List<SelfTask> tasks = sd.getAll(userId);
			if(tasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端个人任务获取失败!");
			}
			String jsonTasks = new Gson().toJson(tasks);
			clientResult.setObject(jsonTasks);
			return "client";
		} else {
			SelfTask task = sd.getSelfTaskById(taskId);
			if(opType.equals("complete")){//完成任务时执行的更新
				task.setState("complete");
				sd.updateSelfTask(task);
			}else if(opType.equals("delete")){//删除任务时执行的更新
				task.setIsdelete("1");
				sd.updateSelfTask(task);
			}else if(opType.equals("gettask")){//获取任务时执行的更新
				selftask = task;
			}else if(opType.equals("edit")){//更新任务时执行的更新
				task.setTitle(selftask.getTitle());
				task.setContent(selftask.getContent());
				task.setStarttime(selftask.getStarttime());
				task.setEndtime(selftask.getEndtime());
				task.setProjectId(selftask.getProjectId());
				task.setGoalId(selftask.getGoalId());
				task.setSightId(selftask.getSightId());
				new SelfTaskDao().updateSelfTask(task);
			}
			return "web";
		}
	}
	
	//获取所有个人任务
	public String obtainSelfTasks(){
		SelfTaskDao sd = new SelfTaskDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<SelfTask> tasks = sd.getAll(userId);
			if(tasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端个人任务获取失败!");
			}
			String jsonTasks = new Gson().toJson(tasks);
			clientResult.setObject(jsonTasks);
			return "json";
		}
		return SUCCESS;
	}

	//获取个人任务今日待办数据
	public String obtainSelfToday(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		selftasks = new SelfTaskDao().getAll(userId);
		for(int i = 0; i < selftasks.size(); i++){
			if(!isToday(selftasks.get(i).getStarttime(), selftasks.get(i).getEndtime()) ||
					isBoxOrDeleteOrComplete(selftasks.get(i))){
				selftasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId, "self");
		List<Goal> goals = new GoalDao().getGoals(userId);
		List<Sight> sights = new SightDao().getSights(userId);
		for(SelfTask selftask : selftasks){
			for(Project project : projects){
				if(selftask.getProjectId() != null && selftask.getProjectId().equals(project.getSelfId()))
					selftask.setProjectId(project.getTitle());
			}
			for(Goal goal : goals){
				if(selftask.getGoalId() != null && selftask.getGoalId().equals(goal.getSelfId()))
					selftask.setGoalId(goal.getTitle());
			}
			for(Sight sight : sights){
				if(selftask.getSightId() != null && selftask.getSightId().equals(sight.getSelfId()))
					selftask.setSightId(sight.getTitle());
			}
		}
		System.out.println(selftasks.size() + "");
		return "json";
	}
	
	//获取个人任务明日待办数据
	public String obtainSelfTomorrow(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		selftasks = new SelfTaskDao().getAll(userId);
		for(int i = 0; i < selftasks.size(); i++){
			if(!isTomorrow(selftasks.get(i).getStarttime(), selftasks.get(i).getEndtime()) ||
					isBoxOrDeleteOrComplete(selftasks.get(i))){
				selftasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId, "self");
		List<Goal> goals = new GoalDao().getGoals(userId);
		List<Sight> sights = new SightDao().getSights(userId);
		for(SelfTask selftask : selftasks){
			for(Project project : projects){
				if(selftask.getProjectId() != null && selftask.getProjectId().equals(project.getSelfId()))
					selftask.setProjectId(project.getTitle());
			}
			for(Goal goal : goals){
				if(selftask.getGoalId() != null && selftask.getGoalId().equals(goal.getSelfId()))
					selftask.setGoalId(goal.getTitle());
			}
			for(Sight sight : sights){
				if(selftask.getSightId() != null && selftask.getSightId().equals(sight.getSelfId()))
					selftask.setSightId(sight.getTitle());
			}
		}
		return "json";
	}
	
	//获取个人任务下一步行动数据
	public String obtainSelfNext(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		selftasks = new SelfTaskDao().getAll(userId);
		for(int i = 0; i < selftasks.size(); i++){
			if(!isNext(selftasks.get(i).getStarttime()) || isBoxOrDeleteOrComplete(selftasks.get(i))){
				selftasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId, "self");
		List<Goal> goals = new GoalDao().getGoals(userId);
		List<Sight> sights = new SightDao().getSights(userId);
		for(SelfTask selftask : selftasks){
			for(Project project : projects){
				if(selftask.getProjectId() != null && selftask.getProjectId().equals(project.getSelfId()))
					selftask.setProjectId(project.getTitle());
			}
			for(Goal goal : goals){
				if(selftask.getGoalId() != null && selftask.getGoalId().equals(goal.getSelfId()))
					selftask.setGoalId(goal.getTitle());
			}
			for(Sight sight : sights){
				if(selftask.getSightId() != null && selftask.getSightId().equals(sight.getSelfId()))
					selftask.setSightId(sight.getTitle());
			}
		}
		return "json";
	}
	
	//获取个人任务备忘箱任务
	public String obtainSelfBox(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		selftasks = new SelfTaskDao().getAll(userId);
		for(int i = 0; i < selftasks.size(); i++){
			if(selftasks.get(i).getIstmp().equals("0") || selftasks.get(i).getIsdelete().equals("1")){	
				selftasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId, "self");
		List<Goal> goals = new GoalDao().getGoals(userId);
		List<Sight> sights = new SightDao().getSights(userId);
		for(SelfTask selftask : selftasks){
			for(Project project : projects){
				if(selftask.getProjectId() != null && selftask.getProjectId().equals(project.getSelfId()))
					selftask.setProjectId(project.getTitle());
			}
			for(Goal goal : goals){
				if(selftask.getGoalId() != null && selftask.getGoalId().equals(goal.getSelfId()))
					selftask.setGoalId(goal.getTitle());
			}
			for(Sight sight : sights){
				if(selftask.getSightId() != null && selftask.getSightId().equals(sight.getSelfId()))
					selftask.setSightId(sight.getTitle());
			}
		}
		return SUCCESS;
	}
	
	//判断是否是今日待办
	private boolean isToday(String dateStart, String dateEnd){
		TodoHelper helper = new TodoHelper();
		Date today = helper.getDate(0);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date comStart = null, comEnd = null;
		try {
			comStart = df.parse(dateStart);
			comEnd = df.parse(dateEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(comEnd.getTime() < today.getTime() || comStart.getTime() > today.getTime() ){
			return false;
		}
		return true;
	}
	
	//判断是否是明日待办
	private boolean isTomorrow(String dateStart, String dateEnd){
		TodoHelper helper = new TodoHelper();
		Date tomo = helper.getDate(1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date comStart = null, comEnd = null;
		try {
			comStart = df.parse(dateStart);
			comEnd = df.parse(dateEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(comEnd.getTime() < tomo.getTime() || comStart.getTime() > tomo.getTime())
			return false;
		return true;
	}
	
	//判断是否是下一步行动任务
	private boolean isNext(String date) {
		TodoHelper helper = new TodoHelper();
		Date next = helper.getDate(2);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date com = null;
		try {
			com = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (com.getTime() < next.getTime())
			return false;
		return true;
	}
	
	//判断 未完成、没删除、不是备忘箱 三个条件
	private boolean isBoxOrDeleteOrComplete(SelfTask task){
		if(task.getIsdelete().equals("1") ||
				task.getIstmp().equals("1") ||
				task.getState().equals("complete")) 
			return true;
		return false;
	}

	//添加任务时，一次性获取此用户所有的项目目标情景数据
	public String obtainPGS(){
		userId = (String)ActionContext.getContext().getSession().get("userId");
		listSights = new ArrayList<Sight>();
		listProjects = new ArrayList<Project>();
		listGoals = new ArrayList<Goal>();
		
		//获取目标数据
		List<Goal> goals = new GoalDao().getGoals(userId);
		if(goals != null)
		for(Goal goal : goals){
			if(goal.getIsdelete().equals("0") && goal.getState().equals("noComplete"))
				listGoals.add(goal);
		}
		//获取项目数据
		if(projectType.equals("self")){
			List<Project> projects = new ProjectDao().getProjects(userId, projectType);
			if(projects != null)
			for(Project project : projects){
				if(project.getIsdelete().equals("0") && project.getState().equals("noComplete"))
					listProjects.add(project);
			}
		}else if(projectType.equals("team")){
			
		}
		//获取情景数据
		List<Sight> sights = new SightDao().getSights(userId);
		if(sights != null)
		for(Sight sight : sights){
			if(sight.getIsdelete().equals("0"))
				listSights.add(sight);
		}
		return "json";
	}
}
