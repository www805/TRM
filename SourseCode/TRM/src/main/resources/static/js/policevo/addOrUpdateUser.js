function getWorkunits() {
    var url=getActionURL(getactionid_manage().addOrUpdateUser_getWorkunits);
    ajaxSubmit(url,null,callbackgetWorkunits);
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
        layer.msg(data.message,{icon: 5});
    }
}

function getRoles() {
    var url=getActionURL(getactionid_manage().addOrUpdateUser_getRoles);
    ajaxSubmit(url,null,callbackgetRoles);
}

function callbackgetRoles(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
            var rolessid_checkbox=$("#rolessid_checkbox").html("");
            if (isNotEmpty(list)){
                for (var i = 0; i < list.length; i++) {
                    var roles = list[i];

                    rolessid_checkbox.append("<input  value='"+roles.ssid+"'   type='checkbox' name='' title='"+roles.rolename+"'>");
                    if (roles.ssid=="defaultrole") {//默认选中默认管理员
                        $("#rolessid_checkbox input[value=" + roles.ssid + "]").prop("checked", true);
                    }
                }
                layui.use('form', function(){
                    var form =  layui.form;
                    form.render('checkbox');
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}




function getUserBySsid(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 5});
        return;
    }

    var url=getActionURL(getactionid_manage().addOrUpdateUser_getUserBySsid);
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
                    $("#rolessid_checkbox input").prop("checked", false);
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
        layer.msg(data.message,{icon: 5});
    }
}



function addOrUpdateUser() {
    var url=null;
    if (isNotEmpty(ssid)){
        url=getActionURL(getactionid_manage().addOrUpdateUser_updateUser);
    }else{
        url=getActionURL(getactionid_manage().addOrUpdateUser_addUser);
    }

    var loginaccount=$("#loginaccount").val();
    var username=$("#username").val();
    var password=$("#password").val();
    var workunitssid=$("#workunitssid option:selected").val();//获取当前选择项.
    var admin_bool=$("#adminbool").prop("checked");
    var adminbool;
    if (admin_bool){
        adminbool=1;
    } else{
        adminbool=2;
    }
    var roles=[];
    $("#rolessid_checkbox :checked").each(function(index, element){
        roles.push({
            ssid:this.value
        });
    });

    if (!isNotEmpty(ssid)){
        var password_again=$("#password_again").val();
        if (password!=password_again) {
            layer.msg("密码输入不一致",{icon: 5});
            return false;
        }
    }

    var data={
        loginaccount:loginaccount,
        username:username,
        password:password,
        workunitssid:workunitssid,
        adminbool:adminbool,
        roles:roles,
        ssid:ssid
    };

    $.ajax({
        url : url,
        type : "post",
        async : true,
        dataType : "json",
        data : JSON.stringify(data),
        timeout : 60000,
        contentType: "application/json",
        success : function(reData) {
            if ($.trim(reData) == null) {
                parent.layer.msg("本次请求失败",{icon: 5});
            } else {
                callbackaddOrUpdateUser(reData);
            }
        },error : function(){
            parent.layer.msg("请求异常",{icon: 5});
        }
    });
}

function callbackaddOrUpdateUser(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("保存成功",{icon: 6,time:500},function () {
                    var nextparam=getAction(getactionid_manage().addOrUpdateUser_updateUser);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_WEB,nextparam.nextPageId);
                        var url=getActionURL(getactionid_manage().main_getUser);
                        window.location.href=url;
                    }
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}