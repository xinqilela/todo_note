package com.linukey.todo.action;

import java.util.List;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.TeamDao;
import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.SelfTask;
import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.User;
import com.linukey.todo.entity.UserTeam;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditTeam extends ActionSupport {
	private String from; //判断请求是来自web前端还是Android端
	private String client_team; // Android端发来的team对象的序列化
	private ClientResult clientResult; //对Android端请求结果的封装
	private String userId; //通过userId查询满足条件的所有team
	private String teamName; //通过teamName查询满足条件的所有team
	private List<Team> teams; //小组
	private String opType;
	private String teamId;
	private Team team;
	
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
	public String getClient_team() {
		return client_team;
	}
	public void setClient_team(String client_team) {
		this.client_team = client_team;
	}
	public ClientResult getClientResult() {
		return clientResult;
	}
	public void setClientResult(ClientResult clientResult) {
		this.clientResult = clientResult;
	}
	
	//保存小组数据
	public String saveTeam(){
		System.out.println("保存团队信息");
		TeamDao teamDao = new TeamDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			
			Team team = new Gson().fromJson(client_team, Team.class);
			UserTeam userTeam = new UserTeam();
			userTeam.setTeamId(team.getTeamId());
			userTeam.setUserId(team.getLeaderId());
			userTeam.setIsdelete("0");
			//保存小组信息
			int id = teamDao.saveTeam(team);
			//把队长的信息保存到UserTeam表里面
			int id1 = teamDao.saveUserTeam(userTeam);
			if(id == -1 || id1 == -1){
				clientResult.setResult(false);
				clientResult.setMessage("Web端团队信息保存失败！");
			}
			//获取最新状态，刷新客户端
			List<Team> teams = teamDao.getTeams(userId);
			if(teams == null){
				clientResult.setResult(false);
				clientResult.setMessage("查无此用户的小组信息!");
			}
			String jsonTeams = new Gson().toJson(teams);
			clientResult.setObject(jsonTeams);
			return "json";
		}else{
			
		}
		return SUCCESS;
	}
	
	public String updateTeam(){
		TeamDao teamDao = new TeamDao();
		if(from != null && from.equals("client")){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			team = (Team)new Gson().fromJson(client_team, Team.class);
			if(opType.equals("edit")){
				if(!teamDao.updateTeam(team)){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端更改小组信息时出现错误!");
				}
			}else if(opType.equals("delete")){
				if(!teamDao.deleteTeam(team.getTeamId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端删除小组的时候出现错误");
				}
			}
			List<Team> teams = teamDao.getTeams(userId);
			if(teams == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端更改小组信息时出现错误!");
			}
			String jsonTeams = new Gson().toJson(teams);
			clientResult.setObject(jsonTeams);
			return "json";
		}else{
			Team t = teamDao.getTeamByTeamId(teamId);
			if(opType.equals("getTeam")){
				team = t;
			}else if(opType.equals("delete")){
				t.setIsdelete("1");
				teamDao.updateTeam(t);
			}else if(opType.equals("edit")){
				t.setTeamname(team.getTeamname());
				t.setContent(team.getContent());
				teamDao.updateTeam(t);
			}
			return "web";
		}
	}
	
	//获取小组数据
	public String obtainTeams(){
		TeamDao teamDao = new TeamDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<Team> teams = teamDao.getTeams(userId);
			if(teams == null){
				clientResult.setResult(false);
				clientResult.setMessage("查无此用户的小组信息!");
			}
			String jsonTeams = new Gson().toJson(teams);
			clientResult.setObject(jsonTeams);
			return "json";
		}else{
			UserDao userDao = new UserDao();
			String userId = (String)ActionContext.getContext().getSession().get("userId");
			teams = teamDao.getTeams(userId);
			for(int i = 0; i < teams.size(); i++){
				if(teams.get(i).getIsdelete().equals("1")){
					teams.remove(i--);
				}
			}
			for(Team team : teams){
				team.setLeaderId(userDao.getUserNameByUserId(team.getLeaderId()));
			}
			return "web";
		}
	}
	
	//通过用户名查找所有满足条件的小组信息
	public String obtainTeamsByTeamName(){
		TeamDao teamDao = new TeamDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<Team> teams = teamDao.getTeamsByTeamName(teamName, userId);
			if(teams == null || teams.size() < 1){
				clientResult.setResult(false);
				clientResult.setMessage("查无此小组信息!");
			}
			String jsonTeams = new Gson().toJson(teams);
			clientResult.setObject(jsonTeams);
			return "json";
		}else{
			
		}
		return SUCCESS;
	}
}


















