<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="/Todoweb/css/nav.css" />

<div id="top_logo">
	<a href="/Todoweb/index.jsp"> <img src="/Todoweb/img/logo.png">
	</a>
</div>
<nav id="top_nav">
	<ul>
		<a href="/Todoweb/index.jsp" id="index_firstpage"><li id="ul_index">首页</li></a>
		<a href="/Todoweb/getintoUserTask" id="self_mode"><li>任务记录</li></a>
		<a href="help.html" id="help"><li>帮助</li></a>
		<a href="download.html" id="download"><li>下载</li></a>
		<s:if test="#session['usergroup'] == 'root'">
			<a href="/Todoweb/acessAdmin.action"><li>管理后台</li></a>
		</s:if>
	</ul>
</nav>
<div id="top_button">
	<s:if test="#session['username']!=null">
		<a href="/Todoweb/acessUserinfo.action" id="usernamelink">${session.username}</a>
		<s:if test="#session['userlogo']!=null">
			<a href="/Todoweb/acessUserinfo.action">
				<img src="${userlogo}" id="userlogo" />
			</a>
		</s:if>
		<a href="/Todoweb/logout.action" id="logout">退出</a>
	</s:if>
	<s:if test="#session['username']==null">
		<a href="/Todoweb/login.jsp"><button id="button_login">登录到网页版</button></a>
		<a href="/Todoweb/register.jsp"><button id="button_regist">注册</button></a>
	</s:if>
</div>