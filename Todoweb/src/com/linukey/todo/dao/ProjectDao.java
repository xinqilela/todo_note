package com.linukey.todo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.Project;
import com.linukey.todo.util.HibernateSessionFactory;

public class ProjectDao extends CommDao<Project> {
	
	/**
	 * 保存项目
	 * @param project
	 * @return
	 */
	public int saveProject(Project project){
		return this.saveOne(project);
	}
	
	/**
	 * 更新项目
	 */
	public boolean updateProject(Project project){
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			String sql = "UPDATE Todo.project SET title = ?, content = ?, userId =?,"+
					"state = ?, type = ?, isdelete = ? WHERE selfId = ?";
			Query q = session.createSQLQuery(sql);
			q.setParameter(0, project.getTitle());
			q.setParameter(1, project.getContent());
			q.setParameter(2, project.getUserId());
			q.setParameter(3, project.getState());
			q.setParameter(4, project.getType());
			q.setParameter(5, project.getIsdelete());
			q.setParameter(6, project.getSelfId());
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
	
	/**
	 * 假删除项目
	 */
	public boolean deleteProject(String projectId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL deleteProject(?)}");
		q.setParameter(0, projectId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	/**
	 * 完成项目
	 */
	public boolean completeProject(String projectId){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query q = session.createSQLQuery("{CALL completeProject(?)}");
		q.setParameter(0, projectId);
		q.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	
	public Project getProjectById(String id){
		List<Project> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Project t where t.id = '" + id + "'");
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
	
	public Project getProjectByProjectId(String projectId){
		List<Project> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Project t where t.selfId = '" + projectId + "'");
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
	 * 获取所有项目
	 * @return
	 */
	public List<Project> getProjects(String userId, String type){
		List<Project> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from Project t where t.userId = '" + userId + "'" + " and t.type = '" + type + "'");
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
	
	//获取用户所有的Project self+team
	public List<Project> getProjects(String userId){
		List<Project> result = new ArrayList<Project>();;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("select id, title, content, state, selfId, userId, type, isdelete from Project where userId in" 
                                     +"(select leaderId from Team where teamId in" 
                                     +"(select teamId from UserTeam where userId = '"+ userId +"' and isdelete = '0'))"
                                     +"and userId != '"+ userId +"' and type='team'");
			List<Object[]> list = q.list();
			if(list != null){
				for(Object[] object : list){
					Project project = new Project((int)object[0], object[1].toString(),
							object[2].toString(), object[3].toString(), object[4].toString(), 
							object[5].toString(), object[6].toString(), object[7].toString());
					result.add(project);
				}
			}
			q = session.createQuery("from Project t where t.userId = '" + userId + "'");
			List<Project> list1 = q.list();
			if(list1 != null){
				for(Project project : list1){
					result.add(project);
				}
			}
			tran.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			if(result != null) result = null;
			e.printStackTrace();
		}
		return result;
	}
}