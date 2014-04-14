<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表平台_报表查询-预留统计</title>
<jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
	<!--顶部 begin-->
	<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
	<!--顶部 end-->
	<div class="m-nav">
		<div class="wrap">当前所在位置：
			<jsp:include page="/projectreport/common/location.jsp"></jsp:include>预留统计
		</div>
	</div>
	<!--内容区 begin-->
	<div class="mai_main clear">
		<!-- 左侧导航-->
		<jsp:include page="/projectreport/common/navLeft.jsp"></jsp:include>
		<!--右侧内容-->
		<div class="mai_wrap">
			<div class="right_ticket por">
				<div class="ticket_nav-bt">
					<ul class="ticket_nav fl">
						<li class="active"><a href="javascript:void(0)">明细</a></li>
					</ul>
					<div class="fr">
						<a class="back-link fr" href="javascript:history.back(-1);">返回<span class="r-cm">&gt;&gt;</span></a>
						<a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(2);"> <span><em
								class="down-ico"></em>导出pdf</span>
						</a> <a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(1);"> <span><em
								class="down-ico"></em>导出excel</span>
						</a>
					</div>
				</div>
				<div class="r-cnt">
					<div class="r-biaoti border-b">
						<span><c:out value="${projectName}"></c:out></span> <a class="r-choose fr"
							href="javascript:;" onclick="showPop()">重选场次</a>
					</div>
					<c:forEach var="item" varStatus="status" items="${reserveStatVo.reserveStatTypes}">
					<div class="qudao-tit pt10"><c:out value="${item.reserveStatName}"></c:out></div>
					<div class="yuliu-cnt">
						<table class="q-s-table mt10">
							<thead>
								<tr>
									<th colspan="2">价位</th>
									<th width="120">总预留</th>
									<c:forEach var="reserveTyleItem" varStatus="reserveTyleStatus" items="${reserveStatVo.reserveTyles}">
									<th width="120"><c:out value="${reserveTyleItem.tyleName}"></c:out></th>
									</c:forEach>
								</tr>
							</thead>
							<tbody>												
							<c:forEach var="rowItem" varStatus="rowStatus" items="${item.rows}">
							 <c:if test='${rowStatus.last}'><tfoot class="t-footer"></c:if>
								<tr>
									<td width="160" rowspan="2"><c:out value="${rowItem.price.priceShowName}"></c:out></td>
									<td width="120">总数量（张）</td>
									<c:forEach var="cell" varStatus="cellStatus" items="${rowItem.rowCells}">									
									<td><c:out value="${cell.quantity}"></c:out></td>
									</c:forEach>
								</tr>
								<tr>
									<td width="120">总金额（元）</td>
									<c:forEach var="cell" varStatus="cellstatus" items="${rowItem.rowCells}">									
									<td><fmt:formatNumber value="${cell.amount}" pattern="#.##" /></td>
									</c:forEach>
								</tr>
								<c:if test='${rowStatus.last}'></tfoot></c:if>							
						    </c:forEach>								
							</tbody>							
						</table>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
						<form id="frmSearch" method="post" action="<%=basePath%>reserveStatReport.do" >
						<input type="hidden" id="source" name="source" value="<c:out value="${source}"/>"/>
						<input type="hidden" id="action" name="action" value="0">
						<input type="hidden" id="projectId" name="projectId" value="<c:out value="${projectId}"/>">
						<input type="hidden" id="performIds" name="performIds" value="<c:out value="${performIds}"/>">
					</form>
	<!--内容区 end-->

	<!--底部 begin-->
	<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
	<!--底部 end-->
	<!--重选场次弹出层 开始-->
