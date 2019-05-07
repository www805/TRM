
function getToaddOrupdateTemplate_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var username =$("#username").val();
    var loginaccount =$("#loginaccount").val();
    var workunitssid=$("#workunitssid option:selected").val();
    var rolessid=$("#rolessid option:selected").val();
    var adminbool=$("#adminbool option:selected").val();
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetUserList);
}

function addOrupdateTemplate(username, loginaccount, workunitssid, rolessid, adminbool, currPage, pageSize){
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
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
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getToaddOrupdateTemplateByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getUserList_init(currPage,pageSize);
    }else if (len==2){
        addOrupdateTemplate('',arguments[0],arguments[1]);
    }else if(len>2){
        addOrupdateTemplate(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
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

        var arrparam=new Array();
        arrparam[0]=username;
        arrparam[1]=loginaccount;
        arrparam[2]=workunitssid;
        arrparam[3]=rolessid;
        arrparam[4]=adminbool;
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
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 删除
 * @param ssid
 */
function deleteUser(ssid) {
    if (!isNotEmpty(ssid)){
        layer.msg("系统异常",{icon: 2});
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
                layer.msg("删除成功",{icon: 1,time:500},function () {
                  /* var nextparam=getAction(getactionid_manage().getUserList_deleteUser);
                     if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                         setpageAction(INIT_WEB,nextparam.nextPageId);
                         var url=getActionURL(getactionid_manage().main_getUser);
                         window.location.href=url;
                     }*/
                    getUserList_init(1,3);
                });
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}


function callbackchangeUser(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            if (isNotEmpty(data.data)){
                layer.msg("操作成功",{icon: 1,time:500},function () {
                    getUserList_init(1,3);
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

        form.on('switch(adminbool_switch)', function(switchdata){
            var obj=switchdata.elem.checked;
            var ssid=switchdata.value;
            if (!isNotEmpty(ssid)){
                layer.msg("系统异常",{icon: 2});
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
    });
});

function addUpdateinfo(num, type) {
    var version = "修改";
    if (type) {
        version = "添加";
    }

    var content = "<div class=\"layui-form-item layui-form-text\" style='margin-top: 20px;padding-right: 20px;'>\n" +
        "            <label class=\"layui-form-label\">问题类型</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <select name=\"city\" lay-verify=\"\" style=\"margin-top: 5px;width: 120px;height: 30px;\">\n" +
        "                    <option value=\"010\">抢劫</option>\n" +
        "                    <option value=\"021\">诈骗</option>\n" +
        "                    <option value=\"0571\">勒索</option>\n" +
        "                </select>\n" +
        "            </div>\n" +
        "            <label class=\"layui-form-label\">问题</label>\n" +
        "            <div class=\"layui-input-block\" style=\"margin-bottom: 10px;\">\n" +
        "                <textarea placeholder=\"请输入问题\" class=\"layui-textarea\"></textarea>\n" +
        "            </div>\n" +
        "            <label class=\"layui-form-label\">参考答案</label>\n" +
        "            <div class=\"layui-input-block\">\n" +
        "                <textarea placeholder=\"请输入答案\" class=\"layui-textarea\"></textarea>\n" +
        "            </div>\n" +
        "            <button class=\"layui-btn layui-btn-normal\" style='float: right;margin-top: 10px;'>" + version + "</button>\n" +
        "        </div>";

    //弹窗层
    layer.open({
        type: 1,
        title: version,
        area: ['620px', '390px'], //宽高
        shadeClose: true,
        content: content
    });
}

function addTemplateProblem(obj) {
    var text = $(obj).parents('tr').find("td").eq(0).text();

    var btn = '<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="del" onclick="DeleteProblem(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>';

    $("#testTable tbody").append("<tr><td>" + text + "</td><td>" + btn + "</td></tr>");
}

function DeleteProblem(obj) {
    $(obj).parents('tr').remove();
}

function tan() {

    layer.open({
        type: 2,
        title: ['模板变更', 'text-align:left;background: #1e9fff;color:#fff;'],
        shadeClose: true,
        shade: false,
        area: ['1200px', '600px'],
        content: 'templateList'
    });
}