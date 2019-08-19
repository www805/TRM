var occurrencetime=null;


var recordtype_conversation1;
var recordtype_conversation2;

function getCases_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var casename=$("#casename").val();
    var casenum=$("#casenum").val();
    var username=$("#username").val();

    var occurrencetime_start=null;//案发时间开始
    var occurrencetime_end=null;//案发结束时间

    if (isNotEmpty(occurrencetime)){
        var arr = occurrencetime.split("~");
        occurrencetime_start=arr[0].trim();
        occurrencetime_end=arr[1].trim();
    }


    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            casenum:casenum,
            username:username,
            occurrencetime_start:occurrencetime_start,
            occurrencetime_end:occurrencetime_end,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCases);
}

function getCases(casename,casenum,username,occurrencetime_start,occurrencetime_end,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            casenum:casenum,
            username:username,
            occurrencetime_start:occurrencetime_start,
            occurrencetime_end:occurrencetime_end,
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
            recordtype_conversation1=data.data.recordtype_conversation1;
            recordtype_conversation2=data.data.recordtype_conversation2;
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
        getCases(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);

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
        var occurrencetime_start=pageparam.occurrencetime_start;
        var occurrencetime_end=pageparam.occurrencetime_end;

        var arrparam=new Array();
        arrparam[0] = casename;
        arrparam[1]=casenum;
        arrparam[2]=username;
        arrparam[3]=occurrencetime_start;
        arrparam[4]=occurrencetime_end;
        showpage("paging",arrparam,'getCasesByParam',currPage,pageCount,pageSize);
    }
}


function open_RecordsByCasessid(casessid,arraignmentslength,creator) {
    if (null==arraignmentslength||arraignmentslength<1){
        layer.msg("该案件没有笔录");
        return;
    }


    if (isNotEmpty(casessid)) {
        var html='<form class="layui-form layui-form-pane site-inline"  style="margin: 15px;"><table creator="'+creator+'"  class="layui-hide" lay-filter="openModelhtml" id="openModelhtml" style="table-layout:fixed"></table></form>';

        var url=getActionURL(getactionid_manage().caseIndex_getRecordByCasessid);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                casessid:casessid,
            }
        };
        ajaxSubmitByJson(url,data,callbackgetRecordByCasessid);


        layui.use(['layer'], function(){
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "笔录列表(提示:双击可对<span style='color: red;'>*进行中</span>的笔录进行编辑,对<span style='color: #00FF00;'>*已完成</span>的笔录进行查看)",
                shade: 0.5,
                shadeClose : true,
                area: ['1200px', '620px'],
                content: html,
                btn: ['返回'],
                skin: 'demo-class',
                yes: function(index, layero){
                    layer.close(index);
                }
            });
        });
    }
}
function callbackgetRecordByCasessid(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            for (var i = 0; i < data.length; i++) {
                var datum = data[i];
                var bool="";
                if (datum.recordbool==1){
                    bool="<span style='color: red ' bool='1'>进行中</span>";
                }else  if (datum.recordbool==2){
                    bool="<span style='color: #00FF00 ' bool='2'>已完成</span>";
                }else  if (datum.recordbool==0){
                    bool="<span style='color: #cccccc ' bool='0'>未开始</span>";
                }else {
                    bool="未知";
                }
                datum["bool"]=bool;
            }

            layui.use(['table'], function(){
                var table = layui.table;

                //展示已知数据
                table.render({
                    elem: '#openModelhtml'

                    ,id: 'idTest'
                    ,cols: [[ //标题栏
                        {field: 'bool', title: '笔录状态',sort:true}
                        ,{field: 'recordname', title: '笔录名称',sort:true,width:350}
                        ,{field: 'askobj', title: '询问对象'}
                        ,{field: 'adminname', title: '询问人'}
                        ,{field: 'recordplace', title: '询问地点'}
                        ,{field: 'recordadminname', title: '记录人'}
                        ,{field: 'createtime', title: '记录时间',sort:true,width:180}
                        ,{fixed: 'right', title:'操作', toolbar: '#operation', width:150}
                    ]]
                    ,data: data
                    ,even: true
                    ,page: true
                    ,height: 'full'
                    ,cellMinWidth: 120
                    ,loading:true
                });
                table.resize('idTest');
                table.on('rowDouble(openModelhtml)', function(obj){
                    var arraignment=dada=obj.data;
                    if (isNotEmpty(arraignment)){
                        var recordssid=arraignment.recordssid;
                        var recordbool=arraignment.recordbool;
                        var recordtypessid=arraignment.recordtypessid;
                        var creator=$("#openModelhtml").attr("creator");
                        towaitRecord(recordssid,recordbool,creator,recordtypessid);
                    }
                });
                //监听行工具事件
                table.on('tool(openModelhtml)', function(obj){
                    var tooldata = obj.data;
                    var recordssid=tooldata.recordssid;
                    if(obj.event === 'del'&&isNotEmpty(recordssid)){
                        layer.confirm('确定要删除该笔录吗', function(index){
                            var url=getActionURL(getactionid_manage().caseIndex_changeboolRecord);
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
                                        layer.msg("删除成功", {time: 500}, function () {
                                            obj.del();
                                            getCasesByParam();
                                        });
                                    }
                                }else{
                                    layer.msg(data.message);
                                }
                            });
                            layer.close(index);
                        });
                    } 
                });

            });
        }
    }else{
        layer.msg(data.message);
    }
}

