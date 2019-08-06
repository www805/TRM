
function updatePersonInfo() {
    var url=getActionURL(getactionid_manage().main_updatePersonInfo);
    var username=$("input[name='username']").val();
    var workunitssid=$("#workunitssid").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            ssid: ssid,
            username: username,
            workunitssid:workunitssid
        }
    };
    ajaxSubmitByJson(url,data,callupdatePersonInfo);
}

function getWorkunits() {
    var url=getActionURL(getactionid_manage().main_getWorkunits);
    var data={
        token:INIT_CLIENTKEY,
        param:{

        }
    };
    ajaxSubmitByJson(url,data,callbackgetWorkunits);
}


function getUserBySsid(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }
    var url=getActionURL(getactionid_manage().main_getUserBySsid);
    var data={
        ssid:ssid
    };
    ajaxSubmit(url,data,callbackgetUserBySsid);
}

function callbackgetUserBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                $("#loginaccount").val(data.loginaccount);
                $("#username").val(data.username);
                $("#password").val(data.password);
                $("#workunitssid").find("option[value='"+data.workunitssid+"']").attr("selected",true);
                if (data.adminbool==1) {
                    $("#adminbool").prop("checked", true);
                }else {
                    $("#adminbool").prop("checked", false);
                }

                var roles=data.roles;
                if(isNotEmpty(roles)){
                    var all = new Array();
                    $("#rolessid_checkbox input").each(function(index, element) {
                        all.push($(this).val());
                    });
                    for (var i = 0; i < roles.length; i++) {
                        var ar=roles[i];
                        for (var j = 0; j < all.length; j++) {
                            if(all[j]==ar.ssid){
                                $("#rolessid_checkbox input[value=" + ar.ssid + "]").prop("checked", true);
                            }
                        }
                    }
                }
                layui.use('form', function(){
                    var form =  layui.form;
                    form.render();
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callbackgetWorkunits(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
            if (isNotEmpty(list)){
                for (var i = 0; i < list.length; i++) {
                    var workunits = list[i];
                    $("#workunitssid").append("<option value='"+workunits.ssid+"' >"+workunits.workname+"</option>");
                }
                layui.use('form', function(){
                    var form =  layui.form;
                    form.render();
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

function callupdatePersonInfo(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if(data.data == 1){
                layer.msg("修改成功",{icon: 1});
                setTimeout("window.parent.location.reload();",1500);
            }else{
                layer.msg(data.message,{icon: 2});
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}