/*
* 谈话列表
* */

var recordtype_conversation1;
var recordtype_conversation2;

//笔录列表渲染
function getRecords_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().conversationIndex_getRecords);
    var recordtypessid=$("#recordtypessid option:selected").val();
    var recordname=$("#recordname").val();
    var recordbool=$("#recordbool option:selected").val();
    var creatorbool=$("#creatorbool").prop("checked");
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            creatorbool:creatorbool,
            conversationbool:1,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecords);
}
function getRecords(recordtypessid,recordname,recordbool,creatorbool,conversationbool,currPage,pageSize){
    recordbool=$("#recordbool option:selected").val();//等于0的时候后台没有返回
    var url=getActionURL(getactionid_manage().conversationIndex_getRecords);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            creatorbool:creatorbool,
            conversationbool:1,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecords);
}
function callbackgetRecords(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            recordtype_conversation1=data.data.recordtype_conversation1;
            recordtype_conversation2=data.data.recordtype_conversation2;
        }
    }else{
        layer.msg(data.message,{icon:5});
    }


}
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
        getRecords_init(currPage,pageSize);
    }else if (len==2){
        getRecords('',arguments[0],arguments[1]);
    }else if(len>2){
        getRecords(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]);
    }

}
function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var recordtypessid=pageparam.recordtypessid;
        var recordname=pageparam.recordname;
        var recordbool=pageparam.recordbool;
        var creatorbool=pageparam.creatorbool;
        var conversationbool=pageparam.creatorbool;

        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        arrparam[2]=recordbool;
        arrparam[3]=creatorbool;
        arrparam[4]=conversationbool;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
    }

}



function changeboolRecord(obj) {
    var recordssid=$(obj).closest("tr").attr("ssid");
    if (isNotEmpty(recordssid)){
        layer.confirm('确定要删除该审讯吗', function(index){
            var url=getActionURL(getactionid_manage().conversationIndex_changeboolRecord);
            var d={
                token:INIT_CLIENTKEY,
                param:{
                    recordssid:recordssid,
                    recordbool:-1 //状态为删除-1
                }
            };
            ajaxSubmitByJson(url,d,function (data) {
                if(null!=data&&data.actioncode=='SUCCESS'){
                    if (isNotEmpty(data)) {
                        layer.msg("删除成功", {time: 500,icon:6}, function () {
                            ggetRecordsByParam();
                        });
                    }
                }else{
                    layer.msg(data.message,{icon:5});
                }
            });
            layer.close(index);
        });
    }

}


//跳转笔录编辑页

function towaitRecord(recordssid,recordbool,creator,creatorname,recordtypessid) {
    if (!isNotEmpty(recordssid)){
        return false;
    }
    if (recordbool==1||recordbool==0){
        if (isNotEmpty(creator)&&creator==sessionadminssid){
            if (recordtypessid==recordtype_conversation1&&isNotEmpty(recordtype_conversation1)){
                //跳转：
                var url=getActionURL(getactionid_manage().conversationIndex_towaitconversation);
                window.location.href=url+"?ssid="+recordssid;
            } else  if (recordtypessid==recordtype_conversation2&&isNotEmpty(recordtype_conversation2)){
                //跳转笔录制作
                var url=getActionURL(getactionid_manage().conversationIndex_towaitRecord);
                window.location.href=url+"?ssid="+recordssid;
            }
        }else {
            layer.msg(creatorname+"正在制作审讯...",{icon:5})
        }
    } else if (recordbool==2||recordbool==3){
        var url=getActionURL(getactionid_manage().conversationIndex_toconversationById);
        window.location.href=url+"?ssid="+recordssid;
    }
}
