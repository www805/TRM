
function control(arr,funs){
	for (var i = 0; i < arr.length; i++) {
		var obj=arr[i];
		if (funs.indexOf(obj.qx)==-1) {
			$("."+obj.div).hide()
		}else{
			$("."+obj.div).show()
		}
	}
}
//获取当前时间，格式YYYY-MM-DD
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
//比较日期
function compareDate(date1,date2){
    var oDate1 = new Date(date1);
    var oDate2 = new Date(date2);
    if(oDate1.getTime() > oDate2.getTime()){
    	return 1;
    } else {
        return -1;
    }
}
function checkNumber(num) {
	 /* var reg = /^[0-9]+.?[0-9]*$/;
	  if (reg.test(theObj)) {
	    return true;
	  }
	  return false;*/
		 // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除
	  /*  if(val === "" || val ==null){
	        return false;
	    }
	    if(!isNaN(val)){
	        return true;
	    }else{
	        return false;
	    }*/
		var reg = /^-?\d+$/;
			if(reg.test(num)){
				  return true;
			}else{
				return false;
			}	 
	}
function qiestr(str) {
	var aa = "(分词)";
	if (isNotEmpty(str)) {
		if (str.slice(str.length - 4, str.length) != aa) {
			str = str.replace("(分词)", "");
			if (parseInt(str.length) > 15) {
				if (str.slice(str.length - 4, str.length) != aa) {
					str = str.substr(0, 13) + "...."
				} else {
					str = str.substr(0, 13) + "....(分词)"
				}
			}

		} else {
			if (parseInt(str.length) > 15) {
				if (str.slice(str.length - 4, str.length) != aa) {
					str = str.substr(0, 13) + "....";
				} else {
					str = str.substr(0, 13) + "....(分词)";
				}
			}
		}
	}
	return str;
}
/*
 * 科学计数法 三位一个逗号
 * */
function toThousands(num) {
    var result = [ ], counter = 0;
    num = (num || 0).toString().split('');
    for (var i = num.length - 1; i >= 0; i--) {
        counter++;
        result.unshift(num[i]);
        if (!(counter % 3) && i != 0) { result.unshift(','); }
    }
    return result.join('');
}
/**
 * 
 * @cmparam obj
 *            obj为一个html元素对象（只能是input的类型的对象） 验证只能为整数
 */
function checkInputTonum(obj) {
	if (obj instanceof jQuery) {
		obj = obj.get(0)
	}
	obj.value = obj.value.replace(/\D/g, '');
}

/**
 * 
 * @cmparam obj
 *            obj为一个html元素对象（只能是input的类型的对象） 验证只能为数字或小数，
 */
function checkInputTodecimal(obj) {
	if (obj instanceof jQuery) {
		obj = obj.get(0);
	}
	obj.value = obj.value.replace(/[^\d.]/g, "");
	obj.value = obj.value.replace(/\.{2,}/g, ".");
	obj.value = obj.value.replace(/^\./g, '');
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
			".");
}

// 去掉所有空格 需要设置第2个参数is_global为:g
function Trimz(str, is_global) {
	var result;
	result = str.replace(/(^\s+)|(\s+$)/g, "");
	if (is_global.toLowerCase() == "g") {
		result = result.replace(/\s/g, "");
	}
	return result;
}

/* 获取当前日期 */
function getNowFormatDate() {
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	return currentdate;
}

// 获取当前系统时间
function getTime() {
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	var shi = date.getHours();
	var fen = date.getMinutes();
	var miao = date.getSeconds();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = date.getFullYear() + seperator1 + month + seperator1
			+ strDate + " " + shi + ":" + fen + ":" + miao;
	return currentdate;
}

/**
 * 模拟操作按键
 *  keydown：点击事件
 * @param el
 * @param evtType
 * @param keyCode
 */
function fireKeyEvent(el, evtType, keyCode) {
	var evtObj;
	if (document.createEvent) {
		if (window.KeyEvent) {//firefox 浏览器下模拟事件
			evtObj = document.createEvent('KeyEvents');
			evtObj.initKeyEvent(evtType, true, true, window, true, false, false, false, keyCode, 0);
		} else {//chrome 浏览器下模拟事件
			evtObj = document.createEvent('UIEvents');
			evtObj.initUIEvent(evtType, true, true, window, 1);

			delete evtObj.keyCode;
			if (typeof evtObj.keyCode === "undefined") {//为了模拟keycode
				Object.defineProperty(evtObj, "keyCode", { value: keyCode });
			} else {
				evtObj.key = String.fromCharCode(keyCode);
			}

			if (typeof evtObj.ctrlKey === 'undefined') {//为了模拟ctrl键
				Object.defineProperty(evtObj, "ctrlKey", { value: true });
			} else {
				evtObj.ctrlKey = true;
			}
		}
		el.dispatchEvent(evtObj);

	} else if (document.createEventObject) {//IE 浏览器下模拟事件
		evtObj = document.createEventObject();
		evtObj.keyCode = keyCode
		el.fireEvent('on' + evtType, evtObj);
	}
	document.body.onkeypress(keyCode); // 主动触发esc
}


//上下文路径

var ctx;

function setCtx(path) {
	ctx = path;
};

