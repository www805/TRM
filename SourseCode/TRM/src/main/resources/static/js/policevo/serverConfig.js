
function getUpdateServerConfig(){

    var url = getActionURL(getactionid_manage().serverconfig_getServerConfig);

    var id =$('input[name="id"]').val();
    var sysname =$('input[name="sysname"]').val();
    var syslogo_filesavessid =$('input[name="syslogo_filesavessid"]').val();
    var clientname =$('input[name="clientname"]').val();
    var client_filesavessid =$('input[name="client_filesavessid"]').val();
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
        syslogo_filesavessid:syslogo_filesavessid,
        clientname:clientname,
        client_filesavessid:client_filesavessid,
        serverip:serverip,
        serverport:serverport,
        authorizebool:authorizebool,
        type:type,
        workstarttime:workstarttime,
        workdays:workdays,
        authorizesortnum:authorizesortnum
    };

    ajaxSubmit(url,data,callServerConfig);
}

function callServerConfig(data){

    if(null!=data&&data.actioncode=='SUCCESS'){

        // var url=getActionURL(getactionid_manage().addOrUpdateKeyword_getKeyword);
        // window.location.href=url;
        alert(data.message);
        window.location.reload();
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

