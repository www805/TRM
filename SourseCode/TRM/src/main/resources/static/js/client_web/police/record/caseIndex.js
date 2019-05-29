function getCases_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var casename=$("#casename").val();
    var casenum=$("#casenum").val();
    var username=$("#username").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            casenum:casenum,
            username:username,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCases);
}

function getCases(casename,casenum,username,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            casenum:casenum,
            username:username,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCases);
}

function callbackgetCases(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message);
    }
}

function getCasesByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;
        getCases_init(currPage,pageSize);
    }else if (len==2){
        getCases('',arguments[0],arguments[1]);
    }else if(len>2){
        getCases(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]);
    }
}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var casename=pageparam.casename;
        var casenum=pageparam.casenum;
        var username=pageparam.username;
        var arrparam=new Array();
        arrparam[0] = casename;
        arrparam[1]=casenum;
        arrparam[2]=username;
        showpage("paging",arrparam,'getCasesByParam',currPage,pageCount,pageSize);
    }
}

