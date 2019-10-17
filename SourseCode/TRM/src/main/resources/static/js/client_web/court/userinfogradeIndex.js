
function getUserinfogradePage_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().userinfogradeIndex_getUserinfogradePage);
    var gradename=$("#gradename").val();


    var data={
        token:INIT_CLIENTKEY,
        param:{
            gradename:gradename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserinfogradePage);
}

function getUserinfogradePage(gradename,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().userinfogradeIndex_getUserinfogradePage);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            gradename:gradename,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetUserinfogradePage);
}

function callbackgetUserinfogradePage(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function getUserinfogradePageByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;
        getUserinfogradePage_init(currPage,pageSize);
    }else if (len==2){
        getgetUserinfogradePage('',arguments[0],arguments[1]);
    }else if(len>2){
        getgetUserinfogradePage(arguments[0],arguments[1],arguments[2]);

    }
}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var gradename=pageparam.gradename;

        var arrparam=new Array();
        arrparam[0] = gradename;

        showpage("paging",arrparam,'getUserinfogradePageByParam',currPage,pageCount,pageSize);
    }
}

function toaddOupdateurl(ssid) {
    if (isNotEmpty(ssid)){
        window.location.href=AddOrUpdateUserinfogradeUrl+"?ssid="+ssid;
    }
}