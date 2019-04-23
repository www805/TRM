
function getUpdateServerConfig(){

    var url = getActionURL(getactionid_manage().serverconfig_getServerConfig);

    var id =$('input[name="id"]').val();
    var sysname =$('input[name="sysname"]').val();
    var syslogourl =$('input[name="syslogourl"]').val();
    var clientname =$('input[name="clientname"]').val();
    var clienturl =$('input[name="clienturl"]').val();
    var serverip =$('input[name="serverip"]').val();
    var serverport =$('input[name="serverport"]').val();
    var authorizebool =$('input[name="authorizebool"]').val();
    var type =$('input[name="type"]').val();
    var workstarttime =$('input[name="workstarttime"]').val();
    var workdays =$('input[name="workdays"]').val();
    var authorizesortnum =$('input[name="authorizesortnum"]').val();

    var data={
        id:id,
        sysname:sysname,
        syslogourl:syslogourl,
        clientname:clientname,
        clienturl:clienturl,
        serverip:serverip,
        serverport:serverport,
        authorizebool:authorizebool,
        type:type,
        workstarttime:workstarttime,
        workdays:workdays,
        authorizesortnum:authorizesortnum
    };

    console.log(url);
    console.log(data);

    // ajaxSubmit(url,data,callServerConfig);
}

function callServerConfig(data){

    if(null!=data&&data.actioncode=='SUCCESS'){

        var url=getActionURL(getactionid_manage().addOrUpdateKeyword_getKeyword);
        window.location.href=url;
    }else{
        // alert(data.message);
        layer.msg(data.message, {time: 5000, icon:5});
    }
}

function showpagetohtml(){

    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;
        var text=$("input[name='text']").val();
        var arrparam=new Array();
        arrparam[0]=text;
        showpage("paging",arrparam,'getKeyWordByParam',currPage,pageCount,pageSize);
    }


}