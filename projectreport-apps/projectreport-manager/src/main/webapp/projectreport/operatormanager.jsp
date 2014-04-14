<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>授权管理_操作员管理</title>
<jsp:include page="common/include.jsp"></jsp:include>
<script type="text/javascript">
	
$(function(){
	query();
	$('#state').combobox('readonly', true);
	$('#level').combobox('readonly', true);
    var editing = false;

     $("#dg").datagrid({
        url: '<%=basePath%>showAllOperator.do',
        //queryParams: queryData(),
        columns: [
            [
                {
                    field: 'operatorName',
                    title: '操作员',
                    width: '180'

                },
                {
                    field: 'statusName',
                    title: '状态',
                    width: '150'
                },
                {
                    field: 'createTimeFormat',
                    title: '创建时间',
                    width: '150'
                },
                {
                    field: 'operatorDept',
                    title: '组织架构',
                    width: '150'
                },
                {
                    field: 'permissionLevelName',
                    title: '权限级别',
                    width: '200',
                    formatter: function (value, row, index) {
                        var operatorId = row.operatorId;
                        if(row.editing){
                        	
                            var o = '<select id="operator'+ operatorId+'" class="select"><option value="1"';
                            if(row.permissionLevel==1){
                            	o+=" selected ";
                            }
                            o+='>操作员权限</option><option value="2"';
                            if(row.permissionLevel==2){
                            	o+=" selected ";
                            }
                            o+='>管理员权限</option></select><a href="javascript:saveOperatorChange('+operatorId+')" class="ml5">保存</a>';
                            return o;
                        }else{
                            var o = '<span>' + value + '</span><a href="#" class="ml5" onclick="editOperator(' + index+', '+operatorId+ ')">更改</a>';
                            return o;
                        }
                    }

                },
                {
                	 field: 'reverseStatusName',
                     title: '操作',
                     width: '168',
                     formatter: function (value, row, index) {
                         var operatorId = row.operatorId;
                         var status = row.status;
                         var reverseStatus = row.reverseStatus;
                         var reverseStatusName = row.reverseStatusName;
                         if(status==1){                           
                             var o = '<span><a href="javascript:operate('+reverseStatus+','+operatorId+');" style="color:#FF0033;">'+reverseStatusName+'</a></span><a href="javascript:deleteOperator('+operatorId+');" class="ml5">删除</a>';
                         	 return o;
                         }else{
                        	 var o = '<span class="cor2 cur"><a href="javascript:operate('+reverseStatus+','+operatorId+');">'+reverseStatusName+'</a></span><a href="javascript:deleteOperator('+operatorId+');" class="ml5">删除</a>';
                             return o;
                         }
                     }	
                }
           ]
        ],
        pagination: true,
        onBeforeEdit : function(index, row) {
            row.editing = true;
            updateActions();
        },
        onAfterEdit : function(index, row) {
            row.editing = false;
            updateActions();
        },
        onCancelEdit : function(index, row) {
            row.editing = false;
            updateActions();
        },
        onBeforeLoad: function () {
            $("#dg").datagrid("clearSelections");
        },
        onLoadSuccess: function (data) {
            $("#dg").datagrid("clearSelections");
        },
        onLoadError: function () {
            //alert("获取数据失败！");
        }
    }); 
});
	
    //启动编辑功能
    function editOperator(index, operatorId) {
    	 //编辑时，取消其他编辑器的编辑
        var rowcount = $('#dg').datagrid('getRows').length;
        for (var i = 0; i < rowcount; i++) {
            if (i != index) {
                $('#dg').datagrid('cancelEdit', i);
            }
        }
        //打开编辑器，开始编辑
        $('#dg').datagrid('beginEdit', index);
    }
    
  //更新表格
    function updateActions() {
        var rowcount = $('#dg').datagrid('getRows').length;
        for (var i = 0; i < rowcount; i++) {
            $('#dg').datagrid('updateRow', {
                index: i,
                row: {action: ''}
            });
        }
    }
  
  	var tag = true;
  	var currentOperId = 0;
  	var currentOperLevel = 0;
  //保存权限级别
  	function saveOperatorChange(operatorId) {
  		currentOperId = operatorId;
  		currentOperLevel = $("#operator" + operatorId).val();
  	  if(currentOperLevel==2){
  		  popLayer($('#layer_save'));
  	  }else{
  		  completeOperatorLevel();
      }
	  }
  
  //完成改变操作员权限级别
  function completeOperatorLevel(){
	  $.ajax({
          url: '<%=basePath%>updateLevel.do',
          data: {operatorId:currentOperId,level:currentOperLevel},
          dataType:"json",
          success: function (msg) {
				if(msg.status!=200){
					popLayer($('#layer_del'));
				}
          	 $("#dg").datagrid("reload");
          }
      });
  }
  
  
   //关闭弹出层
	function closeLayer(obj){
		$(obj).parents(".author_layer").hide();
		$(".mask").remove();
	}
  
	//关闭操作员等级权限弹层
	function checkCloseLevelWin(obj,boo){
		closeLayer(obj);
	  if(boo){
		  completeOperatorLevel();
	  }else{
	  	  $("#dg").datagrid("reload");
	  }
	}
  
  
  //禁用或启用
    function operate(reverseStatus,operatorId) {
	  $.ajax({
            url: '<%=basePath%>updateStatus.do',
            data: {operatorId:operatorId,status:reverseStatus},
            dataType:"json",
            success: function (msg) {
            	if(msg.status!=200){
					popLayer($('#layer_del'));
				}
            	 $("#dg").datagrid("reload");
            }
        });
    }
  
  	//删除操作
    function deleteOperator(operatorId) {
	  var status = 3;
	  $.ajax({
            url: '<%=basePath%>updateStatus.do',
            data: {operatorId:operatorId,status:status},
            dataType:"json",
            success: function (msg) {
            	if(msg.status!=200){
					popLayer($('#layer_del'));
				}
            	 $("#dg").datagrid("reload");
            }
        });
    }
    
  	//获取查询参数
    function query() {
        var queryData = {"userName": $("#userName").val(), "status": $("#userStatus").combobox('getValue'), "level": $("#userLevel").combobox('getValue')};
        $("#dg").datagrid({queryParams: queryData, pageNumber: 1}, 'load');
    }
