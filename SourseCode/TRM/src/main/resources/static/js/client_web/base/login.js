


function userlogin() {
    $("#loginbtn").html('登录中&nbsp;<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop" style="display: inline-block"></i>')
    var url=getActionURL(getactionid_manage().login_userlogin);
    var loginaccount =$("#loginaccount").val();
    var password =$("#password").val();
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
            layer.msg("登陆成功",{time:500},function () {
                var url=getActionURL(getactionid_manage().login_gotomain);
                window.location.href=url;
            });
        }
    }else{
        $("#loginbtn").html('登录')
        layer.msg(data.message);

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
                if (!isNotEmpty(appCache.data.bottom) || !isNotEmpty(appCache.data.bottom.name) || !isNotEmpty(appCache.data.bottom.declaration) || !isNotEmpty(appCache.data.bottom.url)) {
                    return;
                }
                //页脚
                var bottom_name = appCache.data.bottom.name;
                var bottom_declaration = appCache.data.bottom.declaration;
                var bottom_url = appCache.data.bottom.url;
                var bottom_html = bottom_declaration + " <a href=\"" + bottom_url + "\">" + bottom_name + "</a>";
                $("#bottom_mian").html(bottom_html);
            }
        }
        layui.use('element', function(){
            var element =  layui.element;
            element.render();
        });
    }else{
        layer.msg(data.message);
    }
}


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if(r!=null)return  unescape(r[2]); return null;
}
