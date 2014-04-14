/*------------------------------------------------
Create By:			CaoNing
Create Time:		2014-01-20 10:00
*Filename:			script.js
*Version:			1.0.0.0
*Website:			http://www.damai.cn
*Page width:		1000px
------------------------------------------------*/
//弹出层
function popLayer(ele) {
	var $ele = $(ele),
			$win = $(window),
			$winH = $win.height(),
			$winW = $win.width(),
			$mask = $("<div class='mask'></div>"),
			$eleW = 0,
			$eleH = 0,
			$docS = $(document).scrollTop();
	$("body").append($mask.css({
		"height":$(document).height() + "px"
	}))

	$ele.show().css({
		"top":($winH - $ele.height()) / 2 + $docS + "px",
		"left":($winW - $ele.width()) / 2 + "px",
		"zIndex":$mask.css("z-index") + 1
	})

	$(".layer_close").click(function () {
		$(this).parents(".author_layer").hide();
		$mask.remove()
		return false
	})
	
}

$(function(){	
	var iIndex=2;
	
	$('.datagrid-cell').hover(function(){
		$(this).css('z-index', iIndex++);
		$(this).find('.tab_tips').show();
	},function(){
		$(this).find('.tab_tips').hide();
	});
	
})