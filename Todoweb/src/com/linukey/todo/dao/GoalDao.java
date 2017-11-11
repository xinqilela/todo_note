package com.linukey.todo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Project;
import com.linukey.todo.util.HibernateSessionFactory;

public class GoalDao extends CommDao<Goal>{
	/**
	 * 保存目标
	 * @param project
	 * @return
	 */
	public int saveGoal(Goal goal){
		return this.saveOne(goal);
	}
	
	/**
	 * 更新目标
	 */
	public boolean updateGoal(Goal goal){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE Todo.project SET title = ?, content = ?, userId =?,"+
					"state = ?, isdelete = ? WHERE selfId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, goal.getTitle());
			q.setParameter(1, goal.getContent());
			q.setParameter(2, goal.getUserId());
			q.setParameter(3, goal.getState());
			q.setParameter(4, goal.getIsdelete());
			q.setParameter(5, goal.getSelfId());
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
	
	public Goal getGoalByGoalId(String goalId){
		List<Goal> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Goal t where t.selfId = '" + goalId + "'");
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
	
	/**
	 * 假删除目标
	 */
	public boolean deleteGoal(String goalId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL deleteGoal(?)}");
		q.setParameter(0, goalId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	/**
	 * 完成目标
	 */
	public boolean completeGoal(String goalId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL completeGoal(?)}");
		q.setParameter(0, goalId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	/**
	 * 获取所有目标
	 * @return
	 */
	public List<Goal> getGoals(String userId){
		return this.selectAll(Goal.class, userId);
	}
}