String.prototype.trim = function() {
	return Trim(this);
};
function LTrim(str) {
	var i;
	for (i = 0; i < str.length; i++) {
		if (str.charAt(i) != " " && str.charAt(i) != " ")
			break;
	}
	str = str.substring(i, str.length);
	return str;
}
function RTrim(str) {
	var i;
	for (i = str.length - 1; i >= 0; i--) {
		if (str.charAt(i) != " " && str.charAt(i) != " ")
			break;
	}
	str = str.substring(0, i + 1);
	return str;
}
function Trim(str) {
	return LTrim(RTrim(str));
}

function LTrimByChar(str, char) {
	var i;
	for (i = 0; i < str.length; i++) {
		if (str.charAt(i) != char && str.charAt(i) != char)
			break;
	}
	str = str.substring(i, str.length);
	return str;
}
function RTrimByChar(str, char) {
	var i;
	for (i = str.length - 1; i >= 0; i--) {
		if (str.charAt(i) != char && str.charAt(i) != char)
			break;
	}
	str = str.substring(0, i + 1);
	return str;
}
function bothTrimByChar(str, char) {

	var s = str.substring(0, 1);
	if (s.indexOf("'") > -1) {
		str = str.substring(1, str.length);
	}
	s = str.substring(str.length - 1, str.length);
	if (s.indexOf("'") > -1) {
		str = str.substring(0, str.length - 1);
	}

	// str.replace(new RegExp(char,"gm"),"");
	return str;
}
/**
 * 检测字符str是否以char结尾后者开头 startorend 1检测开始 2检测结束
 * 
 * @returns
 */
function checkstartorendofstr(str, char, startorend) {

	if (startorend == 1) {
		if (str.indexOf(char) > -1) { // 要修改
			return true;
		}
	} else if (startorend == 2) {
		if (str.indexOf(char) > -1) { // 要修改
			return true;
		}
	}
	return false;
}

/* ajax提交(开始) url:提交后台方法路径，data:提交参数,func:回调方法 */
function ajaxSubmit(url, data, func) {
	// parent.FuShow();
	$.ajax({
		url : url,
		type : "post",
		async : true,
		// dataType : "json",
		data : data,
		timeout : 60000,
		success : function(reData) {
			if ($.trim(reData) == null) {
				alert("本次请求失败");
			} else {
				func(reData);
			}
		},
		error : function() {
			alert("本次请求失败");
		}
	});
}

/**
 * ajax请求
 * 
 * @cmparam url
 * @cmparam data
 * @cmparam success_fun
 *            请求成功回调
 * @cmparam error_fun
 *            请求失败的回调
 * @cmparam haveloading
 *            是否含有loading页面
 */
function ajaxSubmit(url, data, success_fun, error_fun, haveloading) {
	// parent.FuShow();
	if (haveloading) {
		$.ajax({
			type : "POST",
			url : url,
			async : true,
			// dataType : "json",
			timeout : 60000,
			data : data,
			success : success_fun,
			error : (error_fun != null ? error_fun : ajaxErrDialog),
			beforeSend : showLoading,
			complete : closeLoading
		});
	} else {
		$.ajax({
			type : "POST",
			url : url,
			async : true,
			// dataType : "json",
			timeout : 60000,
			data : data,
			success : success_fun,
			error : (error_fun != null ? error_fun : ajaxErrDialog)
		});
	}
}

/**
 * json格式ajax提交 pp
 * @cmparam url
 * @cmparam data
 * @cmparam success_fun
 */
function ajaxSubmitByJson(url, data, success_fun,error_fun) {
    $.ajax({
        url : url,
        type : "POST",
        async : true,
        dataType : "json",
        contentType: "application/json",
        data : JSON.stringify(data),
        timeout : 60000,
        success : success_fun,
        error : (error_fun != null ? error_fun : ajaxErrDialog)
    });
}

/**
 * ajax 请求失败的提醒
 */
function ajaxErrDialog() {
/*	parent.layer.msg("网络异常,请稍后重试---!", {
		icon : 1
	},1);*/

}

/**
 * 
 * @cmparam str
 * @returns {Boolean}
 */
function isNotEmpty(str) {
	if (str == "" || str == null || str == undefined || str == 'null'
			|| str == 'Null' || str == 'NULL'|| str == NaN || str == 'NaN') {
		return false;
	}
	if (str.length == 0)
		return false;
	return true;
}
/**
 * 校验只要是数字（包含正负整数，0以及正负浮点数）就返回true
 * 
 * @cmparam val
 * @returns {Boolean}
 */
function isNumber(val) {

	var regPos = /^\d+(\.\d+)?$/; // 非负浮点数
	var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; // 负浮点数
	if (regPos.test(val) || regNeg.test(val)) {
		return true;
	} else {
		return false;
	}

}
//判断正整数
function checkRate(s){//是否为正整数
    var re = /^[0-9]+$/ ;
    return re.test(s)
} 

/**
 * 格式化时间戳成YYYY-MM-DD hh:mm:ss
 * 
 * @cmparam timestr
 * @returns {String}
 */
function getFomatTimez(str) {
	var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");

	if (null != str && !reg.test(str)) {
		str = str.substring(0, str.length - 2);
	}
	return str;
}

