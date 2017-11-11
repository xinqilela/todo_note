package com.linukey.todo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.metamodel.source.annotations.entity.EntityClass;

import com.linukey.todo.entity.User;
import com.linukey.todo.util.HibernateSessionFactory;

public abstract class CommDao<T> {
	
	/**
	 * 查询所有对象
	 * @param cls 需要查询的实体类型
	 * @return 查询结果结合
	 */
	protected List<T> selectAll(Class cls){
		List<T> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from " + cls.getName());
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
	
	/**
	 * 查询所有对象
	 * @param cls 需要查询的实体类型
	 * @return 查询结果结合
	 */
	protected List<T> selectAll(Class cls, String userId){
		List<T> list = null;
		try {
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			Query q = session.createQuery("from " + cls.getName() + " t where t.userId = '" + userId + "'");
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
	
	/**
	 * 根据Id删除
	 * @param cls
	 */
	protected void deleteOneById(T t){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		session.delete(t);
		tran.commit();
		session.clear();
		session.close();
	}
	
	/**
	 * 根据实体id更新数据
	 * @param cls
	 * @param id
	 * @return
	 */
	protected boolean updateById(T t){
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		session.update(t);
		tran.commit();
		session.clear();
		session.close();
		return true;
	}
	
	/**
	 * 保存对象到数据库
	 * @param t
	 * @return
	 */
	protected int saveOne(T t){
		int id = -1;
		try{
			Session session = HibernateSessionFactory.getCurrentSession();
			Transaction tran = session.beginTransaction();
			id = (int) session.save(t);
			tran.commit();
			session.clear();
			session.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * 通过id获取数据库中对象
	 * @param cls
	 * @param id
	 * @return
	 */
	public User getEntityByUserId(Class cls, String userId){
		String hql = "from " + cls.getName() + " t where t.userId = '" + userId + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		List<User> users = query.list();
		if(users != null && users.size() > 0)
			return users.get(0);
		return null;
	}
	
	/**
	 * 分页查询数据
	 * @param cls
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public List<T> getAllEntityByPage(Class cls, int start, int pageSize){
		Session session = HibernateSessionFactory.getCurrentSession();
		String hql="from "+cls.getName();
		Query query=session.createQuery(hql);
		query.setFirstResult(start);
		query.setMaxResults(pageSize);
		return query.list();
	}
	
	public T getEntityById(Class cls, int id){
		Session session = HibernateSessionFactory.getCurrentSession();
		String hql = "from " + cls.getName() + " t where t.id =" + id;
		Query query = session.createQuery(hql);
		List<T> list = query.list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	
//	/*
//	 * HibernateSession对象
//	 * */
//	protected Session sess=(Session) HibernateSessionFactory.getCurrentSession();
//	/*
//	 * 需要查询的实体类
//	 * */
//	public T getEntity(Class entityClass,int id)
//	{
//		return (T) sess.get(entityClass,id);
//	}
//	/*
//	 * 根据部门ID查询实体对象
//	 * T是实体类
//	 * taskId是任务编号*/
//	public List<T> findEntityByTaskId(Class entityClass,int taskId)
//	{
//		String hql="from "+entityClass.getName()+" t where t.id="+taskId;
//		Query query=sess.createQuery(hql);
//		return query.list();
//	}
//	/*
//	 * 查询所有，未标记为删除的实体对象
//	 * isdelete=0为没有删除的*/
//	public List<T> findEnitityAll(Class entityClass)
//	{
//		String hql="from "+entityClass.getName()+" t where t.isdelete=0";
//		Query query=sess.createQuery(hql);
//		return query.list();
//		
//	}
//	/*
//	 * 分页查询所有，已经删除的对象*/
//	public List<T> findisdeletedEntityAll(Class entityClass,int start,int pageSize)
//	{
//		String hql="from "+entityClass.getName()+" t where t.isdelete=1";
//		Query query=sess.createQuery(hql);
//		query.setFirstResult(start);
//		query.setMaxResults(pageSize);
//		return query.list();
//	}
//	/*
//	 * 查询所有，未标记为删除的实体对象
//	 * isdelete=0为没有删除的*/
//	public List<T> findisdeletedEnitityAll(Class entityClass)
//	{
//		String hql="from "+entityClass.getName()+" t where t.isdelete=1";
//		Query query=sess.createQuery(hql);
//		return query.list();
//		
//	}
//	/*
//	 * 查询所有，已经完成的的实体对象
//	 * state='完成'*/
//	public List<T> findcompletedEnitityAll(Class entityClass)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='完成'";
//		Query query=sess.createQuery(hql);
//		return query.list();
//		
//	}
//	/*
//	 * 分页查询，已经完成的的实体对象
//	 * state='完成'*/
//	public List<T> findcompletedEnitityAll(Class entityClass,int start,int pageSize)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='完成'";
//		Query query=sess.createQuery(hql);
//		query.setFirstResult(start);
//		query.setMaxResults(pageSize);
//		return query.list();
//		
//	}
//	/*
//	 * 查询所有，已过期的实体对象
//	 * state='过期'*/
//	public List<T> findovertimeEnitityAll(Class entityClass)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='过期'";
//		Query query=sess.createQuery(hql);
//		return query.list();
//		
//	}
//	/*
//	 * 分页查询，已经过期的的实体对象
//	 * state='过期'*/
//	public List<T> findovertimeEnitityAll(Class entityClass,int start,int pageSize)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='过期'";
//		Query query=sess.createQuery(hql);
//		query.setFirstResult(start);
//		query.setMaxResults(pageSize);
//		return query.list();
//		
//	}
//	/*
//	 * 查询所有，未完成的实体对象
//	 * state='未完成'*/
//	public List<T> findnotcompletedEnitityAll(Class entityClass)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='未完成'";
//		Query query=sess.createQuery(hql);
//		return query.list();
//		
//	}
//	/*
//	 * 分页查询，未完成的的实体对象
//	 * state='未完成'*/
//	public List<T> findnotcompletedEnitityAll(Class entityClass,int start,int pageSize)
//	{
//		String hql="from "+entityClass.getName()+" t where t.state='未完成'";
//		Query query=sess.createQuery(hql);
//		query.setFirstResult(start);
//		query.setMaxResults(pageSize);
//		return query.list();
//		
//	}
//	/*
//	 * 分页查询所有，没有删除的对象*/
//	public List<T> findEntityAll(Class entityClass,int start,int pageSize)
//	{
//		String hql="from "+entityClass.getName()+" t where t.isdelete=0";
//		Query query=sess.createQuery(hql);
//		query.setFirstResult(start);
//		query.setMaxResults(pageSize);
//		return query.list();
//	}
//	
//	/*
//	 * 统计指定实体类型 数量*/
//	public int countEntity(Class entityClass){
//		String hql="select count(*) from "+entityClass.getName()+" t where t.isdelete=0";
//		Query query=sess.createQuery(hql);
//		return (int) query.uniqueResult();
//	}
//	/*
//	 * 统计指定实体类型 数量(已经删除的)*/
//	public int countisdeletedEntity(Class entityClass){
//		String hql="select count(*) from "+entityClass.getName()+" t where t.isdelete=1";
//		Query query=sess.createQuery(hql);
//		return (int) query.uniqueResult();
//	}
//	/*
//	 * 统计指定实体类型 数量(已经过期的)*/
//	public int countovertimeEntity(Class entityClass){
//		String hql="select count(*) from "+entityClass.getName()+" t where t.state='过期'";
//		Query query=sess.createQuery(hql);
//		return (int) query.uniqueResult();
//	}
//	/*
//	 * 统计指定实体类型 数量(已完成的)*/
//	public int countcompletedEntity(Class entityClass){
//		String hql="select count(*) from "+entityClass.getName()+" t where t.state='完成'";
//		Query query=sess.createQuery(hql);
//		return (int) query.uniqueResult();
//	}
//	/*
//	 * 统计指定实体类型 数量(未完成的)*/
//	public int countnotcompletedEntity(Class entityClass){
//		String hql="select count(*) from "+entityClass.getName()+" t where t.state='未完成'";
//		Query query=sess.createQuery(hql);
//		return (int) query.uniqueResult();
//	}
//	/*
//	 * 保存实体对象，return1为保存成功*/
//	public int saveEntity(T entity)
//	{
//		int r=0;
//		Transaction tx=null;
//		try {
//			tx=sess.beginTransaction();
//			((org.hibernate.Session) sess).save(entity);
//			tx.commit();
//			r=1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			r=0;
//			if(tx!=null) tx.rollback();
//		}finally{
//			sess.flush();
//			sess.close();
//		}
//		
//		return r;
//	}
//	/*
//	 * 删除指定Id的实体*/
//	public int deleteEntity(Class entityClass,int id)
//	{
//		int r=0;
//		String hql="update "+entityClass.getName()+" t set t.isdelete=1 where t.id="+id;
//		System.out.println("delete hql ="+hql);
//		Transaction tx=null;
//		try {
//			tx=sess.beginTransaction();
//			Query query=sess.createQuery(hql);
//			query.executeUpdate();
//			
//			tx.commit();
//			r=1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			r=0;
//			if(tx!=null)tx.rollback();
//		}finally{
//			sess.flush();
//			sess.close();
//		}
//		return r;
//	}
//	/*
//	 * 插入*/
//	public int insertEntity(T entity)
//	{	
//		Transaction tx=null;
//		Integer r=0;
//		try {
//			tx =sess.beginTransaction();
//			r = (Integer) sess.save(entity);
//			tx.commit();
//			r=1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			if(tx!=null)tx.rollback();
//		}finally{
//			sess.clear();
//			sess.close();
//		}
//		return r;
//	}
	
			
}
