
function getRoleBySsid(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }

    var url=getActionURL(getactionid_manage().addOrUpdateRole_getRoleBySsid);
    var data={
        ssid:ssid
    };
    ajaxSubmit(url,data,callbackgetRoleBySsid);
}

function callbackgetRoleBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
          var data=data.data;
          if (isNotEmpty(data)){
              $("#rolename").val(data.rolename);
              $("#rolebool").val(data.rolebool);
              $("#description").val(data.description);
              if (data.rolebool==1) {
                  $("#rolebool").prop("checked", true);
              }else {
                  $("#rolebool").prop("checked", false);
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

function addOrUpdateRole() {
    var url=null;
    if (isNotEmpty(ssid)){
        url=getActionURL(getactionid_manage().addOrUpdateRole_updateRole);
    }else{
        url=getActionURL(getactionid_manage().addOrUpdateRole_addRole);
    }

    var rolename=$("#rolename").val();
    var role_bool=$("#rolebool").prop("checked");
    var rolebool;
    if (role_bool){
        rolebool=1;
    } else{
        rolebool=2;
    }
    var description=$("#description").val();

    var data={
        rolename:rolename,
        rolebool:rolebool,
        description:description,
        ssid:ssid
    };
    ajaxSubmit(url,data,callbackaddOrUpdateRole);
}

function callbackaddOrUpdateRole(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("保存成功",{icon: 1,time:500},function () {
                    var nextparam=getAction(getactionid_manage().addOrUpdateRole_updateRole);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_WEB,nextparam.nextPageId);
                        var url=getActionURL(getactionid_manage().main_getRole);
                        window.location.href=url;
                    }
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