function getFomatTime(timestr, havehour) {

	var d = new Date(timestr);
	var time = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
	if (havehour) {
		time += " " + d.getHours() + ":" + d.getMinutes() + ":"
				+ d.getSeconds();
	}
	return time;
}

/**
 * 把数字转成时分秒
 * ss一定要是数字
 */
function getFomathms(ss){

	if(isNumber(ss)){
		ss=parseInt(ss);
		var h=parseInt(ss/3600);
		var m=parseInt(ss%3600/60);
		var s=parseInt(ss%3600%60);

		var rr="";
		if(h<10){
			rr="0";
		}
		rr+=h+":";
		if(m<10){
			rr+="0";
		}
		rr+=m+":";
		if(s<10){
			rr+="0";
		}
		rr+=s;
		return rr;
	}
	return ss;
}

// 文件下载请求,uuid
function fileDownload(fileuuid) {
	var url = getPath();
	location.href = url + "/FileDownloadServlet.servlet?uuid=" + fileuuid;
}

// 文件下载请求,url
function fileDownload_url(path) {
	alert(path)
	var url = getPath();
	if (url.indexOf(":") > -1) {
		var arr = url.split(':');
		if (null != arr && arr.length == 3) {
			url = arr[0] + ":" + arr[1];
		}
	}
	url += path;
	parentValueAssignment('downLoadurl', url);
	alert(url)
	location.href = url;
}

// 按钮禁用
function btnDisabled() {
	$("button").prop("disabled", true);
}

// 按钮解除禁用
function btnRelieveDisabled() {
	$("button").prop("disabled", false);
}

// 给父页面的参数赋值
function parentValueAssignment(id, str) {
	parent.document.getElementById(id).value = str;
}

/**
 * tr封装 * data td里面的数据集合的形式, classname class的名字, name name的名字, id id的名字, style
 * 样式 tddata td的数据集合 arrOrStr 1代表td字符串集合/2代表tdlist集合 title title说明 other
 * 其他元素，一般都是框架带的,直接把全部复制过来，包括名称
 */
function addtrbytddata(data, classname, name, id, style, arrOrStr, title, other) {

	var rr = '<tr ';
	if (isNotEmpty(id)) {
		rr += 'id="' + id + '" ';
	}
	if (isNotEmpty(name)) {
		rr += 'name="' + name + '" ';
	}
	if (isNotEmpty(classname)) {
		rr += 'class="' + classname + '" ';
	}
	if (isNotEmpty(style)) {
		rr += 'style="' + style + '" ';
	}
	if (isNotEmpty(title)) {
		rr += 'title="' + title + '" ';
	}
	if (isNotEmpty(other)) {
		rr += other;
	}
	rr += '>';
	if (isNotEmpty(data)) {

		if (arrOrStr == 2) {

			for (var i = 0; i < data.length; i++) {
				var td = data[i];
				if (isNotEmpty(td)) {
					rr += td;
				}
			}
		} else {
			rr += data;
		}
	}
	rr += '</tr>';
	return rr;
}

/**
 * tr封装 最简 data td集合字符串
 */
function addtrbytddata(data) {

	var rr = '<tr ';
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</tr>';
	return rr;
}

/**
 * td封装 data td里面的数据, classname class的名字, name name的名字, id id的名字, style 样式 other
 * 其他元素，一般都是框架带的,直接把全部复制过来，包括名称
 */
