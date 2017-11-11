package com.linukey.todo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Goal;
import com.linukey.todo.entity.Sight;
import com.linukey.todo.util.HibernateSessionFactory;

public class SightDao extends CommDao<Sight> {
	/**
	 * 保存情景
	 * @param project
	 * @return
	 */
	public int saveSight(Sight sight){
		return this.saveOne(sight);
	}
	
	/**
	 * 假删除情景
	 */
	public boolean deleteSight(String sightId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL deleteSight(?)}");
		q.setParameter(0, sightId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	/**
	 * 更新情景
	 */
	public boolean updateSight(Sight sight){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE Todo.project SET title = ?, content = ?, userId =?,"+
					"isdelete = ? WHERE selfId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, sight.getTitle());
			q.setParameter(1, sight.getContent());
			q.setParameter(2, sight.getUserId());
			q.setParameter(3, sight.getIsdelete());
			q.setParameter(4, sight.getSelfId());
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
	
	public Sight getSightBySightId(String sightId){
		List<Sight> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Sight t where t.selfId = '" + sightId + "'");
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
	 * 获取所有情景
	 * @return
	 */
	public List<Sight> getSights(String userId){
		return this.selectAll(Sight.class, userId);
	}
}
