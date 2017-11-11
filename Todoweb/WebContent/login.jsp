<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML !DOCTYPE>
<head>
<meta charset="utf-8">
<title>Todo|登录</title>
<link rel="stylesheet" type="text/css" href="./css/login.css" />
</head>
<body>
	<header>
		<s:include value="./Master/nav.jsp" />
	</header>
	<article>
		<div id="login_container">
			<form action="login" method="post">
				<div id="errinfo">
					<s:fielderror />
				</div>
				<input type="text" id="user_name" name="username" placeholder="用户名/邮箱" required><br /> 
				<input type="password" id="user_pass" name="password" placeholder="密码"	required><br />
				<div id="about_pass">
					<input type="checkbox">记住我
					<a href="findpwd.html" id="forget_pass_span">忘记密码？</a>
				</div>
				<br/>
				<div id="usergroup">
				<input type="radio" id="normaluser" name="usergroup" value="普通用户" checked /><span>普通用户</span> 
				<input type="radio" name="usergroup" value="管理员" /><span>管理员</span>
				</div>
				<br/>
				<input type="submit" value="登录" id="login_button"><br />
			</form>
			<a href="./register.jsp" id="gotoregist">尚无账号？免费注册</a>
		</div>
	</article>
	<footer>
		<s:include value="./Master/footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
//errinfo = document.getElementById("#errinfo ul li span");
//errinfo = document.getElementById("errinfo").getElementsByTagName("ul")[0].getElementsByTagName("li")[0].getElementsByTagName("span")[0];
//document.getElementById("user_name").setCustomValidity(errinfo.innerHTML);
</script>
</HTML>