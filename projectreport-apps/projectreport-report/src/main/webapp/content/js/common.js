// not is "",null,undefined
String.prototype.subCharLength = function(subLen, appstr) {
	var resultLen = 0;
	var strArray = this.split("");
	var restr = this;
	for ( var i = 0; i < strArray.length; i++) {
		resultLen += strArray[i].charLength();
		if (resultLen >= subLen) {
			restr = strArray.slice(0, i + 1).join("") + appstr;
			break;
		}
	}
	return restr;
};

String.prototype.Truncate = function(length, endMark) {
	var l = this.length;
	if (l <= length) {
		return this;
	}

	return this.substring(0, length - 1) + "...";
};

if (!String.trim) {
	String.prototype.trim = function() {
		var str = this;
		var m = str.match(/^\s*(\S+(\s+\S+)*)\s*$/);
		return (m == null) ? "" : m[1];
	};
}

if (!String._FORMAT_SEPARATOR) {
	String._FORMAT_SEPARATOR = String.fromCharCode(0x1f);
	String._FORMAT_ARGS_PATTERN = new RegExp('^[^'
			+ String._FORMAT_SEPARATOR
			+ ']*'
			+ new Array(100).join('(?:.([^' + String._FORMAT_SEPARATOR
					+ ']*))?'));
}

if (!String.format) {
	String.format = function(s) {
		return Array.prototype.join.call(arguments, String._FORMAT_SEPARATOR)
				.replace(String._FORMAT_ARGS_PATTERN, s);
	};
}

if (!''.format) {
	String.prototype.format = function() {
		return (String._FORMAT_SEPARATOR + Array.prototype.join.call(arguments,
				String._FORMAT_SEPARATOR)).replace(String._FORMAT_ARGS_PATTERN,
				this);
	};
}

// 字符串的长充，中文占2个
if (!String.charLength) {
	String.prototype.charLength = function() {
		var sum = 0;
		for ( var i = 0; i < this.length; i++)
			if ((this.charCodeAt(i) >= 0) && (this.charCodeAt(i) <= 255))
				sum = sum + 1;
			else
				sum = sum + 2;
		return sum;
	};
}

if (!Date.format) {
	// alert(new Date().format("yyyy-MM-dd hh:mm:ss"));
	Date.prototype.format = function(style) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"w+" : "\u65e5\u4e00\u4e8c\u4e09\u56db\u4e94\u516d".charAt(this
					.getDay()), // week
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		// millisecond
		}
		if (/(y+)/.test(style)) {
			style = style.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(style)) {
				style = style.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return style;
	};
}

if (!Object.prototype.each) {
	Array.prototype.each = function(fn) {
		if (this.length > 0) {
			for ( var i = 0; i < this.length; i++) {
				fn.call(this[i], i);
			}
		}
	}
}
if (!Number.fixed) {
	Number.prototype.fixed = function(n) {
		with (Math)
			return round(Number(this) * pow(10, n)) / pow(10, n)
	}
}

// 使用jQuery的弹出层改为iframe的，paramsObj对象和Jquery的区别在于多了三个参数，分别是title,url,Id
function openUrl(paramsObj) {
	var tagId = paramsObj.Id || "_dialog";
	var ele = document.getElementById(tagId);
	if (ele != null && ele != undefined) {
		$(ele).dialog('close');
		ele.parentNode.removeChild(ele);
	}
	ele = document.createElement("div");
	ele.setAttribute("id", tagId);
	ele.setAttribute("title", paramsObj.title);
	$(ele).dialog(paramsObj);
	$(ele)
			.html(
					"<iframe id='"
							+ tagId
							+ "_iframe' width='100%' height='100%' frameborder='0' scrolling='yes'></iframe>");
	$(ele).dialog('open');

	paramsObj.url = (paramsObj.url.indexOf("?") == -1) ? paramsObj.url + "?_="
			+ new Date().getTime() : paramsObj.url + "&_="
			+ new Date().getTime();
	$("#" + tagId + "_iframe").attr("src", paramsObj.url); // 兼容IE6

	return false;
}

