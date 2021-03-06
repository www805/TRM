var occurrencetime=null;//案件发生时间
var starttime=null;//谈话时间

function getCases_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var casename=$("#casename").val();
    var username=$("#username").val();

    var occurrencetime_start=null;//案发时间开始
    var occurrencetime_end=null;//案发结束时间
    if (isNotEmpty(occurrencetime)){
        var arr = occurrencetime.split("~");
        occurrencetime_start=arr[0].trim();
        occurrencetime_end=arr[1].trim();
    }

    var starttime_start=null;//谈话时间开始
    var starttime_end=null;//谈话时间结束
    if (isNotEmpty(starttime)){
        var arr = starttime.split("~");
        starttime_start=arr[0].trim();
        starttime_end=arr[1].trim();
    }

    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            username:username,
            occurrencetime_start:occurrencetime_start,
            occurrencetime_end:occurrencetime_end,
            starttime_start:starttime_start,
            starttime_end:starttime_end,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetCases);
}

function getCases(casename,username,occurrencetime_start,occurrencetime_end,starttime_start,starttime_end,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().caseIndex_getCases);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            casename:casename,
            username:username,
            occurrencetime_start:occurrencetime_start,
            occurrencetime_end:occurrencetime_end,
            starttime_start:starttime_start,
            starttime_end:starttime_end,
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
        layer.msg(data.message,{icon: 5});
    }

    layui.use('element', function(){
        var element = layui.element;
    });
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
        getCases(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6],arguments[7]);

    }
}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var casename=pageparam.casename;
        var username=pageparam.username;
        var occurrencetime_start=pageparam.occurrencetime_start;
        var occurrencetime_end=pageparam.occurrencetime_end;
        var starttime_start=pageparam.starttime_start;
        var starttime_end=pageparam.starttime_end;

        var arrparam=new Array();
        arrparam[0] = casename;
        arrparam[1]=username;
        arrparam[2]=occurrencetime_start;
        arrparam[3]=occurrencetime_end;
        arrparam[4]=starttime_start;
        arrparam[5]=starttime_end;
        showpage("paging",arrparam,'getCasesByParam',currPage,pageCount,pageSize);
    }
}


function open_RecordsByCasessid(casessid,arraignmentslength,creator,creatorname) {
    if (!isNotEmpty(creatorname)) {
        creatorname="未知";
    }
    if (null==arraignmentslength||arraignmentslength<1){
        layer.msg("该案件没有笔录",{icon: 5});
        return;
    }


    if (isNotEmpty(casessid)) {
        var html='<form class="layui-form layui-form-pane site-inline"  style="margin: 15px;"><table creator="'+creator+'" creatorname="'+creatorname+'"  class="layui-hide" lay-filter="openModelhtml" id="openModelhtml" style="table-layout:fixed"></table></form>';

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
                }else  if (datum.recordbool==2||datum.recordbool==3){
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
                        var multifunctionbool=arraignment.multifunctionbool;
                        var creator=$("#openModelhtml").attr("creator");
                        var creatorname=$("#openModelhtml").attr("creatorname");
                        towaitRecord(recordssid,recordbool,creator,creatorname,multifunctionbool);
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
                                        layer.msg("删除成功", {icon: 6,time: 500}, function () {
                                            obj.del();
                                            getCasesByParam();
                                        });
                                    }
                                }else{
                                    layer.msg(data.message,{icon: 5});
                                }
                            });
                            layer.close(index);
                        });
                    }
                    if(obj.event === 'detail'&&isNotEmpty(recordssid)){
                        var arraignment=dada=obj.data;
                        if (isNotEmpty(arraignment)){
                            var recordssid=arraignment.recordssid;
                            var recordbool=arraignment.recordbool;
                            var multifunctionbool=arraignment.multifunctionbool;
                            var creator=$("#openModelhtml").attr("creator");
                            var creatorname=$("#openModelhtml").attr("creatorname");
                            towaitRecord(recordssid,recordbool,creator,creatorname,multifunctionbool);
                        }
                    }
                });

            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

