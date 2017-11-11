package com.linukey.todo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Team;
import com.linukey.todo.entity.TeamTask;
import com.linukey.todo.util.HibernateSessionFactory;

public class TeamTaskDao extends CommDao {
	
	//保存团队任务
	public int saveTeamTask(TeamTask teamTask){
		return this.saveOne(teamTask);
	}
	
	public boolean updateTeamTask(TeamTask teamTask){
		try{
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE `Todo`.`teamtask`"+
			"SET title = ?, content = ?, starttime = ?, endtime = ?, clocktime = ?, projectId = ?, state = ?,"+
			" isdelete = ?, teamId = ? WHERE selfId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, teamTask.getTitle());
			q.setParameter(1, teamTask.getContent());
			q.setParameter(2, teamTask.getStarttime());
			q.setParameter(3, teamTask.getEndtime());
			q.setParameter(4, teamTask.getClocktime());
			q.setParameter(5, teamTask.getProjectId());
			q.setParameter(6, teamTask.getState());
			q.setParameter(7, teamTask.getIsdelete());
			q.setParameter(8, teamTask.getTeamId());
			q.setParameter(9, teamTask.getSelfId());
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
	
	//通过任务id获取任务
	public TeamTask getTeamTaskByTaskId(String taskId){
		return (TeamTask)this.getEntityById(TeamTask.class, Integer.parseInt(taskId));
	}
	
	//获取某个用户的所有团队任务
	public List<TeamTask> getTeamTasksByUserId(String userId){
		String hql = "select id, title, content, starttime, endtime, clocktime, projectId, teamId, state, isdelete, selfId  from TeamTask where teamId in (select teamId from UserTeam where userId = '"+ userId + "' and isdelete = '0')";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<Object[]> result = query.list();
		List<TeamTask> teamTasks = new ArrayList<TeamTask>();
		if(result != null){
			for(Object[] object : result){
				TeamTask teamTask = new TeamTask((int)object[0], object[1].toString(),
						object[2].toString(), object[3].toString(), object[4].toString(),
						object[5].toString(), object[6] == null ? null : object[6].toString(), object[7].toString(),
						object[8].toString(), object[9].toString(), object[10].toString());
				teamTasks.add(teamTask);
			}
		}
		tran.commit();
		session.clear();
		session.close();
		if(result != null)
			return teamTasks;
		return null;
	}
}
