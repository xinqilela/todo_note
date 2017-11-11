//文件上传ajax
function ajaxFileUpload() {
	// 调用ajaxfileupload.js中的方法
	$.ajaxFileUpload({
		url : 'upLoadImg',// 上传图片要提交到的action
		secureuri : false,// 是否用安全提交，默认为false
		fileElementId : 'btnFile',// file选择文件的框的id
		dataType : 'json',// 数据返回格式，如果用json，需要修改ajaxfileupload.js中的内容
		success : function(data) {
			for ( var p in data) {
				if (p == "ajaxValues"){
					$("#userimg").attr('src', data[p].userlogo);
					$("#userlogo").attr('src', data[p].userlogo);
				}
			}
		}
	});
}

//个人信息修改ajax
function ajaxUpdateUserInfo(){
	$.post('updateUserInfo', $("#form2").serializeArray(), function(data){
		for(var propName in data){
			if(propName == "ajaxValues" && data[propName].alter != null){
				$("#usernamelink").text(data[propName].alter);
			}
		}
	});
}