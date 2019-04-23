
function login_login(){

    var url=getActionURL(getactionid_manage().login_login);

    var loginaccount =$('input[name="loginaccount"]').val();
    var password =$('input[name="password"]').val();
    var data={
        loginaccount:loginaccount,
        password:password
    };
    ajaxSubmit(url,data,callbackgetAdminInfoPage);

}

function callbackgetAdminInfoPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        //pageshow(data);
        // alert("登录成功");
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        //parent.layer.msg(data.message,{icon: 2},1);
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:6});
    }

}

function login_logout(){

    var url=getActionURL(getactionid_manage().main_logout);

    ajaxSubmit(url,null,callLogout);
}

function callLogout(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        alert(data.message);
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:6});
    }

}