function addtdSeven(data, classname, name, id, style, title, other) {
	var rr = '<td ';
	if (isNotEmpty(id)) {
		rr += 'id="' + id + '" ';
	}
	if (isNotEmpty(name)) {
		rr += 'name="' + name + '" ';
	}
	if (isNotEmpty(classname)) {
		rr += 'class="' + classname + '" ';
	}
	if (isNotEmpty(style)) {
		rr += 'style="' + style + '" ';
	}
	if (isNotEmpty(title)) {
		rr += 'title="' + title + '" ';
	}
	if (isNotEmpty(other)) {
		rr += other;
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</td>';
	return rr;
}
/**
 * td封装 最简 data 数据字符串
 */
function addtd(data) {

	var rr = '<td ';
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</td>';
	return rr;
}

/**
 * 一般input封装 value value里面的数据, classname class的名字, name name的名字, id id的名字, style
 * 样式 type input的类型 other 其他元素，一般都是框架带的,直接把全部复制过来，包括名称
 */
function addinput(value, classname, name, id, style, type, other) {

	var rr = '<input ';
	if (isNotEmpty(id)) {
		rr += 'id="' + id + '" ';
	}
	if (isNotEmpty(name)) {
		rr += 'name="' + name + '" ';
	}
	if (isNotEmpty(classname)) {
		rr += 'class="' + classname + '" ';
	}
	if (isNotEmpty(style)) {
		rr += 'style="' + style + '" ';
	}
	if (isNotEmpty(value)) {
		rr += 'value="' + value + '" ';
	}
	if (isNotEmpty(type)) {
		rr += 'type="' + type + '" ';
	}
	if (isNotEmpty(other)) {
		rr += other;
	}
	rr += '>';
	return rr;
}

/**
 * @cmparam 一般a标签的封装
 * @cmparam data
 *            数据,
 * @cmparam href
 *            链接地址,
 * @cmparam classname
 *            class的名字,
 * @cmparam name
 *            name的名字,
 * @cmparam id
 *            id的名字,
 * @cmparam style
 *            样式
 * @cmparam title
 *            title说明
 * @cmparam other
 *            其他元素，一般都是框架带的,直接把全部复制过来，包括名称
 */
function addanine(data, href, onclick, classname, name, id, style, title, other) {

	var rr = '<a ';
	if (isNotEmpty(id)) {
		rr += 'id="' + id + '" ';
	}
	if (isNotEmpty(name)) {
		rr += 'name="' + name + '" ';
	}
	if (isNotEmpty(classname)) {
		rr += 'class="' + classname + '" ';
	}
	if (isNotEmpty(style)) {
		rr += 'style="' + style + '" ';
	}
	if (isNotEmpty(title)) {
		rr += 'title="' + title + '" ';
	}
	if (isNotEmpty(href)) {
		rr += 'href="' + href + '" ';
	}
	if (isNotEmpty(onclick)) {
		rr += 'onclick="' + onclick + '" ';
	}
	if (isNotEmpty(other)) {
		rr += other;
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</a>';
	return rr;
}

/**
 * @cmparam 一般a标签的封装
 *            精简
 * @cmparam data
 *            数据,
 * 
 */
function addatwo(data, href) {

	var rr = '<a ';

	if (isNotEmpty(href)) {
		rr += 'href="' + href + '" ';
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</a>';
	return rr;
}

/**
 * @cmparam 一般a标签的封装
 *            精简
 * @cmparam data
 *            数据,
 * 
 */
function addathree(data, href, onclick) {

	var rr = '<a ';

	if (isNotEmpty(href)) {
		rr += 'href="' + href + '" ';
	}
	if (isNotEmpty(onclick)) {
		rr += 'onclick="' + onclick + '" ';
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</a>';
	return rr;
}

/**
 * @cmparam 一般a标签的封装
 *            精简
 * @cmparam onclick
 * 
 */
function adda4(data, onclick) {

	var rr = '<a ';

	rr += 'href="#" ';
	if (isNotEmpty(onclick)) {
		rr += 'onclick="' + onclick + '; return false;" ';
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</a>';
	return rr;
}

/**
 * @cmparam 一般button标签的封装
 * @cmparam data
 *            数据,
 * @cmparam onclick
 *            事件名带参数 getfun('123');
 * @cmparam classname
 *            class的名字,
 * @cmparam name
 *            name的名字,
 * @cmparam id
 *            id的名字,
 * @cmparam style
 *            样式
 * @cmparam title
 *            title说明
 * @cmparam other
 *            其他元素，一般都是框架带的,直接把全部复制过来，包括名称
 */
function addbutton(data, onclick, classname, name, id, style, title, other) {

	var rr = '<a ';
	if (isNotEmpty(id)) {
		rr += 'id="' + id + '" ';
	}
	if (isNotEmpty(name)) {
		rr += 'name="' + name + '" ';
	}
	if (isNotEmpty(classname)) {
		rr += 'class="' + classname + '" ';
	}
	if (isNotEmpty(style)) {
		rr += 'style="' + style + '" ';
	}
	if (isNotEmpty(title)) {
		rr += 'title="' + title + '" ';
	}
	if (isNotEmpty(onclick)) {
		rr += 'onclick="' + onclick + '" ';
	}
	if (isNotEmpty(other)) {
		rr += other;
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</a>';
	return rr;
}

/**
 * @cmparam 一般button标签的封装
 *            精简
 * @cmparam data
 *            数据,
 * @cmparam onclick
 *            事件名带参数 getfun('123');
 */
function addbutton_s(data, onclick) {

	var rr = '<button ';
	if (isNotEmpty(onclick)) {
		rr += 'onclick="' + onclick + '" ';
	}
	rr += '>';
	if (isNotEmpty(data)) {
		rr += data;
	}
	rr += '</button>';
	return rr;
}

/**
 * 提示框弹出层 封装(暂时不能用这个)
 * 
 * @cmparam message
 *            弹框信息
 * @cmparam sucfun
 *            成功的处理方法
 * @cmparam failfun
 *            失败的处理方法
 * @cmparam otherparam
 *            方法带的参数
 */
function confirm_h(message) {

	if (!isNotEmpty(message)) {
		message = "提示";
	}

	// 询问框
	parent.layer.confirm(message, {
		btn : [ '确认', '取消' ], // 按钮
		shade : false, // 不显示遮罩
		skin : 'layui-layer-molv' // 样式类名 墨绿
	}, function(index) {
		parent.layer.close(index);
		return true;
	}, function(index) {
		parent.layer.close(index);
		return false;
	});
}

/**
 * 提示框弹出层 封装（完整版）(暂时不能用这个)
 * 
 * @cmparam message
 *            弹框信息
 * @cmparam otherparam
 *            方法带的参数
 * @cmparam truebtn
 *            成功按钮的文字
 * @cmparam falsebtn
 *            失败按钮的文字
 * @cmparam skin
 *            弹框的样式
 * @cmparam shade
 *            是否显示遮罩
 */
function confirm_h(message, truebtn, falsebtn, skin, shade) {

	if (!isNotEmpty(message)) {
		message = "提示";
	}
	if (!isNotEmpty(truebtn)) {
		truebtn = '确认';
	}
	if (!isNotEmpty(falsebtn)) {
		falsebtn = '取消';
	}
	if (!isNotEmpty(skin)) {
		skin = 'layui-layer-molv';
	}
	if (!isNotEmpty(shade)) {
		shade = false;
	}

	// 询问框
	parent.layer.confirm(message, {
		btn : [ truebtn, falsebtn ], // 按钮
		shade : shade, // 不显示遮罩
		skin : skin
	// 样式类名 墨绿
	}, function(index) {

		parent.layer.close(index);
		return true;
	}, function(index) {

		parent.layer.close(index);
		return false;
	});
}

/**
 * 可编辑页面弹出层 封装
 * 
 * @cmparam htmldata
 *            弹出框的页面信息（html格式）
 * @cmparam width
 *            弹出层的宽 一定是一个字符串
 * @cmparam height
 *            弹出层的高 一定是一个字符串
 * @cmparam skin
 *            弹框的样式
 */
function htmlopen(htmldata, width, height, skin) {

	if (!isNotEmpty(htmldata)) {
		htmldata = '调用为空哦';
	}
	if (!isNotEmpty(width)) {
		width = '420px';
	} else {
		if (width.indexOf('px') < 0) {
			width = width + 'px';
		}
	}
	if (!isNotEmpty(height)) {
		height = '240px';
	} else {
		if (height.indexOf('px') < 0) {
			height = height + 'px';
		}
	}
	if (!isNotEmpty(skin)) {
		skin = 'layui-layer-molv';
	}
	// 页面层

	layui.use("layer",function() {
		var layer = layui.layer;  //layer初始化
		layer.open({
			type : 1,
			skin : skin, // 加上边框
			area : [ width, height ], // 宽高
			content : htmldata
		});
	});

}

// -----------------------------
// 分页
/**
 * @cmparam pageid
 *            分页存放的div
 * @cmparam arrparam
 *            请求获取数据的几个查询条件的Array集合，要按请求方法中顺序排，不然会出错
 * @cmparam getdatafun
 *            请求获取数据的方法
 * @cmparam currPage
 *            当前页
 * @cmparam pageCount
 *            总页数
 * @cmparam pageSize
 *            每页大小
 */
function showpage(pageid, arrparam, getdatafun, currPage, pageCount, pageSize) {

	if (!isNotEmpty(getdatafun)) {
		parent.layer.msg('分页失败', {icon: 5});
		return;
	}

	if (!isNotEmpty(currPage)) {
		currPage = 1;
	}
	if (!isNotEmpty(pageCount)) {
		pageCount = 10;
	}
	if (!isNotEmpty(pageSize)) {
		pageSize = 10;
	}
	if (!isNotEmpty(pageid)) {
		pageid = "paging";
	}

	var noFirstClass = null;
	var noLastClass = null;
	if(currPage == 1){
		noFirstClass = "layui-disabled";
	}

	if(currPage == pageCount){
		noLastClass = "layui-disabled";
	}

	var page10_d = Math.ceil(currPage /  5);
	var startsize = page10_d * 5 - 4;
	var stopsize = page10_d * 5;
	var buttonnum = 0;
	if (startsize <= 0) {
		startsize = 1;
	}
	if (stopsize > pageCount) {
		stopsize = pageCount;
	}

	// 处理请求参数集合
	var reqparamdata = '';
	if (isNotEmpty(arrparam) && arrparam.length > 0) {
		for (var i = 0; i < arrparam.length; i++) {
			var param = arrparam[i];
			if (isNotEmpty(param)) {
				if (isNumber(param)) {
					reqparamdata += param + ",";
				} else {
					reqparamdata += '\'' + param + '\','
				}
			} else {
				reqparamdata += 'null,';
			}
		}
	}

	var pagetxt = addbutton("首页", getdatafun + '(' + reqparamdata + 1 + ','
			+ pageSize + ')', noFirstClass, null, null, "padding:3px 15px;cursor: pointer;", null,
		null);
	buttonnum++;
	if (startsize > 1) {// 还可以往前翻页
		// 去单引号，要不然转义会出错
		var reqparamdataprv = reqparamdata.replace(/'/g, "")
		buttonnum++;
		pagetxt += addbutton("«", 'gotobackpage(' + getdatafun + ',\''
				+ reqparamdataprv + '\',' + arrparam.length + ',' + (currPage)
				+ ',' + pageSize + ')', null, null, null, "padding:3px 15px;cursor: pointer;",
				null, null);
	}

	for (var i = startsize; i <= stopsize; i++) {
		if (currPage == i) {
			pagetxt += addbutton(i, getdatafun + '(' + reqparamdata + i + ','
					+ pageSize + ')', null, null, null, "color:#fff;background-color: #1e9fff;border-color: #1e9fff;padding:3px 15px;cursor: pointer;",
					null, null);
		} else {
			pagetxt += addbutton(i, getdatafun + '(' + reqparamdata + i + ','
					+ pageSize + ')', null, null, null, "padding:3px 15px;cursor: pointer;", null,
				null);
			// pagetxt +='<a href="javascript:;" onclick="getdatafun + \'(\' + reqparamdata + i + \',\'+ pageSize + \')\'" data-page="3">3</a>';

		}
		buttonnum++;
	}
	if (stopsize < pageCount) {// 还可以往后面翻页
		// 去单引号，要不然转义会出错
		var reqparamdatanext = reqparamdata.replace(/'/g, "")
		buttonnum++;
		pagetxt += addbutton('»', 'gotonextpage(' + getdatafun + ',\''
				+ reqparamdatanext + '\',' + arrparam.length + ',' + (currPage)
				+ ',' + pageSize + ')', null, null, null, "padding:3px 15px;cursor: pointer;",
				null, null);
	}
	pagetxt += addbutton("末页", getdatafun + '(' + reqparamdata + pageCount
			+ ',' + pageSize + ')', noLastClass, null, null, "padding:3px 15px;cursor: pointer;", null,
		null);
	buttonnum++;
	if (pageCount <= 1) {
		pagetxt = '';
	}
	$('#' + pageid).html(pagetxt);
	// if (pageCount > 1000) {
	// 	$('#' + pageid).width((52 * buttonnum + 45) + "px");
	// } else if (pageCount > 100) {
	// 	$('#' + pageid).width((55 * buttonnum + 40) + "px");
	// } else {
	// 	$('#' + pageid).width((45 * buttonnum + 40) + "px");
	// }

}

/**
 * 下一个10页
 * 
 * @cmparam getdatafun
 * @cmparam arrparam
 * @cmparam currPage
 * @cmparam pageSize
 */
function gotonextpage(getdatafun, arrparam, arrcount, currPage, pageSize) {

	var page10_d = Math.ceil(currPage / 5);
	currPage = page10_d * 5 + 1;

	// 处理请求参数集合
	if (isNotEmpty(arrparam) && arrparam.length > 0) {
		arrparam = RTrimByChar(arrparam, ",");
		var arrparam2 = arrparam.split(",");
		var reqparamdata = new Array();
		for (var i = 0; i < arrcount; i++) {
			var param = arrparam2[i];
			if (isNotEmpty(param)) {

				param = bothTrimByChar(param, "\\\\");
				param = bothTrimByChar(param, "\'");

				reqparamdata[i] = param;
			} else {
				reqparamdata[i] = null;
			}
		}
		// 最多10个参数
		if (reqparamdata.length == 1) {
			getdatafun(reqparamdata[0], currPage, pageSize);
		} else if (reqparamdata.length == 2) {
			getdatafun(reqparamdata[0], reqparamdata[1], currPage, pageSize);
		} else if (reqparamdata.length == 3) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					currPage, pageSize);
		} else if (reqparamdata.length == 4) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], currPage, pageSize);
		} else if (reqparamdata.length == 5) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], currPage, pageSize);
		} else if (reqparamdata.length == 6) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					currPage, pageSize);
		} else if (reqparamdata.length == 7) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], currPage, pageSize);
		} else if (reqparamdata.length == 8) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], currPage, pageSize);
		} else if (reqparamdata.length == 9) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], reqparamdata[8],
					currPage, pageSize);
		} else if (reqparamdata.length == 10) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], reqparamdata[8],
					reqparamdata[9], currPage, pageSize);
		} else {
			parent.layer.msg('分页请求的参数超过预期，失败,参数个数：' + reqparamdata.length, {icon: 5});
			return;
		}
	} else {
		getdatafun(currPage, pageSize);
	}

}

