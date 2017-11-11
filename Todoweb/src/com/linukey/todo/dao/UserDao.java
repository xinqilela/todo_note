package com.linukey.todo.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.linukey.todo.entity.User;
import com.linukey.todo.util.HibernateSessionFactory;
import com.linukey.todo.util.TodoHelper;
import com.opensymphony.xwork2.ActionContext;

public class UserDao extends CommDao<User> {
	
	/**
	 * 检查用户名以及密码是否正确
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkinput(String username, String password, String usergroup){
		if(username == null || password == null || usergroup == null)
			return false;
		
		List<User> users = selectAll(User.class);
		for(User user : users){
			if(user.getUsername().equals(username) && 
					user.getPassword().equals(password) &&
					(user.getUsergroup().equals(usergroup) || user.getUsergroup().equals("root"))){
				saveLoginInfo(user.getUsername(), user.getUserId(), 
						user.getImgpath(), user.getUsergroup());
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 保存用户注册信息
	 * @param username
	 * @param password
	 * @param email
	 * @param repassword
	 * @param phonenumber
	 * @return
	 */
	public int saveUser(String username, String password, String email, String phonenumber){
		List<User> users = selectAll(User.class);
		for(User user : users){
			if(user.getUsername().equals(username))
				return -2;
		}
		String userId = UUID.randomUUID().toString();
		String usergroup = TodoHelper.UserGroup.get("normal");
		String imgPath = "";
		User user = new User(username, password, email, phonenumber, usergroup, userId, imgPath);
		int id = saveOne(user);
		if(id != -1){
			saveLoginInfo(user.getUsername(), user.getUserId(), 
					user.getImgpath(), user.getUsergroup());
		}
		return id;
	}
	
	/**
	 * 保存用户登录信息
	 * @param username
	 * @return
	 */
	public boolean saveLoginInfo(String username, String userId, String userlogo, String usergroup){
		ActionContext.getContext().getSession().put("username", username);
		ActionContext.getContext().getSession().put("userId", userId);
		ActionContext.getContext().getSession().put("userlogo", userlogo);
		ActionContext.getContext().getSession().put("usergroup", usergroup);
		return true;
	}
	
	/**
	 * 当用户信息修改后，修改session里面的username
	 * @param username
	 * @return
	 */
	public boolean updateLoginInfo(String username){
		ActionContext.getContext().getSession().put("username", username);
		return true;
	}
	
	/**
	 * 获取当前登录的用户名
	 * @return
	 */
	public String getUsername(){
		return (String)ActionContext.getContext().getSession().get("username");
	}
	
	/**
	 * 获取当前登录的用户ID
	 * @return
	 */
	public String getUserId(){
		return (String)ActionContext.getContext().getSession().get("userId");
	}
	
	/**
	 * 用户登出
	 * @return
	 */
	public boolean logout(){
		ActionContext.getContext().getSession().clear();
		return true;
	}
	
	/**
	 * 获取当前登录的用户信息
	 * @return
	 */
	public User getUserInfo(){
		String userId = getUserId();
		return getEntityByUserId(User.class, userId);
	}
	
	/**
	 * 保存用户上传图片的存放地址
	 * @param imgPath
	 * @return
	 */
	public boolean setUserImgPath(String userId, String imgPath){
		User user = this.getUserByUserId(userId);
		user.setImgpath(imgPath);
		updateById(user);
		return true;
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return 1代表成功 -1代表保存失败 -2代表用户名已存在
	 */
	public int updateUserInfo(User user){
		List<User> users = selectAll(User.class);
		for(User u : users){
			//在userId不同的条件下，用户名不能相同，否则就是已存在相同用户名
			if(user.getUsername().equals(u.getUsername()) &&
					!user.getUserId().equals(u.getUserId()))
				return -2;
		}
		
		if(updateById(user)){
			updateLoginInfo(user.getUsername());
			return 1;
		}
		return -1;
	}
	
	/**
	 * 获得用户上传的图片的地址
	 * @return
	 */
	public String getUserImgPath(){
		String imgPath = getUserInfo().getImgpath();
		return imgPath;
	}
	
	/**
	 * 分页查询用户信息
	 * @return
	 */
	public List<User> queryUserInfoByPage(int start, int pageSize){
		return getAllEntityByPage(User.class, start, pageSize);
	}
	
	public String getUserLogoUrlByUserId(String userId){
		String hql = "from User t where t.userId = '" + userId + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<User> result = query.list();
		tran.commit();
		session.clear();
		session.close();
		if(result != null && result.size() == 1)
			return result.get(0).getImgpath();
		return null;
	}
	
	/**
	 * 通过用户名查找用户信息
	 * @param username
	 * @return
	 */
	public List<User> searchUserByName(String username){
		String hql = "from User t where t.username = '" + username + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<User> result = query.list();
		tran.commit();
		session.clear();
		session.close();
		return result;
	}
	
	/**
	 * 通过Id删除用户
	 * @param user
	 */
	public void deleteUserById(User user){
		this.deleteOneById(user);
	}
	
	/**
	 * 通过用户Id提权
	 * @param id
	 */
	public void toRootById(int id){
		String hql="update User t set t.usergroup = 'root' where t.id="+id;
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		query.executeUpdate();
		tran.commit();
		session.clear();
		session.close();
	}
	
	//通过userId来获取username
	public String getUserNameByUserId(String userId){
		String hql = "from User t where t.userId = '" + userId + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<User> result = query.list();
		tran.commit();
		session.clear();
		session.close();
		if(result != null && result.size() == 1)
			return result.get(0).getUsername();
		return null;
	}
	
	public User getUserByUserId(String userId){
		String hql = "from User t where t.userId = '" + userId + "'";
		Session session = HibernateSessionFactory.getCurrentSession();
		Transaction tran = session.beginTransaction();
		Query query = session.createQuery(hql);
		List<User> result = query.list();
		tran.commit();
		session.clear();
		session.close();
		if(result != null && result.size() == 1)
			return result.get(0);
		return null;
	}
}

















