
function updatePassword() {
    var url=getActionURL(getactionid_manage().updatePassword);
    var oldpassword=$("input[name='oldpassword']").val();
    var newpassword=$("input[name='newpassword']").val();
    var password=$("input[name='password']").val();

    if ("" == oldpassword) {
        layer.msg("旧密码不能为空",{icon: 2});
        return;
    }
    if ("" == newpassword) {
        layer.msg("新密码不能为空",{icon: 2});
        return;
    }
    if ("" == password) {
        layer.msg("确认密码不能为空",{icon: 2});
        return;
    }

    if(newpassword != password){
        layer.msg("两次确认密码必须一样",{icon: 2});
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid,
            username: username,
            newpassword:newpassword,
            password:password
        }
    };
    ajaxSubmitByJson(url,data,callupdatePassword);
}

function callupdatePassword(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if(data.data == 1){
                layer.msg("修改成功",{icon: 1});
                setTimeout("window.location.reload()",1500);
            }else{
                layer.msg(data.message,{icon: 2});
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}