var fdType = "FD_AVST";
var flushbonadingetinfossid;

//获取默认设备的ssid
function getFDssid() {
    var url=getActionURL(getactionid_manage().dateTime_getFDssid);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var data = {
        token: INIT_CLIENTKEY,
        param: {}
    };

    ajaxSubmitByJson(url, data, callgetFDssid);
}

//获取设备NTP同步设置
function getFDNTP() {
    var url=getActionURL(getactionid_manage().dateTime_getFDNTP);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            flushbonadingetinfossid:flushbonadingetinfossid,
            fdType:fdType
        }
    };

    ajaxSubmitByJson(url, data, callgetFDNTP);
}

//设置x85系统时间
function setEcSystemTime() {
    var url=getActionURL(getactionid_manage().dateTime_setEcSystemTime);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var systemTime = $("input[name='systemTime']").val();

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            systemTime:systemTime,
            flushbonadingetinfossid:flushbonadingetinfossid,
            fdType:fdType
        }
    };

    ajaxSubmitByJson(url, data, callSystemTime);
}


//同步本机时间
function setEcSystemTimeSync() {
    var url=getActionURL(getactionid_manage().dateTime_setEcSystemTimeSync);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            flushbonadingetinfossid:flushbonadingetinfossid,
            fdType:fdType
        }
    };

    ajaxSubmitByJson(url, data, callSystemTime);
}

//设置NTP同步
function setNTP() {
    var url=getActionURL(getactionid_manage().dateTime_setNTP);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var ntpip = $("input[name='ntpip']").val();
    var timeInterval=$("#timeInterval").val();
    var ntpprot = $("input[name='ntpprot']").val();

    if(!isNotEmpty(flushbonadingetinfossid)){
        layer.msg("获取默认设备ssid为空，操作失败",{icon: 5});
        return;
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ntpip:ntpip,
            timeInterval:timeInterval,
            ntpprot:ntpprot,
            flushbonadingetinfossid:flushbonadingetinfossid,
            fdType:fdType
        }
    };

    ajaxSubmitByJson(url, data, callSystemTime);
}

function callSystemTime(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg(data.message,{icon: 6});
            setTimeout(function(){
                location.replace(location.href);
            },2000);
        }
    }else{
        layer.msg(data.message,{icon: 5, time: 3000});
    }
}

function callgetFDNTP(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var fdDNTP = data.data;
            // console.log(fdDNTP);

            var ntp_host = fdDNTP.ntp_host;
            var ntp_intervaltime = fdDNTP.ntp_intervaltime;
            var ntp_port = fdDNTP.ntp_port;

            $("input[name='ntpip']").val(ntp_host);
            $("input[name='ntpprot']").val(ntp_port);
            $("#timeInterval").val(ntp_intervaltime);

            layui.form.render();

        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callgetFDssid(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            flushbonadingetinfossid = data.data;
            getFDNTP();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}