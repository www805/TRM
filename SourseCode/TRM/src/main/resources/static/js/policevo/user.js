

function getAdminInfoPage(username,currPage,pageSize){

    var url=getActionURL(getactionid_manage().getUserList_getUserList);
    var data={
        username:username,
        // loginaccount:loginaccount,
        // roleid:roleid,
        currPage:currPage,
        pageSize:pageSize
    };

    //加载层-风格4
    var index = layer.msg('加载中', {
        icon: 16
        ,shade: 0.01
        ,time: 6000, //20s后自动关闭
    });

    ajaxSubmit(url,data,callbackgetAdminInfoPage);

    layer.close(index);
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
function getAdminInfoPageByParam(){

    var len=arguments.length;

    console.log(len);

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