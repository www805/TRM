
function login_login(){

    var url=getActionURL(getactionid_manage().login_login);

    var loginaccount =$('input[name="loginaccount"]').val();
    var password =$('input[name="password"]').val();
    var data={
        loginaccount:loginaccount,
        password:password
    };
    ajaxSubmit(url,data,callbackgetAdminInfoPage);

}

function callbackgetAdminInfoPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        layer.msg(data.message, {icon: 2});
    }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}

function login_logout(){

    var url=getActionURL(getactionid_manage().main_logout);

    ajaxSubmit(url,null,callLogout);
}

function callLogout(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        //alert(data.message);
        var url=getActionURL(getactionid_manage().login_main);
        window.location.href=url;
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:6});
    }

}

function getNavList() {
    setpageAction(INIT_WEB,"server_web/base/main");
    var url=getActionURL(getactionid_manage().main_getNavList);
    setpageAction(INIT_WEB,"server_web/base/login");

    ajaxSubmitByJson(url,null,callgetNavList);
}

function callgetNavList(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){

        if (isNotEmpty(data.data)) {
            var appCache = data.data;

            //替换logo图标
            $("#systemlogo").css("background-image", "url(\"" + appCache.syslogoimage + "\")");

            //页脚
            var bottom_name = appCache.data.bottom.name;
            var bottom_declaration = appCache.data.bottom.declaration;
            var bottom_url = appCache.data.bottom.url;
            var bottom_html = bottom_declaration + " <a href=\"" + bottom_url + "\">" + bottom_name + "</a>";
            $("#bottom_mian").html(bottom_html);
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
