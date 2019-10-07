
function userloginout() {
    layer.confirm('确定要退出登录吗？', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        var url=getActionURL(getactionid_manage().main_userloginout);
        var data={
            token:INIT_CLIENTKEY
        };
        ajaxSubmitByJson(url,data,callbackuserloginout);
        layer.close(index);
    }, function(index){
        layer.close(index);
    });
}

function callbackuserloginout(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("退出成功",{time:500,icon: 6,},function () {
            var nextparam=getAction(getactionid_manage().main_userloginout);
            if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                setpageAction(INIT_CLIENT,nextparam.nextPageId);
                var url=getActionURL(getactionid_manage().login_gotologin);
                window.location.href=url;
            }
        });
    }else{
        layer.msg(data.message, {icon: 5});
    }
}

//心跳
function getPant() {
    var url=getActionURL(getactionid_manage().main_getPant);
    // var url = "/cweb/base/main/getPant";
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callgetPant);
}

//请求外部文件数据
function getNavList() {
    var url=getActionURL(getactionid_manage().main_getNavList);
    var data={
        token:INIT_CLIENTKEY
    };

    ajaxSubmitByJson(url,data,callgetNavList);
}

function callgetNavList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data.data)) {
            var appCache = data.data;
            var nav_list_HTML = "";

            if (isNotEmpty(appCache.data.nav)) {
                var nav_list = appCache.data.nav;

                for (var i = 0; i < nav_list.length; i++) {
                    var nav = nav_list[i];
                    var dd_HTML = "";
                    var nav_icon_HTML = "";
                    var nav_space = "";
                    if(isNotEmpty(nav.list)){
                        for (var j = 0; j < nav.list.length; j++) {
                            var dd = nav.list[j];
                            var target = "option";
                            if (isNotEmpty(dd.target) && dd.target != "/") {
                                target = dd.target;
                            }
                            dd_HTML += "<dd><a target=\"" + target + "\" href=\"" + dd.url + "\">" + dd.name + "</a></dd>";
                        }

                        dd_HTML = "<dl class=\"layui-nav-child\">" + dd_HTML + "</dl>";
                        nav.url = "javascript:;";
                    }
                    if(isNotEmpty(nav.icon)){
                        nav_icon_HTML = "<i class=\"" + nav.icon + "\"></i>\n";
                    }
                    if(isNotEmpty(nav.iconh)){
                        nav_icon_HTML = "<span class='iconfont iconsizetop'>&#" + nav.iconh + "</span>\n";
                    }
                    if(isNotEmpty(nav.space)){
                        var right = -2;
                        if(isNotEmpty(nav.spaceright)){
                            right = nav.spaceright;
                        }
                        nav_space = "<i style='border-right: 3px solid #bce2ff;position: absolute;top: 15px;right: " + right + "px;width: 2px;height: 50px;'></i>\n";
                    }

                    nav_list_HTML += "<li class=\"layui-nav-item\">\n" +
                        "                <a target=\"option\" style='display:block; padding-top:45px; height:32px; line-height:32px' href=\"" + nav.url + "\">\n" + nav_icon_HTML+
                        "                    <cite>" + nav.name + "</cite>\n" +
                        "                </a>\n" + dd_HTML + nav_space +
                        "            </li>";
                }
                $("#nav_list").html(nav_list_HTML);

                var gnlist = appCache.data.gnlist;
                if (isNotEmpty(gnlist) && gnlist.indexOf("s_v") != -1) {
                    $("#guidepage").hide();
                    $("#pagehome").hide();
                    $("#nav_list").css("right", 250);
                }

                //设置logo和标题
                var logo_title = appCache.data.logotitle;
                if(isNotEmpty(logo_title)){
                    $("#logoimg").attr("src", logo_title.img);
                    if (logo_title.imgtitle != '' && logo_title.imgtitle != '/') {
                        $("#logotitle").html("").css("background", "url(" + logo_title.imgtitle + ") no-repeat").css("background-size", "100% 100%");
                    } else {
                        $("#logotitle").html(logo_title.title);
                    }
                }
            }

            if (isNotEmpty(appCache.data.bottom) && isNotEmpty(appCache.data.bottom.name) && isNotEmpty(appCache.data.bottom.declaration) ) {
                //页脚
                var bottom_html = "";
                var bottom_name = appCache.data.bottom.name;
                var bottom_declaration = appCache.data.bottom.declaration;
                var bottom_url = appCache.data.bottom.url;
                if(!isNotEmpty(bottom_url)){
                    bottom_url="#";
                }

                if (isNotEmpty(appCache.data.bottom.img.src) && appCache.data.bottom.img.src != '/') {
                    $(".layui-footer").css("height", "50px").css("line-height","43px");
                    $("#bottom_mian").css("margin-top", "5px");
                    $("#login_img").css("width", "90px").css("height", "90px");
                    bottom_html = " <a href=\"" + bottom_url + "\">" + "<img style='margin-top: 5px;' width='" + appCache.data.bottom.img.width + "' height='" + appCache.data.bottom.img.height + "' src='" + appCache.data.bottom.img.src + "'>" + "</a>";
                } else {
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

function callgetPant(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        console.log(data.message);
    }
}

function  open_startConversation() {
    var HTML='';
    layer.open({
        id:"startconversation_id",
        type:2,
        title: '填写基础信息',
        shade: 0.3,
        resize:false,
        area: ['50%', '600px'],
        skin: 'startconversation_btn', //样式类名
        content: startConversationURL,
    });
}

//我的资料
function getuserinfo(){
    if (!isNotEmpty(userssid)){
        layer.msg("系统异常",{icon: 5});
        return;
    }
    var url=getActionURL(getactionid_manage().main_getUserBySsid);
    var data={
        ssid:userssid
    };
    ajaxSubmit(url,data,callbackgetUserBySsid);
}
function callbackgetUserBySsid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            if (isNotEmpty(data)){
                $("#loginaccountm").html(data.loginaccount);
                $("#usernamem").html(data.username);
                $("#workunitnamem").html(data.workname);

                var roles=data.roles;
                var rolesname="";
                if(isNotEmpty(roles)){
                    for (var i = 0; i < roles.length; i++) {
                        var rs=roles[i];
                        rolesname+=rs.rolename+"、";
                    }
                    rolesname = (rolesname .substring(rolesname .length - 1) == '、') ? rolesname .substring(0, rolesname .length - 1) : rolesname ;
                }
                $("#rolesm").html(rolesname);
                $("#registertimem").html(data.registertime);

            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

$(function () {
    /*  var _t;
      window.onbeforeunload = function(){
        setTimeout(function(){_t = setTimeout(onunloadcancel, 0)}, 0);
           return "确定要关闭吗?";
       }
      window.onunloadcancel = function(){
           clearTimeout(_t);
           alert("取消离开");
       }*/

   /* 对于trm.exe不适应会卡死
   $(window).bind('beforeunload',function(){
        return '确定要离开当前页面吗';
    });*/



    $("#userinfos").click(function () {
        var html=' <div class="layui-row layui-main " id="userinfoshtml">\
                    <div class="layui-col-sm12" ><div>我的角色：</div><p id="rolesm">加载中...</p></div>\
                    <div class="layui-col-sm12"><div>工作单位：</div><p id="workunitnamem">加载中...</p></div>\
                    <div class="layui-col-sm12"><div>账号：</div><p id="loginaccountm">加载中...</p></div>\
                    <div class="layui-col-sm12"><div>用户名：</div><p id="usernamem">加载中...</p></div>\
                    <div class="layui-col-sm12"><div>注册时间：</div><p id="registertimem">加载中...</p></div>\
                    </div>';
        var layer = layui.layer;
        layer.open({
            type: 1,
            title: "我的资料",
            shade: 0.5,
            shadeClose : true,
            area: ['40%', '50%'],
            content: html,
            btn: ['确定'],
            yes: function(index, layero){
                layer.close(index);
            }
        });
        getuserinfo();
    });
})
