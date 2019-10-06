
var sysOrFd_page=1;//查询哪一个系统的日志，1是笔录系统，2是设备嵌入式系统

function getLogTypeList() {
    var url=getActionURL(getactionid_manage().logShow_getLogTypeList);
    ajaxSubmitByJson(url,
        {
            token:INIT_CLIENTKEY,
            param:
                {
                    sysOrFd:sysOrFd_page
                }
            },
        callbackgetLogTypeList);
    $("#logtype").empty();//清空日志类型列表
}

function callbackgetLogTypeList(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            var list=data.data;
            if (isNotEmpty(list)){
                $("#logtype").empty();
                for (var i = 0; i < list.length; i++) {
                    var logtype = list[i];
                    $("#logtype").append("<option value='"+logtype.value+"' >"+logtype.name+"</option>");
                }
            }
            layui.form.render('select');
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}


function getLogListByType_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().logShow_getLogListByType);
    var createtime =$("#createtime").val();
    var logtype =$("#logtype").val();

    var data={
        token:INIT_CLIENTKEY,
        param:{
            sysOrFd:sysOrFd_page,
            type:logtype,
            time:createtime,
            pagesize:10,
            currentpage:1
        }
    };
    ajaxSubmitByJson(url,data,callbackgetLogListByType);
}

function getLogListByType(createtime,logtype,currPage,pageSize){
    var url=getActionURL(getactionid_manage().logShow_getLogListByType);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            sysOrFd:sysOrFd_page,
            type:logtype,
            time:createtime,
            pagesize:pageSize,
            currentpage:currPage
        }
    };
    ajaxSubmitByJson(url,data,callbackgetLogListByType);
}


function callbackgetLogListByType(data){

    $("#loglist").html("");//清空tbody数据
    $("#paging").html("");//清空分页数据

    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            pageshow(data);
        }
    }else{
        layer.msg(data.message,{icon: 6});
    }
}

var pagelist;
var pageparam;

function pageshow(data){
    pagelist=data.data.logList;
    pageparam=data.data.logPageParam;

    //*存在问题：页面初步加载没有数据的时候table的thead也不会加载...
    if (isNotEmpty(pagelist)) {
        var tablehtml="";
        for(var i=0;i<pagelist.length;i++){
            var trhtml="<tr>";
            var logobj=pagelist[i];
            //写入tr
            trhtml+="<td>"+(i+1)+"</td>";
            if(sysOrFd_page==1){
                trhtml+="<td>"+logobj.user+"</td>";
            }else{
                trhtml+="<td></td>";
            }
            if(logobj.type=="info"){
                trhtml+="<td>正常</td>";
            }else if(logobj.type=="warn"){
                trhtml+="<td>警告</td>";
            }else if(logobj.type=="error"){
                trhtml+="<td>错误</td>";
            }else if(isNotEmpty(logobj.type)){
                trhtml+="<td>设备日志</td>";
            }else{
                trhtml+="<td></td>";
            }

            trhtml+="<td>"+logobj.time+"</td><td>"+logobj.msg+"</td>";

            trhtml+="</tr>";
            tablehtml+=trhtml;
        }
        $("#loglist").append(tablehtml);
        showpagetohtml();
    }

}

/**
 * 局部刷新
 */
function getLogListByTypeByParam(){
    var len=arguments.length;
    if(len==0||len==2){
        var currPage=1;
        var pageSize=10;//测试
        getLogListByType_init(currPage,pageSize);
    }else if(len>2){
        getLogListByType(arguments[0],arguments[1],arguments[2],arguments[3]);
    }

}


function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pagesize;
        var pageCount=pageparam.totalpage;
        var currPage=pageparam.currentpage;

        var type=pageparam.type;
        var time=pageparam.time;


        var arrparam=new Array();
        arrparam[0]=time;
        arrparam[1]=type;

        showpage("paging",arrparam,'getLogListByTypeByParam',currPage,pageCount,pageSize);
    }

}














function changesysOrFd(type){

    $("#li_fd").removeClass("layui-bg-blue");
    $("#li_sys").removeClass("layui-bg-blue");

    if(type==1){
        sysOrFd_page=1;
        $("#li_sys").addClass("layui-bg-blue");
    }else{
        sysOrFd_page=2;
        $("#li_fd").addClass("layui-bg-blue");
    }
    getLogTypeList();
    getLogListByTypeByParam();

}