


function userlogin() {
    $("#loginbtn").html('登录中&nbsp;<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop" style="display: inline-block"></i>')
    var url=getActionURL(getactionid_manage().login_userlogin);
    var loginaccount =$("#loginaccount").val().trim();
    var password =$("#password").val().trim();

    $("#loginaccount").attr("readonly","readonly");
    $("#password").attr("readonly","readonly");
    $("#loginbtn").attr('disabled','disabled');


    var data={
        token:INIT_CLIENTKEY,
        param:{
            loginaccount:loginaccount,
            password:password
        }
    };
    ajaxSubmitByJson(url,data,callbackuserlogin);
}
var updatePassWord_index=null;
function callbackuserlogin(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("登录成功",{icon: 6,time:500,shade: [0.1,'#fff']},function () {
                var url=getActionURL(getactionid_manage().login_gotomain);
                window.location.href=url;
                setTimeout(function () {
                    $("#loginaccount").removeAttr("readonly");
                    $("#password").removeAttr("readonly");
                    $("#loginbtn").removeAttr("disabled");
                },3000)
            });

        }
    }else{
        var data_=data.data;
        if (isNotEmpty(data_.firstloginbool)&&isNotEmpty(data_.ssid)&&data_.firstloginbool==1){
            var loginaccount =$("#loginaccount").val().trim();
            var HTML='<form class="layui-form site-inline"  style="margin: 30px;">\
                    <div class="layui-form-item">\
                        <label class="layui-form-label">账号</label>\
                        <label class="layui-form-label" style="text-align: left;width: 50%" id="loginaccount">'+loginaccount+'</label>\
                    </div>\
                    <div class="layui-form-item">\
                        <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>原密码</label>\
                        <div class="layui-input-block">\
                        <input type="text" name="oldpassword" id="oldpassword" lay-verify="oldpassword" autocomplete="off" placeholder="请输入原密码" class="layui-input">\
                        </div>\
                    </div>\
                    <div class="layui-form-item">\
                        <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>新密码</label>\
                        <div class="layui-input-block">\
                        <input type="password" name="newpassword" id="newpassword" lay-verify="newpassword" autocomplete="off" placeholder="请输入5-12位由字母、数字或者下划线组成的新密码" class="layui-input" >\
                        </div>\
                    </div>\
                    <div class="layui-form-item">\
                        <label class="layui-form-label"><span style="color: red;font-weight: initial">*</span>确认密码</label>\
                        <div class="layui-input-block">\
                        <input type="password" name="passwordm" id="passwordm" lay-verify="passwordm" autocomplete="off" placeholder="请输入确认密码" class="layui-input" >\
                        </div>\
                    </div>\
            </form>';
            layui.use(['layer','element','laydate','form'], function(){
                var form=layui.form;
                updatePassWord_index= layer.open({
                    type:1,
                    title: '请先重置密码',
                    btn: ['确定修改', '取消'] ,
                    shade: 0.3,
                    resize:false,
                    area: ['30%', '40%'],
                    content: HTML,
                    success: function (layero, index) {
                        layero.addClass('layui-form');//添加form标识
                        layero.find('.layui-layer-btn0').attr('lay-filter', 'fromContent').attr('lay-submit', '');//将按钮弄成能提交的
                        form.render();
                    },
                    yes: function(index, layero){
                        var oldpassword=$("#oldpassword").val();
                        var newpassword=$("#newpassword").val();
                        var password=$("#passwordm").val();

                        form.verify({
                            oldpassword:[ /\S/,"请输入原密码"],
                            newpassword:function (value) {
                                if (!(/\S/).test(value)) {
                                    return "请输入新密码"
                                }
                                if (!(/^\w{5,12}$/.test(value))) {
                                    return "请输入5-12位由字母、数字或者下划线组成的密码"
                                }
                            },
                            passwordm:function (value) {
                                if (!(/\S/).test(value)) {
                                    return "请输入确认密码"
                                }
                                if (isNotEmpty(newpassword)&&isNotEmpty(value)&&newpassword!=value) {
                                    return "两次密码不一致"
                                }
                            },
                        });
                        //监听提交
                        form.on('submit(fromContent)', function(data){
                            //开始检测
                            var url=getActionURL(getactionid_manage().login_updatePassWord);

                            var data={
                                token:INIT_CLIENTKEY,
                                param:{
                                    ssid: data_.ssid,
                                    oldpassword: oldpassword,
                                    newpassword:newpassword,
                                    password:password,
                                    firstloginbool:1,//是第一次登录
                                }
                            };
                            ajaxSubmitByJson(url,data,function (d) {
                                if(null!=d&&d.actioncode=='SUCCESS'){
                                    if (isNotEmpty(d.data)){
                                        layer.msg("密码重置成功，快去登录吧~",{icon: 6,time:500,shade: [0.1,'#fff']},function () {
                                            $("#password").focus();  $("#password").val("");
                                            updatePassWord_index=null;
                                            layer.close(index);
                                        });
                                    }
                                }else{
                                    layer.msg(d.message,{icon: 5});
                                }
                            });
                        });
                    }
                    ,btn2: function(index, layero){
                        updatePassWord_index=null;
                        layer.close(index);
                    }
                });
            });


        }else {
            layer.msg(data.message, {icon: 5,anim: 6});
        }
        $("#loginbtn").html('登录')
        $("#loginaccount").removeAttr("readonly");
        $("#password").removeAttr("readonly");
        $("#loginbtn").removeAttr("disabled");
    }
}

