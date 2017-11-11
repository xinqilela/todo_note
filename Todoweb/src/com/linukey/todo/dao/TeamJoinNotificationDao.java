package com.linukey.todo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.TeamJoinInfo;
import com.linukey.todo.entity.User;
import com.linukey.todo.entity.UserTeam;
import com.linukey.todo.util.HibernateSessionFactory;

public class TeamJoinNotificationDao extends CommDao {
	
	//保存申请信息
	public int saveNotification(TeamJoinInfo teamjoin){
		return this.saveOne(teamjoin);
	}
	
	//获取用户的所有通知信息
	public List<TeamJoinInfo> getTeamJoinInfos(String userName){
		List<TeamJoinInfo> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from TeamJoinInfo t where t.toUserName = '" + 
			userName + "' and t.isdelete = '0'");
			list = q.list();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			if(list != null) list = null;
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean refuseJoinTeam(TeamJoinInfo teamJoinData){
		try {
			//让用户从小组退出
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createSQLQuery("{CALL refuseJoinTeam(?,?,?,?,?,?,?,?)}");
			q.setParameter(0, teamJoinData.getFromUserId());
			q.setParameter(1, teamJoinData.getFromUserName());
			q.setParameter(2, teamJoinData.getToUserId());
			q.setParameter(3, teamJoinData.getToUserName());
			q.setParameter(4, teamJoinData.getTeamId());
			q.setParameter(5, teamJoinData.getTeamName());
			q.setParameter(6, teamJoinData.getSelfId());
			q.setParameter(7, UUID.randomUUID().toString());
			q.executeUpdate();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//获取用户的所有申请结果信息
	public List<TeamJoinInfo> getTeamJoinResultInfos(String userName){
		List<TeamJoinInfo> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from TeamJoinInfo t where t.toUserName = '" + userName 
					+ "' and execType != '2'");
			list = q.list();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			if(list != null) list = null;
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean deleteTeamJoinData(TeamJoinInfo teamJoinData){
		String hql = "update teamjoininfo set isdelete = '1' where selfId = '" + teamJoinData.getSelfId() + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createSQLQuery(hql);
		query.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	//用户退出小组
	public boolean userQuitTeam(TeamJoinInfo teamJoinData){
		try {
			//让用户从小组退出
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createSQLQuery("{CALL userQuitTeam(?,?,?,?,?,?,?)}");
			q.setParameter(0, teamJoinData.getFromUserId());
			q.setParameter(1, teamJoinData.getFromUserName());
			q.setParameter(2, teamJoinData.getToUserId());
			q.setParameter(3, teamJoinData.getToUserName());
			q.setParameter(4, teamJoinData.getTeamId());
			q.setParameter(5, teamJoinData.getTeamName());
			q.setParameter(6, UUID.randomUUID().toString());
			q.executeUpdate();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//让某用户加入小组
	public boolean agreeSomeOneToJoinTeam(TeamJoinInfo teamJoinData){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createSQLQuery("{CALL agreeJoinTeam(?,?,?,?,?,?,?,?)}");
			q.setParameter(0, teamJoinData.getFromUserId());
			q.setParameter(1, teamJoinData.getFromUserName());
			q.setParameter(2, teamJoinData.getToUserId());
			q.setParameter(3, teamJoinData.getToUserName());
			q.setParameter(4, teamJoinData.getTeamId());
			q.setParameter(5, teamJoinData.getTeamName());
			q.setParameter(6, teamJoinData.getSelfId());
			q.setParameter(7, UUID.randomUUID().toString());
			q.executeUpdate();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
