
function getRoleList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getRoleList_getRoleList);
    var rolename=$("#rolename").val();
    var rolebool=$("#rolebool option:selected").val();
    var data={
        rolename:rolename,
        rolebool:rolebool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetRoleList);
}
function getRoleList(rolename,rolebool,currPage,pageSize){
    var url=getActionURL(getactionid_manage().getRoleList_getRoleList);
    var data={
        rolename:rolename,
        rolebool:rolebool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetRoleList);
}


function callbackgetRoleList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getRoleListByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getRoleList_init(currPage,pageSize);
    }else if (len==2){
        getRoleList('',arguments[0],arguments[1]);
    }else if(len>2){
        getRoleList(arguments[0],arguments[1],arguments[2],arguments[3]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var rolename=pageparam.rolename;
        var rolebool=pageparam.rolebool;
        var arrparam=new Array();
        arrparam[0] = rolename;
        arrparam[1]=rolebool;
        showpage("paging",arrparam,'getRoleListByParam',currPage,pageCount,pageSize);
    }
}

function deleteRole(ssid){
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
        return;
    }

    layer.confirm('确定要删除该角色吗', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().getRoleList_deleteRole);
        var data={
            ssid:ssid,
            rolebool:-1
        };
        ajaxSubmit(url,data,callbackdeleteRole);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });


}

function callbackdeleteRole(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                layer.msg("删除成功",{icon: 1,time:500},function () {
                   /* var nextparam=getAction(getactionid_manage().getRoleList_deleteRole);
                    if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                        setpageAction(INIT_WEB,nextparam.nextPageId);
                        var url=getActionURL(getactionid_manage().main_getRole);
                        window.location.href=url;
                    }*/
                    getRoleList_init(1,3);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


function callbackchangeRole(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("操作成功",{icon: 1,time:500},function () {
                    getRoleList_init(1,3);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

$(function () {
    layui.use(['layer','form'], function(){
        var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
            ,form = layui.form;

        form.on('switch(rolebool_switch)', function(switchdata){
            var obj=switchdata.elem.checked;
            var ssid=switchdata.value;
            if (!isNotEmpty(ssid)||null==obj){
                layer.msg("系统异常",{icon: 2});
                return;
            }

            var con;
            var rolebool;
            if (obj) {
                con="你确定要启动这个角色吗";
                rolebool=1;
            }else{
                con="你确定要禁用这个角色吗";
                rolebool=2;
            }

            layer.open({
                content:con
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    var url=getActionURL(getactionid_manage().getRoleList_changeboolRole);
                    var data={
                        ssid:ssid,
                        rolebool:rolebool
                    };
                    ajaxSubmit(url,data,callbackchangeRole);
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


