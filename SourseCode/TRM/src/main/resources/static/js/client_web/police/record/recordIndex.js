var records=null;//笔录数据
var recordssid_go=null;//选择的笔录ssid



//笔录类型
function getRecordtypesindex() {
    var url=getActionURL(getactionid_manage().recordIndex_getRecordtypes);

    var data={
        token:INIT_CLIENTKEY,
        param:{}
    };
    ajaxSubmitByJson(url,data,callbackgetRecordtypes);
}
function callbackgetRecordtypes(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)) {
            var list = data.getRecordtypesVOParamList;
            $('#recordtypessid option').not(":lt(1)").remove();
            gets(list);
        }
    }else{
        layer.msg(data.message);
    }
}
 var len="-";
function gets(data) {
        if (isNotEmpty(data)){
            for (var i = 0; i < data.length; i++) {
                var l = data[i];
                if (l.pid!=0){
                    $("#recordtypessid").append("<option value='"+l.ssid+"' > |"+len+" "+l.typename+"</option>");
                }
                if (l.police_recordtypes.length>0){
                    len+=len;
                    gets(l.police_recordtypes);
                    len="-";
                }
            }
        }
    layui.use('form', function(){
        var form = layui.form;
        form.render();
    });
}



//笔录列表渲染
function getRecords_init(currPage,pageSize) {
    var url=getActionURL(getactionid_manage().recordIndex_getRecords);
    var recordtypessid=$("#recordtypessid option:selected").val();
    var recordname=$("#recordname").val();
    var recordbool=$("#recordbool option:selected").val();
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecords);
}
function getRecords(recordtypessid,recordname,recordbool,currPage,pageSize){
    var url=getActionURL(getactionid_manage().recordIndex_getRecords);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecords);
}
function callbackgetRecords(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        if (isNotEmpty(data)){
            records=data.data.pagelist;
            pageshow(data);
            $("#recordtitle").attr("recordtitle_first","false");
            $('#recorddetail').html("");
            var bool= $("#recordtitle").attr("recordtitle_first");
            if (bool=="true"){  return;}
            if (isNotEmpty(records)) {
                var l = records[0];
                $("#recordtitle").text(l.recordname==null?"笔录标题":l.recordname);
                $("#recordtitle").attr("recordtitle_first","true");
                recordssid_go=l.ssid;
                if (isNotEmpty(l)) {
                    var problems=l.problems;
                    if (isNotEmpty(problems)) {
                        for (var z = 0; z< problems.length;z++) {
                            var problem = problems[z];
                            var problemtext=problem.problem==null?"未知":problem.problem;
                            var problemhtml=' <tr><td class="font_red_color">问：'+problemtext+' </td></tr>';
                            var answers=problem.answers;
                            if (isNotEmpty(answers)){
                                for (var j = 0; j < answers.length; j++) {
                                    var answer = answers[j];
                                    var answertext=answer.answer==null?"未知":answer.answer;
                                    problemhtml+='<tr> <td class="font_blue_color">答：'+answertext+' </td></tr>';
                                }
                            }else{
                                problemhtml+='<tr> <td class="font_blue_color">答：</td></tr>';
                            }
                            $("#recorddetail").append(problemhtml);
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
}
function setproblems(recordssid,obj) {
    $('#recorddetail').html("");
    $("#pagelisttemplates_tbody tr").css({"background-color":" #fff"});
    if (isNotEmpty(recordssid)&&isNotEmpty(records)) {
        for (var i = 0; i < records.length; i++) {
            var l = records[i];
            if (l.ssid==recordssid){
                $(obj).css({"background-color":" #f2f2f2"});
                $("#recordtitle").text(l.recordname==null?"笔录标题":l.recordname);
                recordssid_go=l.ssid;
                var problems=l.problems;
                if (isNotEmpty(problems)) {
                    for (var z = 0; z< problems.length;z++) {
                        var problem = problems[z];
                        var problemtext=problem.problem==null?"未知":problem.problem;
                        var problemhtml=' <tr><td class="font_red_color">问：'+problemtext+' </td></tr>';
                        var answers=problem.answers;
                        if (isNotEmpty(answers)){
                            for (var j = 0; j < answers.length; j++) {
                                var answer = answers[j];
                                var answertext=answer.answer==null?"未知":answer.answer;
                                problemhtml+='<tr> <td class="font_blue_color">答：'+answertext+' </td></tr>';
                            }
                        }else{
                            problemhtml+='<tr> <td class="font_blue_color">答： </td></tr>';
                        }
                        $("#recorddetail").append(problemhtml);
                    }
                }
            }
        }
    }


}
function ggetRecordsByParam(){
    var len=arguments.length;
    if(len==0){
        var currPage=1;
        var pageSize=15;//测试
        getRecords_init(currPage,pageSize);
    }else if (len==2){
        getRecords('',arguments[0],arguments[1]);
    }else if(len>2){
        getRecords(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6],arguments[7]);
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

        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        arrparam[2]=recordbool;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
    }

}

//跳转笔录编辑页
function towaitRecord(recordssid,recordbool,creator) {
    if (!isNotEmpty(recordssid)){
        return false;
    }

    if (recordbool!=2){
        if (isNotEmpty(creator)&&creator==sessionadminssid){
            var url=getActionURL(getactionid_manage().recordIndex_towaitRecord);
            window.location.href=url+"?ssid="+recordssid;
        }else {
            layer.msg("笔录正在制作中...")
        }
    } else{
        var url=getActionURL(getactionid_manage().recordIndex_togetRecordById);
        window.location.href=url+"?ssid="+recordssid;
    }
}


