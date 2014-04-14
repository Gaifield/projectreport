<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_报表查询-公安</title>
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--顶部 begin-->
<div class="mai_above">
    <div class="wrap clear">
        <ul class="above_link fr">
            <li><em>欢迎，<c:out value="${sessionScope.userName}"></c:out></em><a href="#"></a></li>
            <li><span>|</span></li>
            <li><span>|</span></li>
            <li><a href="<%=basePath%>modifypwd.do">修改密码</a></li>
            <li><span>|</span></li>
            <li><a href="<%=basePath%>userLogOut.do">退出系统</a></li>
        </ul>
        <div class="above_logo">
            <a href="#"><img src="<%=basePath%>content/images/logo_bg.png"
                             alt="" width="86" height="30"></a>
        </div>
        <ul class="above_nav">
            <li class="active"}><a href="policeReport.do">快速查询</a></li>
        </ul>
    </div>
</div>
<!--顶部 end-->
<!--内容区 begin-->
<div class="mai_main clear">
    <!-- 左侧导航-->
    <div class="mai_left">
        <dl class="left_nav">
            <dt>报表分类</dt>
            <dd>
            	<c:forEach var="item" items="${projects}" ><!--<c:out value="${fn:substring(item.projectName, 0,8)}…"></c:out>  -->
                <p<c:if test='${item.projectId==projectId}'> class="active"</c:if>>
                <a href="policeReport.do?projectId=${item.projectId}&source=${item.dataResource}" title='<c:out value="${item.projectName}"></c:out>'>
                <s:set var="item.projectName" value=""></s:set>
                <c:choose>
    			<c:when test="${fn:length(item.projectName) > 8}">
     			<c:out value="${fn:substring(item.projectName, 0,8)}…"/>
    			</c:when>
    			<c:otherwise>
     			<c:out value="${item.projectName}" />
    			</c:otherwise>
   				</c:choose>
   				</a>
                </p>
				</c:forEach>
            </dd>
        </dl>
    </div>
    <!--右侧内容-->
    <div class="mai_wrap">
        <div class="right_ticket por">
            <div class="ticket_nav-bt">
                <ul class="ticket_nav fl">
                    <li class="active"><a href="javascript:void(0)">明细</a></li>
                </ul>
                <div class="fr">
                    <a class="back-link fr" href="javascript:history.back(-1);">返回<span class="r-cm">&gt;&gt;</span></a>
                    <a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(2);">
                        <span><em class="down-ico"></em>导出pdf</span>
                    </a>
                    <a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(1);">
                        <span><em class="down-ico"></em>导出excel</span>
                    </a>
                </div>
            </div>
            <div class="r-cnt">
                <div class="r-biaoti">
                    <span><c:out value="${projectName}"></c:out></span>
                    <a class="r-choose fr" href="javascript:;" onclick="showPop()">重选场次</a>
                </div>
                <c:forEach var="item" varStatus="status" items="${pageResult.rows}">
                <div class="cc-title-itme <c:if test='${status.index>0}'>mt20</c:if>" ><c:out value="${projectName}"></c:out>----<span class="col-b-link"><fmt:formatDate value="${item.performTime}" pattern="yyyy-MM-dd HH:mm" />（${item.fieldName}）</span>
                </div>
                <table class="r-s-table mt10">
                    <thead>
                    <tr>
                        <th>看台名称</th>
                        <th>总座位数量</th>
                        <th>防涨票数量</th>
                        <th>出票数量</th>
                        <th>剩余可用数量</th>
                        <th>上座率</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="sumSeat" value="0"/>
                    <c:set var="sumProtect" value="0"/>
                    <c:set var="sumSale" value="0"/>
                    <c:set var="sumRemained" value="0"/>                    
                    <c:forEach var="stand" varStatus="index" items="${item.list}">                    	
                    	<tr>
                        <td>${stand.standName}</td>
                        <td>${stand.seatQuantity}</td>
                        <td>${stand.protectQuantity}</td>
                        <td>${stand.saleQuantity}</td>
                        <c:set var="remained" value="${stand.seatQuantity-stand.protectQuantity-stand.saleQuantity}"/>
                        <td>${remained}</td>
                        <td>
                        <c:choose>
                        	<c:when test="${stand.seatQuantity>0}">
                        		<c:set var="percent" value="${stand.saleQuantity/stand.seatQuantity *100}"/>
                        		<fmt:formatNumber value="${percent}" pattern="#.##" />%
                        	</c:when>
                        	<c:otherwise>
                        		<fmt:formatNumber value="0" pattern="#.##" />%
                        	</c:otherwise>
                        </c:choose>
                        </td>
                    	</tr>
	 					<c:set var="sumSeat" value="${sumSeat+stand.seatQuantity}"/>
	                    <c:set var="sumProtect" value="${sumProtect+stand.protectQuantity}"/>
	                    <c:set var="sumSale" value="${sumSale+stand.saleQuantity}"/>
	                    <c:set var="sumRemained" value="${sumRemained+remained}"/>                    	 
                    </c:forEach> 
                    </tbody>                   
                    <tfoot class="t-footer">
                    	<tr>
                        <td>总计</td>
                        <td>${sumSeat}</td>
                        <td>${sumProtect}</td>
                        <td>${sumSale}</td>
                        <td>${sumRemained}</td>
                        <td>
                        <c:choose>
                        	<c:when test="${sumSeat>0}">
                        		<c:set var="sumPercent" value="${sumSale/sumSeat *100}"/>
                        		<fmt:formatNumber value="${sumPercent}" pattern="#.##" />%
                        	</c:when>
                        	<c:otherwise>
                        		<fmt:formatNumber value="0" pattern="#.##" />%
                        	</c:otherwise>
                        </c:choose>  
                        </td>
                    	</tr>
                    </tfoot>
                </table>
                </c:forEach>
                <div class="right_stern">
					<form id="frmSearch" method="post" action="<%=basePath%>policeReport.do" >
						<input type="hidden" id="source" name="source" value="<c:out value="${source}"></c:out>"/>
						<input type="hidden" id="action" name="action" value="0" />
						<input type="hidden" id="projectId" name="projectId" value="${projectId}" />
						<input type="hidden" id="performIds" name="performIds" value="<c:out value="${performIds}"></c:out>" />
						<input type="hidden" id="page" name="page" value="${page}" />
					</form>
					 <script type="text/javascript">
					 var pg=new showPages({name:'pg',total:parseInt('${pageResult.total}'),size:parseInt('${rows}'),page:parseInt('${page}'),
						 paging:function(page){
							 pageChang(page);
					 	 }
					 });
					 pg.printHtml(0);
					</script>				
				</div>
            </div>
        </div>
    </div>
