function showPages(pams) { // 初始化属性
	this.name = pams.name; // 对象名称
	this.total=isNaN(pams.total)?0:pams.total;
	this.size=isNaN(pams.size)?0:pams.size;
	this.page = isNaN(pams.page)?1:pams.page; // 当前页数
	this.pageCount = this.getPageCount(); // 总页数
	this.argName = 'page'; // 参数名
	this.showTimes = 0; // 打印次数
	this.paging=pams.paging;
	this.info= pams.info ? pams.info : '场次信息';
}
showPages.prototype.getPageCount = function() {
	var pc = this.total/this.size;
	if((this.total % this.size)>0)
		++pc;
	return pc;
};

showPages.prototype.getPage = function() { // 丛url获得当前页数,如果变量重复只获取最后一个
	/*
	var args = location.search;
	var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
	var chk = args.match(reg);
	this.page = RegExp.$1;
	*/
	return this.page;
};
showPages.prototype.checkPages = function() { // 进行当前页数和总页数的验证
	if (isNaN(parseInt(this.page)))
		this.page = 1;
	if (isNaN(parseInt(this.pageCount)))
		this.pageCount = 1;
	if (this.page < 1)
		this.page = 1;
	if (this.pageCount < 1)
		this.pageCount = 1;
	if (this.page > this.pageCount)
		this.page = this.pageCount;
	this.page = parseInt(this.page);
	this.pageCount = parseInt(this.pageCount);
};

showPages.prototype.createHtml = function(mode) { // 生成html代码
	var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
	if (mode == '' || typeof (mode) == 'undefined')
		mode = 0;
	// 模式2 (前后缩略,页数,首页,前页,后页,尾页)
	if (prevPage < 1) {
		strHtml += '<span>上一页</span>';
	} else {
		strHtml += '<a href="javascript:' + this.name
				+ '.toPage(' + prevPage + ');">上一页</a>';
	}
	if (this.page != 1)
		strHtml += '<a href="javascript:' + this.name+ '.toPage(1);">1</a>';
	if (this.page >=5)
		strHtml += '<span>...</span>';
	var endPage=1;
	if (this.pageCount > this.page +2) {
		endPage = this.page + 2;
	} else {
		endPage = this.pageCount;
	}
	for ( var i = this.page - 2; i <= endPage; i++) {
		if (i > 0) {
			if (i == this.page) {
				strHtml += '<span>' + i + '</span>';
			} else {
				if (i != 1 && i != this.pageCount) {
					strHtml += '<a href="javascript:' + this.name + '.toPage('
							+ i + ');">' + i + '</a>';
				}
			}
		}
	}
	if (this.page + 5 < this.pageCount)
		strHtml += '<span>...</span>';
	if (this.page != this.pageCount)
		strHtml += '<a href="javascript:' + this.name + '.toPage('
				+ this.pageCount + ');">' + this.pageCount + '</a>';
	if (nextPage > this.pageCount) {
		strHtml += '<span>下一页</span>';
	} else {
		strHtml += '<a href="javascript:' + this.name
				+ '.toPage(' + nextPage + ');">下一页</a>';
	}
	strHtml += '<div class="page-sur">';
	strHtml += '<em class="fl">到第</em><input class="page-sur-inp fl" type="text" id="pageInput' + this.showTimes + '" value="' + this.page + '" onkeypress="return ' + this.name + '.formatInputPage(event);" onfocus="this.select()" onblur="' + this.name + '.checkPage(document.getElementById(\'pageInput' + this.showTimes + '\'));" ><em class="fl">页</em>';
	strHtml += '<button class="page-sur-btn fl" type="button" onclick="' + this.name + '.toPage(document.getElementById(\'pageInput' + this.showTimes + '\').value);">确定</button>';
	strHtml += '</div>';

	return strHtml;
};
showPages.prototype.createUrl = function(page) { // 生成页面跳转url
	if (isNaN(parseInt(page)))
		page = 1;
	if (page < 1)
		page = 1;
	if (page > this.pageCount)
		page = this.pageCount;
	var url = location.protocol + '//' + location.host + location.pathname;
	var args = location.search;
	var reg = new RegExp('([\?&]?)' + this.argName + '=[^&]*[&$]?', 'gi');
	args = args.replace(reg, '$1');
	if (args == '' || args == null) {
		args += '?' + this.argName + '=' + page;
	} else if (args.substr(args.length - 1, 1) == '?'
			|| args.substr(args.length - 1, 1) == '&') {
		args += this.argName + '=' + page;
	} else {
		args += '&' + this.argName + '=' + page;
	}
	return url + args;
};
showPages.prototype.toPage = function(page) { // 页面跳转
	var turnTo = 1;
	if (typeof (page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	} else {
		turnTo = page;
	}
/*	self.location.href = this.createUrl(turnTo);*/
	if(this.paging){
		this.paging(turnTo);
	}
};
showPages.prototype.printHtml = function(mode) { // 显示html代码
	this.getPage();
	this.checkPages();
	this.showTimes += 1;
	document.write('<div id="pages_' + this.name + '_' + this.showTimes
			+ '" class="right_page"></div>');
	document.getElementById('pages_' + this.name + '_' + this.showTimes).innerHTML = this
			.createHtml(mode);
	document.write('<p class="stern_tips">当前共有<span>'+this.total+'</span>条'+this.info+'，每页'+this.size+'条，共'+this.pageCount+'页</p>');
};
showPages.prototype.formatInputPage = function(e) { // 限定输入页数格式
	var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
	var key =0;
	if (!ie)
		key = e.which;
	else
		key = event.keyCode;
	if (key == 8 || key == 46 || (key >= 48 && key <= 57))
		return true;
	return false;
};
showPages.prototype.checkPage = function(obj) { // 限定输入页数
	var vl=obj.value;
	if(isNaN(vl))
		vl=this.page;
	else{
		vl=parseInt(vl);
		if(vl<1)
			vl=1;
		else if(vl>this.pageCount){
			vl=this.pageCount;
		}
	}
	obj.value=vl;
};

$(document).ready(function(){
	$(".rc-loading").removeClass("rc-loading");
	window.onbeforeunload=function (){
		$(".r-cnt").addClass("rc-loading"); 
		setTimeout(function(){
			$(".rc-loading").removeClass("rc-loading");
		},1000*2);
	};
});