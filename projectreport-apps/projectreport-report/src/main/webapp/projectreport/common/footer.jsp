<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div class="mai_footer">
		<div class="wrap">
			<div class="footer_infor">
				<a href="#">公司介绍</a><span>|</span><a href="#">品牌识别</a><span>|</span><a
					href="#">联系方式</a><span>|</span><a href="#">招商合作</a><span>|</span><a
					href="#">招聘信息</a><span>|</span><a href="#">团队建设</a><span>|</span><a
					href="#">隐私声明</a><span>|</span><a href="#">全国销售分布</a><span>|</span><a
					href="#">友情链接</a><span>|</span><a href="#">网站地图</a>
			</div>
			<p class="footer_txt">版权所有 大麦网 Copyright2003-2013 All Rights
				Reserved 京ICP备11043884号 京公网安备11010102000430号</p>

			<p class="footer_txt">北京红马传媒文化发展有限公司</p>

			<div class="footer_img">
				<a href="#"><img src="<%=basePath%>content/images/02.jpg" alt="" width="214"
					height="40" /></a> <a href="#"><img src="<%=basePath%>content/images/03.jpg" alt=""
					width="75" height="40" /></a>
			</div>
		</div>
</div>