</script>
</head>

<body>
	<!--header begin-->
	<jsp:include page="common/top.jsp">
	<jsp:param value="2" name="code"/>
	</jsp:include>
	<!--header end-->

	<!--内容区 begin-->
	<div class="author_main clear">
		<div class="author_cho">
			<div class="cho_line">
				<span class="cho_title">操作员：</span> <input type="text" class="text"
					id="userName" style="width: 175px;" /> <span
					class="cho_title ml30">状态：</span> <select id="userStatus"
					class="easyui-combobox" editable = false  name="state" style="width: 120px;">
					<option value="0">全部</option>
					<option value="1">启用</option>
					<option value="2">禁用</option>
				</select> <span class="cho_title ml30">权限级别：</span> <select id="userLevel"
					class="easyui-combobox" editable = false name="level" style="width: 120px;">
					<option value="0">全部</option>
					<option value="1">操作员权限</option>
					<option value="2">管理员权限</option>
				</select> <a class="cho_btn" href="javascript:query();">搜索</a>
			</div>
		</div>

		<div class="author_con">
			<table id="dg">
				<thead>
					<tr>
						<th data-options="field:'title01'" width="180">操作员</th>
						<th data-options="field:'title02'" width="150">状态</th>
						<th data-options="field:'title04'" width="150">创建时间</th>
						<th data-options="field:'title03'" width="150">组织架构</th>
						<th data-options="field:'title05'" width="200">权限级别</th>
						<th data-options="field:'title06'" width="168">操作</th>
					</tr>
				</thead>
			</table>

			<!-- <div class="easyui-pagination" data-options="total:114,layout:['list','sep','first','prev','links','next','last','sep','refresh']"></div> -->
		</div>
	</div>

	<!--内容区 end-->

	<!--底部 begin-->
	<jsp:include page="common/footer.jsp"></jsp:include>
	<!--底部 end-->


	<!--弹层-->
	<div class="author_layer layer_w400" id="layer_save">
		<div class="layer_title">
			<a class="layer_close" href="#" title="关闭"></a>
			<h4>温馨提示</h4>
		</div>

		<div class="layer_con">
			<p class="layer_txt">更改后此操作员将会拥有管理员的所有权限</p>
		</div>

		<div class="layer_btn">
			<a class="btn" onclick="checkCloseLevelWin(this,true);" href="#">确定</a><a
				class="btn" onclick="checkCloseLevelWin(this,false);" href="#">取消</a>
		</div>

	</div>

	<div class="author_layer layer_w400" id="layer_del">
		<div class="layer_title">
			<a class="layer_close" href="#" title="关闭"></a>
			<h4>温馨提示</h4>
		</div>

		<div class="layer_con">
			<p class="layer_txt">系统至少保留一个管理员和操作员，不能更改、禁用或删除</p>
		</div>
		<div class="layer_btn">
			<a class="btn" href="#" onclick="closeLayer(this);">关闭</a> 
		</div>
	</div>

</body>
</html>
