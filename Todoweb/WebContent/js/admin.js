function usermanager(){
	document.getElementById("usermanager").style.display = 'block';
	document.getElementById("systemsetting").style.display = 'none';
	document.getElementById("oneinfo").style.display = 'none';
}

function systemsetting(){
	document.getElementById("systemsetting").style.display = 'block';
	document.getElementById("usermanager").style.display = 'none';
	document.getElementById("oneinfo").style.display = 'none';
}

function oneinfo(){
	document.getElementById("oneinfo").style.display = 'block';
	document.getElementById("usermanager").style.display = 'none';
	document.getElementById("systemsetting").style.display = 'none';
}

function deleteUser(){
	users = document.getElementsByClassName("userId");
	var str = "";
	for(var i = 0; i < users.length; i++){
		if(users[i].checked == true){
			if(i == users.length-1)
				str += users[i].value;
			else
				str += users[i].value + ",";
		}
	}
	if(str == ""){
		alert("请选择要删除的用户!");
		return;
	}
	a = document.getElementById("delete");
	a.href = "deleteUser?deleteIds=" + str.toString() + "&pageNow=" + document.getElementById("pageNow").value;
	a.onclick();
}

function deleteUser_oneinfo(){
	users = document.getElementsByClassName("userId");
	var str = "";
	for(var i = 0; i < users.length; i++){
		if(users[i].checked == true){
			if(i == users.length-1)
				str += users[i].value;
			else
				str += users[i].value + ",";
		}
	}
	if(str == ""){
		alert("请选择要删除的用户!");
		return;
	}
	a = document.getElementById("oneinfo_delete");
	a.href = "deleteUser?deleteIds=" + str.toString() + "&pageNow=" + document.getElementById("pageNow").value;
	a.onclick();
}

function toRoot(){
	users = document.getElementsByClassName("userId");
	var str = "";
	for(var i = 0; i < users.length; i++){
		if(users[i].checked == true){
			if(i == users.length-1)
				str += users[i].value;
			else
				str += users[i].value + ",";
		}
	}
	if(str == ""){
		alert("请选择要提权的用户!");
		return;
	}
	a = document.getElementById("toRoot");
	a.href = "toRoot?toRoots=" + str.toString() + "&pageNow=" + document.getElementById("pageNow").value;
	a.onclick();
}

function toRoot_oneinfo(){
	users = document.getElementsByClassName("userId");
	var str = "";
	for(var i = 0; i < users.length; i++){
		if(users[i].checked == true){
			if(i == users.length-1)
				str += users[i].value;
			else
				str += users[i].value + ",";
		}
	}
	if(str == ""){
		alert("请选择要提权的用户!");
		return;
	}
	a = document.getElementById("toRoot_oneinfo");
	a.href = "toRoot?toRoots=" + str.toString() + "&pageNow=" + document.getElementById("pageNow").value;
	a.onclick();
}