/**
 * 上一个10页
 * 
 * @cmparam getdatafun
 * @cmparam arrparam
 * @cmparam currPage
 * @cmparam pageSize
 */
function gotobackpage(getdatafun, arrparam, arrcount, currPage, pageSize) {

	var page10_d = Math.ceil(currPage / 5);
	currPage = (page10_d - 1) * 5;

	// 处理请求参数集合
	if (isNotEmpty(arrparam)) {

		arrparam = RTrimByChar(arrparam, ",");
		var arrparam2 = arrparam.split(",");
		var reqparamdata = new Array();
		for (var i = 0; i < arrcount; i++) {
			var param = arrparam2[i];
			if (isNotEmpty(param)) {
				param = bothTrimByChar(param, "\\\\");
				param = bothTrimByChar(param, "\'");

				reqparamdata[i] = param;
			} else {
				reqparamdata[i] = null;
			}
		}
		// 最多10个参数
		if (reqparamdata.length == 1) {
			getdatafun(reqparamdata[0], currPage, pageSize);
		} else if (reqparamdata.length == 2) {
			getdatafun(reqparamdata[0], reqparamdata[1], currPage, pageSize);
		} else if (reqparamdata.length == 3) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					currPage, pageSize);
		} else if (reqparamdata.length == 4) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], currPage, pageSize);
		} else if (reqparamdata.length == 5) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], currPage, pageSize);
		} else if (reqparamdata.length == 6) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					currPage, pageSize);
		} else if (reqparamdata.length == 7) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], currPage, pageSize);
		} else if (reqparamdata.length == 8) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], currPage, pageSize);
		} else if (reqparamdata.length == 9) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], reqparamdata[8],
					currPage, pageSize);
		} else if (reqparamdata.length == 10) {
			getdatafun(reqparamdata[0], reqparamdata[1], reqparamdata[2],
					reqparamdata[3], reqparamdata[4], reqparamdata[5],
					reqparamdata[6], reqparamdata[7], reqparamdata[8],
					reqparamdata[9], currPage, pageSize);
		} else {
			parent.layer.msg('分页请求的参数超过预期，失败' + reqparamdata.length, {icon: 5});
			return;
		}
	} else {
		getdatafun(currPage, pageSize);
	}
}



