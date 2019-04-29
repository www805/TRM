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
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getdataInfosByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
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

function startdownServer(type,datainfossid) {
    $("[lay-filter='progress_demo']").css("visibility","visible");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    var upserverip=$("#upserverip").val();
    if (!isNotEmpty(upserverip)){
        layer.msg("请输入要你要同步的上级服务器IP",{icon: 2});
        return false;
    }
    
    if (null==type) {
        layer.msg("系统异常",{icon: 2});
        return false;
    }

    var url=getActionURL(getactionid_manage().startDownServer_startdownServer);


    var formData = new FormData();
    formData.append("type",type);
    formData.append("upserverip",upserverip);
    if (isNotEmpty(datainfossid)){
        formData.append("datainfossid",datainfossid);
    }
    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        $("#startdownServer_btn").removeClass('layui-btn-disabled').prop("disabled",false);
        $("#startdownServer_btn").text("全部同步");
        $("[lay-filter='progress_demo']").css("visibility","hidden");
        $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
        $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");
        callbackstartdownServer(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", progressFunction, false);

    xhr.send(formData);
}

function progressFunction(evt) {
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
        $("#startdownServer_btn").text("同步中" + completePercent);
        $("#startdownServer_btn").addClass('layui-btn-disabled').prop("disabled" , true);
        $("[lay-filter='progress_demo']").css("visibility","visible");
        $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
        $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
    }
};

function callbackstartdownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

        }
    }else{
       // layer.msg(data.message,{icon: 2});
        $("#closeddownServer_btn").show();
        $("#startdownServer_btn").addClass('layui-btn-disabled').prop("disabled" , true);
        $("[lay-filter='progress_demo']").css("visibility","hidden");
        $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
        $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    }
}



function closeddownServer() {
    $("#closeddownServer_btn").hide();
    $("#startdownServer_btn").removeClass('layui-btn-disabled');
    $("#startdownServer_btn").val("全部同步");
   /* var datainfossids=[];
    if (datainfossid==-1){
        //全部同步
        if (isNotEmpty(datainfos)){
            for (var i = 0; i < datainfos.length; i++) {
                var datum = datainfos[i];
                datainfossids.push(datum.ssid);
            }
        }
    }else{
        datainfossids.push(datainfossid);
    }


    var url=getActionURL(getactionid_manage().downServer_closeddownServer);
    var data={
        datainfossids:datainfossids,
        downserverssid:downserverssid,
    };
    index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
        ,title:"关闭同步中"
    });
    $.ajax({
        url : url,
        type : "post",
        async : true,
        dataType : "json",
        data : JSON.stringify(data),
        timeout : 60000,
        contentType: "application/json",
        success : function(reData) {
            if ($.trim(reData) == null) {
                parent.layer.msg("本次请求失败",{icon: 2});
            } else {
                callbackcloseddownServer(reData);
            }
        },error : function(){
            parent.layer.msg("请求异常",{icon: 2});
        }
    });*/
}
function callbackcloseddownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
    layer.close(index);
}



