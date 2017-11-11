package com.linukey.todo.dao;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class CheckUserPermissionInterceptor extends AbstractInterceptor {
	public String  intercept(ActionInvocation invocation) throws Exception{
		ActionContext ctx = invocation.getInvocationContext();
		String username = (String)ctx.getSession().get("username");
		if(username != null && !username.isEmpty()){
			return invocation.invoke();
		}
		((ActionSupport)invocation.getAction()).addActionError("您还没有登录系统，请先登录系统!");
		return Action.LOGIN;
	}
}