function getHome(yearstype) {
    var url=getActionURL(getactionid_manage().homepage_getHome);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            yearstype:yearstype
        }
    };
    ajaxSubmitByJson(url,data,callbackgetHome);
}

function callbackgetHome(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var liveurl_=data.liveurl;
            if (isNotEmpty(liveurl_)){
                liveurl=liveurl_;
            }
            initplayer();//初始化地址

            var stateSQ=data.stateSQ;
            if (isNotEmpty(stateSQ)&&stateSQ==1){
                $("#stateSQtxt").html("<span style='color: #00FF00'>正常运行中</span>")
            }else {
                $("#stateSQtxt").html("<span style='color: red'>异常，请注意</span>")
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//获取总控服务器状态
function getServerStatus() {
    var url=getActionURL(getactionid_manage().homepage_getServerStatus);
    // var url = "/cweb/base/main/getServerStatus";
    var data={
        token:INIT_CLIENTKEY
    };
    ajaxSubmitByJson(url,data,callgetServerStatus);
}

function callgetServerStatus(data) {
    if (null != data && data.actioncode == 'SUCCESS') {
        // console.log(data.data);

        $("#ec").html("连接中").removeClass("success").addClass("error");
        $("#mc").html("连接中").removeClass("success").addClass("error");

        var serverStatus = data.data;
        if (isNotEmpty(serverStatus) && serverStatus.length > 0) {

            for (var i = 0; i < serverStatus.length; i++) {

                var server = serverStatus[i];
                if ("zk" == server.servername && server.status == 1) {
                    $("#guidepage a").attr("href", server.url);
                }else if ("ec" == server.servername && server.status == 1) {
                    $("#ec").html("已启动").removeClass("error").addClass("success");
                } else if ("mc" == server.servername && server.status == 1) {
                    $("#mc").html("已启动").removeClass("error").addClass("success");
                }
            }
        }
    }
}

//一键谈话跳转审讯页面
var skipCheckbool=-1;//是否跳过检测：默认-1
var toUrltype=1;//跳转笔录类型 1笔录制作页 2笔录查看列表
function to_waitconversationURL() {
    var url=getActionURL(getactionid_manage().homepage_addCaseToArraignment);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            skipCheckbool:skipCheckbool,
            conversationbool:2,//一键谈话
        }
    };
    ajaxSubmitByJson(url,data,callbackaddCaseToArraignment);
}

function callbackaddCaseToArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var recordssid=data.recordssid;
            if (isNotEmpty(recordssid)&&toUrltype==1){
                var index = layer.msg('开始进行审讯', {icon: 6,shade:0.1,time:500
                },function () {
                    location.href=window.waitconversationURL+"?ssid="+recordssid
                });
            }else if(toUrltype==2){
                location.href =  window.recordIndexURL;
            }
        }
    }else{
        var data2=data.data;
        if (isNotEmpty(data2)){
            var recordingbool=data2.recordingbool
            var recordssid=data2.recordssid;
            var checkStartRecordVO=data2.checkStartRecordVO;

            if (null!=recordingbool&&recordingbool==true){
                //存在笔录正在进行中，跳转笔录列表，给出提示：建议他先结束制作中的
                if (isNotEmpty(checkStartRecordVO)){
                    var msg=checkStartRecordVO.msg;
                    if (isNotEmpty(msg)){
                        layer.confirm("<span style='color:red'>"+msg+"</span>", {
                            btn: ['开始审讯',"查看审讯列表","取消"], //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转审讯制作控制台");
                                //保存
                                skipCheckbool = 1;
                                to_waitconversationURL();
                                layer.close(index);
                            },
                            btn2: function(index) {
                                console.log("跳转审讯列表")
                                toUrltype=2;
                                skipCheckbool =1;
                                to_waitconversationURL();
                                layer.close(index);
                            },
                            btn3: function(index) {
                                layer.close(index);
                            }
                        });
                    }
                }
            }else {
                layer.msg(data.message,{icon: 5});
            }
        }else {
            layer.msg(data.message,{icon: 5});
        }
    }
}