</div>
<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->

<!--重选场次弹出层 开始-->
<div class="choose-cc">
    <a class="c-pop-close" href="javascript:;">✖</a>
    <form id="frmSearchPerform">
    <input type="hidden" name="source" value="<c:out value="${source}"></c:out>" />
	<input type="hidden" name="projectId" value="${projectId}" />					
<div class="r-search pl10 clear">
						<label>按演出时间搜索：</label> <input class="inp-txt1 w157 Wdate"
							type="text" id="startTime" name="startTime"
							pattern="yyyy-MM-dd HH:mm" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\');}'})" />
							<span class="r-flag">至</span> <input class="inp-txt1 w157 Wdate"
							type="text" id="endTime" name="endTime"
							pattern="yyyy-MM-dd HH:mm" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'startTime\');}'})" />
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
function pageChang(page){
	$("#page").val(page);
	$("#action").val("0");
	$("#frmSearch").submit();	
}

function searchPageData(action){	
	$("#action").val(action);
	$("#page").val(1);
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
        $(".p-mask").css({"height":$("html").height() + "px"})
        $e.show().css({
            "left":($W - $w) / 2 + "px",
            "top":($H - $h) / 2 + "px"
        })

        $(".c-pop-close").click(function(){
            $e.fadeOut();
            $(".p-mask").fadeOut().remove();
        })

        $("body").delegate(".p-mask", "click", function () {
            $e.fadeOut();
            $(this).fadeOut().remove();
        })
    }
</script>
</body>
</html>