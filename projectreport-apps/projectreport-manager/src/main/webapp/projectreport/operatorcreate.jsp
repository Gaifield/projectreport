<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>授权管理_新建操作员</title>
<jsp:include page="common/include.jsp"></jsp:include>

<!--jquery autocomplete组件  -->
<link rel="stylesheet"
	href=" <%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.css">
<script
	src=" <%=basePath%>thirdparty/jquery-autocomplete/jquery.autocomplete.min.js"></script>

<script type="text/javascript">
	//jquery autocomplete组件自动填充
	$(document).ready(function() {
		$("#tags").autocomplete("<%=basePath%>findOperatorsByKeyword.do", {
			extraParams : {
				userName : function() {
					return $('#tags').val();
				}
			},
			delay: 10,
            matchCase: false,
            selectFirst: true,
            cacheLength: 3,
            matchSubset:false,
            minChars: 1,     //最少输入字条
            max: 30,
            autoFill: false,    //是否选多个,用","分开
            //mustMatch: true,    //是否全匹配, 如数据中没有此数据,将无法输入
            matchContains: false,   //是否全文搜索,否则只是前面作为标准
            autoFill: false,    //自动填充
            scrollHeight: 220,
            width: 254,
            multiple: false,
			dataType : 'json',
			//加入对返回的json对象进行解析函数，函数返回一个数组    
			parse : function(data) {
				var rows = [];
				for (var i = 0; i < data.length; i++) {
					rows[rows.length] = {
						data : data[i].userName+" | "+ (data[i].userDept!=null ? data[i].userDept:""),
						value : data[i].userId,
						result : data[i].userName
					};
				}
				return rows;
			},
			formatItem : function(row, i, n) {
				return row;
			},formatMatch: function(row, i, max) {
                return row.userName;
            }			
		});
		});
	
	//选中弹出对话框
	function checkedOpen(){
		if($("#adminPower").attr("checked")){
			popLayer($('#layer_cho'));
		}	
	}
	
	//点击取消关闭弹层，滞空复选框
	function closeWin(obj){
		//1、关闭弹出层
		closeLayer(obj);
		
		//2、滞空复选框
		$("#adminPower").attr("checked",false);
	}
	
	//关闭弹出层
	function closeLayer(obj){
		$(obj).parents(".author_layer").hide();
		$(".mask").remove();
	}
	
	//保存操作员或管理员
	function sub(){
		var userName = $("#tags").val();
		var level = $('input[name="adminLevel"]:checked').val();
		if(level!=2){
			level = 1;
		}
		$.ajax({
            url:"<%=basePath%>saveOperator.do",
			type : "post",
			data : {
				userName : userName,
				level:level
			},
			success : function(msg) {
				var data = $.parseJSON(msg);
				
				if(data.status==200){
					window.location.href='<%=basePath%>operatorManage.do';
				}else if(data.status==100){
					alert(data.data);
				}else if(data.status==101){
					alert(data.data);
				}else if(data.status==500){
					alert(data.data);
				} 
			}
		});
	}
	
	//取消跳转
	function skit(){
		document.location.href='<%=basePath%>operatorManage.do';
	}
</script>

</head>

<body>
	<!--header begin-->
	<jsp:include page="common/top.jsp">
	<jsp:param value="3" name="code"/>
	</jsp:include>
	<!--header end-->

	<!--内容区 begin-->
	<div class="author_main clear">
		<div class="author_title">
			<h4>新建操作员</h4>
		</div>
		<div class="author_box" style="height: 300px;">
			<ul class="author_list">
				<li><span class="author_name">操 作 员：</span>

					<div class="author_refer">
						<input type="text" id="tags" name="userName" class="text" maxlength="50" />
					</div> <label class="checkbox ml20"
					onclick="checkedOpen();"> <input type="checkbox" id="adminPower" value="2" name="adminLevel"/> <span>授予管理员权限</span>
				</label></li>
			</ul>
			<div class="author_btn pl100">
				<a href="javascript:sub();" class="btn">保存</a> <a href="javascript:skit();" class="btn">取消</a>
			</div>
			<input type="hidden" id="id" />
		</div>
	</div>

	<!--内容区 end-->

	<!--底部 begin-->
	<jsp:include page="common/footer.jsp"></jsp:include>
	<!--底部 end-->


	<!--弹层-->
	<div class="author_layer layer_w400" id="layer_cho">
		<div class="layer_title">
			<a class="layer_close" href="#" title="关闭"></a>
			<h4>温馨提示</h4>
		</div>

		<div class="layer_con">
			<p class="layer_txt">勾选后操作员将会拥有管理员的所有权限</p>
		</div>

		<div class="layer_btn">
			<a class="btn" onclick="closeLayer(this);" href="#">确  定</a><a class="btn" href="#" onclick="closeWin(this);">取消</a>
		</div>

	</div>

</body>
</html>