function updatePassWord() {

}

function getNavList() {
    setpageAction(INIT_CLIENT,"client_web/base/main");
    var url=getActionURL(getactionid_manage().main_getNavList);
    setpageAction(INIT_CLIENT,"client_web/base/login");

    ajaxSubmitByJson(url,null,callgetNavList);
}

function callgetNavList(data) {
    // $("#clientimage").attr('src',"/uimaker/images/login-img.png");
    if(null!=data&&data.actioncode=='SUCCESS'){

        if (isNotEmpty(data.data)) {
            var appCache = data.data;

            //替换logo图标
            // $("#clientimage").css("background-image", "url(\"" + appCache.clientimage + "\")");
            if (isNotEmpty(appCache.clientimage)) {
                $("#clientimage").attr('src',appCache.clientimage);
            }

            //如果是单机版就隐藏按钮
            // if (isNotEmpty(appCache.data.gnlist) && appCache.data.gnlist.indexOf("s_v") != -1) {
            //     $("#guidepage").hide();
            // }

            if (isNotEmpty(appCache.data)) {

                try {
                    if (isNotEmpty(appCache.data.login.img) && appCache.data.login.img != '/') {
                        $("#login_img").attr("src", appCache.data.login.img).css("width", "90px").css("height", "90px");
                    }
                } catch (e) {
                }

                if (!isNotEmpty(appCache.data.bottom) || !isNotEmpty(appCache.data.bottom.name) || !isNotEmpty(appCache.data.bottom.declaration) ) {
                    return;
                }
                //页脚
                var trmBottom = appCache.data.bottom;
                var bottom_html = "";
                var bottom_name = trmBottom.name;
                var bottom_declaration = trmBottom.declaration;
                var bottom_url = trmBottom.url;
                if(!isNotEmpty(bottom_url)){
                    bottom_url="#";
                }

                if (isNotEmpty(trmBottom.image.src) && trmBottom.image.src != '/') {
                    $(".layui-footer").css("height", "50px").css("margin-top","5px");
                    bottom_html = " <a href=\"" + bottom_url + "\">" + "<img style='margin-top: 7px;' width='" + trmBottom.image.width + "' height='" + trmBottom.image.height + "' src='" + trmBottom.image.src + "'>" + "</a>";
                }else{
                    bottom_html = bottom_declaration + " <a href=\"" + bottom_url + "\">" + bottom_name + "</a>";
                }
                $("#bottom_mian").html(bottom_html);
            }
        }
        layui.use('element', function(){
            var element =  layui.element;
            element.render();
        });
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if(r!=null)return  unescape(r[2]); return null;
}


$(function () {
    layui.use(['layer','element','form'], function(){
        var layer = layui.layer; //获得layer模块
        var element = layui.element;
        var form=layui.form;
        //使用模块

        //自定义验证规则
        form.verify({
            loginaccount:[/\S/,'请输入账号'], password: [/\S/,'请输入密码']
        });
        form.on('submit(loginbtn)', function (data) {
            userlogin();
            return false;
        });
    });
})