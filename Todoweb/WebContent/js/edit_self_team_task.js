$(document).ready(function() {
     $('#covered').hide()
     $('#add_self_task').hide()
     $('#self_task_occupy').click(function(){
         $('#covered').show()
         $('#add_self_task').show()
     })
     $('#task_edit_button_cancle').click(function(){
         $('#covered').hide()
         $('#add_self_task').hide()
     })     
     $('#add_team_task').hide()
     $('#team_task_occupy').click(function(){
         $('#covered').show()
         $('#add_team_task').show()
     })
     $('#team_task_cancle_save').click(function(){
         $('#covered').hide()
         $('#add_team_task').hide()
     })
     $('#setting_group').hide()
     $('#triangle-up').hide()
     $('#top_left').click(function () {
    $('#setting_group').slideToggle('0.8s')
    $('#triangle-up').slideToggle('0.8s')
  })
  flatpickr('.flatpickr', {
    enableTime: true
  })
})