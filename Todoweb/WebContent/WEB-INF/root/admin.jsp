<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML !DOCTYPE>
<head>
<meta charset="utf-8">
<title>Todo|多彩生活，简单记录...</title>
<link rel="stylesheet" type="text/css" href="/Todoweb/css/admin.css" />
<link rel="stylesheet" type="text/css" href="/Todoweb/css/admin-usermanager.css" />
<link rel="stylesheet" type="text/css" href="/Todoweb/css/admin-systemsetting.css" />
<script src="/Todoweb/js/jquery-3.1.1.js"></script>
<script src="/Todoweb/js/admin.js"></script>
</head>
<body>
	<header>
		<s:include value="../../Master/nav.jsp" />
	</header>
	<article>
		<div id="mainbox">
			<div id="leftbox">
				<ul>
				    <li class="store"><span class="store-icon"></span><a href="#" onclick="usermanager()">用户信息管理</a></li>
				    <li class="movies"><span class="movies-icon"></span><a href="#" onclick="systemsetting()">系统设置</a></li>
				    <li class="music"><span class="music-icon"></span><a href="#">Music</a></li>
				    <li class="books"><span class="books-icon"></span><a href="#">Books</a></li>
				    <li class="magazines"><span class="magazines-icon"></span><a href="#">Magazines</a></li>
				    <li class="devices"><span class="devices-icon"></span><a href="#">Devices</a></li>
			  	</ul>
			</div>
			<div id="rightbox">
				<div id="usermanager">
					<div id="searchUser">
						<s:url id="url_search" value="">
							<s:param name="username" value=""></s:param>
						</s:url>
						<form action="searchUser" id="form1" method="post">
							<input type="textfield" name="pageNow" id="pageNow" value="${pageNow}" style="display:none" />
							<input type="textfield" name="judgeSearch" id="searchjudge" value="${judgeSearch}" style="display:none;" />
							<input type="textfield" name="searchUser"  id="input_search" value="${searchUser}"  placeholder="查找用户信息" />
							<input type="submit" id="btn_search" value="查找" />
						</form>
					</div>
					<div id="tableUsers">
						<table id="showtable">
							<thead>
								<tr>
									<th></th>
									<th>用户名</th>
									<th>邮箱</th>
									<th>手机号码</th>
									<th>用户类型</th>
								</tr>
							</thead>
							<s:iterator value="users">
								<tr>
									<td><input style="cursor:pointer;" value="${id}" class="userId" type="checkbox" /></td>
									<td><s:property value="username" /></td>
									<td><s:property value="email" /></td>
									<td><s:property value="phonenumber" /></td>
									<td><s:property value="usergroup" /></td>
								</tr>
							</s:iterator>
						</table>
						<s:url id="url_pre" value="getUsersByPage.action">
							<s:param name="pageNow" value="pageNow-1"></s:param>
						</s:url>
						<s:url id="url_next" value="getUsersByPage.action">
							<s:param name="pageNow" value="pageNow+1"></s:param>
						</s:url>
						<s:url id="url_current" value="getUsersByPage.action">
							<s:param name="pageNow" value="pageNow" />
						</s:url>
						<s:if test="users != null">
							<a href="#" onclick="toRoot()" id="toRoot">提 权</a>
							<a href="#" onclick="deleteUser()" id="delete">删 除</a>
							<s:a href="%{url_pre}" id="pre">上一页</s:a>
							<s:a href="%{url_next}" id="beh">下一页</s:a>
						</s:if>
					</div>
				</div>
				<div id="oneinfo"><!-- css在admin-usermanager里面 -->
					<s:a href="%{url_current}" id="btnBack">返回</s:a>
					<s:if test="resultUsers != null">
						<table id="showtable">
							<thead>
								<tr>
									<th></th>
									<th>用户名</th>
									<th>邮箱</th>
									<th>手机号码</th>
									<th>用户类型</th>
								</tr>
							</thead>
							<s:iterator value="resultUsers">
								<tr>
									<td><input style="cursor:pointer;" value="${id}" type="checkbox" class="userId" /></td>
									<td><s:property value="username" /></td>
									<td><s:property value="email" /></td>
									<td><s:property value="phonenumber" /></td>
									<td><s:property value="usergroup" /></td>
								</tr>
							</s:iterator>
						</table>
						<a href="#" onclick="toRoot_oneinfo()" id="oneinfo_toRoot">提 权</a>
						<a href="#" onclick="deleteUser_oneinfo()" id="oneinfo_delete">删 除</a>
					</s:if>
					<s:if test="resultUsers == null">
						<span id="searchnull">查找结果为空!</span>
					</s:if>
				</div>
				<div id="systemsetting">
				
				</div>
			</div>
		</div>
	</article>
	<footer>
		<s:include value="../../Master/footer.jsp" />
	</footer>
	<script type="text/javascript">
		window.onload = function() {
			if(document.getElementById("searchjudge").value == "1"){
				oneinfo();
				document.getElementById("searchjudge").value = "0";
			}
		}
	</script>
</body>
</HTML>