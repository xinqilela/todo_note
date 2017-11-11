<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML !DOCTYPE>
<head>
<meta charset="utf-8">
<title>Todo|多彩生活，简单记录...</title>
<link rel="stylesheet" type="text/css" href="./css/index.css" />
	<script type='text/javascript' src='/Todoweb/js/im/webim.config.js'></script>
	<script type='text/javascript' src='/Todoweb/js/im/strophe-1.2.8.min.js'></script>	
	<script type='text/javascript' src='/Todoweb/js/im/websdk-1.4.10.js'></script>
</head>
<body>
	<header>
		<s:include value="./Master/nav.jsp" />
	</header>
	<article>
		<div id="main">
			<div id="img_show_container">
				<div id="show_img">
					<img src="./img/test1.png" id="first_img" class="img"> 
					<img src="./img/test2.png" id="second_img" class="img"> 
				</div>
			</div>
			<div id="radio_button">
				<div id="first_circle" style="background:#6B59CE"></div>
				<div id="second_circle"></div>
			</div>
			<div id="back_head">
				<span id="back">&lt</span>
				<sapn id="head">&gt</span>
			</div>
		</div>
	</article>
	<footer>
		<s:include value="./Master/footer.jsp" />
	</footer>
	<script src="/Todoweb/js/index.js"></script>
	<script src="/Todoweb/js/jquery-3.1.1.js"></script>
</body>
</HTML>