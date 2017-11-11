package com.linukey.todo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Sight;
import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.User;
import com.linukey.todo.entity.UserTeam;
import com.linukey.todo.util.HibernateSessionFactory;

public class TeamDao extends CommDao {

	//保存小组信息
	public int saveTeam(Team team) {
		return this.saveOne(team);
	}
	
	//保存小组-用户信息
	public int saveUserTeam(UserTeam userTeam){
		return this.saveOne(userTeam);
	}

	//获取用户的所有小组信息
	public List<Team> getTeams(String userId){
		String hql = "select id, teamname, content, leaderId, teamId, isdelete, leaderName from Team where teamId in (select teamId from UserTeam where userId = '" + 
	userId + "' and isdelete = '0')";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<Object[]> result = query.list();
		List<Team> teams = new ArrayList<Team>();
		if(result != null){
			for(Object[] object : result){
				Team team = new Team((int)object[0], object[1].toString(),
						object[2].toString(), object[3].toString(), 
						object[4].toString(), object[5].toString(), object[6].toString());
				teams.add(team);
			}
		}
		tran.commit();
		session.clear();
		session.close();
		if(result != null)
			return teams;
		return null;
	}
	
	public boolean updateTeam(Team team){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE Todo.team SET teamname = ?, content = ?,leaderId = ?, leaderName = ?,isdelete = ?" +
					" WHERE teamId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, team.getTeamname());
			q.setParameter(1, team.getContent());
			q.setParameter(2, team.getLeaderId());
			q.setParameter(3, team.getLeaderName());
			q.setParameter(4, team.getIsdelete());
			q.setParameter(5, team.getTeamId());
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
	
	public boolean deleteTeam(String teamId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL deleteTeam(?)}");
		q.setParameter(0, teamId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	//通过小组名来获取所有满足条件的小组信息
	public List<Team> getTeamsByTeamName(String teamName, String userId){
		String hql = "select id, teamname, content, leaderId, teamId, isdelete, leaderName from Team where teamname = '" + teamName
					+"' and isdelete = '0' and teamId not in (select teamId from UserTeam where userId = '" + userId + "' and isdelete = '0')"; 
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<Object[]> result = query.list();
		List<Team> teams = new ArrayList<Team>();
		if(result != null){
			for(Object[] object : result){
				Team team = new Team((int)object[0], object[1].toString(),
						object[2].toString(), object[3].toString(), object[4].toString(), 
						object[5].toString(), object[6].toString());
				teams.add(team);
			}
		}
		tran.commit();
		session.clear();
		session.close();
		if(teams != null)
			return teams;
		return null;
	}
	
	public Team getTeamByTeamId(String teamId){
		List<Team> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Team t where t.teamId = '" + teamId + "'");
			list = q.list();
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			if(list != null) list = null;
			e.printStackTrace();
		}
		if(list != null && list.size() == 1)
			return list.get(0);
		return null;
	}
}