/**
 * laytpl 更新数据
 * pagelisttemplates 模板对应的script
 * pagelistview 把数据写入的view 的id
 */
function detail() {
	layui.use('laytpl', function () {
		var laytpl = layui.laytpl;
		var getTpl = document.getElementById("pagelisttemplates").innerHTML;
		var tpl = laytpl(getTpl);

		if(pagelist){

			tpl.render(pagelist, function (html) {
				var dataview=document.getElementById("pagelistview");
				dataview.innerHTML = html;

			});

		}
		layui.use('form', function(){
			var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
			form.render();
		});

	});

}

/**
 * 当前页的所有动作
 */
var pageActionByPage;

/**
 * 给当前页面更新动作集合
 * @cmparam InitVO
 * @cmparam pageid
 */
function setpageAction(InitVO,pageid){

	if(isNotEmpty(InitVO)&&isNotEmpty(pageid)){
		var code=InitVO.code;
		if(code=="0"){
			var pageList=InitVO.pageList;
			for(var i=0;i<pageList.length;i++){
				if(pageid==pageList[i].pageid){
					pageActionByPage=pageList[i];
					break;
				}
			}
		}
	}
}

/**
 * 通过动作actionid获取需要使用的动作对象
 * @cmparam actionid
 * @returns {*}
 */