//案件归档
function changeboolCase(ssid,oldcasebool) {
    var con="案件归档后将不再被提讯，确定要归档吗";
    if (isNotEmpty(oldcasebool)&&oldcasebool==2){
        layer.msg("案件已归档",{icon: 6});
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
            layer.msg("归档成功", {time: 500,icon: 6}, function () {
                getCasesByParam();
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function continueCase(ssid,casebool) {
    if (casebool==2){
        layer.msg("案件已归档",{icon: 5});
        return;
    }
    if (casebool!=3){
        layer.msg("案件未暂停,不需要继续~",{icon: 6});
        return;
    }

    var con="案件将重新开启，确认吗";
    //开始改变状态
    layer.open({
        content:con
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
            var url=getActionURL(getactionid_manage().caseIndex_changeboolCase);
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid,
                    bool:casebool
                }
            };
            ajaxSubmitByJson(url,data,callbackcontinueCase);
            layer.close(index);
        }
        ,btn2: function(index, layero){
            layer.close(index);
        }
    });
}

function callbackcontinueCase(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)) {
            layer.msg("案件重新开启成功", {time: 500,icon: 6},function () {
                getCasesByParam();
                var addcasetoarraignmentvo_data=data.data.addcasetoarraignmentvo_data;
                if (isNotEmpty(addcasetoarraignmentvo_data)){
                    addcasetoarraignmentvo_data = eval('(' + addcasetoarraignmentvo_data + ')');
                    var recordssid=addcasetoarraignmentvo_data.recordssid;
                    var multifunctionbool=addcasetoarraignmentvo_data.multifunctionbool;

                    if (isNotEmpty(addcasetoarraignmentvo_data)&&isNotEmpty(recordssid)){

                        var btn=['开始笔录',"查看审讯列表","取消"];
                        btn2= function(index) {
                            console.log("跳转笔录列表")
                            var url = getActionURL(getactionid_manage().caseIndex_torecordIndex);
                            window.location.href = url;
                            layer.close(index);
                        };
                        if (gnlist.indexOf(HK_O)!=-1){
                            btn=['开始笔录',"取消"];
                            btn2=function(index) {
                                layer.close(index);
                            };
                        }
                        if (gnlist.indexOf(FY_T)!=-1){
                            btn=['开始庭审',"查看笔录列表","取消"];
                        }


                        layer.confirm("<span style='color:red'>新的笔录/审讯已生成</span>", {
                            btn:btn, //按钮
                            shade: [0.1,'#fff'], //不显示遮罩
                            btn1:function(index) {
                                console.log("跳转笔录制作中");
                                var index =layer.msg('开始进行笔录', {shade:[0.1,"#fff"],icon:6,time:500
                                },function () {
                                    if (gnlist.indexOf(FY_T)!=-1){
                                        var url=getActionURL(getactionid_manage().caseIndex_towaitCourt);
                                        window.location.href=url+"?ssid="+recordssid;
                                    }else {
                                        if (multifunctionbool==1){
                                            //跳转审讯制作中：
                                            var url=getActionURL(getactionid_manage().caseIndex_towaitconversation);
                                            window.location.href=url+"?ssid="+recordssid;
                                        } else if (multifunctionbool==2||multifunctionbool==3){
                                            //跳转笔录制作
                                            var url=getActionURL(getactionid_manage().caseIndex_towaitRecord);
                                            window.location.href=url+"?ssid="+recordssid;
                                        }
                                    }

                                });
                                layer.close(index);
                            },
                            btn2: btn2,
                            btn3: function(index) {
                                layer.close(index);
                            }
                        });
                    }
                }
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function delcase(ssid,casebool) {
    layer.confirm('该案件下的笔录将会清空，确定要删除该案件吗', function(index){
        var url=getActionURL(getactionid_manage().caseIndex_changeboolCase);
        var d={
            token:INIT_CLIENTKEY,
            param:{
                ssid:ssid,
                bool:-1
            }
        };
        ajaxSubmitByJson(url,d,function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
                if (isNotEmpty(data)) {
                    layer.msg("删除成功", {time: 500,icon: 6}, function () {
                        getCasesByParam();
                    });
                }
            }else{
                layer.msg(data.message,{icon: 5});
            }
        });
        layer.close(index);
    });
}

function toaddOupdateurl(ssid,casebool) {
    if (casebool==2){
        layer.msg("案件已归档",{icon: 5});
        return;
    }
    if (isNotEmpty(ssid)&&casebool!=2){
        window.location.href=addOupdateurl+"?ssid="+ssid;
    }
}

/*
跳转笔录编辑页
multifunctionbool:控制跳转界面
 */
function towaitRecord(recordssid,recordbool,creator,creatorname,multifunctionbool) {
    if (!isNotEmpty(recordssid)){
        return false;
    }
    if (recordbool==1||recordbool==0){
        //进入制作页面
        if (isNotEmpty(creator)&&creator==sessionadminssid){
            if (multifunctionbool==1){
                var url=getActionURL(getactionid_manage().caseIndex_towaitconversation);
                window.location.href=url+"?ssid="+recordssid;
            } else if (multifunctionbool==2||multifunctionbool==3){
                if (gnlist.indexOf(FY_T)!=-1)
                {
                    //法庭跳转
                    var url=getActionURL(getactionid_manage().caseIndex_towaitCourt);
                    window.location.href=url+"?ssid="+recordssid;
                }else {
                    //其他跳转
                    var url=getActionURL(getactionid_manage().caseIndex_towaitRecord);
                    window.location.href=url+"?ssid="+recordssid;
                }
            }
        }else {
            layer.msg(creatorname+"正在制作笔录...")
        }
    } else  if (recordbool==2||recordbool==3){
        //进入回放界面:后期决定去掉快速回放界面
        /*if (multifunctionbool==1||multifunctionbool==2){
            var url=getActionURL(getactionid_manage().caseIndex_toconversationById);
            window.location.href=url+"?ssid="+recordssid;
        }else if(multifunctionbool==3){*/
            if (gnlist.indexOf(FY_T)!= -1)
            {
                //法庭跳转
                var url=getActionURL(getactionid_manage().caseIndex_togetCourtDetail);
                window.location.href=url+"?ssid="+recordssid;
            }else {
                //其他跳转
                var url=getActionURL(getactionid_manage().caseIndex_togetRecordById);
                window.location.href=url+"?ssid="+recordssid;
            }
        /*}*/
    }
}


/**
 * 导出
 * @param ssid
 * @param casename
 */
//导出U盘
var exportUdisktime=0;//打包请求秒数间隔
function exportUdisk(ssid,total_filenum,finish_filenum){
    var timer_exportUdisk=null;
    if (isNotEmpty(ssid)){
        total_filenum=total_filenum==null?0:parseInt(total_filenum);
        finish_filenum=finish_filenum==null?0:parseInt(finish_filenum);
        if (total_filenum<1){
            layer.msg("未找到可导出文件",{icon:5});
            return;
        }

        layer.confirm('已完成笔录：'+total_filenum+'；即将导出您已确认的笔录：'+finish_filenum+'', {
            btn: ['立即导出','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            if (finish_filenum<1){
                layer.msg("未找到您已确认的笔录",{icon:5});
                layer.close(index);
                return;
            }


            if (exportUdisktime>0){
                layer.msg("导出太频繁，请"+exportUdisktime+"秒后再试",{icon:5});
                return;
            }

           var exportUdisksetInterval= setInterval(function () {
                exportUdisktime--;
               if (exportUdisktime<1){
                   clearInterval(exportUdisksetInterval);
               }
            },1000)

            if (exportUdisktime<1){
                layer.msg("导出中，请稍等...", {
                    icon: 16,
                    shade: [0.1, 'transparent']
                });
                var url=getActionURL(getactionid_manage().caseIndex_exportUdisk);
                var data={
                    param:{
                        ssid:ssid
                    }
                };
                $("#progress_"+ssid+"").css("visibility","visible");
                $("#progress_"+ssid+" .layui-progress-text").text("0%");
                $("#progress_"+ssid+" .layui-progress-bar").width("0%");

                exportUdisktime=15;
                $.ajax({
                    url : url,
                    type : "POST",
                    async : true,
                    dataType : "json",
                    contentType: "application/json",
                    data : JSON.stringify(data),
                    timeout : 60000,
                    xhr: function() {
                        var xhr = new XMLHttpRequest();
                        xhr.upload.addEventListener('progress', function (e) {
                            var completePercent = Math.round(e.loaded / e.total * 100)+ "%";
                            $("#progress_"+ssid+"").css("visibility","visible");
                            $("#progress_"+ssid+" .layui-progress-text").text(completePercent);
                            $("#progress_"+ssid+" .layui-progress-bar").width(completePercent);
                        },false);
                        return xhr;
                    },
                    success : function callbackexportUdisk(data) {
                        $("#progress_"+ssid+"").css("visibility","hidden");
                        if(null!=data&&data.actioncode=='SUCCESS'){
                            var data=data.data;
                            if (isNotEmpty(data)){
                                var downurl=data.downurl;
                                layer.msg("导出成功,等待下载中...",{icon: 6,time:800},function () {
                                    //开始请求获取进度
                                    timer_exportUdisk=setInterval(function () {
                                        exportUdiskProgress(ssid,downurl,timer_exportUdisk);
                                    },1000)
                                });

                            }
                        }else {
                            layer.msg(data.message,{icon: 5});
                        }
                    }
                });
            }
            layer.close(index);
        }, function(index){
            layer.close(index);
        });



    }
}

function exportUdiskProgress(ssid,downurl,timer_exportUdisk){
    var url=getActionURL(getactionid_manage().caseIndex_exportUdiskProgress);
    var data_={
        token:INIT_CLIENTKEY,
        param:{
            ssid:ssid,
        }
    };
    ajaxSubmitByJson(url,data_,function callbackexportUdiskProgress(data) {
        var data2=data.data;
        if(null!=data&&data.actioncode=='SUCCESS'){
            //开始显示进度
            if (isNotEmpty(data2)){
                var gzipCacheParam=data2.gzipCacheParam;
                if (isNotEmpty(gzipCacheParam)){
                    var totalzipnum=gzipCacheParam.totalzipnum==null?0:gzipCacheParam.totalzipnum;//总共有多少个需要打包的文件
                    var overzipnum=gzipCacheParam.overzipnum==null?0:gzipCacheParam.overzipnum;//已经完成了多少个文件
                    var shu=(overzipnum/totalzipnum)*100;
                    shu=parseInt(shu)+ "%";
                    var ssid=data2.ssid;
                    $("#progress_"+ssid+"").css("visibility","visible");
                    $("#progress_"+ssid+" .layui-progress-text").text(shu);
                    $("#progress_"+ssid+" .layui-progress-bar").width(shu);

                    layui.use(['layer','element','slider','form'], function(){
                        var element = layui.element;
                        element.render('progress');
                        //使用模块
                    });
                }
            }
        }else if(null!=data&&data.actioncode=='SUCCESS_NOTHINGTODO'){
            var ssid=data2.ssid;
            $("#progress_"+ssid+"").css("visibility","hidden");
            clearInterval(timer_exportUdisk);
            if (isNotEmpty(downurl)){
                var $a = $("<a></a>").attr("href", downurl).attr("download", "打包文件");
                $a[0].click();
            }
        }else {
            console.log(data.message);
        }
    });
}








//导出光盘
var exportLightdisk_index=null;
var exportLightdisktime=0;//打包请求秒数间隔
function exportLightdisk(ssid,total_filenum,finish_filenum){
    /* if (isNotEmpty(ssid)){
        total_filenum=total_filenum==null?0:parseInt(total_filenum);
        finish_filenum=finish_filenum==null?0:parseInt(finish_filenum);
        if (total_filenum<1){
            layer.msg("未找到可导出文件",{icon:5});
            return;
        }

        layer.confirm('已完成笔录：'+total_filenum+'；即将导出你已确认的笔录：'+finish_filenum+'', {
            btn: ['立即导出','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            if (finish_filenum<1){
                layer.msg("未找到您已确认的笔录",{icon:5});
                layer.close(index);
                return;
            }

            if (exportLightdisktime>0){
                layer.msg("导出太频繁，请"+exportLightdisktime+"秒后再试",{icon:5});
                return;
            }

            var exportLightdisksetInterval=setInterval(function () {
                exportLightdisktime--;
                if (exportLightdisktime<1){
                    clearInterval(exportLightdisksetInterval);
                }
            },1000)

            if (exportLightdisktime<1){
                exportLightdisk_index=layer.msg("导出中，请稍等...", {
                    icon: 16,
                    shade: [0.1, 'transparent']
                });
                var url=getActionURL(getactionid_manage().caseIndex_exportLightdisk);
                var data={
                    token:INIT_CLIENTKEY,
                    param:{
                        ssid:ssid
                    }
                };
                $("#progress_"+ssid+"").css("visibility","visible");
                $("#progress_"+ssid+" .layui-progress-text").text("0%");
                $("#progress_"+ssid+" .layui-progress-bar").width("0%");
                exportLightdisktime=15;
                $.ajax({
                    url : url,
                    type : "POST",
                    async : true,
                    dataType : "json",
                    contentType: "application/json",
                    data : JSON.stringify(data),
                    timeout : 60000,
                    xhr: function() {
                        var xhr = new XMLHttpRequest();
                        xhr.upload.addEventListener('progress', function (e) {
                            var completePercent = Math.round(e.loaded / e.total * 100)+ "%";
                            $("#progress_"+ssid+"").css("visibility","visible");
                            $("#progress_"+ssid+" .layui-progress-text").text(completePercent);
                            $("#progress_"+ssid+" .layui-progress-bar").width(completePercent);
                        },false);
                        return xhr;
                    },
                    success : function callbackexportLightdisk(data) {
                        $("#progress_"+ssid+"").css("visibility","hidden");
                        if(null!=data&&data.actioncode=='SUCCESS'){
                            var data=data.data;
                            layer.msg("导出成功...",{icon: 6});
                        }else {
                            layer.msg(data.message,{icon: 5});
                        }
                    }

                });
            }
            layer.close(index);
        }, function(index){
            layer.close(index);
        });

    }*/
}









