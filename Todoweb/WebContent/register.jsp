<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML !DOCTYPE>
<head>
<meta charset="utf-8">
<title>Todo|登录</title>
<link rel="stylesheet" type="text/css" href="./css/regist.css" />
</head>
<body>
	<header>
		<s:include value="./Master/nav.jsp" />
	</header>
	<article>
		<div id="regist_container">
			<form action="regist" method="post">
				<div id="errinfo">
					<s:fielderror />
				</div>
				<input type="text" id="user_name" name="username" placeholder="用户名" required><br />
				<input type="password" id="user_pass" name="password" placeholder="密码" required><br />
				<input type="password" id="user_pass_agin" name="repassword" placeholder="确认密码" required><br />
				<input type="email" id="user_email" name="email" placeholder="邮箱" required><br />
				<input type="text" id="user_phone" name="phonenumber" placeholder="手机号" required><br />
				<div id="about_pass">
					<span id="remember_pass_span"><input type="checkbox" required>我同意Todo的服务条款</span>
				</div>
				<input type="submit" value="注册" id="regist_button"><br />
			</form>
			<a href="./login.jsp" id="gotologin_">已有账号，去登录</a>
		</div>
	</article>
	<footer>
		<s:include value="./Master/footer.jsp" />
	</footer>
</body>
</HTML>