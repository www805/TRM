function getdataInfos_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().startDownServer_getdataInfos);
    var dataname_cn=$("#dataname_cn").val();
    var data={
        dataname_cn:dataname_cn,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackggetdataInfos);
}
function getdataInfos(dataname_cn,currPage,pageSize){
    var url=getActionURL(getactionid_manage().startDownServer_getdataInfos);
    var data={
        dataname_cn:dataname_cn,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackggetdataInfos);
}


function callbackggetdataInfos(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

/**
 * 局部刷新
 */
function getdataInfosByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getdataInfos_init(currPage,pageSize);
    }else if (len==2){
        getdataInfos('',arguments[0],arguments[1]);
    }else if(len>2){
        getdataInfos(arguments[0],arguments[1],arguments[2]);
    }
}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var dataname_cn=pageparam.dataname_cn;
        var arrparam=new Array();
        arrparam[0] = dataname_cn;
        showpage("paging",arrparam,'getdataInfosByParam',currPage,pageCount,pageSize);
    }
}



var index;
function startdownServer(type,datainfossid) {

    var upserverip=$("#upserverip").val();
    if (!isNotEmpty(upserverip)){
        layer.msg("请输入要你要同步的上级服务器IP",{icon: 5});
        return false;
    }

    if (null==type) {
        layer.msg("系统异常",{icon:5});
        return false;
    }

    //初始化进度条
    $("[lay-filter='progress_demo']").css("visibility","visible");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");
    var url=getActionURL(getactionid_manage().startDownServer_startdownServer);


    var formData = new FormData();
    formData.append("type",type);
    formData.append("upserverip",upserverip);
    if (isNotEmpty(datainfossid)){
        formData.append("datainfossid",datainfossid);
    }

    index = layer.load(1, {
        shade: [0.1,'#fff']
    });

    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        callbackstartdownServer(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", progressFunction, false);

    xhr.send(formData);
}

function progressFunction(evt) {
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#startdownServer_btn").text("同步中" + completePercent).addClass('layui-btn-disabled').prop("disabled" , true);
        $("[lay-filter='progress_demo']").css("visibility","visible");
        $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
        $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
    }
};

function callbackstartdownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            layer.msg("同步成功",{icon: 5});
            $("#closeddownServer_btn").hide();
            $("#startdownServer_btn").text("全部同步").removeClass('layui-btn-disabled').prop("disabled" , false);
        }
    }else{
        layer.msg(data.message,{icon: 5});
        $("#closeddownServer_btn").show();
        $("#startdownServer_btn").text("同步失败").addClass('layui-btn-disabled').prop("disabled" , true);
    }
    $("[lay-filter='progress_demo']").css("visibility","hidden");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");
    layer.close(index);
}



function closeddownServer() {
    $("[lay-filter='progress_demo']").css("visibility","hidden");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    var upserverip=$("#upserverip").val();
     var url=getActionURL(getactionid_manage().startDownServer_closeddownServer);

    var formData = new FormData();
    formData.append("upserverip",upserverip);

    index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
        ,title:"关闭同步中"
    });
    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        callbackcloseddownServer(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", progressFunction2, false);

    xhr.send(formData);

}

function progressFunction2(evt) {
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#closeddownServer_btn").text("关闭同步中" + completePercent).addClass('layui-btn-disabled').prop("disabled" , true);
        $("[lay-filter='progress_demo']").css("visibility","visible");
        $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
        $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
    }
};
function callbackcloseddownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            $("#closeddownServer_btn").hide();
            $("#startdownServer_btn").text("全部同步").removeClass('layui-btn-disabled').prop("disabled" , false);
        }
    }else{
        layer.msg(data.message,{icon: 5});
        $("#closeddownServer_btn").show().text("强制关闭同步").removeClass('layui-btn-disabled').prop("disabled" , false);
    }
    layer.close(index);
    $("[lay-filter='progress_demo']").css("visibility","hidden");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

}



