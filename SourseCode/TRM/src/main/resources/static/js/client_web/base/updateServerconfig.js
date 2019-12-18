var serverconfigssid=null;
function getServerconfig() {
    var url=getActionURL(getactionid_manage().updateServerconfig_getServerconfig);
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callbackgetServerconfig);
}

function callbackgetServerconfig(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
           var serverconfigAndFilesave=data.serverconfigAndFilesave;
           if (isNotEmpty(serverconfigAndFilesave)) {
               serverconfigssid=serverconfigAndFilesave.ssid;
               $("#clientname").val(serverconfigAndFilesave.clientname);
               if (isNotEmpty(serverconfigAndFilesave.client_downurl)){
                   $("#client_urlshow").prop("src",serverconfigAndFilesave.client_downurl);
                   $("#client_urlshow").css({"width":"150px","height":"150px"});
               } else{
                   $("#client_urlshow").css("display","none");
               }

               if (serverconfigAndFilesave.authorizebool==1){
                   $("#authorizebool").text("已授权");
                   $("#authorizebool").removeClass("layui-btn-danger");
               }else{
                   $("#authorizebool").text("未授权");
                   $("#authorizebool").addClass("layui-btn-danger");

               }
               $("#serverip").val(serverconfigAndFilesave.serverip);
               $("#serverport").val(serverconfigAndFilesave.serverport);
               $("#workdays").val(serverconfigAndFilesave.workdays);
           }
        }
    }else{
        layer.msg(data.message,{icon: 5});

    }
}

function showimg(obj) {
    var file = obj.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onerror = function (e) {
        $("#client_urlshow").css("display","none");
    }
    reader.onload = function (e) {
        $("#client_urlshow").attr("src",e.target.result);
        $("#client_urlshow").css("display","block");
        $("#client_urlshow").css({"width":"150px","height":"150px"});
    }

}


function updateServerconfig() {
    $("[lay-filter='progress_demo']").css("visibility","visible");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    var url=getActionURL(getactionid_manage().updateServerconfig_updateServerconfig);

    var clientname=$("#clientname").val();
    var serverip=$("#serverip").val();
    var serverport= $("#serverport").val();
    var workdays=$("#workdays").val();

    if (!isNotEmpty(clientname)) {
        layer.msg("请输入客户端名称",{icon: 5});
        $("#clientname").focus();
        return;
    }

    if (clientname.length > 15) {
        layer.msg("客户端名称不能超过15个字符",{icon: 5});
        $("#clientname").focus();
        return;
    }

    if (!isNotEmpty(serverip)) {
        layer.msg("请输入服务器IP",{icon: 5});
        $("#serverip").focus();
        return;
    }

    if (!isNotEmpty(serverport)) {
        layer.msg("请输入服务器端口",{icon: 5});
        $("#serverport").focus();
        return;
    }

    if (!isNotEmpty(workdays)) {
        layer.msg("请输入同步时间(天",{icon: 5});
        $("#workdays").focus();
        return;
    }



    var data={
            clientname:clientname,
            serverip:serverip,
            serverport:serverport,
            workdays:workdays,
            ssid:serverconfigssid
    };

    var formData = new FormData();
    var file = document.getElementById("client_url").files[0];
    formData.append("param", JSON.stringify(data));
    formData.append("token", INIT_CLIENTKEY);
    formData.append("client_url", file);


    // XMLHttpRequest 对象
    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        $("#saveapkbtn").attr('disabled', false);
        $("#saveapkbtn").val("保存");
        $("[lay-filter='progress_demo']").css("visibility","hidden");
        callbackupdateServerconfig(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(formData);
}

function progressFunction(evt) {
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#saveapkbtn").val("保存中" + completePercent);
        $("[lay-filter='progress_demo']").css("visibility","visible");
        $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
        $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
    }
};

function callbackupdateServerconfig(str) {
    var data = eval('(' + str + ')');
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("保存成功",{icon: 6,time:500},function () {
                getServerconfig();

                //刷新左上角标题
                top.location.reload();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

/**
 * 重置socket连接，一般情况下不用
 */
function rebootsocket(){

    layer.confirm('确认重置通讯?', {
        btn: ['确定','取消'] //按钮
    }, function(index){
        $("#socketbtn").hide();
        layer.close(index);
        //请求后台重启socket
        var url=getActionURL(getactionid_manage().updateServerconfig_rebootsocket);
        var data={
            token:INIT_CLIENTKEY
        };
        ajaxSubmitByJson(url,data,callbackrebootsocket);

    }, function(){

    });

}


function callbackrebootsocket(data) {
    if(null!=data){
        layer.msg(data.message,{icon: 1});
    }else{
        layer.msg("网络连接异常，请重试",{icon: 5});
    }
}

/**
 * 重置ftpserver，一般情况下不用
 */
function rebootftpserver(){

    layer.confirm('确认重启存储服务?', {
        btn: ['确定','取消'] //按钮
    }, function(index){
        $("#ftpserverbtn").hide();
        layer.close(index);
        //请求后台重启ftpserver
        var url=getActionURL(getactionid_manage().updateServerconfig_rebootFTPServer);
        var data={
            token:INIT_CLIENTKEY
        };
        ajaxSubmitByJson(url,data,callbackrebootftpserver);

    }, function(){

    });

}


function callbackrebootftpserver(data) {
    if(null!=data){
        layer.msg(data.message,{icon: 1});
    }else{
        layer.msg("网络连接异常，请重试",{icon: 5});
    }
}