<div class="choose-cc">	
    <a class="c-pop-close" href="javascript:;">✖</a>
    <form id="frmSearchPerform">
    <input type="hidden" id="source" name="source" value="<c:out value="${source}"/>"/>
	<input type="hidden" name="projectId" value="<c:out value="${projectId}"/>">	
	<input type="hidden" id="performIds" name="performIds" value="<c:out value="${performIds}"/>">				
    <div class="r-search pl10 clear">
						<label>按演出时间搜索：</label> <input class="inp-txt1 w157 Wdate"
							type="text" id="startTime" name="startTime"
							pattern="yyyy-MM-dd HH:mm" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})">
							<span class="r-flag">至</span> <input class="inp-txt1 w157 Wdate"
							type="text" id="endTime" name="endTime"
							pattern="yyyy-MM-dd HH:mm" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\');}'})">
								<a class="cho_btn" href="javascript:queryPerforms();">搜索</a>
					</div>
    </form>
    <div class="r-s-table-cnt">
        <table class="r-s-table">
            <thead>
            <tr>
                <th width="120"><label class="radio-label"><input type="checkbox" id="cbAll" onclick="checkAll();">场次ID</label></th>
                <th width="160">场次名称</th>
                <th width="160">演出时间</th>
                <th>演出场馆</th>
            </tr>
            </thead>
            <tbody id="performs">           
            </tbody>
        </table>
    </div>
    <div class="s-btn-line">
        <span class="btn btn-dis mr20" id="btnDisabled">查看报表</span>
        <a class="btn mr20" href="javascript:searchPageData(0);"  style="display:none;" id="btnEnabled">查看报表</a>
        <a class="btn" href="javascript:closePop();">取消</a>
    </div>
</div>

<!--重选场次弹出层 结束-->
	<script type="text/javascript">
	
	function searchPageData(action){	
		$("#action").val(action);
		$("#performIds").val(Utils.getValueByName("performId",","));
		$("#frmSearch").submit();
		 if(action != 0){
	        	$(".rc-loading").removeClass("rc-loading");
	        }
	}
	
	function queryPerforms(){
		$("#performs").empty();
		var dataPara = getFormJson($("#frmSearchPerform"));		
        $.ajax({
            type: "GET",
            data: dataPara,
            dataType:"json",
            url: "<%=basePath%>queryPerform.do",
            success: function (resultData) {
                //场次列表
				var strLine='<tr>'
	            +'<td><label class="radio-label"><input type="checkbox" name="performId" value="$1" onclick="switchButton();" $6>$2</label></td>'
	            +'<td>$3</td>'
	            +'<td>$4</td>'
	            +'<td title="$5">$5</td>'
	            +'</tr>';
				var ckAll=false;	            
	            var ids = $("#performIds").val();
	            if(ids==""){
	            	ckAll=true;
	            }else{
	            	ids=","+ids+",";
	            }
	            var ckd=false;
            	for(var i=0;i<resultData.rows.length;i++){
            		var item = resultData.rows[i];   
            		var strCk="";            		
            		if(ckAll || ids.indexOf(","+item.performInfoID+",")>-1){
            			strCk="checked";
            			ckd=true;
            		}            		         	
            		$("#performs").append(strLine.format(item.performInfoID,item.performId,item.performName,item.performTime.replace("T"," "),item.fieldName,strCk));
            	}
            	switchButton(ckd);
            },
            error: function (msg) {}
        });
	}
	//全选/取消全选
	function checkAll(){
		var st = $("#cbAll").attr("checked");
		$("input[name=performId]").each(function(i){
			if(st){
				$(this).attr("checked",true);
			}else{
				$(this).attr("checked",false);
			}
		});
		switchButton(st);
	}
	
	//切换场次检索按钮
	function switchButton(checked){
	   if (checked== undefined){
			$("input[name=performId]").each(function(i){
				if($(this).attr("checked")){
					checked=true;
					return false;
				}
			});
		}	   
		if(checked){
			$("#btnEnabled").show();
			$("#btnDisabled").hide();
			
		}else{
			$("#btnEnabled").hide();
			$("#btnDisabled").show();			
		}
	}
	
    $(function () {
        $("input[name='chosse-date']").click(function () {
            var $vale = $(this).val();
            if ($vale == 3) {
                $(".r-date-cnt").show();
            } else {
                $(".r-date-cnt").hide();
            }
        });
    });
	function closePop(){
    	$(".choose-cc").hide();
    	$(".p-mask").fadeOut().remove();
    }    
    function showPop() {
    	queryPerforms();
        var $mask = '<div class="p-mask"></div>',
                $e = $(".choose-cc"),
                $w = $e.width(),
                $h = $e.height(),
                $W = $(window).width(),
                $H = $(window).height();

        $("body").append($mask);
        $(".p-mask").css({"height": $("html").height() + "px"});
        $e.show().css({
            "left": ($W - $w) / 2 + "px",
            "top": ($H - $h) / 2 + "px"
        });

        $(".c-pop-close").click(function () {
            $e.fadeOut();
            $(".p-mask").fadeOut().remove();
        });

        $("body").delegate(".p-mask", "click", function () {
            $e.fadeOut();
            $(this).fadeOut().remove();
        });
    }
</script>
</body>
</html>
