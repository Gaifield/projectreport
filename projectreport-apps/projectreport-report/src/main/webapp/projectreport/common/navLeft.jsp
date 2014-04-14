<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="mai_left">
	<dl class="left_nav">
		<dt>报表分类</dt>
		<dd>
			<c:forEach var="item" varStatus="status" items="${reports}">
			<p<c:if test='${fn:contains(requestUrl,item.reportUrl)}'> class="active"</c:if>>
				<a href="<c:out value="${item.reportUrl}"/>?projectId=<c:out value="${projectId}"/>&source=<c:out value="${source}"/>"><c:out value="${item.reportName}"/></a>
			</p>
			</c:forEach>		
		</dd>
	</dl>
</div>