//案件归档
function changeboolCase(ssid,oldcasebool) {
    var con="案件归档后将不再被提讯，确定要归档吗";
    if (isNotEmpty(oldcasebool)&&oldcasebool==2){
        layer.msg("案件已归档");
        return;
    }

    if (oldcasebool==0){
        con="案件还未提讯，确定归档吗"
    }

    layer.open({
        content:con
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
            var url=getActionURL(getactionid_manage().caseIndex_changeboolCase);
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid,
                    bool:2
                }
            };
            ajaxSubmitByJson(url,data,callbackchangeboolCase);
            layer.close(index);
        }
        ,btn2: function(index, layero){
            layer.close(index);
        }
    });
}

function callbackchangeboolCase(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)) {
            layer.msg("归档成功", {time: 500}, function () {
                getCasesByParam();
            });
        }
    }else{
        layer.msg(data.message);
    }
}


function toaddOupdateurl(ssid,casebool) {
    if (casebool==2){
        layer.msg("案件已归档");
        return;
    }
    if (isNotEmpty(ssid)&&casebool!=2){
        window.location.href=addOupdateurl+"?ssid="+ssid;
    }
}


//跳转笔录编辑页
function towaitRecord(recordssid,recordbool,creator,recordtypessid) {
    if (!isNotEmpty(recordssid)){
        return false;
    }

    if (recordbool==2){
        if (isNotEmpty(recordtype_conversation1)&&isNotEmpty(recordtype_conversation2)&&recordtypessid==recordtype_conversation1||recordtypessid==recordtype_conversation2){
            //跳转审讯回放
            var url=getActionURL(getactionid_manage().caseIndex_toconversationById);
            window.location.href=url+"?ssid="+recordssid;
       } else{
           //跳转笔录回放
            var url=getActionURL(getactionid_manage().caseIndex_togetRecordById);
            window.location.href=url+"?ssid="+recordssid;
       }
    } else{
        if (isNotEmpty(creator)&&creator==sessionadminssid){
            if (recordtypessid==recordtype_conversation1&&isNotEmpty(recordtype_conversation1)){
                //跳转审讯制作中：
                var url=getActionURL(getactionid_manage().caseIndex_towaitconversation);
                window.location.href=url+"?ssid="+recordssid;
            } else {
                //跳转笔录制作
                var url=getActionURL(getactionid_manage().caseIndex_towaitRecord);
                window.location.href=url+"?ssid="+recordssid;
            }

         }else {
            layer.msg("笔录正在制作中...")
        }
    }
}


