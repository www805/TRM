
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

// 给父页面的参数赋值
function parentValueAssignment(id, str) {
	parent.document.getElementById(id).value = str;
}
