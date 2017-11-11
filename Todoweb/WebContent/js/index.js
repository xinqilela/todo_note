$(document).ready(function () {
  var currentIndex = 0
  var imgLength = $('.img').length
  // 自动轮播效果的实现
  // 定义计时器
  var autoChange = setInterval(function () {
    currentIndex = currentIndex < imgLength - 1 ? currentIndex + 1 : 0
    changeTo(currentIndex)
  }, 6000)
  // 选中圆点效果实现
  $('#radio_button').find('div').each(function (item) {
    $(this).click(function () {
      clearInterval(autoChange)
      changeTo(item)
      currentIndex = item
      autoChangeAgain()
     
    })
  })
  // 重置计时器
  function autoChangeAgain () {
    autoChange = setInterval(function () {
      currentIndex = currentIndex < imgLength - 1 ? currentIndex + 1 : 0
      changeTo(currentIndex)
    }, 3000)
  }
  // 鼠标放到左箭头
  $('#back').hover(function () {
    clearInterval(autoChange)
  }, function () {
    autoChangeAgain()
  })
  // 鼠标放到右箭头
  $('#head').hover(function () {
    clearInterval(autoChange)
  }, function () {
    autoChangeAgain()
  })
  // 左箭头点击处理
  $('#back').click(function () {
    currentIndex = (currentIndex > 0) ? (--currentIndex) : (imgLength - 1)
    changeTo(currentIndex)
  })
  // 右箭头点击处理
  $('#head').click(function () {
    currentIndex = (currentIndex < imgLength - 1) ? (++currentIndex) : 0
    changeTo(currentIndex)
  })
})
function changeTo (num) {
  switch(num){
  case 0:
	  $('#show_img').animate({ left: '0%' }, 'fast')
	  $('#first_circle').css("background-color","#6B59CE");
	  $('#second_circle').css("background-color","#FFFFFF");
	  break;
  case 1:
	  $('#show_img').animate({ left: '-100%' }, 'fast')
	  $('#first_circle').css("background-color","#FFFFFF");
	  $('#second_circle').css("background-color","#6B59CE");
	  break;
  }
}
