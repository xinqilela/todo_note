package com.linukey.todo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.SelfTask;
import com.linukey.todo.util.HibernateSessionFactory;

/**
 * 保存个人任务到数据库
 * @author linukey
 *
 */
public class SelfTaskDao extends CommDao<SelfTask> {
	/**
	 * 保存个人任务
	 * @param selftask
	 * @return
	 */
	public int saveSelfTask(SelfTask selftask){
		return this.saveOne(selftask);
	}
	
	/**
	 * 修改个人任务
	 * @param selftask
	 * @return
	 */
	public boolean updateSelfTask(SelfTask selftask){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE Todo.selftask SET title = ?, content = ?, starttime = ?,"+ 
						" endtime = ?, clocktime = ?, projectId = ?, goalId = ?, sightId = ?,"+ 
						" userId = ?, state = ?, isdelete = ?, istmp = ?"+
						" WHERE selfId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, selftask.getTitle());
			q.setParameter(1, selftask.getContent());
			q.setParameter(2, selftask.getStarttime());
			q.setParameter(3, selftask.getEndtime());
			q.setParameter(4, selftask.getClocktime());
			q.setParameter(5, selftask.getProjectId());
			q.setParameter(6, selftask.getGoalId());
			q.setParameter(7, selftask.getSightId());
			q.setParameter(8, selftask.getUserId());
			q.setParameter(9, selftask.getState());
			q.setParameter(10, selftask.getIsdelete());
			q.setParameter(11, selftask.getIstmp());
			q.setParameter(12, selftask.getSelfId());
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
	
	public SelfTask getSelfTaskById(int id){
		return this.getEntityById(SelfTask.class, id);
	}
	
	/**
	 * 获取所有个人任务
	 * @return
	 */
	public List<SelfTask> getAll(String userId){
		return this.selectAll(SelfTask.class, userId);
	}
}
