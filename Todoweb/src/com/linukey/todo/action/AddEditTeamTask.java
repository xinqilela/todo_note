package com.linukey.todo.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.ProjectDao;
import com.linukey.todo.dao.SelfTaskDao;
import com.linukey.todo.dao.TeamDao;
import com.linukey.todo.dao.TeamTaskDao;
import com.linukey.todo.entity.Project;
import com.linukey.todo.entity.SelfTask;
import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.TeamTask;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditTeamTask extends ActionSupport {
	private String userId;
	private String client_teamtask; //接受来自Android端的TeamTask序列化数据
	private ClientResult clientResult; //返回给Android端的结果封装
	private String from; //判断请求来自哪个端
	private List<TeamTask> teamTasks;
	private TeamTask teamtask;
	private String opType;
	private String taskId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}

	public List<TeamTask> getTeamTasks() {
		return teamTasks;
	}
	public void setTeamTasks(List<TeamTask> teamTasks) {
		this.teamTasks = teamTasks;
	}
	public TeamTask getTeamtask() {
		return teamtask;
	}
	public void setTeamtask(TeamTask teamtask) {
		this.teamtask = teamtask;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClient_teamtask() {
		return client_teamtask;
	}
	public void setClient_teamtask(String client_teamtask) {
		this.client_teamtask = client_teamtask;
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
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String saveTeamTask(){
		TeamTaskDao teamTaskDao = new TeamTaskDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamTask teamTask = new Gson().fromJson(client_teamtask, TeamTask.class);
			int id = teamTaskDao.saveTeamTask(teamTask);
			if(id == -1){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端保存团队任务失败!");
			}
			List<TeamTask> teamTasks = teamTaskDao.getTeamTasksByUserId(userId);
			if(teamTasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端获取团队任务失败");
			}
			String jsonData = new Gson().toJson(teamTasks);
			clientResult.setObject(jsonData);
			return "json";
		}else{
			teamtask.setIsdelete("0");
			teamtask.setClocktime("0:0");
			teamtask.setState("noComplete");
			System.out.println(teamtask.getProjectId() + "");
			teamTaskDao.saveTeamTask(teamtask);
			return "web";
		}
	}
	public String updateTeamTask(){
		TeamTaskDao teamTaskDao = new TeamTaskDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamTask teamTask = new Gson().fromJson(client_teamtask, TeamTask.class);
			if(!teamTaskDao.updateTeamTask(teamTask)){
				clientResult.setResult(false);
				clientResult.setMessage("服务器上更新团队任务失败!");
			}
			List<TeamTask> teamTasks = teamTaskDao.getTeamTasksByUserId(userId);
			if(teamTasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端获取团队任务失败");
			}
			String jsonData = new Gson().toJson(teamTasks);
			clientResult.setObject(jsonData);
			return "json";
		}else{
			TeamTask task = teamTaskDao.getTeamTaskByTaskId(taskId);
			if(opType.equals("gettask")){
				teamtask = task;
			}else if(opType.equals("edit")){
				task.setTitle(teamtask.getTitle());
				task.setContent(teamtask.getContent());
				task.setStarttime(teamtask.getStarttime());
				task.setEndtime(teamtask.getEndtime());
				task.setProjectId(teamtask.getProjectId());
				task.setTeamId(teamtask.getTeamId());
				teamTaskDao.saveTeamTask(task);
			}else if(opType.equals("complete")){
				task.setState("complete");
				teamTaskDao.saveTeamTask(task);
			}else if(opType.equals("delete")){
				task.setIsdelete("1");
				teamTaskDao.saveTeamTask(task);
			}
			return "web";
		}
	}
	public String obtainTeamTasks(){
		TeamTaskDao teamTaskDao = new TeamTaskDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<TeamTask> teamTasks = teamTaskDao.getTeamTasksByUserId(userId);
			if(teamTasks == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端获取团队任务失败");
			}
			String jsonData = new Gson().toJson(teamTasks);
			clientResult.setObject(jsonData);
			return "json";
		}else{
			return "web";
		}
	}
	public String obtainTeamToday(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		teamTasks = new TeamTaskDao().getTeamTasksByUserId(userId);
		for(int i = 0; i < teamTasks.size(); i++){
			if(!isToday(teamTasks.get(i).getStarttime(), teamTasks.get(i).getEndtime()) ||
					isDeleteOrComplete(teamTasks.get(i))){
				teamTasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId);
		List<Team> teams = new TeamDao().getTeams(userId);
		for(TeamTask teamtask : teamTasks){
			for(Project project : projects){
				if(teamtask.getProjectId().equals(project.getSelfId()))
					teamtask.setProjectId(project.getTitle());
			}
			for(Team team : teams){
				if(teamtask.getTeamId().equals(team.getTeamId()))
					teamtask.setTeamId(team.getTeamname());
			}
		}
		System.out.println(teamTasks.size() + "");
		return "json";
	}
	
	public String obtainTeamTomorrow(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		teamTasks = new TeamTaskDao().getTeamTasksByUserId(userId);
		for(int i = 0; i < teamTasks.size(); i++){
			if(!isTomorrow(teamTasks.get(i).getStarttime(), teamTasks.get(i).getEndtime()) ||
					isDeleteOrComplete(teamTasks.get(i))){
				teamTasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId);
		List<Team> teams = new TeamDao().getTeams(userId);
		for(TeamTask teamtask : teamTasks){
			for(Project project : projects){
				if(teamtask.getProjectId().equals(project.getSelfId()))
					teamtask.setProjectId(project.getTitle());
			}
			for(Team team : teams){
				if(teamtask.getTeamId().equals(team.getTeamId()))
					teamtask.setTeamId(team.getTeamname());
			}
		}
		return "json";
	}
	
	public String obtainTeamNext(){
		String userId = (String)ActionContext.getContext().getSession().get("userId");
		teamTasks = new TeamTaskDao().getTeamTasksByUserId(userId);
		for(int i = 0; i < teamTasks.size(); i++){
			if(!isNext(teamTasks.get(i).getStarttime()) || isDeleteOrComplete(teamTasks.get(i))){
				teamTasks.remove(i);
				i--;
			}
		}
		List<Project> projects = new ProjectDao().getProjects(userId);
		List<Team> teams = new TeamDao().getTeams(userId);
		for(TeamTask teamtask : teamTasks){
			for(Project project : projects){
				if(teamtask.getProjectId().equals(project.getSelfId()))
					teamtask.setProjectId(project.getTitle());
			}
			for(Team team : teams){
				if(teamtask.getTeamId().equals(team.getTeamId()))
					teamtask.setTeamId(team.getTeamname());
			}
		}
		return "json";
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
	private boolean isDeleteOrComplete(TeamTask task){
		if(task.getIsdelete().equals("1") ||
				task.getState().equals("complete")) 
			return true;
		return false;
	}
}
