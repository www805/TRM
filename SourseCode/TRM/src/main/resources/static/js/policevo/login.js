
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
        layer.msg(data.message, {icon: 5,offset: 't'	});
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
        setpageAction(INIT_WEB, "server_web/base/login");
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
            // if (isNotEmpty(appCache.syslogoimage)) {
            //     $("#systemlogo").css("background-image", "url(\"" + appCache.syslogoimage + "\")");
            // }else{
            //     $("#systemlogo").css("background-image", "url(\"/uimaker/images/loginlogo-5aa2cf210dbf067bd57c42a470703719.png\")");
            // }

            //如果是单机版就修改名称
            if (isNotEmpty(appCache.data.gnlist) && appCache.data.gnlist.indexOf(S_V) != -1) {
                $("#guidepage a").html("返回客户端");
            }

            if (isNotEmpty(appCache.data)) {

                if (!isNotEmpty(appCache.data.bottom) || !isNotEmpty(appCache.data.bottom.name) || !isNotEmpty(appCache.data.bottom.declaration)) {
                    return;
                }
                //页脚
                var trmBottom = appCache.data.bottom;
                var bottom_html = "";
                var bottom_name = trmBottom.name;
                var bottom_declaration = trmBottom.declaration;
                var bottom_url = trmBottom.url;
                if(!isNotEmpty(bottom_url)){
                    bottom_url = "#";
                }

                if (isNotEmpty(trmBottom.image.src) && trmBottom.image.src != '/') {
                    $(".systemlogo").css({"background-image":"url(" + trmBottom.image.src + ")"});
                    if(isNotEmpty(trmBottom.image.width) && isNotEmpty(trmBottom.image.height)){
                        $(".systemlogo").css("background-size", trmBottom.image.width + "px " + trmBottom.image.height + 'px');
                    }
                }else{
                    $("#systemlogo").css("background-image", "url(\"/uimaker/images/loginlogo-5aa2cf210dbf067bd57c42a470703719.png\")");
                }
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
            loginaccount:function (value) {
                if (!(/\S/).test(value)) {
                    return "请输入登录账号";
                }
                if (!(/^(?!.*\s).{2,}$/.test(value))) {
                    return "请输入2个以上字符不含空格的登录账号";
                }
            },
            password:function (value) {
                if (!(/\S/).test(value)) {
                    return "请输入密码";
                }
            }
        });

        // 监听提交
        form.on('submit(loginbtn)', function(data){

            if(isNotEmpty(data.field.loginaccount) && isNotEmpty(data.field.password)){
                login_login();
            }
            return false;
        });
    });
})