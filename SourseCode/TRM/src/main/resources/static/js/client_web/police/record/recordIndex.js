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
        layer.msg(data.message,{icon: 5});
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
    var creatorbool=$("#creatorbool").prop("checked");
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            creatorbool:creatorbool,
            currPage:currPage,
            pageSize:pageSize
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecords);
}
function getRecords(recordtypessid,recordname,recordbool,creatorbool,currPage,pageSize){
     recordbool=$("#recordbool option:selected").val();//等于0的时候后台没有返回
    var url=getActionURL(getactionid_manage().recordIndex_getRecords);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordtypessid:recordtypessid,
            recordname:recordname,
            recordbool:recordbool,
            creatorbool:creatorbool,
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
                $("#recordtitle").attr("title",l.recordname==null?"笔录标题":l.recordname);
                recordssid_go=l.ssid;
                if (isNotEmpty(l)) {
                    var problems=l.problems;
                    if (isNotEmpty(problems)) {
                        var q="问：";
                        var w="答：";
                        var q_class="font_red_color";
                        if (gnlist.indexOf(FY_T) != -1){
                            q="";
                            w="";
                            q_class="";
                        }
                        for (var z = 0; z< problems.length;z++) {
                            var problem = problems[z];
                            var problemtext=problem.problem==null?"未知":problem.problem;
                            var problemhtml=' <tr><td class="'+q_class+'">'+q+''+problemtext+' </td></tr>';
                            if (gnlist.indexOf(FY_T)<0) {//法院版本不需要答
                                var answers=problem.answers;
                                if (isNotEmpty(answers)){
                                    for (var j = 0; j < answers.length; j++) {
                                        var answer = answers[j];
                                        var answertext=answer.answer==null?"未知":answer.answer;
                                        problemhtml+='<tr> <td class="font_blue_color">'+w+''+answertext+' </td></tr>';
                                    }
                                }else{
                                    problemhtml+='<tr> <td class="font_blue_color">'+w+'</td></tr>';
                                }
                            }
                            $("#recorddetail").append(problemhtml);
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message,{icon: 5});
    }

    var time = setInterval(function () {
        var trlen=$("#pagelisttemplates_tbody tr").length;
        if (null!=records&&trlen>0) {
            $("#pagelisttemplates_tbody tr").hover(function(){
                $("#hoverspan2",this).show();
                $("#hoverspan1",this).hide();
            },function(){
                $("#hoverspan2",this).hide();
                $("#hoverspan1",this).show();
            });
            clearInterval(time)
        }
    },100)

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
                $("#recordtitle").attr("title",l.recordname==null?"笔录标题":l.recordname);
                recordssid_go=l.ssid;
                var problems=l.problems;
                if (isNotEmpty(problems)) {
                    var q="问：";
                    var w="答：";
                    var q_class="font_red_color";
                    if (gnlist.indexOf(FY_T) != -1){
                        q="";
                        w="";
                        q_class="";
                    }
                    for (var z = 0; z< problems.length;z++) {
                        var problem = problems[z];
                        var problemtext=problem.problem==null?"未知":problem.problem;
                        var problemhtml=' <tr><td class="'+q_class+'">'+q+''+problemtext+' </td></tr>';
                        if (gnlist.indexOf(FY_T)<0) {//法院版本不需要答
                            var answers=problem.answers;
                            if (isNotEmpty(answers)){
                                for (var j = 0; j < answers.length; j++) {
                                    var answer = answers[j];
                                    var answertext=answer.answer==null?"未知":answer.answer;
                                    problemhtml+='<tr> <td class="font_blue_color">'+w+''+answertext+' </td></tr>';
                                }
                            }else{
                                problemhtml+='<tr> <td class="font_blue_color">'+w+' </td></tr>';
                            }
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

        var arrparam=new Array();
        arrparam[0]=recordtypessid;
        arrparam[1]=recordname;
        arrparam[2]=recordbool;
        arrparam[3]=creatorbool;
        showpage("paging",arrparam,'ggetRecordsByParam',currPage,pageCount,pageSize);
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
                var url=getActionURL(getactionid_manage().recordIndex_towaitconversation);
                window.location.href=url+"?ssid="+recordssid;
            } else if (multifunctionbool==2||multifunctionbool==3){
                if (gnlist.indexOf(FY_T)!=-1)
                {
                    //法庭跳转
                    var url=getActionURL(getactionid_manage().recordIndex_towaitCourt);
                    window.location.href=url+"?ssid="+recordssid;
                }else {
                    //其他跳转
                    var url=getActionURL(getactionid_manage().recordIndex_towaitRecord);
                    window.location.href=url+"?ssid="+recordssid;
                }
            }
        }else {
            layer.msg(creatorname+"正在制作笔录...")
        }
    } else  if (recordbool==2||recordbool==3){
        //进入回放界面：后期决定去掉快速回放界面
       /* if (multifunctionbool==1||multifunctionbool==2){
            var url=getActionURL(getactionid_manage().recordIndex_toconversationById);
            window.location.href=url+"?ssid="+recordssid;
        }else if(multifunctionbool==3){*/
            if (gnlist.indexOf(FY_T)!= -1)
            {
                //法庭跳转
                var url=getActionURL(getactionid_manage().recordIndex_togetCourtDetail);
                window.location.href=url+"?ssid="+recordssid;
            }else {
                //其他跳转
                var url=getActionURL(getactionid_manage().recordIndex_togetRecordById);
                window.location.href=url+"?ssid="+recordssid;
            }
        /*}*/
    }
}



function changeboolRecord(obj) {
    var recordssid=$(obj).closest("tr").attr("ssid");
    var $tr=$(obj).closest("tr");

    if (isNotEmpty(recordssid)){
        layer.confirm('确定要删除该笔录吗', function(index){
            var url=getActionURL(getactionid_manage().recordIndex_changeboolRecord);
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
                    layer.msg(data.message,{icon: 5});
                }
            });
            layer.close(index);
        });
    }

}




