function getRoleList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getRoleList_getRoleList);
    var data={
        name:name,
        currPage:currPage,
        pageSize:pageSize
    };

    ajaxSubmit(url,data,callbackgetAdminInfoPage);
}
function getRoleList(name,currPage,pageSize){
    var url=getActionURL(getactionid_manage().getRoleList_getRoleList);
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
        //parent.layer.msg(data.message,{icon: 2},1);
        alert(data.message);
    }

}

/**
 * 局部刷新
 */
function getRoleListByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getRoleList_init(currPage,pageSize);
    }else if (len==2){
        getRoleList('',arguments[0],arguments[1]);
    }else if(len>2){
        getRoleList(arguments[0],arguments[1],arguments[2]);
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
        showpage("paging",arrparam,'getRoleListByParam',currPage,pageCount,pageSize);
    }


}