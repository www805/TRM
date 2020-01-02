/**
 * 情绪报告 在getrecordbyid
 */

var starttime;
function getPhreports_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getRecordById_getPhreports);
    var phreportname=$("#phreportname").val();
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
            phreportname:phreportname,
            starttime_start:starttime_start,
            starttime_end:starttime_end,
            currPage:currPage,
            pageSize:pageSize,
            recordssid:recordssid
        }
    };
    ajaxSubmitByJson(url,data,callbackgetPhreports);
}
function getPhreports(phreportname,starttime_start,starttime_end,currPage,pageSize) {
    var url=getActionURL(getactionid_manage().getRecordById_getPhreports);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            phreportname:phreportname,
            starttime_start:starttime_start,
            starttime_end:starttime_end,
            currPage:currPage,
            pageSize:pageSize,
            recordssid:recordssid
        }
    };
    ajaxSubmitByJson(url,data,callbackgetPhreports);
}
function callbackgetPhreports(data){
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
function getPhreportsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=10;
        getPhreports_init(currPage,pageSize);
    }else if (len==2){
        getPhreports('',arguments[0],arguments[1]);
    }else if(len>2){
        getPhreports(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]);

    }
}
function showpagetohtml(){
    if(isNotEmpty(pageparam)){
        var pageSize=pageparam.pageSize;
        var pageCount=pageparam.pageCount;
        var currPage=pageparam.currPage;

        var phreportname=pageparam.phreportname;
        var starttime_start=pageparam.starttime_start;
        var starttime_end=pageparam.starttime_end;

        var arrparam=new Array();
        arrparam[0] = phreportname;
        arrparam[1]=starttime_start;
        arrparam[2]=starttime_end;
        showpage("paging",arrparam,'getPhreportsByParam',currPage,pageCount,pageSize);
    }
}


function delPhreport(ssid) {
    if (isNotEmpty(ssid)){
        layer.confirm('确定要删除该情绪报告吗', function(index){
            var url=getActionURL(getactionid_manage().getRecordById_delPhreport);
            var data={
                token:INIT_CLIENTKEY,
                param:{
                    ssid:ssid,
                    filebool:-1
                }
            };
            ajaxSubmitByJson(url,data,callbackdelPhreport);
            layer.close(index);
        });

    }
}
function callbackdelPhreport(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data.data)){
            layer.msg("删除成功",{icon: 6});
            getPhreportsByParam();
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}

function downfile(url,filename) {
    if (isNotEmpty(url)){
        var $a = $("<a></a>").attr("href", url).attr("download", filename);
        $a[0].click();
    }
}

function previewfile(url,filename) {
    if (!isNotEmpty(url)||url=="undefined"){
        layer.msg("未找到预览文件",{icon: 5});
        return;
    }
    if (isNotEmpty(url)) {
        layer.open({
            type:2,
            title:filename,
            content:url,
            shadeClose:true,
            area: ['50%', '700px'],
        });
    }

}

//*******************************************************************情绪报告的生成start****************************************************************//
function uploadPhreport() {
    layer.prompt({title: '请输入情绪报告名称', formType:0}, function(phreportname, index){
        //开始收集生成的数据
        var phreportdataList;
        var dataList=[];
        $("#recordreals input[type='checkbox']:checked").each(function(index, element){
            var html=$(this).closest("div");
            var username_time=$(html).find("#username_time").html();
            var translatext=$(html).find("#translatext").html();
            var ph_stress=$(html).find(".layui-badge").html();
            var usertype=$(html).attr("usertype");
            var dqphdate=$(html).attr("dqphdate");
            if (isNotEmpty(username_time)&&isNotEmpty(translatext)) {
                var content=null;
                if (usertype==1){
                    content = "<div style='text-align: left'><p style='color: #000000;font-size: 14px'>"+dqphdate+"</p><p style='color: #999;'>"+username_time+"  </p><span style='color: #fff; background: #0181cc;'>"+translatext+"</span></div>";
                }else  if (usertype==2) {
                    content = "<div style='text-align: right'><p style='color: #000000;font-size: 14px'>"+dqphdate+"</p><p style='color: #999'>"+ph_stress+"  "+username_time+" </p> <span  style='color: #fff; background: #ef8201;'>"+translatext+"</span></div>";
                }
                if (isNotEmpty(content)){
                    dataList.push(content);
                }
            }
        });

        if (!isNotEmpty(dataList)) {
            layer.msg("请先勾选需要生成的情绪数据");
            return;
        }

        var url=getActionURL(getactionid_manage().getRecordById_uploadPhreport);
        //需要收拾数据
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                phreportname:phreportname,
                phreportdataList:dataList
            }
        };
        ajaxSubmitByJson(url, data, callbackuploadPhreport);
        layer.close(index);
    });

}
function callbackuploadPhreport(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var  downurl=data.downurl;
            if (isNotEmpty(downurl)){
                var $a = $("<a></a>").attr("href", downurl).attr("download", "down");
                $a[0].click();
                layer.msg('情绪报告生成成功,等待下载中...',{icon:6});
                getPhreportsByParam();
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }
}
//*******************************************************************情绪报告的生成end****************************************************************//