function getpackdownList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().packdownList_getPackdownList);
    var filename=$("#filename").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            filename:filename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetpackdownList);
}
function getpackdownList(filename,currPage,pageSize){
    var url=getActionURL(getactionid_manage().packdownList_getPackdownList);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            filename:filename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetpackdownList);
}


function callbackgetpackdownList(data){
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
function getpackdownListByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getpackdownList_init(currPage,pageSize);
    }else if (len==2){
        getpackdownList('',arguments[0],arguments[1]);
    }else if(len>2){
        getpackdownList(arguments[0],arguments[1],arguments[2]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var filename=pageparam.filename;
        var arrparam=new Array();
        arrparam[0] = filename;
        showpage("paging",arrparam,'getpackdownListByParam',currPage,pageCount,pageSize);
    }
}

function changeboolPackdown(ssid) {
    if (isNotEmpty(ssid)){
        layer.confirm('确定要删除该插件吗', function(index){
            var url=getActionURL(getactionid_manage().packdownList_changeboolPackdown);
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid,
                    filebool:-1
                }
            };
            ajaxSubmitByJson(url,data,callbackdelpackdown);
            layer.close(index);
        });

    }
}
function callbackdelpackdown(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            layer.msg("删除成功",{icon: 6});
            getpackdownListByParam();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function uploadFile_click() {
    $("#uploadFile").click();
}
function uploadPackdown() {
    var file = document.getElementById("uploadFile").files[0];
    if (!isNotEmpty(file)){
        layer.msg("请选择文件进行上传",{icon: 5});
        return;
    }
    $("[lay-filter='progress_demo']").css("visibility","visible");
    $("[lay-filter='progress_demo'] .layui-progress-text").text("0%");
    $("[lay-filter='progress_demo'] .layui-progress-bar").width("0%");

    var url=getActionURL(getactionid_manage().packdownList_uploadPackdown);

    var formData = new FormData();
    formData.append("token", INIT_CLIENTKEY);
    formData.append("file", file);
    // XMLHttpRequest 对象
    var xhr = new XMLHttpRequest();
    xhr.open("post", url, true);
    xhr.timeout = 500000;
    xhr.onload = function(data) {
        $("[lay-filter='progress_demo']").css("visibility","hidden");
        callbackuploadWordTemplate(xhr.responseText);
    };
    xhr.upload.addEventListener("progress", function (evt) {
        if (evt.lengthComputable) {
            var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
            $("[lay-filter='progress_demo']").css("visibility","visible");
            $("[lay-filter='progress_demo'] .layui-progress-text").text(completePercent);
            $("[lay-filter='progress_demo'] .layui-progress-bar").width(completePercent);
        }
    }, false);
    xhr.send(formData);
}


function callbackuploadWordTemplate(str) {
    var data = eval('(' + str + ')');
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("上传成功",{time:500,icon:6},function () {
                getpackdownListByParam();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function downPackdown(url,filename) {
    if (isNotEmpty(url)){
        var $a = $("<a></a>").attr("href", url).attr("download", filename);
        $a[0].click();
    }

}