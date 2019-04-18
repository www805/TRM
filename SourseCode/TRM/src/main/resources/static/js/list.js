



function getAdminInfoPageByParam_init(currPage,pageSize){

    var url=getUrl_manage().ceshi2;
    var name =$("#name").val();
    var data={
        name:name,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetAdminInfoPage);

}

function getAdminInfoPage(name,currPage,pageSize){


    var url=getActionURL(getactionid_manage().main_getRole);
    var data={
        name:name,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetAdminInfoPage);
}

function callbackgetAdminInfoPage(data){

    if(null!=data&&data.actioncode=='SUCCESS'){
        pageshow(data);
    }else{
        parent.layer.msg(data.message,{icon: 2},1);
    }

}



function getAdminInfoPageByParam(){

    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getAdminInfoPageByParam_init(currPage,pageSize);
    }else if (len==2){
        getAdminInfoPage('',arguments[0],arguments[1]);
    }else if(len>2){
        getAdminInfoPage(arguments[0],arguments[1],arguments[2]);
    }

}


function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;
        var name=pageparam.name;
        var arrparam=new Array();
        arrparam[0]=name;
        showpage("paging",arrparam,'getAdminInfoPageByParam',currPage,pageCount,pageSize);
    }


}