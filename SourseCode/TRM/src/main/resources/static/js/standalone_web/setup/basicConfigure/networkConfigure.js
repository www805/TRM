var fdType = "FD_AVST";

function getFDNetWork() {
    var url=getActionURL(getactionid_manage().networkConfigure_getFDNetWork);
    // var url = "/cweb/setUp/basicConfigure/getNetworkConfigure";

    var data = {
            token: INIT_CLIENTKEY,
            param: {
                flushbonadingetinfossid:"sxsba2",
                fdType:fdType
            }
        };

    ajaxSubmitByJson(url, data, callgetFDNetWork);
}


function setNetworkConfigure() {
    var url=getActionURL(getactionid_manage().networkConfigure_setNetworkConfigure);
    // var url = "/cweb/setUp/basicConfigure/setNetworkConfigure";

    var ip=$("input[name='ip']").val();
    var subnetMask=$("input[name='subnetMask']").val();
    var gateway=$("input[name='gateway']").val();

    var data = {
        token: INIT_CLIENTKEY,
        param: {
            ip:ip,
            subnetMask:subnetMask,
            gateway:gateway
        }
    };

    ajaxSubmitByJson(url, data, callsetNetworkConfigure);
}

function callgetFDNetWork(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

            if(isNotEmpty(data.data.lanList)){
                var dataInfo = data.data.lanList[0];
                layui.form.val('example', {
                    "ip": dataInfo.ip
                    ,"netmask": dataInfo.netmask //子网掩码
                    ,"gateway": dataInfo.gateway //网关
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function callsetNetworkConfigure(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            location.replace(location.href);
            layer.msg(data.message,{icon: 6});
            // layer.msg("修改成功，因修改IP、子掩码、网关系统可能会有3-5秒的延迟",{icon: 6});
            // setTimeout(function(){
            //     location.replace(location.href);
            // },5000);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}