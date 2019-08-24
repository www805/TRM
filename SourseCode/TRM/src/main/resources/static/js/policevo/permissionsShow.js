
function getRoles() {
    var url=getActionURL(getactionid_manage().permissionsShow_getRoles);
    ajaxSubmit(url,null,callbackgetRoles);
}
function callbackgetRoles(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
            $("#rolessid").html("");
            if (isNotEmpty(list)){
                for (var i = 0; i < list.length; i++) {
                    var roles = list[i];
                    $("#rolessid").append("<option value='"+roles.ssid+"' >"+roles.rolename+"</option>");
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

function getPermissions() {
    var url=getActionURL(getactionid_manage().permissionsShow_getPermissions);
    ajaxSubmit(url,null,callgetPermissions);
}
function callgetPermissions(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                var permissionsVOParams=data.permissionsVOParams;
                $("#menuBody").html("");
                $("#funBody").html("");
                if (isNotEmpty(permissionsVOParams)) {
                    for (var i = 0; i < permissionsVOParams.length; i++) {
                        var datum = permissionsVOParams[i];
                        var Bodyhtml=' <fieldset class="layui-elem-field " >\
                                                            <legend><input type="checkbox"  title="'+datum.name+'" value="'+datum.ssid+'" id="allChoose" lay-filter="allChoose" ></legend>\
                                                            <div class="layui-field-box">';
                        var permissionsList=datum.permissionsList;
                        if (isNotEmpty(permissionsList)) {
                            for (var j = 0; j < permissionsList.length; j++) {
                                var fun = permissionsList[j];
                                Bodyhtml+=' <input type="checkbox" lay-skin="primary" title="'+fun.name+'"  value="'+fun.ssid+'" onclick="checkboxlist(this);" id="oneChoose"  lay-filter="oneChoose">';
                            }
                        }

                        Bodyhtml+='</div></fieldset>';
                        if (datum.type==1){
                            $("#menuBody").append(Bodyhtml);
                        }else if (datum.type==2){
                            $("#funBody").append(Bodyhtml);
                        }
                    }
                    getPermissionsByRoleSsid();//根据角色ssid获取全部权限
                }

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
    layui.use('form', function(){
        var form =  layui.form;
        form.render();

        form.on('checkbox(allChoose)', function (data) {
            var $dq_html=$(this).closest("fieldset").find(".layui-field-box");
            $("input",$dq_html).each(function () {
                this.checked = data.elem.checked;
            });
            form.render('checkbox');
        });

        form.on('checkbox(oneChoose)', function (data) {
            var $dq_allChoosehtml=$(this).closest("fieldset").find("#allChoose");
            var $dq_html=$(this).closest("fieldset").find(".layui-field-box");
            var i = 0;
            var j = 0;
            $("input",$dq_html).each(function () {
                if( this.checked === true ) { i++; }j++;
            });
            if( i == j ){
                $($dq_allChoosehtml).prop("checked",true);
                form.render('checkbox');
            }else{
                //不需要设置：左侧菜单例如用户管理是一级目录类型   右侧权限例如：用户管理只是标题没有实际权限
               /* $($dq_allChoosehtml).removeAttr("checked");
                form.render('checkbox');*/
            }
        });
    });
}

function getPermissionsByRoleSsid() {
    var rolessid=$("#rolessid option:selected").val();
    if (isNotEmpty(rolessid)){
        var url=getActionURL(getactionid_manage().permissionsShow_getPermissionsByRoleSsid);
        var data={
            rolessid:rolessid
        };
        ajaxSubmit(url,data,callgetPermissionsByRoleSsid);
    }
}

function callgetPermissionsByRoleSsid(data) {
    $("#funcheckboxs input[type='checkbox']").prop("checked", false);
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
            if (isNotEmpty(list)){
                var all = new Array();
                $("#funcheckboxs input[type='checkbox']").each(function(index, element) {
                    all.push($(this).val());
                });
                for (var i = 0; i < list.length; i++) {
                    var l=list[i];
                    for (var j = 0; j < all.length; j++) {
                        if(all[j]==l.permissionsssid){
                            $("#funcheckboxs input[value=" + l.permissionsssid + "]").prop("checked", true);
                        }
                    }
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

function updateRoleToPermissions() {
    var rolessid=$("#rolessid option:selected").val();
    var permissions=[];
    $("#funcheckboxs input[type='checkbox']:checked").each(function(index, element){
        permissions.push({
            ssid:this.value
        });
    });
    if (isNotEmpty(rolessid)){
        var url=getActionURL(getactionid_manage().permissionsShow_updateRoleToPermissions);
        var data={
            rolessid:rolessid,
            permissions:permissions
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
                    callbackupdateRoleToPermissions(reData);
                }
            },error : function(){
                parent.layer.msg("请求异常",{icon:5});
            }
        });
    }
}

function callbackupdateRoleToPermissions(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        if (isNotEmpty(data)) {
            var data = data.data;
            if (isNotEmpty(data)) {
                layer.msg("保存成功", {icon: 6});
                getPermissionsByRoleSsid();
            }
        } else {
            layer.msg(data.message, {icon: 5});
        }
        layui.use('form', function () {
            var form = layui.form;
            form.render();
        });
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

