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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报表平台_报表查询-跨项目</title>
    <jsp:include page="common/include.jsp"></jsp:include>
</head>

<body>
<!--顶部 begin-->
<jsp:include page="/projectreport/common/top.jsp"></jsp:include>
<!--顶部 end-->
<div class="m-nav">
    <div class="wrap">当前所在位置：
        <jsp:include page="/projectreport/common/location.jsp"></jsp:include>总销售金额统计表
    </div>
</div>
<!--内容区 begin-->
<div class="mai_main clear">
    <!-- 左侧导航-->
    <div class="mai_left">
        <dl class="left_nav">
            <dt>报表分类</dt>
            <dd>
                <p class="active"><a href="#">总销售金额统计</a></p>
            </dd>
        </dl>
    </div>
    <!--右侧内容-->
    <div class="mai_wrap">
        <div class="right_ticket por">
            <div class="ticket_nav-bt">
                <ul class="ticket_nav fl">
                    <li class="active"><a href="javascript:void(0)">汇总</a></li>
                </ul>
                <div class="fr">
                	<form id="frmSearch" method="post" action="<%=basePath%>multiProjectReport.do" >
                		<input type="hidden" id="action" name="action" value="0" />
						<input type="hidden" id="projectIds" name="projectIds" value="<c:out value="${projectIds}"></c:out>" />
						
                    <a class="back-link fr" href="javascript:history.back(-1);">返回<span class="r-cm">&gt;&gt;</span></a>
                    <a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(2);">
                        <span><em class="down-ico"></em>导出pdf</span>
                    </a>
                    <a class="down-btn mt7 mr10 fr" href="javascript:searchPageData(1);">
                        <span><em class="down-ico"></em>导出excel</span>
                    </a>
                    </form>
                </div>
            </div>
            <div class="r-cnt">
                <div class="yuliu-cnt">
                    <table class="q-s-table mt10">
                        <thead>
                        <tr>
                            <th width="100">项目ID</th>
                            <th width="150">项目名称</th>
                            <th width="80">项目状态</th>
                            <th width="130">演出开始时间</th>
                            <th width="130">演出结束时间</th>
                            <th width="110">演出场馆</th>
                            <th width="110">本日销售金额</th>
                            <th width="100">总销售金额</th>
                            <th>剩余票房</th>
                        </tr>
                        </thead>
                        <tbody>
                    	<c:set var="sumToday" value="0"/>
                    	<c:set var="sumSale" value="0"/>
                    	<c:set var="sumRemained" value="0"/>  
                        <c:forEach var="item" varStatus="status" items="${projects}">
                        <tr>
                            <td>${item.piaoCnId}</td>
                            <td>
                                <div class="d-w140" title="<c:out value="${item.projectName}"></c:out>" style= "overflow:hidden;text-overflow:ellipsis;white-space:nowrap;"><c:out value="${item.projectName}"></c:out></div>
                            </td>
                            <td>
                            <c:choose>  
						    	<c:when test="${item.projectStatus==4}">正在销售</c:when>    
						    	<c:when test="${item.projectStatus==5}">已经结束</c:when> 
							</c:choose>
                            </td>
                            <td>
                            <div class="d-w120" title="${item.startTime}" style= "overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${item.startTime}</div>
                            </td>
                            <td>
                            <div class="d-w120" title="${item.endTime}" style= "overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${item.endTime}</div>
                            </td>
                            <td>
                                <div class="d-w105" title="${item.performField}" style= "overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${item.performField}</div>
                            </td>
                            <td><fmt:formatNumber value="${item.todayMoney}" pattern="#.##" /></td>
                            <td><fmt:formatNumber value="${item.totalMoney}" pattern="#.##" /></td>
                            <td><fmt:formatNumber value="${item.remainingBoxOffice}" pattern="#.##" /></td>
                        </tr>
	                    	<c:set var="sumToday" value="${sumToday+item.todayMoney}"/>
	                    	<c:set var="sumSale" value="${sumSale+item.totalMoney}"/>
	                    	<c:set var="sumRemained" value="${sumRemained+item.remainingBoxOffice}"/> 
                        </c:forEach>
                        </tbody>
                        <tfoot class="fb">
                        <tr>
                            <td>总计：</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td><fmt:formatNumber value="${sumToday}" pattern="#.##" /></td>
                            <td><fmt:formatNumber value="${sumSale}" pattern="#.##" /></td>
                            <td><fmt:formatNumber value="${sumRemained}" pattern="#.##" /></td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!--内容区 end-->

<!--底部 begin-->
<jsp:include page="/projectreport/common/footer.jsp"></jsp:include>
<!--底部 end-->

<script type="text/javascript">
	function searchPageData(action){	
		$("#action").val(action);
		$("#frmSearch").submit();
		 if(action != 0){
	        	$(".rc-loading").removeClass("rc-loading");
	        }
	}
</script>
</body>
</html>