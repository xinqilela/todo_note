package com.linukey.todo.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.struts2.json.annotations.JSON;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.GoalDao;
import com.linukey.todo.dao.ProjectDao;
import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Project;
import com.linukey.todo.entity.TaskClassify;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditGoal extends ActionSupport {
	private String from; //判断是app请求还是web前段
	private String client_goal; //app端保存goal时序列号的goal对象
	private ClientResult clientResult = null; //返回app端的结果对象
	private String userId;
	private List<Goal> listGoals; //web前端请求所有目标数据
	private Goal goal; //web前端要保持的goal
	private String goalId;
	private String opType;
	
	public String getGoalId() {
		return goalId;
	}
	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	public List<Goal> getListGoals() {
		return listGoals;
	}
	public void setListGoals(List<Goal> listGoals) {
		this.listGoals = listGoals;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getClient_goal() {
		return client_goal;
	}
	public void setClient_goal(String client_goal) {
		this.client_goal = client_goal;
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

	//保存目标
	public String saveGoal(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			Goal goal = new Gson().fromJson(client_goal, Goal.class);
			int id = new GoalDao().saveGoal(goal);
			if(id == -1){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端保存失败!");
			}
			List<Goal> goals = new GoalDao().getGoals(userId);
			if(goals == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonGoals = new Gson().toJson(goals);
			clientResult.setObject(jsonGoals);
			return "json";
		}else{
			goal.setSelfId(UUID.randomUUID().toString());
			String userId = (String)ActionContext.getContext().getSession().get("userId");
			goal.setUserId(userId);
			goal.setIsdelete("0");
			goal.setState("noComplete");
			new GoalDao().saveGoal(goal);
			return "json";
		}
	}
	
	//更新目标
	public String updateGoal(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			GoalDao goalDao = new GoalDao();
			Goal g = new Gson().fromJson(client_goal, Goal.class);
			if(opType.equals("edit")){
				if(!goalDao.updateGoal(g)){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端目标更新失败!");
				}
			}else if(opType.equals("complete")){
				if(!goalDao.completeGoal(g.getSelfId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端目标完成失败!");
				}
			}else if(opType.equals("delete")){
				if(!goalDao.deleteGoal(g.getSelfId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端目标删除失败");
				}
			}
			List<Goal> goals = new GoalDao().getGoals(userId);
			if(goals == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonGoals = new Gson().toJson(goals);
			clientResult.setObject(jsonGoals);
			return "json";
		}else{
			GoalDao goalDao = new GoalDao();
			Goal g = goalDao.getGoalByGoalId(goalId);
			if(opType.equals("getGoal")){
				goal = g;
			}else if(opType.equals("edit")){
				g.setTitle(goal.getTitle());
				g.setContent(goal.getContent());
				goalDao.updateGoal(g);
			}else if(opType.equals("complete")){
				goalDao.completeGoal(goalId);
			}else if(opType.equals("delete")){
				goalDao.deleteGoal(goalId);
			}
			return "web";
		}
	}
	
	//获取所有目标
	public String obtainGoals(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<Goal> goals = new GoalDao().getGoals(userId);
			if(goals == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonGoals = new Gson().toJson(goals);
			clientResult.setObject(jsonGoals);
			return "json";
		}else{
			userId = (String)ActionContext.getContext().getSession().get("userId");
			listGoals = new ArrayList<Goal>();
			List<Goal> goals = new GoalDao().getGoals(userId);
			if(goals != null)
			for(Goal goal : goals){
				if(goal.getIsdelete().equals("0") && goal.getState().equals("noComplete"))
					listGoals.add(goal);
			}
			return "json";
		}
	}
}
