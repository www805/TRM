
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
        alert(data.message);
    }

}