(function(window) {

	var Utils = {
		// 对Html进行编码
		htmlEncode : function(s, bIgnoreApos) {
			s = s.replace(/&/g, "&amp;");
			if (!bIgnoreApos)
				s = s.replace(/\'/g, "#wxjapos;");
			s = s.replace(/</g, "&lt;");
			s = s.replace(/>/g, "&gt;");
			s = s.replace(/\"/g, "&quot;");
			return s;
		},
		// 对经过编码的Html进行解码
		htmlDecode : function(s, bIgnoreApos) {
			s = s.replace(/&quot;/g, "\"");
			s = s.replace(/&gt;/g, ">");
			s = s.replace(/&lt;/g, "<");
			if (!bIgnoreApos)
				s = s.replace(/#wxjapos;/g, "'");
			s = s.replace(/&amp;/g, "&");
			return s;
		},
		// 获取url参数
		request : function(paras) {
			var url = location.href;
			var paraString = url.substring(url.indexOf("?") + 1, url.length)
					.split("&");
			var paraObj = {};
			for (i = 0; j = paraString[i]; i++) {
				paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j
						.substring(j.indexOf("=") + 1, j.length);
			}
			var returnValue = paraObj[paras.toLowerCase()];
			if (typeof (returnValue) == "undefined") {
				return "";
			} else {
				return returnValue;
			}
		},
		// 替换模板对象
		replaceTemplateObj : function(input, obj) {
			for ( var item in obj) {
				input = input.replace(new RegExp("{" + item + "}", "gi"),
						obj[item]);
			}
			return input;
		},
		getPos : function(ele) {
			var curleft = ele.offsetLeft || 0;
			var curtop = ele.offsetTop || 0;
			while (ele = ele.offsetParent) {
				curleft += eval(ele.offsetLeft);
				curtop += ele.offsetTop;
			}
			return {
				x : curleft,
				y : curtop
			};
		},
		getCss : function(ele) {
			if (document.defaultView)
				return document.defaultView.getComputedStyle(ele, null)[css];// FF
																				// or
																				// Other
			else
				return ele.currentStyle[css];// IE
		},
		// 设置控件的值
		setVal : function(idOfName, value) {
			if (idOfName.indexOf("id:") > -1) {
				return Utils.setValueById(idOfName.substring(3), value);
			} else if (idOfName.indexOf("name:") > -1) {
				return Utils.setValueByName(idOfName.substring(5), value);
			} else {
				return Utils.setValueByName(idOfName, value);
			}
		},
		setValueById : function(id, value) {
			var ele;
			if ((ele = document.getElementById(id)) == null)
				return;
			switch (ele.type) {
			case "select-one":
				for ( var i = 0; i < ele.options.length; i++)
					if (ele.options[i].value == value) {
						ele.options[i].selected = true;
						break;
					}
				break;
			case "select-multiple":
				for ( var i = 0; i < ele.options.length; i++)
					if (ele.options[i].value == value) {
						ele.options[i].selected = true;
					}
				break;
			case "radio":
			case "checkbox":
				if (ele.value == value)
					ele.checked = true;
				break;
			default:
				ele.value = value;
				break;
			}
		},
		setValueByName : function(name, value) {
			var eles;
			if ((eles = document.getElementsByName(name)).length == 0)
				return;
			for ( var ei = 0; ei < eles.length; ei++) {
				switch (eles[ei].type) {
				case "select-one":
					for ( var i = 0; i < eles[ei].options.length; i++)
						if (eles[ei].options[i].value == value) {
							eles[ei].options[i].selected = true;
							break;
						}
					break;
				case "select-multiple":
					for ( var i = 0; i < eles[ei].options.length; i++)
						if (eles[ei].options[i].value == value) {
							eles[ei].options[i].selected = true;
						}
					break;
				case "radio":
				case "checkbox":
					if (eles[ei].value == value)
						eles[ei].checked = true;
					break;
				default:
					eles[ei].value = value;
					break;
				}
			}
		},
		getVal : function(idOfName, separator) {
			if (idOfName.indexOf("id:") > -1) {
				return Utils.getValueById(idOfName.substring(3), separator);
			} else if (idOfName.indexOf("name:") > -1) {
				return Utils.getValueByName(idOfName.substring(5), separator);
			} else {
				return Utils.getValueByName(idOfName, separator);
			}
		},
		getValueById : function(id, separator) {
			var ele;
			if ((ele = document.getElementById(id)) == null)
				return "";
			switch (ele.type) {
			case "select-multiple":
				var vals = new Array();
				for ( var i = 0; i < ele.options.length; i++) {
					if (ele.options[i].selected) {
						vals.push(ele.options[i].value);
					}
				}
				return vals.length == 0 ? "" : vals.join(separator || ",");
				break;
			default:
				return ele.value;
				break;
			}
		},
		getValueByName : function(name, separator) {
			var eles;
			if ((eles = document.getElementsByName(name)).length == 0)
				return "";
			var vals = new Array();
			for ( var i = 0; i < eles.length; i++) {
				switch (eles[i].type) {
				case "select-multiple":
					for ( var j = 0; j < eles[i].options.length; j++)
						if (eles[i].options[j].selected)
							vals.push(eles[i].options[j].value);
					break;
				case "radio":
				case "checkbox":
					if (eles[i].checked)
						vals.push(eles[i].value);
					break;
				default:
					vals.push(eles[i].value);
					break;
				}
			}
			return vals.length == 0 ? "" : vals.join(separator || ",");
		},
		forEachCheckbox : function(name, selectOfInvertOfCancel) {
			var nodes;
			if ((nodes = document.getElementsByName(name)).length == 0)
				return false;
			switch (selectOfInvertOfCancel.toLowerCase()) {
			case "select":
				for ( var i = 0; i < nodes.length; i++)
					nodes[i].checked = true;
				break;
			case "invert":
				for ( var i = 0; i < nodes.length; i++)
					nodes[i].checked = nodes[i].checked == true ? false : true;
				break;
			case "cancel":
				for ( var i = 0; i < nodes.length; i++)
					nodes[i].checked = false;
				break;
			}
		},
		addEvent : function(elem, type, handle) {
			document.addEventListener ? elem.addEventListener(type, handle,
					false) : elem.attachEvent("on" + type, handle);
		},
		removeEvent : function(elem, type, handle) {
			elem.removeEventListener ? elem.removeEventListener(type, handle,
					false) : elem.detachEvent("on" + type, handle);
		},
		Ajax : function(obj) {
			if (!Utils.xmlhttp) {
				var xmlhttp = false;
				if (window.XMLHttpRequest) {
					xmlhttp = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					try {
						xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
					} catch (e) {
						try {
							xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
						} catch (e) {
						}
					}
				}
				Utils.xmlhttp = xmlhttp;
			}
			Utils.xmlhttp.onreadystatechange = function() {
				if (Utils.xmlhttp.readyState == 4) {
					if (Utils.xmlhttp.status == 200) {
						if (obj.success)
							obj.success(Utils.xmlhttp.responseText);
					} else {
						if (obj.error)
							obj.error();
					}
				}
			}
			if (obj.cache == false) {
				Utils._random = (obj.url.indexOf("?") == -1) ? "?_="
						+ new Date().getTime() : "&_=" + new Date().getTime();
			}
			Utils.xmlhttp.open(obj.type || "GET", obj.url
					+ (Utils._random || ""), (obj.async == undefined) ? true
					: false);
			Utils.xmlhttp.send(null);
		},
		ready : function(fn) {
			this.bindReady();
			readyList.push(fn);
		},
		bindReady : function(fn) {

			if (readyBound) {
				return;
			}
			readyBound = true;

			// Catch cases where $(document).ready() is called after the
			// browser event has already occurred.
			if (document.readyState === "complete") {
				triggerReadyHandler();
			}

			// Mozilla, Opera and webkit nightlies currently support this event
			if (document.addEventListener) {
				// Use the handy event callback
				document.addEventListener("DOMContentLoaded", DOMContentLoaded,
						false);

				// A fallback to window.onload, that will always work
				window.addEventListener("load", triggerReadyHandler, false);

				// If IE event model is used
			} else if (document.attachEvent) {
				// ensure firing before onload,
				// maybe late but safe also for iframes
				document.attachEvent("onreadystatechange", DOMContentLoaded);

				// A fallback to window.onload, that will always work
				window.attachEvent("onload", triggerReadyHandler);
				// If IE and not a frame
				// continually check to see if the document is ready
				var toplevel = false;

				try {
					toplevel = window.frameElement == null;
				} catch (e) {
				}

				if (document.documentElement.doScroll && toplevel) {
					doScrollCheck();
				}
			}
		},
		size : function(name) {// test:ie,ff,chrome,opera,360
			return Math.max(document.documentElement["client" + name],
					document.body["scroll" + name], // document.documentElement["scroll"
													// + name],
					document.body["offset" + name],
					document.documentElement["offset" + name]);
		},
		width : function() {
			return this.size("Width");
		},
		height : function() {
			return this.size("Height");
		},
		scroll : function(name) {
			return Math.max(document.documentElement["scroll" + name],
					document.body["scroll" + name]);
		},
		scrollTop : function() {
			return this.scroll("Top");
		},
		scrollWidth : function() {
			return this.scroll("Width");
		},
		dialog : function(parms) {
			Dialog.show(parms);
		},
		iDialog : function(parms) {
			Dialog.showIframe(parms);
		},
		dialogClose : function(id) {
			Dialog.close(id);
		}
	};

	var readyList = [], readyBound = false;
	function triggerReadyHandler() {
		for ( var i = 0; i < readyList.length; i++) {
			readyList.pop()();
		}
	}

	// Cleanup functions for the document ready method
	if (document.addEventListener) {
		DOMContentLoaded = function() {
			document.removeEventListener("DOMContentLoaded", DOMContentLoaded,
					false);
			triggerReadyHandler();
		};

	} else if (document.attachEvent) {
		DOMContentLoaded = function() {
			// Make sure body exists, at least, in case IE gets a little
			// overzealous (ticket #5443).
			if (document.readyState === "complete") {
				document.detachEvent("onreadystatechange", DOMContentLoaded);
				triggerReadyHandler();
			}
		};
	}

	function doScrollCheck() {
		try {
			document.documentElement.doScroll("left");
		} catch (error) {
			setTimeout(doScrollCheck, 1);
			return;
		}
		// and execute any waiting functions
		triggerReadyHandler();
	}

	window.Utils = Uts = Utils;

})(window);

// rd guigang test:ie,ff,chrome,opera,360
Dialog = {
	dialogs : null,
	cacheBox : null,
	onSizeEvent : null,
	setTitleHTML : function(parms) {
		if (parms.title) {
			parms.titleHTML = "<div style='border:1px solid #AAA; margin:2px;height:24px;line-height:24px; padding:5px;background:#D7D7D7'><span style='float:left;font-size:12px;'>"
					+ parms.title
					+ "</span><span style='float:right;cursor:pointer;margin-right:10px;' title='关闭对话框' onclick='Utils.dialogClose(\""
					+ parms.id + "\")'>×</span></div>";
			parms.titleHeight = 40; // 自定义HTML标题高度(需注意margin/padding/border会导致偏移)
			parms.height += parms.titleHeight;
		}
	},
	show : function(parms) {
		this.setTitleHTML(parms);
		this.core(parms);

	},
	showIframe : function(parms) {
		this.setTitleHTML(parms);
		parms.padding = 0;
		parms.innerHTML = "<iframe id='$1-iframe' width='100%' height='100%' frameborder='0' scrolling='auto'></iframe>"
				.format(parms.id);
		this.core(parms);
		document.getElementById(parms.id + "-iframe").setAttribute("src",
				parms.src); // 兼容IE6把src放到后面设置
	},
	close : function(id) {
		var box = document.getElementById(id + "-box");
		if (box) {
			// var node = document.getElementById(id);
			// if (node != null) {
			// node.style.display = "none";
			// this.cacheBox.appendChild(node);
			// }
			document.body.removeChild(box);
			this.dialogs.remove(id);
		}
		$('#menu li', parent.document).css('position', 'relative');
	},
	onSize : function() {
		var dialogs = Dialog.dialogs.items();
		for ( var i = 0; i < dialogs.length; i++) {
			this.onChange(dialogs[i].value);
		}
	},
	onChange : function(obj) {
		var scrTop = Utils.scrollTop(), pageW = Utils.width(), pageH = Utils
				.height();
		var x = parseInt((pageW - obj.width) / 2);
		var y = scrTop + (document.documentElement.clientHeight / 2)
				- (obj.height / 2);

		var dialogBox = document.getElementById(obj.id + "-dialogBox");
		dialogBox.style.left = x + "px";
		dialogBox.style.top = y + "px";
	},
	core : function(parms) {

		var obj = {
			id : parms.id || "dialog",
			width : parms.width || 0,
			height : parms.height || 0,
			zIndex : parms.zIndex || 9999,
			border : parms.border == 0 ? 0 : parms.border || 1,
			padding : parms.padding || 0,
			modal : parms.modal || false,
			titleHeight : parms.titleHeight || 0,
			titleHTML : parms.titleHTML || null, // 头部HTML内容
			innerHTML : parms.innerHTML || null, // 对话框HTML内容
			transparent : parms.transparent || false, // 背景透明
			x : parms.x || null,
			y : parms.y || null
		};

		this.close(obj.id);
		var scrTop = Utils.scrollTop(), pageW = Utils.width(), pageH = Utils
				.height();

		if (!obj.x && !obj.y) {
			obj.x = parseInt((pageW - obj.width > 0 ? pageW - obj.width : 0) / 2);
			obj.y = scrTop + (document.documentElement.clientHeight / 2)
					- (obj.height / 2);
		}

		var box = document.createElement("div");
		with (box) {
			box.id = obj.id + "-box";
			style.zIndex = obj.zIndex;
			// 遮罩层样式
			// style.position = "absolute";
			// style.top = 0;
			// style.left=0;
		}

		if (obj.modal) {
			var overlay = document.createElement("div");
			overlay.setAttribute("id", obj.id + "-overlay");
			with (overlay) {
				style.width = !window.XMLHttpRequest ? pageW + "px" : "100%";
				style.height = pageH + "px";
				style.top = "0px";
				style.left = "0px";
				style.position = "absolute";
				style.background = "#fff";
				style.opacity = "0.7"; // 除IE外其它浏览器
				style.filter = "alpha(opacity=70)"; // ie
				innerHTML = "<iframe width='100%' height='100%' frameborder='0' scrolling='auto'></iframe>";
			}
			box.appendChild(overlay);
		}

		var dialogBox = document.createElement("div");
		dialogBox.setAttribute("id", obj.id + "-dialogBox");
		with (dialogBox) {
			style.width = obj.width + "px";
			style.height = obj.height + "px";
			style.position = "absolute";
			style.border = obj.border + "px solid #AAA";
			style.left = obj.x + "px";
			style.top = obj.y + "px";
		}

		if (obj.titleHTML) {
			var title = document.createElement("div");
			title.innerHTML = obj.titleHTML;
			;
			dialogBox.appendChild(title);
		}

		var content = document.createElement("div");
		content.style.overflow = "auto";
		if (!obj.transparent)
			content.style.background = "#fff";
		content.style.height = (obj.height - obj.titleHeight) + "px";

		if (obj.innerHTML) {
			content.innerHTML = "<div style='padding:" + obj.padding
					+ "px;height:100%;'>" + obj.innerHTML + "</div>";
		} else {
			var minbox = document.createElement("div");
			var ele = document.getElementById(obj.id);
			ele.style.padding = obj.padding + "px";
			ele.style.display = "";
			minbox.appendChild(ele);
			content.appendChild(minbox);
		}

		dialogBox.appendChild(content);
		box.appendChild(dialogBox);
		document.body.appendChild(box);

		if (!this.dialogs) {
			this.dialogs = new Dictionary();
			this.dialogs.add(obj.id, obj);
		} else {
			if (!this.dialogs.contains(obj.id))
				this.dialogs.add(obj.id, obj);
		}
		if (!this.cacheBox) { // 缓存以前弹出层的元素
			var cbox = document.createElement("div");
			cbox.setAttribute("style", "display:none;");
			this.cacheBox = cbox;
			document.body.appendChild(cbox);
		}

		if (!this.onSizeEvent) { // 窗口大小改变,对话框位置以及遮罩层也随着改变
			this.onSizeEvent = window.onresize = function() {
				Dialog.onSize();
			};
		}
	}
};

function Repeater(repeaterObjs) {
	this.headerTemplate, // 页眉
	this.itemTemplate, // 正常项
	this.alternatingTemplate, // 交替项
	this.separatorTemplate, // 分隔符
	this.footerTemplate, // 页脚
	this.objs = repeaterObjs || null, // 对象列表
	this.version = "damai rd guigang Repeater Control 1.0";
}

Repeater.prototype = {
	parseTag : function(html, obj) {
		if (!html)
			return null;
		return this.parseFunc(this.parseObj(html, obj));
	},
	parseObj : function(html, obj) {
		if (!html || !obj)
			return null;
		return html.replace(/\{([a-z_]+)\}/gi, function(m, s) {
			return obj[s] || "";
		});
	},
	parseFunc : function(html) {
		return html.replace(/\{([a-z_]+\([^\}]*\))\}/gi, function(m, s) {
			return eval(s);
		});
	},
	parse : function(repeaterObjs) {
		if (repeaterObjs) {
			this.objs = repeaterObjs;
		}
		if (!this.objs) {
			return "repeaterObjs error:Can not be empty";
		}
		if (!Object.prototype.toString.call(this.objs) === "[object Array]") {
			return "repeaterObjs error:Is not an array";
		}
		var ret = new Array();
		if (this.headerTemplate)
			ret.push(this.headerTemplate);
		for ( var i = 1; i <= this.objs.length; i++) {
			if (i % 2 === 1) {// 正常项
				ret.push(this.parseTag(this.itemTemplate, this.objs[i - 1]));
			} else {// 交替项
				ret.push(this.parseTag(
						this.alternatingTemplate ? this.alternatingTemplate
								: this.itemTemplate, this.objs[i - 1]));
			}
			if (this.separatorTemplate && i < this.objs.length)
				ret.push(this.separatorTemplate);
		}
		if (this.footerTemplate)
			ret.push(this.footerTemplate);
		return ret.join('\n');
	}
};

function Dictionary() {
	this._hash = new Object();
}

Dictionary.prototype = {
	add : function(key, value) {
		var ret = false;
		if (key) {
			if (!this.contains(key)) {
				this._hash[key] = value ? value : value == 0 ? 0 : null;
				ret = true;
			}
		}
		return ret;
	},
	remove : function(key) {
		delete this._hash[key];
	},
	count : function() {
		var i = 0;
		for ( var k in this._hash) {
			i++;
		}
		return i;
	},
	items : function(key) {
		if (key) {
			return this._hash[key];
		} else {
			var temps = new Array();
			for ( var k in this._hash) {
				temps.push({
					key : k,
					value : this._hash[k]
				});
			}
			return temps;
		}
	},
	contains : function(key) {
		return this._hash[key] != undefined;
	},
	clear : function() {
		for ( var k in this._hash) {
			delete this._hash[k];
		}
	}
};

/* 火狐不支持outerHTMl Writer:王亚龙 2011/8/19 */
if (typeof (HTMLElement) != "undefined") {
	// HTMLElement.prototype.__defineSetter__("outerHTML", function (s) {
	// var r = this.ownerDocument.createRange();
	// r.setStartBefore(this);
	// var df = r.createContextualFragment(s);
	// this.parentNode.replaceChild(df, this);
	// return s;
	// });
	// HTMLElement.prototype.__defineGetter__("outerHTML", function () {
	// var a = this.attributes, str = "<" + this.tagName, i = 0;
	// for (; i < a.length; i++)
	// if (a[i].specified)
	// str += " " + a[i].name + '="' + a[i].value + '"';
	// if (!this.canHaveChildren)
	// return str + " />";
	// return str + ">" + this.innerHTML + "</" + this.tagName + ">";
	// });

	// HTMLElement.prototype.__defineGetter__("canHaveChildren", function () {
	// return
	// !/^(area|base|basefont|col|frame|hr|img|br|input|isindex|link|meta|param)$/.test(this.tagName.toLowerCase());
	// });
}
/* 火狐不支持outerHTML 结束 */

function getFormJson(frm) {
	var o = {};
	var a = $(frm).serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
}

// 弹窗关闭
function TanChuangClosed(id) {

	$(".mask").hide();
	$("#" + id).hide();

}