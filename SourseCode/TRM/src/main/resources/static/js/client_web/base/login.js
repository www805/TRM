


function userlogin() {
    $("#loginbtn").html('登录中&nbsp;<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop" style="display: inline-block"></i>')
    var url=getActionURL(getactionid_manage().login_userlogin);
    var loginaccount =$("#loginaccount").val().trim();
    var password =$("#password").val().trim();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            loginaccount:loginaccount,
            password:password
        }
    };
    ajaxSubmitByJson(url,data,callbackuserlogin);
}

function callbackuserlogin(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("登录成功",{icon: 6,time:500,shade: [0.1,'#fff']},function () {
                var url=getActionURL(getactionid_manage().login_gotomain);
                window.location.href=url;
            });
        }
    }else{
        $("#loginbtn").html('登录')
        layer.msg(data.message, {icon: 5,anim: 6});
    }
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

            if (isNotEmpty(appCache.data)) {

                if (isNotEmpty(appCache.data.login.img) && appCache.data.login.img != '/') {
                    $("#login_img").attr("src", appCache.data.login.img).css("width", "90px").css("height", "90px");
                }

                if (!isNotEmpty(appCache.data.bottom) || !isNotEmpty(appCache.data.bottom.name) || !isNotEmpty(appCache.data.bottom.declaration) || !isNotEmpty(appCache.data.bottom.url)) {
                    return;
                }
                //页脚
                var bottom_html = "";
                var bottom_name = appCache.data.bottom.name;
                var bottom_declaration = appCache.data.bottom.declaration;
                var bottom_url = appCache.data.bottom.url;

                if (isNotEmpty(appCache.data.bottom.img.src) && appCache.data.bottom.img.src != '/') {
                    $(".layui-footer").css("height", "50px").css("margin-top","5px");
                    bottom_html = " <a href=\"" + bottom_url + "\">" + "<img style='margin-top: 7px;' width='" + appCache.data.bottom.img.width + "' height='" + appCache.data.bottom.img.height + "' src='" + appCache.data.bottom.img.src + "'>" + "</a>";
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