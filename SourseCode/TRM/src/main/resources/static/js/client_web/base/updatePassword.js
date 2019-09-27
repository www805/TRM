
function updatePassword() {
    var url=getActionURL(getactionid_manage().main_updatePassWord);
    var oldpassword=$("input[name='oldpassword']").val();
    var newpassword=$("input[name='newpassword']").val();
    var password=$("input[name='password']").val();

    if ("" == oldpassword) {
        layer.msg("旧密码不能为空",{icon: 5});
        return;
    }
    if ("" == newpassword) {
        layer.msg("新密码不能为空",{icon: 5});
        return;
    }
    if ("" == password) {
        layer.msg("确认密码不能为空",{icon: 5});
        return;
    }

    if(newpassword != password){
        layer.msg("两次确认密码必须一样",{icon: 5});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid,
            oldpassword: oldpassword,
            newpassword:newpassword,
            password:password,
            firstloginbool:-1,//不是第一次登录
        }
    };
    ajaxSubmitByJson(url,data,callupdatePassword);
}

function callupdatePassword(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if(data.data == 1){
                layer.msg("修改成功",{icon: 6});
                setTimeout("window.location.reload()",1500);
            }else{
                layer.msg(data.message,{icon: 5});
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}