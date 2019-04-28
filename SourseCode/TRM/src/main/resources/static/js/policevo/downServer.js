var datainfos=null;

function getdownServers_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().downServer_getdownServers);
    var upserverip=$("#upserverip").val();
    var data={
        upserverip:upserverip,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetdownServers);
}
function getdownServers(upserverip,currPage,pageSize){
    var url=getActionURL(getactionid_manage().downServer_getdownServers);
    var data={
        upserverip:upserverip,
        currPage:currPage,
        pageSize:pageSize
    };
    ajaxSubmit(url,data,callbackgetdownServers);
}


function callbackgetdownServers(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
            if (isNotEmpty(data.data)) {
                datainfos=data.data.datainfos;
            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
}

/**
 * 局部刷新
 */
function getdownServersByParam(){
    var len=arguments.length;

    if(len==0){
        var currPage=1;
        var pageSize=3;//测试
        getdownServers_init(currPage,pageSize);
    }else if (len==2){
        getdownServers('',arguments[0],arguments[1]);
    }else if(len>2){
        getdownServers(arguments[0],arguments[1],arguments[2]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var upserverip=pageparam.upserverip;
        var arrparam=new Array();
        upserverip[0] = upserverip;
        showpage("paging",arrparam,'getdownServersByParam',currPage,pageCount,pageSize);
    }
}



function openModel(ssid) {
    if (isNotEmpty(ssid)) {
        var html=' <input type="button" class="layui-btn layui-btn-normal"   style="margin: 10px"  id="zong2" onclick="startdownServer('+ssid+',-1,this);"   value="全部同步"/>\
                     <input type="button" class="layui-btn layui-btn-danger"  style="margin: 10px;" id="zong1" onclick="closeddownServer('+ssid+',-1);"  value="强制关闭同步"  />\
                     <table class="layui-hide" lay-filter="openModelhtml" id="openModelhtml" style="overflow-x: hidden"></table>';

        layui.use(['layer','table'], function(){
            var layer = layui.layer;
            layer.open({
                type: 1,
                title: "选择表单进行同步",
                shade: 0.5,
                shadeClose : true,
                area: ['900px', '600px'],
                content: html,
                btn: ['返回'],
                skin: 'demo-class',
                yes: function(index, layero){
                    layer.close(index);
                }
            });

            var table = layui.table;

            //展示已知数据
            table.render({
                elem: '#openModelhtml'
                ,cols: [[ //标题栏
                    {field: 'dataname', title: '表单名称'}
                    ,{field: 'dataname_cn', title: '笔录名称'}
                    ,{field: 'mappername', title: '询问对象'}
                    ,{field: 'typessid', title: '类型'}
                    ,{ title: '操作', toolbar: '#barDemo'}
                ]]
                ,data: datainfos
                ,skin: 'line' //表格风格
                ,even: true
            });

            table.on('tool(openModelhtml)', function(obj){
                var data = obj.data;
                if(obj.event === 'startdownServer_btn'){
                    startdownServer(ssid,data.ssid );
                } else if(obj.event === 'closeddownServer_btn'){
                    closeddownServer(ssid,data.ssid );
                }
            });
        });
    }else{
        layer.msg("参数错误",{icon: 2});
        return false;
    }
}


var index ;
function startdownServer(downserverssid,datainfossid,obj) {
    if (!isNotEmpty(downserverssid)&&!isNotEmpty(datainfossid)) {
        layer.msg("系统异常",{icon: 2});
        return false;
    }

    var datainfossids=[];
    if (datainfossid==-1){
    //全部同步
        if (isNotEmpty(datainfos)){
            for (var i = 0; i < datainfos.length; i++) {
                var datum = datainfos[i];
                datainfossids.push(datum.ssid);
            }
        }
    }else{
        datainfossids.push(datainfossid);
    }

    var url=getActionURL(getactionid_manage().downServer_startdownServer);
    var data={
        datainfossids:datainfossids,
        downserverssid:downserverssid,
    };


    index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
        ,title:"同步中"
    });

    $.ajax({
        url : url,
        type : "post",
        async : true,
        dataType : "json",
        data : JSON.stringify(data),
        timeout : 60000,
        contentType: "application/json",
        success : function(reData) {
            if ($.trim(reData) == null) {
                parent.layer.msg("本次请求失败",{icon: 2});
            } else {
                callbackstartdownServer(reData);
            }
        },error : function(){
            parent.layer.msg("请求异常",{icon: 2});
        }
    });
}
function callbackstartdownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
    layer.close(index);
}



function closeddownServer(downserverssid,datainfossid) {
    if (!isNotEmpty(downserverssid)&&!isNotEmpty(datainfossid)) {
        layer.msg("系统异常",{icon: 2});
        return false;
    }

    var datainfossids=[];
    if (datainfossid==-1){
        //全部同步
        if (isNotEmpty(datainfos)){
            for (var i = 0; i < datainfos.length; i++) {
                var datum = datainfos[i];
                datainfossids.push(datum.ssid);
            }
        }
    }else{
        datainfossids.push(datainfossid);
    }


    var url=getActionURL(getactionid_manage().downServer_closeddownServer);
    var data={
        datainfossids:datainfossids,
        downserverssid:downserverssid,
    };
    index = layer.load(1, {
        shade: [0.1,'#fff'] //0.1透明度的白色背景
        ,title:"关闭同步中"
    });
    $.ajax({
        url : url,
        type : "post",
        async : true,
        dataType : "json",
        data : JSON.stringify(data),
        timeout : 60000,
        contentType: "application/json",
        success : function(reData) {
            if ($.trim(reData) == null) {
                parent.layer.msg("本次请求失败",{icon: 2});
            } else {
                callbackcloseddownServer(reData);
            }
        },error : function(){
            parent.layer.msg("请求异常",{icon: 2});
        }
    });
}
function callbackcloseddownServer(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){

        }
    }else{
        layer.msg(data.message,{icon: 2});
    }
    layer.close(index);
}







