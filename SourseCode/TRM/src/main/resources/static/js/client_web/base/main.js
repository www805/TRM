
function userloginout() {
    var url=getActionURL(getactionid_manage().main_userloginout);
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callbackuserloginout);
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
                            dd_HTML += "<dd><a target=\"option\" href=\"" + dd.url + "\">" + dd.name + "</a></dd>";
                        }

                        dd_HTML = "<dl class=\"layui-nav-child\">" + dd_HTML + "</dl>";
                        nav.url = "javascript:;";
                    }
                    if(isNotEmpty(nav.icon)){
                        nav_icon_HTML = "<i class=\"" + nav.icon + "\"></i>\n";
                    }
                    if(isNotEmpty(nav.iconh)){
                        nav_icon_HTML = "<span class='iconfont'>&#" + nav.iconh + "</span>\n";
                    }
                    if(isNotEmpty(nav.space)){
                        nav_space = "<i style='border-right: 3px solid #bce2ff;position: absolute;top: 9px;right: 0;width: 3px;height: 28px;'></i>\n";
                    }

                    nav_list_HTML += "<li class=\"layui-nav-item\">\n" +
                        "                <a target=\"option\" href=\"" + nav.url + "\">\n" + nav_icon_HTML+
                        "                    <cite>" + nav.name + "</cite>\n" +
                        "                </a>\n" + dd_HTML + nav_space +
                        "            </li>";
                }
                $("#nav_list").html(nav_list_HTML);
            }

            if (isNotEmpty(appCache.data.bottom) && isNotEmpty(appCache.data.bottom.name) && isNotEmpty(appCache.data.bottom.declaration) && isNotEmpty(appCache.data.bottom.url)) {
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
        layer.msg(data.message,{icon: 5});
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
})
