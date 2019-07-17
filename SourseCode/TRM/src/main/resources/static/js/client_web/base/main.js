function userloginout() {
    var url=getActionURL(getactionid_manage().main_userloginout);
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callbackuserloginout);
}

function callbackuserloginout(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        layer.msg("登出成功",{time:500},function () {
            var nextparam=getAction(getactionid_manage().main_userloginout);
            if (isNotEmpty(nextparam.gotopageOrRefresh)&&nextparam.gotopageOrRefresh==1){
                setpageAction(INIT_CLIENT,nextparam.nextPageId);
                var url=getActionURL(getactionid_manage().login_gotologin);
                window.location.href=url;
            }
        });
    }else{
        layer.msg(data.message);
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

        if (isNotEmpty(data.data)) {
            var appCache = data.data;
            var nav_list_HTML = "";
            var nav_list = appCache.data.nav;

            for (var i = 0; i < nav_list.length; i++) {
                var nav = nav_list[i];
                var dd_HTML = "";
                if(isNotEmpty(nav.list)){
                    for (var j = 0; j < nav.list.length; j++) {
                        var dd = nav.list[j];
                        dd_HTML += "<dd><a target=\"option\" href=\"" + dd.url + "\">" + dd.name + "</a></dd>";
                    }

                    dd_HTML = "<dl class=\"layui-nav-child\">" + dd_HTML + "</dl>";
                    nav.url = "javascript:;";
                }
                nav_list_HTML += "<li class=\"layui-nav-item\">\n" +
                    "                <a target=\"option\" href=\"" + nav.url + "\">\n" +
                    "                    <i class=\"" + nav.icon + "\"></i>\n" +
                    "                    <cite>" + nav.name + "</cite>\n" +
                    "                </a>\n" + dd_HTML +
                    "            </li>";
            }

            //页脚
            var bottom_name = appCache.data.bottom.name;
            var bottom_declaration = appCache.data.bottom.declaration;
            var bottom_url = appCache.data.bottom.url;
            var bottom_html = bottom_declaration + " <a href=\"" + bottom_url + "\">" + bottom_name + "</a>";
            $("#bottom_mian").html(bottom_html);
            $("#nav_list").html(nav_list_HTML);
        }
        layui.use('element', function(){
            var element =  layui.element;
            element.render();
        });
    }else{
        layer.msg(data.message);
    }
}

// layui.use(['laypage', 'form', 'layer', 'layedit', 'laydate','element', 'upload'], function(){
//     var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
//         ,form = layui.form
//         ,layer = layui.layer
//         ,layedit = layui.layedit
//         ,laydate = layui.laydate
//         ,laypage = layui.laypage
//         ,element = layui.element;
//
//     form.render();
// });