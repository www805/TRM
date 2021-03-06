
function getUserList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var username =$("#username").val();
    var loginaccount =$("#loginaccount").val();
    var workunitssid=$("#workunitssid option:selected").val();
    var rolessid=$("#rolessid option:selected").val();
    var adminbool=$("#adminbool option:selected").val();
    var temporaryaskbool=$("#temporaryaskbool option:selected").val();
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
        temporaryaskbool:temporaryaskbool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetUserList);
}

function getUserList(username,loginaccount,workunitssid,rolessid,adminbool,temporaryaskbool,currPage,pageSize){
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
        temporaryaskbool:temporaryaskbool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetUserList);
}


function callbackgetUserList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 6});
    }
}

/**
 * 局部刷新
 */
function getUserListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getUserList_init(currPage,pageSize);
    }else if (len==2){
        getUserList('',arguments[0],arguments[1]);
    }else if(len>2){
        getUserList(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6],arguments[7]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var username=pageparam.username;
        var loginaccount=pageparam.loginaccount;
        var workunitssid=pageparam.workunitssid;
        var rolessid=pageparam.rolessid;
        var adminbool=pageparam.adminbool;
        var temporaryaskbool=pageparam.temporaryaskbool;

        var arrparam=new Array();
        arrparam[0]=username;
        arrparam[1]=loginaccount;
        arrparam[2]=workunitssid;
        arrparam[3]=rolessid;
        arrparam[4]=adminbool;
        arrparam[5]=temporaryaskbool;
        showpage("paging",arrparam,'getUserListByParam',currPage,pageCount,pageSize);
    }

}

function getRoles() {
    var url=getActionURL(getactionid_manage().getUserList_getRoles);
    ajaxSubmit(url,null,callbackgetRoles);
}

function callbackgetRoles(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
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
}

function getWorkunits() {
    var url=getActionURL(getactionid_manage().getUserList_getWorkunits);
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
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function deleteUser(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 5});
        return;
    }

    layer.confirm('确定要删除该用户吗', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().getUserList_deleteUser);
        var data={
            ssid:ssid,
            adminbool:-1
        };
        ajaxSubmit(url,data,callbackdeleteUser);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });
}

function callbackdeleteUser(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("删除成功",{icon: 6,time:500},function () {
                  /* var nextparam=getAction(getactionid_manage().getUserList_deleteUser);
                     if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                         setpageAction(INIT_WEB,nextparam.nextPageId);
                         var url=getActionURL(getactionid_manage().main_getUser);
                         window.location.href=url;
                     }*/
                    getUserListByParam();
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function callbackchangeUser(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("操作成功",{icon: 6,time:500},function () {
                    getUserListByParam();
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}




$(function () {
    layui.use(['layer','form'], function(){
        var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
            ,form = layui.form;

        form.on('switch(adminbool_switch)', function(switchdata){
            var obj=switchdata.elem.checked;
            var ssid=switchdata.value;
            if (!isNotEmpty(ssid)){
                layer.msg("系统异常",{icon: 5});
                return;
            }

            var con;
            var adminbool;
            if (obj) {
                con="你确定要恢复这个用户吗";
                adminbool=1;
            }else{
                con="你确定要禁用这个用户吗";
                adminbool=2;
            }
            layer.open({
                content:con
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    var url=getActionURL(getactionid_manage().getUserList_changeboolUser);
                    var data={
                        ssid:ssid,
                        adminbool:adminbool
                    };
                    ajaxSubmit(url,data,callbackchangeUser);
                    switchdata.elem.checked=obj;
                    form.render();
                    layer.close(index);
                }
                ,btn2: function(index, layero){
                    //按钮【按钮二】的回调
                    switchdata.elem.checked=!obj;
                    form.render();
                    layer.close(index);
                }
                ,cancel: function(){
                    //右上角关闭回调
                    switchdata.elem.checked=!obj;
                    form.render();
                }
            });
        });

        form.on('switch(temporaryaskbool_switch)', function(switchdata){
            var obj=switchdata.elem.checked;
            var ssid=switchdata.value;
            if (!isNotEmpty(ssid)){
                layer.msg("系统异常",{icon: 5});
                return;
            }

            var con;
            var temporaryaskbool;
            if (obj) {
                con="你确定要改为临时吗";
                temporaryaskbool=1;
            }else{
                con="你确定去掉该用户的临时状态吗";
                temporaryaskbool=-1;
            }
            layer.open({
                content:con
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    var url=getActionURL(getactionid_manage().getUserList_changeboolUser);
                    var data={
                        ssid:ssid,
                        temporaryaskbool:temporaryaskbool
                    };
                    ajaxSubmit(url,data,callbackchangeUser);
                    switchdata.elem.checked=obj;
                    form.render();
                    layer.close(index);
                }
                ,btn2: function(index, layero){
                    //按钮【按钮二】的回调
                    switchdata.elem.checked=!obj;
                    form.render();
                    layer.close(index);
                }
                ,cancel: function(){
                    //右上角关闭回调
                    switchdata.elem.checked=!obj;
                    form.render();
                }
            });
        });



    });
});

