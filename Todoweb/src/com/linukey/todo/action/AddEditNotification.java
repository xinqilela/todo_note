package com.linukey.todo.action;

import java.util.List;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.TeamDao;
import com.linukey.todo.dao.TeamJoinNotificationDao;
import com.linukey.todo.dao.UserDao;
import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.TeamJoinInfo;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditNotification extends ActionSupport {
	private String from; //判断请求来自web前端还是其他端
	private String client_TeamJoinData; //来自Android端的对象的序列化
	private ClientResult clientResult; //给客户端的结果的封装
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public String getClient_TeamJoinData() {
		return client_TeamJoinData;
	}
	public void setClient_TeamJoinData(String client_TeamJoinData) {
		this.client_TeamJoinData = client_TeamJoinData;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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

	//保存小组申请信息
	public String saveTeamJoinNotification(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamJoinInfo teamjoin = (TeamJoinInfo)new Gson().fromJson(client_TeamJoinData, TeamJoinInfo.class);
			if(teamJoinDao.saveNotification(teamjoin) == -1){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端申请通知保存失败!");
			}
			return "json";
		}else{
			return "json";
		}
	}
	
	public String getTeamJoinJsonDatasByUserName(String userName){
		List<TeamJoinInfo> teamJoinDatas = new TeamJoinNotificationDao().getTeamJoinInfos(userName);
		return new Gson().toJson(teamJoinDatas);
	}
	
	//获取所有的小组申请信息
	public String getTeamJoinNotifications(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			clientResult.setObject(getTeamJoinJsonDatasByUserName(userName));
			return "json";
		}else{
			return "json";
		}
	}
	
	public String deleteTeamJoinData(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamJoinInfo teamJoinData = new Gson().fromJson(client_TeamJoinData, TeamJoinInfo.class);
			teamJoinDao.deleteTeamJoinData(teamJoinData);
			clientResult.setObject(getTeamJoinJsonDatasByUserName(teamJoinData.getToUserName()));
			return "json";
		}else{
			return "json";
		}
	}

	//用户退出小组
	public String userQuitTeam(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamJoinInfo teamJoinData = new Gson().fromJson(client_TeamJoinData, TeamJoinInfo.class);
			//让该用户退出小组并且把消息通知发给组长
			if(!teamJoinDao.userQuitTeam(teamJoinData)){
				clientResult.setResult(false);
				clientResult.setMessage("服务器在处理用户退出小组时出现问题!");
			}
			clientResult.setObject(getTeamJoinJsonDatasByUserName(teamJoinData.getFromUserName()));
			return "json";
		}else{
			return "web";
		}
	}
	
	//同意用户加入小组
	public String agreeSomeOneToJoinTeam(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamJoinInfo teamJoinData = new Gson().fromJson(client_TeamJoinData, TeamJoinInfo.class);
			if(!teamJoinDao.agreeSomeOneToJoinTeam(teamJoinData)){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端让用户加入小组失败!");
			}
			clientResult.setObject(getTeamJoinJsonDatasByUserName(teamJoinData.getToUserName()));
			return "json";
		}else{
			return "json";
		}
	}
	
	//拒绝用户加入
	public String refuseSomeOneToJoinTeam(){
		TeamJoinNotificationDao teamJoinDao = new TeamJoinNotificationDao();
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			TeamJoinInfo teamJoinData = new Gson().fromJson(client_TeamJoinData, TeamJoinInfo.class);
			if(!teamJoinDao.refuseJoinTeam(teamJoinData)){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端处理失败!");
			}
			clientResult.setObject(getTeamJoinJsonDatasByUserName(teamJoinData.getToUserName()));
			return "json";
		}else{
			return "json";
		}
	}
}






