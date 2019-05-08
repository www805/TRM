
function getServerconfig() {
    var url=getActionURL(getactionid_manage().updateServerconfig_getServerconfig);
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callbackgetServerconfig);
}

function callbackgetServerconfig(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
           console.log(data)
        }
    }else{
        layer.msg(data.message);

    }
}

function updateServerconfig() {
    var url=getActionURL(getactionid_manage().updateServerconfig_updateServerconfig);
    var loginaccount =$("#loginaccount").val();
    var password =$("#password").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            loginaccount:loginaccount,
            password:password
        }
    };
    ajaxSubmitByJson(url,data,callbackupdateServerconfig);
}

function callbackupdateServerconfig(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("登陆成功",{time:500},function () {
                var url=getActionURL(getactionid_manage().login_gotomain);
                window.location.href=url;
            });
        }
    }else{
        layer.msg(data.message);

    }
}