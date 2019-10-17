
function getUserinfogradeByssid() {
    if (!isNotEmpty(ssid)){
        return;
    }

    var url=getActionURL(getactionid_manage().AddOrUpdateUserinfograde_getUserinfogradeByssid);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid:ssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserinfogradeByssid);
}
function callbackgetUserinfogradeByssid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var userinfograde=data.userinfograde;
                if (isNotEmpty(userinfograde)){
                    $("#gradename").val(userinfograde.gradename);
                    $("#gradetype").val(userinfograde.gradetype);
                    $("#grade").val(userinfograde.grade);
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form =  layui.form;
        form.render();
    });
}


function addOrUpdateUserinfograde() {
    var url=null;
    if (isNotEmpty(ssid)){
        url=getActionURL(getactionid_manage().AddOrUpdateUserinfograde_updateUserinfograde);
    }else{
        url=getActionURL(getactionid_manage().AddOrUpdateUserinfograde_addUserinfograde);
    }
    var gradename=$("#gradename").val();
    var gradetype=$("#gradetype").val();
    var grade=$("#grade").val();


    if (!isNotEmpty(gradename)) {
        layer.msg("请输入级别名称",{icon:5});
        $("#gradename").focus();
        return;
    }
    if (!isNotEmpty(gradetype)){
        layer.msg("请选择级别类型",{icon:5});
        $("#gradetype").focus();
        return;
    }
    if (!isNotEmpty(grade)){
        layer.msg("请选择级别值",{icon:5});
        $("#grade").focus();
        return;
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid:ssid,
            gradename:gradename,
            gradetype:gradetype,
            grade:grade,
        }
    };
    ajaxSubmitByJson(url,data,callbackaddOrUpdateUserinfograde);
}
function callbackaddOrUpdateUserinfograde(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("保存成功",{icon: 6,time:500},function () {
                    javascript:window.history.go(-1);return false;
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

