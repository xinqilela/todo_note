<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Todo | 个人任务</title>
<link rel="stylesheet" type="text/css" href="/Todoweb/css/usertask.css" />
<link rel="stylesheet" href="/Todoweb/css/style.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
</head>

<body>
	<header id="header">
		<div id="head">
			<img src="${userlogo}" id="userlogo" onclick="usermenuOnclick()">
		</div>
		<div id="addtask">
			<img src="/Todoweb/img/add.png" onclick="showOrHideAddEditWindow('#addselect')" />
		</div>
		<div id="search">
			<form action="" method="post">
				<input type="text" id="searchInput" placeholder="请输入要搜索的内容" />
				<input type="submit" id="searchBtn" value="" />
			</form>
		</div>
	</header>
	<div id="leftmenu">
		<ul id="leftmenu-ul">
			<li id="selfMenu" onclick="menuOnClick('selfMenu')">
				<span>个人任务</span>
			</li>
			<ul class="sub">
				<li id="li_selfToday" onclick="getSelfTask(this, 'today')"><img src="/Todoweb/img/user.png" /><a>今日待办</a>
				</li>
				<li onclick="getSelfTask(this, 'tomorrow')"><img src="/Todoweb/img/user.png" /><a>明日待办</a>
				</li>
				<li onclick="getSelfTask(this, 'next')"><img src="/Todoweb/img/user.png" /><a>下一步行动</a>
				</li>
				<li onclick="getSelfTask(this, 'box')"><img src="/Todoweb/img/user.png" /><a>备忘箱</a>
				</li>
				<li id="li_selfProject" onclick="getSelfPGS(this, 'self')"><img src="/Todoweb/img/user.png" /><a>项目</a>
				</li>
				<li id="li_selfGoal" onclick="getSelfPGS(this, 'goal')"><img src="/Todoweb/img/user.png" /><a>目标</a>
				</li>
				<li id="li_selfSight" onclick="getSelfPGS(this, 'sight')"><img src="/Todoweb/img/user.png" /><a>情景</a>
				</li>
			</ul>
			<li id="teamMenu" onclick="menuOnClick('teamMenu')">
				<span>团队任务</span>
			</li>
			<ul class="sub">
				<li id="li_teamToday" onclick="getTeamTask(this, 'today')"><img class="icon" src="/Todoweb/img/user.png" /><a>今日待办</a>
				</li>
				<li onclick="getTeamTask(this, 'tomorrow')"><img class="icon" src="/Todoweb/img/user.png" /><a>明日待办</a>
				</li>
				<li onclick="getTeamTask(this, 'next')"><img class="icon" src="/Todoweb/img/user.png" /><a>下一步行动</a>
				</li>
				<li id="li_TeamProject" onclick="getTeamProject(this)"><img class="icon" src="/Todoweb/img/user.png" /><a>项目</a>
				</li>
				<li onclick="getTeams(this)"><img class="icon" src="/Todoweb/img/user.png" /><a>小组</a>
				</li>
			</ul>
			<li id="taskState" onclick="menuOnClick('taskState')">
				<span>任务状态</span>
			</li>
			<ul class="sub">
				<li><img class="icon" src="/Todoweb/img/user.png" /><a href="">已完成</a>
				</li>
				<li><img class="icon" src="/Todoweb/img/user.png" /><a href="">未完成</a>
				</li>
				<li><img class="icon" src="/Todoweb/img/user.png" /><a href="">过期</a>
				</li>
				<li><img class="icon" src="/Todoweb/img/user.png" /><a href="">垃圾箱</a>
				</li>
			</ul>
		</ul>
	</div>
	<div id="rightcontent">
	</div>
	<div id="usermenu" style="display:none;">
		<ul class="mainmenu">
			<li><img src="/Todoweb/img/user.png" alt="User icon"
				class="icon" /> <span><a id="home" href="/Todoweb/index.jsp">首页</a></span>
			</li>
			<li><img src="/Todoweb/img/user.png" alt="User icon" class="icon" /> <span>账户</span></li>
			<ul class="submenu">
				<li><span>个人信息</span></li>
				<li><span>Picture</span></li>
				<li><span>Go Premium</span></li>
			</ul>
			<li><img src="/Todoweb/img/envelope.png" alt="Envelope icon" class="icon"> <span>消息</span>
				<div class="messages">23</div></li>
			<ul class="submenu">
				<li><span>New</span></li>
				<li><span>Sent</span></li>
				<li><span>Trash</span></li>
			</ul>
			<li><img src="/Todoweb/img/cog.png" alt="Cog icon" class="icon"> <span>设置</span></li>
			<ul class="submenu">
				<li><span>同步</span></li>
				<li><span>Password</span></li>
				<li><span>Notifications</span></li>
				<li><span>Privacy</span></li>
				<li><span>Payments</span></li>
			</ul>
			<li><img src="/Todoweb/img/key.png" alt="Key icon" class="icon"><span><a
					id="logout" href="/Todoweb/logout.action">退出</a></span></li>
		</ul>
	</div>
	<div id="screendark" style="display:none;"></div>
	<div id="addselftask">
		<h5 id="selftaskhead">添加个人任务</h5>
		<form name="form_saveSelfTask">
			<input id="title" name="selftask.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="selftask.content" type="text" required placeholder="描述" /><br />
			<div id="box"> 
				<input type="checkbox" name="selftask.istmp" style="cursor:pointer;" />备忘箱<br />
			</div> 
			<input name="selftask.starttime" class="datepicker" type="text" required placeholder="开始时间" /><br />
			<input name="selftask.endtime" class="datepicker" type="text" required placeholder="结束时间" /><br />
			<!-- <input id="clock" name="selftask.clocktime" type="text" required placeholder="提醒时间" /><br /> -->
			<div id="project">
				项目:
				<select class="task_select" id="selfListProjects" name="selftask.projectId">
				</select>
			</div>
			<br/>
			<div id="goal">
				目标:
				<select class="task_select" id="selfListGoals" name="selftask.goalId">
				</select>
			</div>
			<br/>
			<div id="sight">
				情景:
				<select class="task_select" id="selfListSights" name="selftask.sightId">
				</select>
			</div>
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfTask('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addselftask')" />
		</form>
	</div>
	<div id="editselftask">
		<h5 id="selftaskhead">修改个人任务</h5>
		<form name="form_editSelfTask">
			<input name="selftask.id" type="text" style="display:none" />
			<input id="title" name="selftask.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="selftask.content" type="text" required placeholder="描述" /><br />
			<div id="box"> 
				<input type="checkbox" name="selftask.istmp" style="cursor:pointer;" />备忘箱<br />
			</div> 
			<input name="selftask.starttime" class="datepicker" type="text" required placeholder="开始时间" /><br />
			<input name="selftask.endtime" class="datepicker" type="text" required placeholder="结束时间" /><br />
			<!-- <input id="clock" name="clocktime" type="" required placeholder="提醒时间" /><br /> -->
			<div id="project" name="project">
				项目:
				<select class="task_select" id="selfListProjects1" name="selftask.projectId">
				</select>
			</div>
			<br/>
			<div id="goal">
				目标:
				<select class="task_select" id="selfListGoals1" name="selftask.goalId">
				</select>
			</div>
			<br/>
			<div id="sight">
				情景:
				<select class="task_select" id="selfListSights1" name="selftask.sightId">
				</select>
			</div>
			<br/>
			<input type="button" value="修改" id="save" onclick="saveSelfTask('edit')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editselftask')" />
		</form>
	</div>
	<div id="addteamtask">
		<h5 id="selftaskhead">添加团队任务</h5>
		<form name="form_saveTeamTask">
			<input id="title" name="teamtask.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="teamtask.content" type="text" required placeholder="描述" /><br />
			<input name="teamtask.starttime" class="datepicker" type="text" required placeholder="开始时间" /><br />
			<input name="teamtask.endtime" class="datepicker" type="text" required placeholder="结束时间" /><br />
			<!-- <input id="clock" name="selftask.clocktime" type="text" required placeholder="提醒时间" /><br /> -->
			<div id="project">
				项目:
				<select class="task_select" id="teamListProjects" name="teamtask.projectId">
				</select>
			</div>
			<br/>
			<div id="team">
				小组:
				<select class="task_select" id="ListTeams" name="teamtask.teamId">
				</select>
			</div>
			<br/>
			<br/>
			<input type="button" value="保存" id="save" onclick="saveTeamTask('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addteamtask')" />
		</form>
	</div>
	<div id="editteamtask">
		<h5 id="selftaskhead">编辑团队任务</h5>
		<form name="form_editTeamTask">
			<input name="teamtask.id" type="text" style="display:none" />
			<input id="title" name="teamtask.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="teamtask.content" type="text" required placeholder="描述" /><br />
			<input name="teamtask.starttime" class="datepicker" type="text" required placeholder="开始时间" /><br />
			<input name="teamtask.endtime" class="datepicker" type="text" required placeholder="结束时间" /><br />
			<div id="project">
				项目:
				<select class="task_select" id="teamListProjects1" name="teamtask.projectId">
				</select>
			</div>
			<br/>
			<div id="team">
				小组:
				<select class="task_select" id="ListTeams1" name="teamtask.teamId">
				</select>
			</div>
			<br/>
			<br/>
			<input type="button" value="保存" id="save" onclick="saveTeamTask('edit')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editteamtask')" />
		</form>
	</div>
	<div id="addTeamProject">
		<h5 id="selftaskhead">添加团队项目</h5>
		<form name="form_saveTeamProject">
			<input id="title" name="project.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="project.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveTeamProject('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addTeamProject')" />
		</form>
	</div>
	<div id="editTeamProject">
		<h5 id="selftaskhead">修改团队项目</h5>
		<form name="form_editTeamProject">
			<input id="id" name="project.selfId" type="text" style="display:none" /><br/>
			<input id="title" name="project.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="project.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveTeamProject('edit')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editTeamProject')" />
		</form>
	</div>
	<div id="editSelfProject">
		<h5 id="selftaskhead">修改个人项目</h5>
		<form name="form_editSelfProject">
			<input id="id" name="project.selfId" type="text" style="display:none" /><br/>
			<input id="title" name="project.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="project.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfProject('edit')"/>
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editSelfProject')" />
		</form>
	</div>
	<div id="editSelfGoal">
		<h5 id="selftaskhead">修改个人目标</h5>
		<form name="form_editSelfGoal">
			<input id="id" name="goal.selfId" type="text" style="display:none" /><br/>
			<input id="title" name="goal.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="goal.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfGoal('edit')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editSelfGoal')" />
		</form>
	</div>
	<div id="editSelfSight">
		<h5 id="selftaskhead">修改个人情景</h5>
		<form name="form_editSelfSight">
			<input id="id" name="sight.selfId" type="text" style="display:none" /><br/>
			<input id="title" name="sight.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="sight.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfSight('edit')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#editSelfSight')" />
		</form>
	</div>
	<div id="addSelfProject">
		<h5 id="selftaskhead">添加个人项目</h5>
		<form name="form_saveSelfProject">
			<input id="title" name="project.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="project.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfProject('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addSelfProject')" />
		</form>
	</div>
	<div id="addSelfGoal">
		<h5 id="selftaskhead">添加目标</h5>
		<form name="form_saveGoal">
			<input id="title" name="goal.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="goal.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfGoal('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addSelfGoal')" />
		</form>
	</div>
	<div id="addSelfSight">
		<h5 id="selftaskhead">添加情景</h5>
		<form name="form_saveSight">
			<input id="title" name="sight.title" type="text" required placeholder="标题" /><br /> 
			<input id="content" name="sight.content" type="text" required placeholder="描述" /><br />
			<br/>
			<input type="button" value="保存" id="save" onclick="saveSelfSight('save')" />
			<input type="button" value="取消" id="cancel" onclick="showOrHideAddEditWindow('#addSelfSight')" />		
		</form>
	</div>
	<div id="addselect" style="display:none;">
		<table id="addselect_table">
			<tr><td class="addselect_td" onclick="addSelect('selftask')">个人任务</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('project')">项目</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('sight')">情景</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('goal')">目标</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('teamtask')">团队任务</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('teamproject')">团队项目</td></tr>
			<tr><td class="addselect_td" onclick="addSelect('cancel')">取消</td></tr>
		</table>
	</div>
	<script src="//code.jquery.com/jquery-1.12.4.js"></script>
	<script src="/Todoweb/js/jquery-3.1.1.js"></script>
	<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="/Todoweb/js/usertask.js"></script>
	<script>
		$(".datepicker").datepicker({
			 dateFormat:"yy-mm-dd"
		});
	</script>
</body>
</html>