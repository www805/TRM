function getPermissionsByMenu() {
    var url=getActionURL(getactionid_manage().main_getPermissionsByMenu);
    ajaxSubmit(url,null,callbackgetPermissionsByMenu);
}

function callbackgetPermissionsByMenu(data) {
  //  console.log("main__"+data);
}

function getNavList() {
    var url=getActionURL(getactionid_manage().main_getNavList);
    ajaxSubmitByJson(url,null,callgetNavList);
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

                    nav_list_HTML += "<li class=\"layui-nav-item\">\n" +
                        "                <a target=\"option\" href=\"" + nav.url + "\">\n" + nav_icon_HTML+
                        "                    <cite>" + nav.name + "</cite>\n"+
                        "                </a>\n" + dd_HTML +
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