function getAction(actionid){

	if(isNotEmpty(pageActionByPage)&&isNotEmpty(actionid)){

		if(pageActionByPage.actionList.length==0){
			return ;
		}
		for(var i=0;i<pageActionByPage.actionList.length;i++){
			if(actionid==pageActionByPage.actionList[i].actionId){
				return pageActionByPage.actionList[i];
			}
		}
	}

}


/**
 * 通过动作actionid获取需要使用的动作的URL
 * @cmparam actionid
 * @returns {*}
 */
function getActionURL(actionid){

	if(isNotEmpty(pageActionByPage)&&isNotEmpty(actionid)){

		if(pageActionByPage.actionList.length==0){
			return ;
		}
		for(var i=0;i<pageActionByPage.actionList.length;i++){

			if(actionid==pageActionByPage.actionList[i].actionId){
				return pageActionByPage.actionList[i].reqURL;
			}
		}
	}

}

/**
 * 检测字符串开头是否为指定字符串
 */
if (typeof String.prototype.startsWith != 'function'){
    String.prototype.startsWith = function (str){
        return this.slice(0, str.length) == str;
    };
}
//判断当前字符串是否以str结束
if (typeof String.prototype.endsWith != 'function') {
    String.prototype.endsWith = function (str){
        return this.slice(-str.length) == str;
    };
}






//*****************************************************关于身份证**********************************************//

/**
 * 二代身份证检测：
 * @param idcode
 * @returns {boolean}
 */
function checkIDCard(idcode){
    // 加权因子
    var weight_factor = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
    // 校验码
    var check_code = ['1', '0', 'X' , '9', '8', '7', '6', '5', '4', '3', '2'];

    var code = idcode + "";
    var last = idcode[17];//最后一位

    var seventeen = code.substring(0,17);

    // ISO 7064:1983.MOD 11-2
    // 判断最后一位校验码是否正确
    var arr = seventeen.split("");
    var len = arr.length;
    var num = 0;
    for(var i = 0; i < len; i++){
        num = num + arr[i] * weight_factor[i];
    }

    // 获取余数
    var resisue = num%11;
    var last_no = check_code[resisue];

    // 格式的正则
    // 正则思路
    /*
    第一位不可能是0
    第二位到第六位可以是0-9
    第七位到第十位是年份，所以七八位为19或者20
    十一位和十二位是月份，这两位是01-12之间的数值
    十三位和十四位是日期，是从01-31之间的数值
    十五，十六，十七都是数字0-9
    十八位可能是数字0-9，也可能是X
    */
    var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;
    // 判断格式是否正确
    var format = idcard_patter.test(idcode);

    // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
    return last === last_no && format ? true : false;
}


/**
 * 检测15位和18位身份证号码
 * @param idcode
 * @returns {boolean}
 */
function checkByIDCard(idcode) {
	if (isNotEmpty(idcode)){
		if (idcode.length==15) {
            var idcard_patter = /^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$/;
            return idcard_patter.test(idcode);
		}else if (idcode.length==18){
            return  checkIDCard(idcode);
		}
	}
	return false;
}


/**
 * 身份证解析：前提身份证号码为正确的
 * @param idcode：身份证号码
 * @param type：1出生日期 2性别 3年龄
 * @returns {*}
 */
