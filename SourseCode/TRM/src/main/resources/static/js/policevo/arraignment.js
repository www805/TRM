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
        layer.msg(data.message,{icon: 2});
    }
}

function getArraignmentListByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;//测试
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
        var arrparam=new Array();
        arrparam[0] = casename;
        arrparam[1]=casenum;
        arrparam[2]=username;
        showpage("paging",arrparam,'getArraignmentListByParam',currPage,pageCount,pageSize);
    }
}

function openModel(ssid) {
    if (isNotEmpty(ssid)) {
        var html='<table class="layui-hide" lay-filter="openModelhtml" id="openModelhtml"></table>';

        var url=getActionURL(getactionid_manage().arraignment_getArraignmentByCaseSsid);
        var data={
            caseSsid:ssid
        };
        ajaxSubmit(url,data,callbackgetArraignmentByCaseSsid);


        layui.use(['layer'], function(){
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "选择提讯进行查看(<span style='color: red;font-size: 12px'>双击跳转详情页</span>)",
                shade: 0.5,
                shadeClose : true,
                area: ['800px', '500px'],
                content: html,
                btn: ['返回'],
                skin: 'demo-class',
                yes: function(index, layero){
                    layer.close(index);
                }
            });
        });
    }else{
        layer.msg("参数错误",{icon: 2});
        return false;
    }
}

function callbackgetArraignmentByCaseSsid(data) {


    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var data=data.data;
            for (var i = 0; i < data.length; i++) {
                var datum = data[i];
                if (datum.recordbool==1){
                    datum.recordbool="进行中";
                }else if (datum.recordbool==2){
                    datum.recordbool="已完成";
                }else if (datum.recordbool==0){
                    datum.recordbool="未开始";
                }else {
                    datum.recordbool="未知";
                }

            } 

            layui.use(['table'], function(){
                var table = layui.table;

                //展示已知数据
                table.render({
                    elem: '#openModelhtml'
                    ,cols: [[ //标题栏
                        {field: 'recordbool', title: '笔录状态'}
                        ,{field: 'recordname', title: '笔录名称'}
                        ,{field: 'askobj', title: '询问对象'}
                        ,{field: 'adminname', title: '询问人'}
                        ,{field: 'recordplace', title: '询问地点'}
                        ,{field: 'recordadminname', title: '记录人'}
                        ,{field: 'createtime', title: '记录时间'}
                    ]]
                    ,data: data
                    ,skin: 'line' //表格风格
                    ,even: true
                });
                table.on('rowDouble(openModelhtml)', function(obj){
                    var arraignment=dada=obj.data;
                    if (isNotEmpty(arraignment)){
                        var ssid=arraignment.recordssid;
                        var getArraignmentShowurl=getActionURL(getactionid_manage().arraignment_getArraignmentShow);
                      window.location.href=getArraignmentShowurl+"?ssid="+ssid;
                    }
                });

            });
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }




}

