
function login_login(){

    var url=getActionURL(getactionid_manage().login_login);

    console.log(pageActionByPage);
    console.log(url);

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
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        layer.msg(data.message, {icon: 2});
    }
}

function login_logout(){

    var url=getActionURL(getactionid_manage().main_logout);

    ajaxSubmit(url,null,callLogout);
}

function callLogout(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        //alert(data.message);
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:6});
    }

}