function getAnalysisIdCard(idcode, type) {
    if (idcode.length==15){
        if (type == 1) {
            //获取出生日期
            let birth = "19"+idcode.substring(6,8)+"-"+idcode.substring(8,10)+"-"+idcode.substring(10,12);
            return birth;
        }
        if (type == 2) {
            //获取性别
            if (parseInt(idcode.substr(14, 1)) % 2 == 1) { return 1; } else {return 2;}
        }
        if (type == 3) {
            //获取年龄
            var myDate = new Date();
            var month = myDate.getMonth() + 1;
            var day = myDate.getDate();
            var age = myDate.getFullYear() - parseInt("19"+idcode.substring(6, 8)) - 1;
            if (idcode.substring(8, 10) < month || idcode.substring(8, 12) == 10 && idcode.substring(10, 12) <= day) { age++;}
            return age;
        }
    }else  if (idcode.length==18){
        if (type == 1) {
            //获取出生日期
            let birth = idcode.substring(6, 10) + "-" + idcode.substring(10, 12) + "-" + idcode.substring(12, 14);
            return birth;
        }
        if (type == 2) {
            //获取性别
            if (parseInt(idcode.substr(16, 1)) % 2 == 1) { return 1; } else {return 2;}
        }
        if (type == 3) {
            //获取年龄
            var myDate = new Date();
            var month = myDate.getMonth() + 1;
            var day = myDate.getDate();
            var age = myDate.getFullYear() - idcode.substring(6, 10) - 1;
            if (idcode.substring(10, 12) < month || idcode.substring(10, 12) == month && idcode.substring(12, 14) <= day) { age++;}
            return age;
        }
    }
    return "";
}

/**
 * 身份证读卡
 */
function getIDCardreader() {
    var cardtypetext=$("#cards option:selected").text();
    if ($.trim(cardtypetext)!="居民身份证"){
        layer.msg("请先选择居民身份证",{icon:5});
        return;
    }
    layer.msg("开始读卡，请稍等",{icon:6});
    $.ajax({
        dataType: "JSONP",
        type: "get",
        url: "http://localhost:8989/api/ReadMsg",
        timeout: 3000,
        success: function (data) {
            if (isNotEmpty(data)){
                reset();
                var retmsg=data.retmsg==null?"未知错误":data.retmsg;
                var CardType=data.CardType;//0身份证 1其他国家身份证 2港澳居住证
                var username="";
                if (CardType==0||CardType==2){
                    var bool=checkByIDCard(data.cardno);
                    if (!bool){
                        return;
                    }
                    username=data.name==null?"":data.name;

                    var nation=data.nation;
                    $("#national option").each(function () {
                        var txt=$(this).text();
                        var value=$(this).attr("value");
                        if (txt.indexOf(nation)>-1){
                            $("#national").val(value);
                            return;
                        }
                    })
                    var nationality_value=$("#nationality option[title='China']").attr("value");
                    $("#nationality").val(nationality_value);
                } else if (CardType==1){
                    var nation=data.nation;
                    $("#nationality option").each(function () {
                        var txt=$(this).text();
                        var value=$(this).attr("value");
                        if (txt.indexOf(nation)>-1){
                            $("#nationality").val(value);
                            return;
                        }
                    })
                    username=data.EngName==null?"":data.EngName
                }
                $("#username").val(username);
                $("#cardnum").val(data.cardno);
                $("#domicile").val(data.address);
                $("#sex").val(data.sex=="女"?2:(data.sex=="男"?1:-1));
                layui.use('form', function(){
                    var form =  layui.form;
                    form.render();
                });
                layer.msg(retmsg,{icon:6,time:1000},function () {
                    getUserByCard();
                });
            }
        },
        error: function (e) {
            layer.msg("请先确认身份证读卡设备是否插上",{icon:5})
        }
    });
}

//*****************************************************关于身份证**********************************************//

//========================================js通用常量:需要和java文件的同步========================start
//存储在cookie的记住密码
var CLIENT_LOGINACCOUNT="CLIENT_LOGINACCOUNT";
var CLIENT_REMEMBERME="CLIENT_REMEMBERME";
var SERVER_LOGINACCOUNT="SERVER_LOGINACCOUNT";
var SERVER_REMEMBERME="SERVER_REMEMBERME";

//多角色默认ssid
var USERINFOGRADE1="userinfograde1";//原告
var USERINFOGRADE2="userinfograde2";//被告
var USERINFOGRADE3="userinfograde3";//被告诉讼代理人
var USERINFOGRADE4="userinfograde4";//审判长
var USERINFOGRADE5="userinfograde5";//书记员
var USERINFOGRADE6="userinfograde6";//陪审员
var USERINFOGRADE7="userinfograde7";//审判员
var USERINFOGRADE8="userinfograde8";//原告诉讼代理人

var S_V = "s_v";//单机版
var O_V = "o_v";//联机版
var C_E = "c_e";//客户端
var S_E = "s_e";//服务器
var GA_T = "ga_t";//公安版
var JW_T = "jw_t";//纪委版
var JCW_T = "jcw_t";//监察委版
var FY_T = "fy_t";//法院版
var AVST_T = "avst_t";//顺泰伟诚
var COMMON_O = "common_o";//通用版
var HK_O = "hk_o";//海康
var NX_O = "nx_o";//宁夏
//授权功能
var RECORD_F="record_f";
var ASR_F="asr_f";
var TTS_F="tts_f";
var FD_F="fd_f";
var PH_F="ph_f";
//========================================js通用常量:需要和java文件的同步========================end


