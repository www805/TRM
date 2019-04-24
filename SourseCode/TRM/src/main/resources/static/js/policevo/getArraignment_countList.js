
function getArraignment_count_init(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);
    var data={
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetRoleList);
}
function getArraignment_countList(starttime,endtime,times,currPage,pageSize){
    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);
    var data={
        starttime:starttime,
        endtime:endtime,
        times:times,
        currPage:currPage,
        pageSize:pageSize
    };

    ajaxSubmit(url,data,callbackgetRoleList);
}

function getArraignment_count_search(currPage,pageSize) {

    var url=getActionURL(getactionid_manage().arraignment_count_getArraignment_countList);

    var starttime=$("#starttime").val();
    var endtime=$("#endtime").val();
    var times=$("#times").val();

    var data={
        starttime:starttime,
        endtime:endtime,
        times:times,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetRoleList);
}

function callbackgetRoleList(data){
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
function getArraignment_countByParam(){
    var len=arguments.length;

    // alert(len);
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getArraignment_count_init(currPage,pageSize);
    }else if (len==2){
        getArraignment_countList('',arguments[0],arguments[1]);
    }else if(len>2){
        getArraignment_countList(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var starttime=pageparam.starttime;
        var endtime=pageparam.endtime;
        var times=pageparam.times;
        var arrparam=new Array();
        arrparam[0] = starttime;
        arrparam[1]=endtime;
        arrparam[2]=times;
        showpage("paging",arrparam,'getArraignment_countByParam',currPage,pageCount,pageSize);
    }
}
