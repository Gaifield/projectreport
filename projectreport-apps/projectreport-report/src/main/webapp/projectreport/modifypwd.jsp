<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>授权管理_修改密码</title>
<jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
	<!--顶部 begin-->
	<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
	<!--顶部 end-->

	<!--内容区 begin-->
	<div class="author_main clear">
		<div class="author_password">
			<ul class="password_con">
				<li><span>原始密码：</span> <input id="oldPassword" type="password"
					class="text" />

					<p id="oldPasswordError" style="display: none">*原始密码输入错误</p></li>
				<li><span>新 密 码：</span> <input id="newPassword" type="password"
					class="text" onkeyup="sureNewPassword()" />

					<p id="newPasswordError" style="display: none">*密码修改失败<!-- 包含非法字符，请重新输入 --></p></li>
				<li><span>确认新密码：</span> <input id="newPasswordAgain"
					type="password" class="text" onkeyup="sureNewPassword()" />

					<p id="newPasswordAgainError" style="display: none">*两次输入不一致</p></li>
			</ul>
			<div class="author_btn">
				<a href="#" class="btn mr20" onclick="submit()">保存</a> <a href="queryProjectSellAndOver.do"
					class="btn">取消</a>
			</div>
		</div>
	</div>

	<!--内容区 end-->

	<!--密码修改成功提示-->
	<div class="author_layer layer_w400" id="modifysuccess">
	    <div class="layer_title">
	        <a class="layer_close" href="queryProjectSellAndOver.do" title="关闭"></a>
	        <h4>温馨提示</h4>
	    </div>
	
	    <div class="layer_con">
	        <div class="layer_txt tc">恭喜您，密码修改成功。 </div>
	    </div>
	    <div class="layer_btn"><a class="btn" href="queryProjectSellAndOver.do"  >确定</a></div>
	</div>
	<!--底部 begin-->
	<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
	<!--底部 end-->
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>content/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		href="<%=basePath%>content/easyui/themes/icon.css">
	<script type="text/javascript"
		src="http://dui.damai.cn/damai_v2/common/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>content/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		$(function() {

			$("#oldPassword").validatebox({
				required : true,
				missingMessage : "旧密码不能为空"
			});
			$("#newPassword").validatebox({
				required : true,
				missingMessage : "新密码不能为空"
			});
			$("#newPasswordAgain").validatebox({
				required : true,
				missingMessage : "新密码确认不能为空"
			});

		});

		//点击保存按钮触发该方法
		function submit() {
			$("#oldPasswordError").attr("style","display:none;");
			$("#newPasswordError").attr("style","display:none;");
			$("#newPasswordAgainError").attr("style", "display:none;");
			
			//确认三个输入框验证通过
			if ($("#oldPassword").validatebox('isValid') && $("#newPassword").validatebox('isValid')
					&& $("#newPasswordAgain").validatebox('isValid')) {
					
				var oldPwd = $("#oldPassword").val();
				var newPwd = $("#newPassword").val();
				var newPwdAgain = $("#newPasswordAgain").val();

				//确认新老密码输入正确
				if (newPwd == newPwdAgain) {
						$.ajax({
							url:"<%=basePath%>modifyPassword.do",
							data:{'oldPassword':oldPwd ,'newPassword':newPwd},
							success:function(msg){
								msg = eval("("+msg+")");
								//1: "密码错误" 2: "密码修改成功" 3: "密码修改失败"
								 var data = msg.data;
								if(data == 1){
									//alert("修改成功");
									popLayer("#modifysuccess");
								}else if(data == 2){
									$("#oldPasswordError").attr("style","display:inline;");
								}else {
									$("#newPasswordError").attr("style","display:inline;");
								}
								
							}
						});
				} else {
					$("#newPasswordAgainError").attr("style", "display:inline;");
				}
			}
		}

		//确认新密码
		function sureNewPassword() {
			if ($("#newPassword").val() != $("#newPasswordAgain").val()) {
				$("#newPasswordAgainError").attr("style", "display:inline;");
			} else {
				$("#newPasswordAgainError").attr("style", "display:none;");
			}
		}
		
		//<!--弹层-->
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
            "height": $(document).height() + "px"
        }))

        $ele.show().css({
            "top": ($winH - $ele.height()) / 2 + $docS + "px",
            "left": ($winW - $ele.width()) / 2 + "px",
            "zIndex": $mask.css("z-index") + 1
        })
    }
    
	</script>
</body>
</html>
