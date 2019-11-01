var fdType = "FD_AVST";
var flushbonadingetinfossid;
var networkConfigurebool;

function getFDNetWork() {
    var url=getActionURL(getactionid_manage().networkConfigure_getFDNetWork);
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

    ajaxSubmitByJson(url, data, callgetFDNetWork);
}

//获取默认设备的ssid
function getFDssid() {
    var url=getActionURL(getactionid_manage().networkConfigure_getFDssid);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var data = {
        token: INIT_CLIENTKEY,
        param: {}
    };

    ajaxSubmitByJson(url, data, callgetFDssid);
}

function setNetworkConfigure() {
    var url=getActionURL(getactionid_manage().networkConfigure_setNetworkConfigure);
    // var url = "/cweb/setUp/basicConfigure/setNetworkConfigure";

    var ip=$("input[name='ip']").val();
    var netmask=$("input[name='netmask']").val();
    var gateway=$("input[name='gateway']").val();

    var params = [];
    var param = {
        ip_new: ip,
        netmask: netmask,
        gateway: gateway,
        flushbonadingetinfossid: flushbonadingetinfossid,
        fdType: fdType
    };
    params.push(param);

    var ip2=$("input[name='ip2']").val();
    var netmask2=$("input[name='netmask2']").val();
    var gateway2=$("input[name='gateway2']").val();

    if(isNotEmpty(ip2) && isNotEmpty(netmask2) && isNotEmpty(gateway2)){
        var param2 = {
            ip_new: ip2,
            netmask: netmask2,
            gateway: gateway2,
            dev: "eth1",
            flushbonadingetinfossid: flushbonadingetinfossid,
            fdType: fdType
        };
        params.push(param2);
    }

    var data = {
        token: INIT_CLIENTKEY,
        param: params
    };

    ajaxSubmitByJson(url, data, callsetNetworkConfigure);
}

function callgetFDNetWork(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            if(isNotEmpty(data.data.lanList)){

                var lanlist = data.data.lanList;
                var lanHTML = "";
                var lanNum = "";

                for (var i = 0; i < lanlist.length; i++) {

                    var dataInfo = data.data.lanList[i];
                    if (i > 0) {
                        lanHTML += "\n";
                        lanNum = i + 1;
                    }

                    lanHTML += '<div class="layui-form-item">\n' +
                        '                <div class="layui-inline">\n' +
                        '                    <label class="layui-form-label">IP' + lanNum + '</label>\n' +
                        '                    <div class="layui-input-block">\n' +
                        '                        <input type="text" name="ip' + lanNum + '" lay-verify="setip" autocomplete="off" placeholder="请输入IP' + lanNum + '" class="layui-input" value="' + dataInfo.ip + '">\n' +
                        '                    </div>\n' +
                        '                </div>\n' +
                        '            </div>\n' +
                        '            <div class="layui-form-item">\n' +
                        '                <div class="layui-inline">\n' +
                        '                    <label class="layui-form-label">子网掩码' + lanNum + '</label>\n' +
                        '                    <div class="layui-input-block">\n' +
                        '                        <input type="text" name="netmask' + lanNum + '" lay-verify="required" autocomplete="off" placeholder="请输入子网掩码' + lanNum + '" class="layui-input" value="' + dataInfo.netmask + '">\n' +
                        '                    </div>\n' +
                        '                </div>\n' +
                        '            </div>\n' +
                        '            <div class="layui-form-item">\n' +
                        '                <div class="layui-inline">\n' +
                        '                    <label class="layui-form-label">网关' + lanNum + '</label>\n' +
                        '                    <div class="layui-input-block">\n' +
                        '                        <input type="text" name="gateway' + lanNum + '" lay-verify="gatewayip" autocomplete="off" placeholder="请输入网关地址' + lanNum + '" class="layui-input" value="' + dataInfo.gateway + '">\n' +
                        '                    </div>\n' +
                        '                </div>\n' +
                        '            </div>';
                }

                $("#ecip").html(lanHTML);

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callsetNetworkConfigure(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("操作成功",{icon: 6});
            setTimeout(function(){location.replace(location.href);},1500);
            // layer.msg("修改成功，因修改IP、子掩码、网关系统可能会有3-5秒的延迟",{icon: 6});
            // setTimeout(function(){
            //     location.replace(location.href);
            // },5000);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callgetFDssid(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            flushbonadingetinfossid = data.data;
            getFDNetWork();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}