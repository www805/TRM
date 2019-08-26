var occurrencetime=null;

function getArraignmentList_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().arraignment_getArraignmentList);
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
        casename:casename,
        casenum:casenum,
        username:username,
        occurrencetime_start:occurrencetime_start,
        occurrencetime_end:occurrencetime_end,
        currPage:currPage,
        pageSize:pageSize
    };
  ajaxSubmit(url,data,callbackgetArraignmentList);
}

function getArraignmentList(casename,casenum,username,occurrencetime_start,occurrencetime_end,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().arraignment_getArraignmentList);
    var data={
        casename:casename,
        casenum:casenum,
        username:username,
        occurrencetime_start:occurrencetime_start,
        occurrencetime_end:occurrencetime_end,
        currPage:currPage,
        pageSize:pageSize
    };
     ajaxSubmit(url,data,callbackgetArraignmentList);
}

function callbackgetArraignmentList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function getArraignmentListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getArraignmentList_init(currPage,pageSize);
    }else if (len==2){
        getArraignmentList('',arguments[0],arguments[1]);
    }else if(len>2){
        getArraignmentList(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6]);
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

        showpage("paging",arrparam,'getArraignmentListByParam',currPage,pageCount,pageSize);
    }
}

function openModel(casessid,arraignmentslength,creator) {
    if (null==arraignmentslength||arraignmentslength<1){
        layer.msg("该案件没有笔录",{icon: 5});
        return;
    }


    if (isNotEmpty(casessid)) {
        var html='<form class="layui-form layui-form-pane site-inline"  style="margin: 15px;"><table creator="'+creator+'"  class="layui-hide" lay-filter="openModelhtml" id="openModelhtml" style="table-layout:fixed"></table></form>';

        var url=getActionURL(getactionid_manage().arraignment_getArraignmentByCaseSsid);
        var data={
            caseSsid:casessid,
        };
        ajaxSubmit(url,data,callbackgetArraignmentByCaseSsid);


        layui.use(['layer'], function(){
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "笔录列表",
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
    }else{
        layer.msg("参数错误",{icon: 6});
        return false;
    }
}

function callbackgetArraignmentByCaseSsid(data) {
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
                        var creator=$("#openModelhtml").attr("creator");
                     /*   towaitRecord(recordssid,recordbool,creator);*/
                    }
                });
            });
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }

}

//跳转笔录编辑页
function towaitRecord(recordssid,recordbool,creator) {
    if (!isNotEmpty(recordssid)){
        return false;
    }

    if (recordbool==2){
        var url=getActionURL(getactionid_manage().arraignment_getArraignmentShow);
        window.location.href=url+"?ssid="+recordssid;
    } else{
        layer.msg("笔录正在制作中...",{icon: 5})
    }
}



