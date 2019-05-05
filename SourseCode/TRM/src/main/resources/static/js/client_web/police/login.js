


function userlogin() {
    var url=getActionURL(getactionid_manage().login_userlogin);
    var loginaccount =$("#loginaccount").val();
    var password =$("#password").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            loginaccount:loginaccount,
            password:password
        }
    };
    ajaxSubmitByJson(url,data,callbackuserlogin);
}

function callbackuserlogin(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("登陆成功",{time:200},function () {
                var url=getActionURL(getactionid_manage().login_gotomain);
                window.location.href=url;
            });
        }
    }else{
        layer.msg(data.message);

    }
}