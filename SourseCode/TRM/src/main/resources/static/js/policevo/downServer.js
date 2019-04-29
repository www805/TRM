var lastIP=null;

function getdownServers_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().downServer_getdownServers);
    var upserverip=$("#upserverip").val();
    var data={
        upserverip:upserverip,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetdownServers);
}
function getdownServers(upserverip,currPage,pageSize){
    var url=getActionURL(getactionid_manage().downServer_getdownServers);
    var data={
        upserverip:upserverip,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetdownServers);
}


function callbackgetdownServers(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            if (isNotEmpty(data.data)) {
                lastIP=data.data.lastIP;
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getdownServersByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getdownServers_init(currPage,pageSize);
    }else if (len==2){
        getdownServers('',arguments[0],arguments[1]);
    }else if(len>2){
        getdownServers(arguments[0],arguments[1],arguments[2]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var upserverip=pageparam.upserverip;
        var arrparam=new Array();
        upserverip[0] = upserverip;
        showpage("paging",arrparam,'getdownServersByParam',currPage,pageCount,pageSize);
    }
}

$(function () {

    $("#startdownServer").click(function () {
       var tostartDownServerUrl=getActionURL(getactionid_manage().downServer_tostartDownServer);
        window.location.href=tostartDownServerUrl+"?lastIP="+lastIP;
    });
});






