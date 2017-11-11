<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Todo | 个人信息修改</title>
	<link rel="stylesheet" type="text/css" href="/Todoweb/css/userinfo.css" />
	<script src="/Todoweb/js/userinfo.js"></script>
	<script src="/Todoweb/js/jquery-3.1.1.js"></script>
	<script src="/Todoweb/js/ajaxfileupload.js"></script>
	<script src="/Todoweb/js/userinfo.js"></script>
</head>

<body>
	<header>
		<s:include value="../../Master/nav.jsp" />
	</header>
	<article>
		<div id="container">
			<div id="right_container">
				<form action="upLoadImg" method="post" id="form1" enctype="multipart/form-data">
					<s:fielderror />
		
						<div id="test">
							<img src="${userlogo}" id="userimg" />
						</div>

					<input type="file" id="btnFile" accept="image/jpeg, image/png" name="upload" />
					<input type="button" onclick="ajaxFileUpload()" id="btnUpLoad" value="上传头像" />
				</form>
			</div>
			<div id="box">
				<s:set name="username" value="username" />
				<form action="updateUserInfo" id="form2" method="post">
					<div id="left_container">
						<div class="info">
							<label class="user_info" id="user_info_first">用户名：</label>
							<input type="text" class="info_input" name="username" value="${username}" placeholder="用户名">
						</div>
						<br>
						<div class="info">
							<label class="user_info">邮箱账号：</label>
						<input type="email" class="info_input" id="email" name="email" value="${email}" placeholder="邮箱">
						</div>
						<br>
						<div class="info">
							<label class="user_info">手机号：</label>
							<input type="tel" class="info_input" id="phonenumber" name="phonenumber" value="${phonenumber}" placeholder="手机号码">
						</div>
						<br> 
						<input type="button" id="repass" value="更改密码"><br>
						<div id="last_div">
							<input type="reset" id="btnCancel" value="取消" id="cancle_btn">
							<input type="button" onclick="ajaxUpdateUserInfo()" id="btnSave" value="保存" id="save_btn">
						</div>
					</div>
				</form>
			</div>
		</div>
	</article>
	<footer>
		<s:include value="../../Master/footer.jsp" />
	</footer>
</body>
</html>