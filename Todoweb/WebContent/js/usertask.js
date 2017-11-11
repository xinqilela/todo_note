$(document).ready( function() {
	//用户菜单设置
	var $submenu = $('.submenu');
	var $mainmenu = $('.mainmenu');
	$submenu.hide();
	$submenu.on('click','li', function() {
		$submenu.siblings().find('li').removeClass('chosen');
		$(this).addClass('chosen');
	});
	$mainmenu.on('click', 'li', function() {
		$(this).next('.submenu').slideToggle();
	});
	//左侧任务菜单设置
	$("#selfMenu").next('.sub').slideDown();
	$("#teamMenu").next('.sub').slideUp();
	$("#taskState").next('.sub').slideUp();
});
//用这两个全局变量保存状态，编辑或添加后可以回到状态
var currentLi; //当前选中的左侧菜单栏
var currentMenu; //当前正在查看的任务类型
//左侧菜单点击
function menuOnClick(menu){
	if(menu == "selfMenu"){
		$("#selfMenu").next('.sub').slideDown();
		$("#teamMenu").next('.sub').slideUp();
		$("#taskState").next('.sub').slideUp();
	}else if(menu == "teamMenu"){
		$("#selfMenu").next('.sub').slideUp();
		$("#teamMenu").next('.sub').slideDown();
		$("#taskState").next('.sub').slideUp();
	}else if(menu == "taskState"){
		$("#selfMenu").next('.sub').slideUp();
		$("#teamMenu").next('.sub').slideUp();
		$("#taskState").next('.sub').slideDown();
	}
}
//用户菜单点击
function usermenuOnclick(){
	var usermenu = document.getElementById("usermenu");
	if(usermenu.style.display == 'none')
		usermenu.style.display = 'block';
	else if(usermenu.style.display == 'block')
		usermenu.style.display = 'none';
}
//窗口显示与隐藏
function showOrHideAddEditWindow(wId){
	var addselftask = $(wId);
	var screendark = $('#screendark');
	(addselftask.css('display') == 'block') ? (addselftask.css('display', 'none')) : (addselftask.css('display','block'));
	(screendark.css('display') == 'block') ? (screendark.css('display', 'none')) : (screendark.css('display','block'));
}
//左侧菜单点中效果
function changeClass(li){
	currentLi = li;
    $('.sub').children("li").removeClass("leftmenuSelect");
    $(li).addClass("leftmenuSelect");
}
//保存个人任务
function saveSelfTask(judge){
	var form, action, opType, taskId;
	if(judge == 'save'){
		action = 'saveSelfTask';
		form = document.forms["form_saveSelfTask"];
		showOrHideAddEditWindow("#addselftask");
	}else if(judge == 'edit'){
		action = 'updateSelfTask';
		opType = 'edit';
		form = document.forms["form_editSelfTask"];
		taskId = form.elements["selftask.id"].value;
		showOrHideAddEditWindow("#editselftask");
	}
	$.post(action, {"selftask.title":form.elements["selftask.title"].value,
		"selftask.content":form.elements["selftask.content"].value,
		"selftask.istmp":(form.elements["selftask.istmp"].checkbox==true)?"1":"0",
		"selftask.starttime":form.elements["selftask.starttime"].value,
		"selftask.endtime":form.elements["selftask.endtime"].value,
		"selftask.projectId":form.elements["selftask.projectId"].value,
		"selftask.goalId":form.elements["selftask.goalId"].value,
		"selftask.sightId":form.elements["selftask.sightId"].value,
		"opType":opType,
		"taskId":taskId}, function(data){
			getSelfTask(currentLi, currentMenu);
	}, "json");
}
//添加个人任务
function addselftask(){
	//获得项目，目标，情景数据
	$.post("getSelfPGS", {"projectType":"self"}, function(data){
		for(var propName in data){
			if(propName == "listGoals"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				}
				$('#selfListGoals').html(result);
			}
			if(propName == "listProjects"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				}
				$('#selfListProjects').html(result);
			}
			if(propName == "listSights"){                                                                                                                                                                                                                                                                  
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				}
				$('#selfListSights').html(result);				
			}
		}
	}, "json");
	showOrHideAddEditWindow('#addselftask');
}
//获取个人任务数据
function getSelfTask(li, menu){
	var jsonstr = JSON.stringify(li);
	jsonstr = jsonstr.replace(/"/gm, "'");
	var jsonstr1 = JSON.stringify(menu);
	jsonstr1 = jsonstr1.replace(/"/gm, "'");
	changeClass(li);
	var action;
	if(menu == 'today') action = 'getSelfToday';
	else if(menu == 'tomorrow') action = 'getSelfTomorrow';
	else if(menu == 'next') action = 'getSelfNext';
	else if(menu == 'box') action = 'getSelfBox';
	currentMenu = menu;
	$.post(action, null, function(data){
		for(var propName in data){
			if(propName == "ajaxValues"){
				var result = "<table id='showtasks'>" +
				"<thead><tr>" +
				"<th>标题</th><th>描述</th><th>开始时间</th>"+
				"<th>结束时间</th><th>提醒时间</th><th>项目</th>"+
				"<th>目标</th><th>情景</th>" +
				"<th colspan='3'>操作</th>" +
				"</tr></thead>";
				$(data[propName]).each(function(i,n){
					var tmp = 
					"<tr>" + 
					"<td width='10%' class='other'><s:property value='title' />"+ (n.title!=undefined?n.title:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='content' />"+ (n.content!=undefined?n.content:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='starttime' />"+ (n.starttime!=undefined?n.starttime:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='endtime' />"+ (n.endtime!=undefined?n.endtime:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='clocktime' />"+ (n.clocktime!=undefined?n.clocktime:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='projectId' />"+ (n.projectId!=undefined?n.projectId:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='goalId' />"+ (n.goalId!=undefined?n.goalId:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='sightId' />"+ (n.sightId!=undefined?n.sightId:"") +"</td>" +
					"<td width='10%' style='display:none'><s:property value='id' /></td>" +
					"<td width='5%' class='btnTask' onclick=\"editSelfTask('"+n.id+"')\">编辑</td>" +
					"<td width='5%' class='btnTask' onclick=\"updateSelfTask('"+n.id+"','complete')\">完成</td>"+
					"<td width='5%' class='btnTask' onclick=\"updateSelfTask('"+n.id+"','delete')\">删除</td>"+
					"</tr>";
					result += tmp;
				});
				result += "</table>";
				$("#rightcontent").html(result);
			}
		}
	}, "json");
}
//编辑个人任务
function editSelfTask(id){
	//获得项目，目标，情景数据
	$.post("getSelfPGS", {'projectType':'self'}, function(data){
		for(var propName in data){
			if(propName == "listGoals"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++)
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				$('#selfListGoals1').html(result);
			}
			if(propName == "listProjects"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++)
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				$('#selfListProjects1').html(result);
			}
			if(propName == "listSights"){                                                                                                                                                                                                                                                                  
				var result = "";
				for(var i = 0; i < data[propName].length; i++)
					result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				$('#selfListSights1').html(result);				
			}
		}
	}, "json");
	$.post("updateSelfTask", {'opType':'gettask', 'taskId':id}, function(data){
		$("#editselftask").find("input").each(function () {    
			if(this.name == 'selftask.id') $(this).val(data['id']);
		    if(this.name == 'selftask.title') $(this).val(data['title']);
		    if(this.name == 'selftask.content') $(this).val(data['content']);
		    if(this.name == 'selftask.starttime') $(this).val(data['starttime']);
		    if(this.name == 'selftask.endtime') $(this).val(data['endtime']);
		    if(this.name == 'selftask.clocktime') $(this).val(data['clocktime']);
		})
		$("#editselftask").find("select").each(function () {
		    if(this.name == 'selftask.projectId') $(this.val(data['projectId']));
		    if(this.name == 'selftask.goalId') $(this).val(data['goalId']);
		    if(this.name == 'selftask.sightId') $(this).val(data['sightId']);
		})
	}, "json");
	showOrHideAddEditWindow("#editselftask");
}
//update个人任务
function updateSelfTask(id, opType){
	$.post("updateSelfTask", {'opType':opType,'taskId':id}, function(data){
		getSelfTask(currentLi, currentMenu);
	}, "json");
}
//选择添加类型
function addSelect(type){
	showOrHideAddEditWindow("#addselect");
	if(type=="selftask"){
		addselftask();
	}else if(type == "project"){
		showOrHideAddEditWindow("#addSelfProject");
	}else if(type == "sight"){
		showOrHideAddEditWindow("#addSelfSight");
	}else if(type == "goal"){
		showOrHideAddEditWindow("#addSelfGoal");
	}else if(type=="teamtask"){
		addteamtask();
	}else if(type == "teamproject"){
		showOrHideAddEditWindow("#addTeamProject");
	}else if(type == "cancel"){
	}
}
//获取个人项目，目标，情景
function getSelfPGS(li, menu){
	currentMenu = menu;
	changeClass(li);
	var action;
	if(menu == 'self') action = 'getProjects?projectType=self';
	else if(menu == 'goal') action = 'getSelfGoals';
	else if(menu == 'sight') action = 'getSelfSights';
	$.post(action, null, function(data){
		for(var propName in data){
			if(propName == "listGoals" || propName == "listSights" || propName == "listProjects"){
				var result = 
					"<table id='showtasks'><thead><tr>" +
					"<th>标题</th><th>描述</th><th></th><th></th><th></th>" +
					"<th></th><th></th><th></th><th colspan='4'>操作</th></tr></thead>";
				$(data[propName]).each(function(i,n){
					var tmp = 
						"<tr>" +
						"<td width='10%' class='other'><s:property value='title' />" + (n.title!=undefined?n.title:"") + "</td>" +
						"<td width='10%' class='other'><s:property value='content' />" + (n.content!=undefined?n.content:"") + "</td>" +
						"<td width='10%'><s:property value='starttime' /></td>" +
						"<td width='10%'><s:property value='endtime' /></td>" +
						"<td width='10%'><s:property value='clocktime' /></td>" +
						"<td width='10%'><s:property value='project'/></td>" +
						"<td width='10%'><s:property value='teamname' /></td>" +
						"<td width='5%'><s:property value='teamname' /></td>" +
						"<td width='5%' class='btnTask'>查看</td>" +
						"<td width='5%' onclick=\"editPGS('"+n.selfId+"','"+menu+"')\" class='btnTask'>编辑</td>";
					if(menu != 'sight'){
						tmp += "<td width='5%' onclick=\"updatePGS('"+menu+"','"+n.selfId+"','complete')\" class='btnTask'>完成</td>";
						}
						tmp += "<td width='5%' onclick=\"updatePGS('"+menu+"','"+n.selfId+"','delete')\" class='btnTask'>删除</td>";
						tmp += "</tr>";
					result += tmp;
				});
				result += "</table>";
				$("#rightcontent").html(result);
			}
		}
	}, "json");
}
//保存情景
function saveSelfSight(type){
	var action, form, sightId;
	if(type == "save"){
		action = "saveSight";
		showOrHideAddEditWindow("#addSelfSight");
	}else if(type == "edit"){
		action = "updateSight";
		form = document.forms["form_editSelfSight"];
		sightId = form.elements["sight.selfId"].value;
		showOrHideAddEditWindow("#editSelfSight");
	}
	$.post(action, {"sight.title":form.elements["sight.title"].value,
		"sight.content":form.elements["sight.content"].value,
		"sightId":sightId,
		"opType":"edit"}, function(data){
			getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//保存个人项目
function saveSelfProject(type){
	var action, projectId, form;
	if(type == "save"){
		action = "saveProject";
		showOrHideAddEditWindow("#addSelfProject");
	}else if(type == "edit"){
		action = "updateProject";
		form = document.forms["form_editSelfProject"];
		projectId = form.elements["project.selfId"].value;
		showOrHideAddEditWindow("#editSelfProject");
	}
	$.post(action, {"project.title":form.elements["project.title"].value,
		"project.content":form.elements["project.content"].value,
		"opType":"edit",
		"projectId":projectId}, function(data){
			getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//保存目标
function saveSelfGoal(type){
	var action, goalId, form;
	if(type == "save") {
		action = "saveGoal";
		showOrHideAddEditWindow("#addSelfGoal");
	}
	else if(type == "edit") {
		action = "updateGoal";
		form = document.forms["form_editSelfGoal"];
		goalId = form.elements["goal.selfId"].value;
		showOrHideAddEditWindow("#editSelfGoal");
	}
	$.post(action, {"goal.title":form.elements["goal.title"].value,
		"goal.content":form.elements["goal.content"].value,
		"goalId":goalId,
		"opType":"edit"}, function(data){
			getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//保存团队项目
function saveTeamProject(type){
	var action, projectId, form;
	if(type == "save"){
		action = "saveProject";
		showOrHideAddEditWindow("#addTeamProject");
	}else if(type == "edit"){
		action = "updateProject";
		form = document.forms["form_editTeamProject"];
		projectId = form.elements["project.selfId"].value;
		showOrHideAddEditWindow("#editTeamProject");
	}
	$.post(action, {"project.title":form.elements["project.title"].value,
		"project.content":form.elements["project.content"].value,
		"opType":"team",
		"project.selfId":projectId}, function(data){
			getTeamProejct(currentLi, currentMenu);
	}, "json");
}
//获取团队任务数据
function getTeamTask(li, menu){
	var jsonstr = JSON.stringify(li);
	jsonstr = jsonstr.replace(/"/gm, "'");
	var jsonstr1 = JSON.stringify(menu);
	jsonstr1 = jsonstr1.replace(/"/gm, "'");
	changeClass(li);
	var action;
	if(menu == 'today') action = 'getTeamToday';
	else if(menu == 'tomorrow') action = 'getTeamTomorrow';
	else if(menu == 'next') action = 'getTeamNext';
	currentMenu = menu;
	$.post(action, null, function(data){
		for(var propName in data){
			if(propName == "teamTasks"){
				var result = "<table id='showtasks'>" +
				"<thead><tr>" +
				"<th>标题</th><th>描述</th><th>开始时间</th>"+
				"<th>结束时间</th><th>项目</th>"+
				"<th>teamName</th>" +
				"<th colspan='3'>操作</th>" +
				"</tr></thead>";
				$(data[propName]).each(function(i,n){
					var tmp = 
					"<tr>" + 
					"<td width='10%' class='other'><s:property value='title' />"+ (n.title!=undefined?n.title:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='content' />"+ (n.content!=undefined?n.content:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='starttime' />"+ (n.starttime!=undefined?n.starttime:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='endtime' />"+ (n.endtime!=undefined?n.endtime:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='projectId' />"+ (n.projectId!=undefined?n.projectId:"") +"</td>" +
					"<td width='10%' class='other'><s:property value='teamId' />"+ (n.teamId!=undefined?n.teamId:"") +"</td>" +
					"<td width='10%' style='display:none'><s:property value='id' /></td>" +
					"<td width='5%' class='btnTask' onclick=\"editTeamTask('"+n.id+"')\">编辑</td>" +
					"<td width='5%' class='btnTask' onclick=\"updateTeamTask('"+n.id+"','complete')\">完成</td>"+
					"<td width='5%' class='btnTask' onclick=\"updateTeamTask('"+n.id+"','delete')\">删除</td>"+
					"</tr>";
					result += tmp;
				});
				result += "</table>";
				$("#rightcontent").html(result);
			}
		}
	}, "json");
}
//获取获取团队项目
function getTeamProject(li){
	changeClass(li);
	var action = "getProjects?projectType=team";
	$.post(action, null, function(data){
		for(var propName in data){
			if(propName == "listProjects"){
				var result = 
					"<table id='showtasks'><thead><tr>" +
					"<th>标题</th><th>描述</th><th></th><th></th><th></th>" +
					"<th></th><th></th><th></th><th colspan='4'>操作</th></tr></thead>";
				$(data[propName]).each(function(i,n){
					var tmp = 
						"<tr>" +
						"<td width='10%' class='other'><s:property value='title' />" + (n.title!=undefined?n.title:"") + "</td>" +
						"<td width='10%' class='other'><s:property value='content' />" + (n.content!=undefined?n.content:"") + "</td>" +
						"<td width='10%'><s:property value='starttime' /></td>" +
						"<td width='10%'><s:property value='endtime' /></td>" +
						"<td width='10%'><s:property value='clocktime' /></td>" +
						"<td width='10%'><s:property value='project'/></td>" +
						"<td width='10%'><s:property value='teamname' /></td>" +
						"<td width='5%'><s:property value='teamname' /></td>" +
						"<td width='5%' class='btnTask'>查看</td>" +
						"<td width='5%' onclick=\"editTeamProject('"+n.selfId+"')\" class='btnTask'>编辑</td>" +
						"<td width='5%' onclick=\"updateTeamProject('"+ n.selfId +"','complete')\" class='btnTask'>完成</td>" +
						"<td width='5%' onclick=\"updateTeamProject('"+ n.selfId +"','delete')\" class='btnTask'>删除</td>" +
						"</tr>";
					result += tmp;
				});
				result += "</table>";
				$("#rightcontent").html(result);
			}
		}
	}, "json");
}
//获取teams
function getTeams(li){
	changeClass(li);
	var action = "getTeams";
	$.post(action, null, function(data){
		for(var propName in data){
			if(propName == "teams"){
				var result = 
					"<table id='showtasks'><thead><tr>" +
					"<th>标题</th><th>描述</th><th>组长</th><th></th><th></th>" +
					"<th></th><th></th><th></th><th colspan='4'>操作</th></tr></thead>";
				$(data[propName]).each(function(i,n){
					var tmp = 
						"<tr>" +
						"<td width='10%' class='other'><s:property value='teamname' />" + (n.teamname!=undefined?n.teamname:"") + "</td>" +
						"<td width='10%' class='other'><s:property value='content' />" + (n.content!=undefined?n.content:"") + "</td>" +
						"<td width='10%' class='other'><s:property value='leaderId'/>" + (n.leaderId!=undefined?n.leaderId:"") + "</td>" +
						"<td width='10%'><s:property value='endtime' /></td>" +
						"<td width='10%'><s:property value='clocktime' /></td>" +
						"<td width='10%'><s:property value='project'/></td>" +
						"<td width='10%'><s:property value='teamname' /></td>" +
						"<td width='5%'><s:property value='teamname' /></td>" +
						"<td width='5%' class='btnTask'>查看</td>" +
						"<td width='5%' onclick=\"editPGS('"+propName+"')\" class='btnTask'>编辑</td>" +
						"<td width='5%' onclick=\"updateTeam('"+n.teamId+"')\" class='btnTask'>删除</td>" +
						"</tr>";
					result += tmp;
				});
				result += "</table>";
				$("#rightcontent").html(result);
			}
		}
	}, "json");
}
//添加团队任务
function addteamtask(){
	//获得项目
	$.post("getProjects", {"projectType":"team"}, function(data){
		for(var propName in data){
			if(propName == "listProjects"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					if(data[propName][i].type == 'team')
						result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				}
				$('#teamListProjects').html(result);
			}
		}
	}, "json");
	//获取小组数据
	$.post("getTeams", null, function(data){
		for(var propName in data){
			if(propName == "teams"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					result += "<option value=\""+data[propName][i].teamId+"\">" + data[propName][i].teamname + "<\/option>";
				}
				$('#ListTeams').html(result);
			}
		}
	}, "json");	
	showOrHideAddEditWindow("#addteamtask");
}
//保存团队任务
function saveTeamTask(judge){
	var form, action, opType, taskId;
	if(judge == 'save'){
		action = 'saveTeamTask';
		form = document.forms["form_saveTeamTask"];
		showOrHideAddEditWindow("#addteamtask");
	}else if(judge == 'edit'){
		action = 'updateTeamTask';
		opType = 'edit';
		form = document.forms["form_editTeamTask"];
		taskId = form.elements["teamtask.id"].value;
		showOrHideAddEditWindow("#editteamtask");
	}
	$.post(action, {"teamtask.title":form.elements["teamtask.title"].value,
		"teamtask.content":form.elements["teamtask.content"].value,
		"teamtask.starttime":form.elements["teamtask.starttime"].value,
		"teamtask.endtime":form.elements["teamtask.endtime"].value,
		"teamtask.projectId":form.elements["teamtask.projectId"].value,
		"teamtask.teamId":form.elements["teamtask.teamId"].value,
		"opType":opType,
		"taskId":taskId}, function(data){
			getTeamTask(currentLi, currentMenu);
	}, "json");
}
//编辑团队任务
function editTeamTask(id){
	//获得项目
	$.post("getProjects", {"projectType":"team"}, function(data){
		for(var propName in data){
			if(propName == "listProjects"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					if(data[propName][i].type == 'team')
						result += "<option value=\""+data[propName][i].selfId+"\">" + data[propName][i].title + "<\/option>";
				}
				$('#teamListProjects1').html(result);
			}
		}
	}, "json");
	//获取小组数据
	$.post("getTeams", null, function(data){
		for(var propName in data){
			if(propName == "teams"){
				var result = "";
				for(var i = 0; i < data[propName].length; i++){
					result += "<option value=\""+data[propName][i].teamId+"\">" + data[propName][i].teamname + "<\/option>";
				}
				$('#ListTeams1').html(result);
			}
		}
	}, "json");	
	$.post("updateTeamTask", {'opType':'gettask', 'taskId':id}, function(data){
		$("#editteamtask").find("input").each(function () {    
			if(this.name == 'teamtask.id') $(this).val(data['id']);
		    if(this.name == 'teamtask.title') $(this).val(data['title']);
		    if(this.name == 'teamtask.content') $(this).val(data['content']);
		    if(this.name == 'teamtask.starttime') $(this).val(data['starttime']);
		    if(this.name == 'teamtask.endtime') $(this).val(data['endtime']);
		})
		$("#editteamtask").find("select").each(function () {
		    if(this.name == 'teamtask.projectId') $(this.val(data['projectId']));
		    if(this.name == 'teamtask.teamId') $(this).val(data['teamId']);
		})
	}, "json");
	showOrHideAddEditWindow("#editteamtask");
}
//更改团队任务
function updateTeamTask(id, opType){
	$.post("updateTeamTask", {'opType':opType,'taskId':id}, function(data){
		getTeamTask(currentLi, currentMenu);
	}, "json");
}
//编辑团队项目
function editTeamProject(id){
	$.post("updateProject", {'opType':'getProject', 'projectId':id}, function(data){
		for(var propName in data){
			if(propName == "project"){
				$("#editTeamProject").find("input").each(function () {    
					if(this.name == 'project.selfId') $(this).val(data[propName].selfId);
				    if(this.name == 'project.title') $(this).val(data[propName].title);
				    if(this.name == 'project.content') $(this).val(data[propName].content);
				})
			}
		}
	}, "json");
	showOrHideAddEditWindow("#editTeamProject");
}
//编辑PGS
function editPGS(id, menu){
	if(menu == 'self') editSelfProject(id);
	else if(menu == 'goal') editSelfGoal(id);
	else if(menu == 'sight') editSelfSight(id);
}
function editSelfProject(id){
	$.post("updateProject", {'opType':'getProject', 'projectId':id}, function(data){
		for(var propName in data){
			if(propName == "project"){
				$("#editSelfProject").find("input").each(function () {    
					if(this.name == 'project.selfId') $(this).val(data[propName].selfId);
				    if(this.name == 'project.title') $(this).val(data[propName].title);
				    if(this.name == 'project.content') $(this).val(data[propName].content);
				})
			}
		}
	}, "json");
	showOrHideAddEditWindow("#editSelfProject");
}
function editSelfSight(id){
	$.post("updateSight", {'opType':'getSight', 'sightId':id}, function(data){
		for(var propName in data){
			if(propName == "sight"){
				$("#editSelfSight").find("input").each(function () {    
					if(this.name == 'sight.selfId') $(this).val(data[propName].selfId);
				    if(this.name == 'sight.title') $(this).val(data[propName].title);
				    if(this.name == 'sight.content') $(this).val(data[propName].content);
				})
			}
		}
	}, "json");
	showOrHideAddEditWindow("#editSelfSight");
}
function editSelfGoal(id){
	$.post("updateGoal", {'opType':'getGoal', 'goalId':id}, function(data){
		for(var propName in data){
			if(propName == "goal"){
				$("#editSelfGoal").find("input").each(function () {    
					if(this.name == 'goal.selfId') $(this).val(data[propName].selfId);
				    if(this.name == 'goal.title') $(this).val(data[propName].title);
				    if(this.name == 'goal.content') $(this).val(data[propName].content);
				})
			}
		}
	}, "json");
	showOrHideAddEditWindow("#editSelfGoal");
}
function updatePGS(type, id, opType){
	if(type == "self") updateProject(id, opType);
	else if(type == "goal") updateGoal(id, opType);
	else if(type == "sight") updateSight(id, opType);
}
//完成、删除个人目标
function updateGoal(id, opType){
	$.post("updateGoal", {'opType':opType,'goalId':id}, function(data){
		getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//删除个人情景
function updateSight(id, opType){
	$.post("updateSight", {'opType':opType,'sightId':id}, function(data){
		getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//完成、删除个人项目
function updateProject(id, opType){
	$.post("updateProject", {'opType':opType,'projectId':id}, function(data){
		getSelfPGS(currentLi, currentMenu);
	}, "json");
}
//完成、删除团队项目
function updateTeamProject(id, opType){
	$.post("updateProject", {'opType':opType,'projectId':id}, function(data){
		getTeamProject(currentLi, currentMenu);
	}, "json");
}
function updateTeam(id){
	$.post("updateTeam", {'opType':"delete",'teamId':id}, function(data){
		getTeams(currentLi);
	}, "json");
}






















































