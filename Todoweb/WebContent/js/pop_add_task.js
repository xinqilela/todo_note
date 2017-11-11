$(document).ready(function () {
  $('#add_team_task').hide()
  $('#task_calender').hide()

  $('#add_self_task').hide()   //隐藏id为add_self_task的div（即隐藏添加个人任务的div）
  $('#covered').hide()         //隐藏id为covered的div（即隐藏覆盖div）
  
  
  //点击add_task按钮时，显示添加个人任务的div和覆盖div
  $('#add_task').click(function () {  
    $('#add_self_task').show()
    $('#covered').show()
  })
  
  
  //点击保存按钮时，隐藏添加个人任务的div和覆盖div
  $('#task_edit_button_save').click(function () { 
    $('#add_self_task').hide()
    $('#covered').hide()
  })
  
  
  //点击取消按钮时，隐藏添加个人任务的div和覆盖div
  $('#task_edit_button_cancle').click(function () {
    $('#add_self_task').hide()
    $('#covered').hide()
  })
  
  
  //个人信息小窗口下拉实现
  $('#setting_group').hide()  //隐藏id为setting_group的div
  $('#triangle-up').hide()   // 隐藏id为triangle-up的上三角
  $('#top_left').click(function () { // 点击用户名区域是下滑div，再次点击时上滑
    $('#setting_group').slideToggle('0.8s')
    $('#triangle-up').slideToggle('0.8s')
  })
  flatpickr('.flatpickr', {
    enableTime: true
  })
  
  

  $('#self_task_occupy').click(function(){
    $('#add_self_task').show()
    $('#covered').show()
  })
  $('#team_task_occupy').click(function(){
    $('#add_self_task').show()
    $('#covered').show()
  })
})