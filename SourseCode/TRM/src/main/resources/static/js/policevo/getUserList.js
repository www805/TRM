
function getUserList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var username =$("#username").val();
    var loginaccount =$("#loginaccount").val();
    var workunitssid=$("#workunitssid option:selected").val();
    var rolessid=$("#rolessid option:selected").val();
    var adminbool=$("#adminbool option:selected").val();
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetUserList);
}

function getUserList(username,loginaccount,workunitssid,rolessid,adminbool,currPage,pageSize){
    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var data={
        username:username,
        loginaccount:loginaccount,
        workunitssid:workunitssid,
        rolessid:rolessid,
        adminbool:adminbool,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetUserList);
}


function callbackgetUserList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            layui.use('form', function(){
             var form =  layui.form;
             form.render();
            });
        }
    }else{
        parent.layer.msg(data.message,{icon: 2},1);
    }
}

/**
 * 局部刷新
 */
function getUserListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getUserList_init(currPage,pageSize);
    }else if (len==2){
        getUserList('',arguments[0],arguments[1]);
    }else if(len>2){
        getUserList(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var username=pageparam.username;
        var loginaccount=pageparam.loginaccount;
        var workunitssid=pageparam.workunitssid;
        var rolessid=pageparam.rolessid;
        var adminbool=pageparam.adminbool;

        var arrparam=new Array();
        arrparam[0]=username;
        arrparam[1]=loginaccount;
        arrparam[2]=workunitssid;
        arrparam[3]=rolessid;
        arrparam[4]=adminbool;
        showpage("paging",arrparam,'getUserListByParam',currPage,pageCount,pageSize);
    }


}