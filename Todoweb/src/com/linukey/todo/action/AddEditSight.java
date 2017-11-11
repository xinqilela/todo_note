package com.linukey.todo.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.struts2.json.annotations.JSON;

import com.google.gson.Gson;
import com.linukey.todo.dao.ClientResult;
import com.linukey.todo.dao.GoalDao;
import com.linukey.todo.dao.ProjectDao;
import com.linukey.todo.dao.SightDao;
import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Project;
import com.linukey.todo.entity.Sight;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddEditSight extends ActionSupport {
	private String from; //判断请求来自web前端还是Android端
	private String client_sight; //Android端保存Sight时，序列化的sight对象
	private ClientResult clientResult = null; //返回给Android端的结果
	private String userId;
	private List<Sight> listSights; //web前端请求所有情景数据
	private Sight sight; //web前端保存情景数据
	private String sightId;
	private String opType;
	
	public String getSightId() {
		return sightId;
	}
	public void setSightId(String sightId) {
		this.sightId = sightId;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public Sight getSight() {
		return sight;
	}
	public void setSight(Sight sight) {
		this.sight = sight;
	}
	public List<Sight> getListSights() {
		return listSights;
	}
	public void setListSights(List<Sight> listSights) {
		this.listSights = listSights;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getClient_sight() {
		return client_sight;
	}
	public void setClient_sight(String client_sight) {
		this.client_sight = client_sight;
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
	 * 保存情景
	 */
	public String saveSight(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			Sight sight = new Gson().fromJson(client_sight, Sight.class);
			int id = new SightDao().saveSight(sight);
			if(id == -1){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端保存失败!");
			}
			List<Sight> sights = new SightDao().getSights(userId);
			if(sights == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonSights = new Gson().toJson(sights);
			clientResult.setObject(jsonSights);
			return "json";
		}else{
			String userId = (String)ActionContext.getContext().getSession().get("userId");
			sight.setUserId(userId);
			sight.setSelfId(UUID.randomUUID().toString());
			sight.setIsdelete("0");
			new SightDao().saveSight(sight);
			return "json";
		}
	}
	
	/**
	 * 更新情景
	 * @return
	 */
	public String updateSight(){
		System.out.println("updateProject");
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			SightDao sightDao = new SightDao();
			Sight s = new Gson().fromJson(client_sight, Sight.class);
			if(opType.equals("edit")){
				if(!sightDao.updateSight(s)){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端项目更新失败!");
				}
			}else if(opType.equals("delete")){
				if(!sightDao.deleteSight(s.getSelfId())){
					clientResult.setResult(false);
					clientResult.setMessage("服务器端项目删除失败!");
				}
			}
			List<Sight> sights = new SightDao().getSights(userId);
			if(sights == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonSights = new Gson().toJson(sights);
			clientResult.setObject(jsonSights);
			return "json";
		}else{
			SightDao sightDao = new SightDao();
			Sight s = sightDao.getSightBySightId(sightId);
			if(opType.equals("getSight")){
				sight = s;
			}else if(opType.equals("edit")){
				s.setTitle(sight.getTitle());
				s.setContent(sight.getContent());
				sightDao.updateSight(s);
			}else if(opType.equals("delete")){
				sightDao.deleteSight(sightId);
			}
			return "web";
		}
	}
	
	/**
	 * 获取所有情景
	 * @return
	 */
	public String obtainSights(){
		if(from != null && from.equals(TodoHelper.From.get("client"))){
			clientResult = new ClientResult();
			clientResult.setResult(true);
			List<Sight> sights = new SightDao().getSights(userId);
			if(sights == null){
				clientResult.setResult(false);
				clientResult.setMessage("服务器端项目获取失败!");
			}
			String jsonSights = new Gson().toJson(sights);
			clientResult.setObject(jsonSights);
			return "json";
		}else{
			userId = (String)ActionContext.getContext().getSession().get("userId");
			listSights = new ArrayList<Sight>();
			List<Sight> sights = new SightDao().getSights(userId);
			if(sights != null)
			for(Sight sight : sights){
				if(sight.getIsdelete().equals("0"))
					listSights.add(sight);
			}
			return "json";
		}
	}
}
