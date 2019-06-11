var templatessid=null;//模板ssid
var recordUserInfos;//询问人和被询问人数据
var td_lastindex={};//td的上一个光标位置  为0需要处理一下
var recorduser=[];//会议用户集合
var mcbool=null;//会议状态


//跳转变更模板页面
function opneModal_1() {
    var url=getActionURL(getactionid_manage().waitRecord_tomoreTemplate);

    var index = layer.open({
        type: 2,
        title:'变更模板',
        content:url,
        area: ['1000px', '680px'],
        btn: ['应用模板','导入该模板全部题目'],
        yes:function(index, layero){
           var editSsid = $(window.frames["layui-layer-iframe"+index])[0].editSsid;
            templatessid=editSsid;
            getTemplateById();
            layer.close(index);
        },
        btn2:function(index, layero){
            var editSsid = $(window.frames["layui-layer-iframe"+index])[0].editSsid;
            templatessid=editSsid;
              setAllproblem();
            layer.close(index);
        }
    });
}

//变更模板题目
function getTemplateById() {
    if (isNotEmpty(templatessid)){
        var url=getActionURL(getactionid_manage().waitRecord_getTemplateById);

        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: templatessid
            }
        };
        ajaxSubmitByJson(url, data, callTemplateById);
    }
}
function callTemplateById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var template=data.template;
            if (isNotEmpty(template)){
                var templateToProblems=template.templateToProblems;
               $("#templatetoproblem_html").html("");
                if (isNotEmpty(templateToProblems)) {
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'referanswer='"+templateToProblem.referanswer+"'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);
                    }
                }
              }
        }
    }else{
        layer.msg(data.message);
    }
}

//导入全部题目
function setAllproblem() {
    if (isNotEmpty(templatessid)){
        var url=getActionURL(getactionid_manage().waitRecord_getTemplateById);

        var data={
            token:INIT_CLIENTKEY,
            param:{
                ssid: templatessid
            }
        };
        ajaxSubmitByJson(url, data, callsetAllproblem);
    }
}
function callsetAllproblem(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var template=data.template;
            if (isNotEmpty(template)){
                var templateToProblems=template.templateToProblems;
                $("#templatetoproblem_html").html("");
                if (isNotEmpty(templateToProblems)) {
                    for (var i = 0; i < templateToProblems.length; i++) {
                        var templateToProblem = templateToProblems[i];
                        var html="<tr> <td ondblclick='copy_problems(this);'referanswer='"+templateToProblem.referanswer+"'>问：<span >"+templateToProblem.problem+"</span></td></tr>";
                        $("#templatetoproblem_html").append(html);

                        var html='<tr >\
                                <td style="padding: 0;width: 90%;" class="onetd" >\
                                    <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q"  onkeydown="qw_keydown(event);"  placeholder="'+templateToProblem.problem+'" >'+templateToProblem.problem+'</label></div>\
                                    <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);"  placeholder="'+templateToProblem.referanswer+'"></label></div>\
                                </td>\
                                <td style="float: right;">\
                                                                <div class="layui-btn-group">\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                                                                <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                                                                <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                                                                </div>\
                                                            </td>\
                                                            </tr>';
                        $("#recorddetail").append(html);
                    }
                    $("#recorddetail label").focus(function(){
                        td_lastindex["key"]=$(this).closest("tr").index();
                        td_lastindex["value"]=$(this).attr("name");
                    });
                }
            }
        }
    }else{
        layer.msg(data.message);
    }

}

//导入单个题目
function copy_problems(obj) {
    var text=$(obj).find("span").text();
    var w=$(obj).attr("referanswer");
    var html='<tr>\
        <td style="padding: 0;width: 90%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(event);"  class=""  placeholder="'+text+'" >'+text+'</label></div>\
            <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);"  class="" placeholder="'+w+'"></label></div>\
        </td>\
        <td style="float: right;">\
            <div class="layui-btn-group">\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
            <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete" ></i>删除</a>\
            </div>\
        </td>\
        </tr>';
    $("#recorddetail").append(html);
    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
}

//tr移动删除事件
function tr_remove(obj) {
     var bool=$(obj).parents("tr").attr("automaticbool");
     if (isNotEmpty(bool)&&bool==1){
         laststarttime_qq=-1;
         laststarttime_ww=-1;
         last_type=-1;//1问题 2是答案
         qq="";
         ww="";
         www="";
     }
    $(obj).parents("tr").remove();
}
function tr_up(obj) {
    var $tr = $(obj).parents("tr");
    $tr.prev().before($tr);
}
function tr_downn(obj) {
    var $tr = $(obj).parents("tr");
    $tr.next().after($tr);
}

//录音按钮显示隐藏 type:1开始录音
function img_bool(obj,type){
    if (type==1){
        //开始会议
        if (!isNotEmpty(mtssid)){
            startMC();
        }else{
            layer.msg("会议已关闭");
            return;
        }
    }else{
       //暂停录音
      /*  $(obj).css("display","none");
        $(obj).siblings().css("display","block");*/
    }
}

//粘贴语音翻译文本
var copy_text_html="";
var touchtime = new Date().getTime();


function copy_text(obj,event) {
    var text=$(obj).text();
    copy_text_html=text;
    var classc=$(obj).closest("div").attr("class");


    //鼠标左击右击
   if( new Date().getTime() - touchtime < 250 ){
        if(3 == event.which){
            $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').append(copy_text_html);
        }else if(1 == event.which){
            $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').append(copy_text_html);
        }
    }else{
        touchtime = new Date().getTime();
    }






    //字典定位问答
    /* if (classc=="btalk") {
         $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="w"]').append(copy_text_html);
     }else if(classc=="atalk"){
         $('#recorddetail tr:eq("'+td_lastindex["key"]+'") label[name="q"]').append(copy_text_html);
     }*/
    //当前下标问答
    /* $("#recorddetail label").each(function(){
         var lastindex=$(this).closest("tr").index();
         var value=$(this).attr("name");
         //当前下标问答
         if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
             $(this).append(copy_text_html);
         }
    });*/
    copy_text_html="";
    return false;

}





//编辑框下面按钮事件-------------------------------start
var currenttime;//当前时间
var yesterdaytime;//昨日时间
var occurrencetime_format;//案发时间
function btn(obj) {
    var selected=$(obj).closest("div[name='btn_div']").attr("showorhide");
    if (isNotEmpty(selected)&&selected=="false"){
        $("div[name='btn_div']").attr("showorhide","false");
        $("div[name='btn_div']").removeClass("layui-form-selected");
        $(obj).closest("div[name='btn_div']").attr("showorhide","true");
        $(obj).closest("div[name='btn_div']").addClass("layui-form-selected");
    }else if (isNotEmpty(selected)&&selected=="true") {
        $(obj).closest("div[name='btn_div']").attr("showorhide","false");
        $(obj).closest("div[name='btn_div']").removeClass("layui-form-selected");
    }
}
function get_case_time(obj) {

    if (isNotEmpty(occurrencetime_format)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(occurrencetime_format);
            }
        });
        btn(obj);
    }
}
function get_current_time(obj) {
    getTime();
    if (isNotEmpty(currenttime)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(currenttime);
            }
        });
        btn(obj);
    }
}
function get_yesterday_time(obj) {
    getTime();
    if (isNotEmpty(yesterdaytime)){
        $("#recorddetail label").each(function(){
            var lastindex=$(this).closest("tr").index();
            var value=$(this).attr("name");
            if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                $(this).append(yesterdaytime);
            }
        });
        btn(obj);
    }
}
//获取当期时间
function getTime() {
    var url=getActionURL(getactionid_manage().waitRecord_getTime);
    var data={
        token:INIT_CLIENTKEY,
    };
    ajaxSubmitByJson(url,data,callbackgetTime);
}
function callbackgetTime(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            currenttime=data.currenttime;
            yesterdaytime=data.yesterdaytime;
        }
    }else{
        layer.msg(data.message);
    }
}

//开启情绪分析
/*function random(lower, upper) {
    return Math.floor(Math.random() * (upper - lower)) + lower;
}
function openxthtml(obj) {
    var xtbool=$(obj).attr("xtbool");
    if (xtbool==1){
        layer.msg("情绪分析已开启");
        return;
    }else{
        layer.confirm('确定要启动情绪分析吗', {
            btn: ['确认','取消'], //按钮
            shade: [0.1,'#fff'], //不显示遮罩
        }, function(index){
            $("#xthtml").css("display","block");
            $(obj).attr("xtbool","1");
            var strings=["高度紧张","中度紧张","不很紧张","平平淡淡"];
            var ran=random(1,100);
            var t = setInterval(function (args) {
                $("#xthtml #xt1").html('<i class="layui-icon">&#xe67a;</i>'+strings[Math.floor(Math.random()*strings.length)]+'  ');
                $("#xthtml #xt2").html('<i class="layui-icon">&#xe6af;</i> '+random(1,100)+' ');
                $("#xthtml #xt3").html('<i class="layui-icon">&#xe69c;</i> '+random(1,100)+' ');
                $("#xthtml #xt4").html('<i class="layui-icon">&#xe6fc;</i> '+random(1,100)+'/分  ');
                $("#xthtml #xt5").html('<i class="layui-icon">&#xe62c;</i>'+random(1,100)+'mmHg  ');
            },2000);

            layer.close(index);
        }, function(index){
            layer.close(index);
        });
    }
}*/
//编辑框下面按钮事件-------------------------------end


//获取笔录信息
function getRecordById() {
    var url=getActionURL(getactionid_manage().waitRecord_getRecordById);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid:recordssid,
        }
    };
    ajaxSubmitByJson(url,data,callbackgetRecordById);
}
function callbackgetRecordById(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var record=data.record;
            if (isNotEmpty(record)){

                mcbool=record.mcbool;
                
                if (null!=mcbool&&mcbool==1){
                    $("#endrecord").css("display","none");
                    $("#endrecord").siblings().css("display","block");
                }

                //询问人和被询问人信息
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recordUserInfos=recordUserInfosdata;
                    var user1={
                        username:recordUserInfosdata.username
                        ,userssid:recordUserInfosdata.userssid
                        ,grade:2
                    };
                    var user2={
                        username:recordUserInfosdata.adminname
                        ,userssid:recordUserInfosdata.adminssid
                        ,grade:1
                    };
                    recorduser.push(user1);
                    recorduser.push(user2);
                }

                //问答
                var problems=record.problems;
                $("#recorddetail").html("");
                if (isNotEmpty(problems)) {
                    for (var z = 0; z< problems.length;z++) {
                        var problem = problems[z];
                        var problemtext=problem.problem==null?"未知":problem.problem;
                        var problemhtml= '<tr>\
                        <td style="padding: 0;width: 90%;" class="onetd">\
                            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(event);" placeholder="'+problemtext+'">'+problemtext+'</label></div>';
                        var answers=problem.answers;
                        if (isNotEmpty(answers)){
                            for (var j = 0; j < answers.length; j++) {
                                var answer = answers[j];
                                var answertext=answer.answer==null?"未知":answer.answer;
                                problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);" placeholder="'+answertext+'">'+answertext+'</label></div>';
                            }
                        }else{
                            problemhtml+='<div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);" placeholder=""></label></div>';
                        }
                        problemhtml+=' </td>\
                        <td style="float: right;">\
                            <div class="layui-btn-group">\
                            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                        <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                        <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                        </div>\
                        </td>\
                        </tr>';
                        $("#recorddetail").append(problemhtml);

                        $("#recorddetail label").focus(function(){
                            td_lastindex["key"]=$(this).closest("tr").index();
                            td_lastindex["value"]=$(this).attr("name");
                        });
                    }
                }

                //获取提讯会议ssid
                var police_arraignment=record.police_arraignment;
                if (isNotEmpty(police_arraignment)){
                    var mtssiddata=police_arraignment.mtssid;
                    if (isNotEmpty(mtssiddata)){
                        mtssid=mtssiddata;
                        getRecordrealing();
                    }
                }

            }
            //案件信息
            var caseAndUserInfo=data.caseAndUserInfo;
            $("#caseAndUserInfo_html").html("");
            if (isNotEmpty(caseAndUserInfo)){
                var occurrencetime_formatdata=caseAndUserInfo.occurrencetime_format;
                if (isNotEmpty(occurrencetime_formatdata)){
                    occurrencetime_format=occurrencetime_formatdata;
                }
                var  init_casehtml="<tr><td>案件名称</td><td>"+caseAndUserInfo.casename+"</td></tr>\
                                  <tr><td>案件人</td><td>"+caseAndUserInfo.username+"</td> </tr>\
                                  <tr><td>当前案由</td><td>"+caseAndUserInfo.cause+"</td></tr>\
                                  <tr><td>案件时间</td> <td>"+caseAndUserInfo.occurrencetime+"</td> </tr>\
                                  <tr><td>案件编号</td><td>"+caseAndUserInfo.casenum+"</td> </tr>\
                                  <tr><td>询问人一</td><td>"+recordUserInfosdata.adminname+"</td></tr>\
                                  <tr><td>询问人二</td> <td>"+recordUserInfosdata.otheradminname+"</td> </tr>\
                                  <tr><td>记录人</td><td>"+recordUserInfosdata.recordadminname+"</td> </tr>";
                $("#caseAndUserInfo_html").html(init_casehtml);
            }
        }
    }else{
        layer.msg(data.message);
    }
}


//开始会议
var mtssid=null;//会议ssid
var useretlist=null;
function startMC() {
    $("#endrecord").css("display","none");
    $("#endrecord").siblings().css("display","block");
    if (isNotEmpty(recordUserInfos)){
        var tdList=[];
        var user1={
            username:recordUserInfos.username
            ,userssid:recordUserInfos.userssid
            ,grade:2 //1主麦，2副麦，有时需要一些特殊的处理(主麦只有一个)
        };
        var user2={
            username:recordUserInfos.adminname
            ,userssid:recordUserInfos.adminssid
            ,grade:1
        };

        tdList.push(user1);
        tdList.push(user2);

        var url="/v1/police/out/startRercord";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                meetingtype: 2       //会议类型，1视频/2音频
                ,tdList:tdList
                ,recordssid:recordssid
            }
        };
        ajaxSubmitByJson(url, data, callbackstartMC);
    }else {
        layer.msg("参数为空");
    }
}
function callbackstartMC(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            var asrnum=data.asrnum;
            var mtssiddata=data.mtssid;
            var polygraphnum=data.polygraphnum;
            var recordnum=data.recordnum;
            useretlist=data.useretlist;
            if (isNotEmpty(useretlist)){
                for (var i = 0; i < useretlist.length; i++) {
                    var useret = useretlist[i];
                    var userssid1=useret.userssid;
                    for (var j = 0; j < recorduser.length; j++) {
                        var u = recorduser[j];
                        var userssid2=u.userssid;
                        if (userssid1==userssid2) {
                            var grade=u.grade;
                            if (1==grade){
                                liveurl=useret.livingurl;
                                console.log("liveurl_____"+liveurl)
                                break;
                            }
                        }
                    }
                }
            }

            mtssid=mtssiddata;
          /*  updateArraignment();*/
            layer.msg("会议已开启");
        }
    }else{
        layer.msg(data.message);
    }
}

//结束会议
function overMC() {
    if (isNotEmpty(recordssid)){
        var url="/v1/police/out/overRercord";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid:mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackoverMC);
    }
}
function callbackoverMC(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("overMC(返回结果_"+data);
            if (data){
                mtssid=null;//会议ssid
                 layer.msg("会议已关闭");
            }
        }
    }else{
        layer.msg(data.message);
    }
}

//保存按钮
//recordbool 1进行中 2已结束
function addRecord(recordbool) {
    if (isNotEmpty(recordssid)){
        var url=getActionURL(getactionid_manage().waitRecord_addRecord);

        //需要收拾数据
        var recordToProblems=[];//题目集合
        $("#recorddetail td.onetd").each(function (i) {
            var arr={};
            var answers=[];//答案集合
            var q=$(this).find("label[name='q']").text();
            q=q.replace(/\s/g,'');
                    //经过筛选的q
                    var ws=$(this).find("label[name='w']");
                    if (isNotEmpty(q)){
                        if (null!=ws&&ws.length>0){
                            for (var j = 0; j < ws.length; j++) {
                                var w =ws.eq(j).text();
                                w=w.replace(/\s/g,'');
                                        //经过筛选的w
                                        answers.push({
                                            answer:w
                                        });
                            }
                        }
                        recordToProblems.push({
                            problem:q,
                            answers:answers
                        });
                    }
        });
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                recordbool:recordbool,
                recordToProblems:recordToProblems
            }
        };
        ajaxSubmitByJson(url, data, calladdRecord);
        if (recordbool==2) {
            overMC();//结束会议
            window.history.go(-1);
        }
    }else{
        layer.msg("系统异常");
    }
}
function calladdRecord(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg('保存成功', {
                btn: ['去查看', '继续编辑'],
                yes:function(index){
                    parent.document.getElementById("record_select").click();
                    layer.close(index);
                }, btn2:function(index){
                    layer.close(index);
                    return false;
                }});
        }
    }else{
        layer.msg(data.message);
    }
}

//结束笔录按钮
function overRecord() {
    layer.confirm('是否结束笔录', {
        btn: ['确认','取消'], //按钮
        shade: [0.1,'#fff'], //不显示遮罩
    }, function(index){
        addRecord(2);//保存问答信息

        layer.close(index);
    }, function(index){

        layer.close(index);
    });
}


//导出word
function exportWord(obj){
    var url=getActionURL(getactionid_manage().waitRecord_exportWord);
    var paramdata={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
        }
    };
    ajaxSubmitByJson(url, paramdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                window.location.href = data;
                layer.msg("导出成功,等待下载中...");
            }
        }else{
            layer.msg("导出失败");
        }
        btn(obj);
    });
}

function exportPdf(obj) {
    var url=getActionURL(getactionid_manage().waitRecord_exportPdf);
    var paramdata={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid,
        }
    };
    ajaxSubmitByJson(url, paramdata, function (data) {
        if(null!=data&&data.actioncode=='SUCCESS'){
            var data=data.data;
            if (isNotEmpty(data)){
                window.location.href = data;
                layer.msg("导出成功,等待下载中...");
            }
        }else{
            layer.msg("导出失败");
        }
        btn(obj);
    });
}

//修改提讯数据（注入会议ssid）
/*
function updateArraignment() {
    if (isNotEmpty(mtssid)){
        var url=getActionURL(getactionid_manage().waitRecord_updateArraignment);
        var data={
            token:INIT_CLIENTKEY,
            cmparam:{
                recordssid: recordssid,
                mtssid:mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackupdateArraignment);
    }
}
function callbackupdateArraignment(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            layer.msg("会议已开启");
        }
    }else{
        layer.msg(data.message);
    }
}
*/


/**
 * 获取会议实时数据
 */
function getRecordrealing() {
    if (isNotEmpty(mtssid)) {
        var url="/v1/police/out/getRecordrealing";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetgetRecordrealing);
    }
}
function callbackgetgetRecordrealing(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var datas = data.data;
        var loadindex = layer.msg("加载中，请稍等...", {
            icon: 16,
            time:1000
        });

        var list= datas.list;
        var fdCacheParams= datas.fdCacheParams;
        if (isNotEmpty(fdCacheParams)){
            for (var i = 0; i < fdCacheParams.length; i++) {
                var fdCacheParam = fdCacheParams[i];
                liveurl=fdCacheParam.livingUrl;
            }
        }


        if (isNotEmpty(list)) {
            layer.close(loadindex);
            for (var i = 0; i < list.length; i++) {
                var data=list[i];
                if (isNotEmpty(recorduser)){
                    for (var j = 0; j < recorduser.length; j++) {
                        var user = recorduser[j];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";
                            //实时会议数据
                            if (usertype==1){
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+' </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }else if (usertype==2){
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").remove();
                            }

                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals');
                            div.scrollTop = div.scrollHeight;
                        }
                    }
                }
            }
        }
    }else{
        layer.msg(data.message);
    }
}

/**
 * 监测数据
 */
function getPolygraphdata() {
    if (isNotEmpty(mtssid)) {
        var url="/v1/police/out/getPolygraphdata";
        var data={
            token:INIT_CLIENTKEY,
            param:{
                mtssid: mtssid
            }
        };
        ajaxSubmitByJson(url, data, callbackgetPolygraphdata);
    }else{
        console.log("身心监测信息未找到__"+mtssid);
    }
}
function callbackgetPolygraphdata(data) {
    if(null!=data&&data.actioncode=='SUCCESS') {
        var data=data.data;
        if (isNotEmpty(data)){
            var obj=data.t;
            if (isNotEmpty(obj)) {
                myChart.hideLoading();
                var hr=obj.hr.toFixed(0)==null?0:obj.hr.toFixed(0);//心率
                var br=obj.br.toFixed(0)==null?0:obj.br.toFixed(0);//呼吸次数
                var  status=obj.status;
                var status_text="未知";
                if (status==0){
                    status_text="正常";
                }else if (status==1){
                    status_text="紧张";
                }else if (status==2){
                    status_text="生理疲劳";
                }else if (status==3){
                    status_text="昏昏欲睡";
                }
                var relax=obj.relax.toFixed(0)==null?0:obj.relax.toFixed(0);
                var stress=obj.stress.toFixed(0)==null?0:obj.stress.toFixed(0);
                var bp=obj.bp.toFixed(0)==null?0:obj.bp.toFixed(0);
                var spo2=obj.spo2.toFixed(0)==null?0:obj.spo2.toFixed(0);
                var hrv=obj.hrv.toFixed(0)==null?0:obj.hrv.toFixed(0);

                $("#xthtml #xt1").html(' '+status_text+'  | ');
                $("#xthtml #xt2").html(' '+relax+' | ');
                $("#xthtml #xt3").html(' '+stress+' | ');
                $("#xthtml #xt4").html(' '+bp+' | ');
                $("#xthtml #xt5").html(' '+spo2+'  ');
               $("#xthtml #xt6").html(' '+hr+' | ');
                $("#xthtml #xt7").html(' '+hrv+' | ');
                $("#xthtml #xt8").html(' '+br+' | ');

                var hr_snr=obj.hr_snr.toFixed(0)==null?0:obj.hr_snr.toFixed(0);;
                if (isNotEmpty(hr_snr)&&hr_snr>0.1){
                    $("#showmsg").css({"color": "#3c763d","background-color":"#dff0d8","border-color":"#d6e9c6"});
                    $("#showmsg strong").text("心率准确监测中");
                }else{
                    $("#showmsg").css({"color": "#a94442","background-color":"#f2dede","border-color":"#ebccd1"});
                    $("#showmsg strong").text("心率监测不准确");
                }

                addData_hr(true,hr);
                addData_hrv(true,hrv);
                addData_br(true,br);
                addData_relax(true,relax);
                addData_stress(true,stress);
                addData_bp(true,bp);
                addData_spo2(true,spo2);


                $("#monitor_btn span").each(function (e) {
                    var type=$(this).attr("type");
                    var name=$(this).text();
                    var isn=$(this).attr("isn");
                    if (isn==1){
                        if (type=="hr") {
                            date1=date_hr;
                            data1=data_hr;
                        }else if (type=="hrv") {
                            date1=date_hrv;
                            data1=data_hrv;
                        }else if (type=="br") {
                            date1=date_br;
                            data1=data_br;
                        }else if (type=="relax") {
                            date1=date_relax;
                            data1=data_relax;
                        }else if (type=="stress") {
                            date1=date_stress;
                            data1=data_stress;
                        }else if (type=="bp") {
                            date1=date_bp;
                            data1=data_bp;
                        }else if (type=="spo2") {
                            date1=date_spo2;
                            data1=data_spo2;
                        }
                    }
                });
                myChart.setOption({
                    xAxis: {
                        data: date1
                    },
                    series: [{
                        data: data1
                    }]
                });
            }
        }
    }else{
        layer.msg(data.message);
    }
}



/**
 * 视频地址切换 type 1主麦 type 2副麦
 */

function select_liveurl(obj,type){
    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    if (isNotEmpty(useretlist)){
        for (var i = 0; i < useretlist.length; i++) {
            var useret = useretlist[i];
            var userssid1=useret.userssid;
            for (var j = 0; j < recorduser.length; j++) {
                var u = recorduser[j];
                var userssid2=u.userssid;
                if (userssid1==userssid2) {
                    var grade=u.grade;
                    if (type==grade){
                        liveurl=useret.livingurl;
                        console.log("liveurl_____"+liveurl)
                        break;
                    }
                }
            }
        }
    }
    initplayer();
}

/**
 * 身心监测按钮组
 */
function select_monitor(obj) {
    $(obj).attr("isn","1");
    $(obj).siblings().attr("isn","-1");

    $(obj).removeClass("layui-bg-gray");
    $(obj).siblings().addClass("layui-bg-gray");
    myChart.showLoading();
    var name=$(obj).text();
    myChart.setOption({
        title: {
            text: name,
        },
        series: [{
            name:name,
        }]
    });
}

function qw_keydown(event) {
    var e = event || window.event;
    if (e && e.keyCode == 13) { //回车键的键值为13
        event.preventDefault();
    }
}


var datadata={};

var laststarttime_qq=-1;
var laststarttime_ww=-1;
var last_type=-1;//1问题 2是答案
var qq="";
var ww="";
var www="";
$(function () {
    //回车加trtd
    var trtd_html='<tr>\
        <td style="padding: 0;width: 90%;" class="onetd">\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(event);" ></label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);" placeholder=""></label></div>\
                </td>\
                <td style="float: right;">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                    </div>\
                </td>\
                </tr>';
    document.onkeydown = function (event) {
        var e = event || window.event;
        if (e && e.keyCode == 13) { //回车键的键值为13
            $("#recorddetail").append(trtd_html);
            $("#recorddetail label").focus(function(){
                td_lastindex["key"]=$(this).closest("tr").index();
                td_lastindex["value"]=$(this).attr("name");
            });
        }
    };



    $("#dl_dd dd").click(function () {
        var text=$(this).attr('lay-value');
        //文本
        $("#recorddetail label").each(function(){
                var lastindex=$(this).closest("tr").index();
                var value=$(this).attr("name");
                if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
                    $(this).append(text);
                }
        });
        btn(this);
    })

    var defaults = {}
        , one_second = 1000
        , one_minute = one_second * 60
        , one_hour = one_minute * 60
        , one_day = one_hour * 24
        , startDate = new Date()
        , face = document.getElementById('jishi');

    var requestAnimationFrame = (function() {
        return window.requestAnimationFrame       ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame    ||
            window.oRequestAnimationFrame      ||
            window.msRequestAnimationFrame     ||
            function( callback ){
                window.setTimeout(callback, 1000 / 60);
            };
    }());

    tick();
    function tick() {

        var now = new Date()
            , elapsed = now - startDate
            , parts = [];

        parts[0] = '' + Math.floor( elapsed / one_hour );
        parts[1] = '' + Math.floor( (elapsed % one_hour) / one_minute );
        parts[2] = '' + Math.floor( ( (elapsed % one_hour) % one_minute ) / one_second );

        parts[0] = (parts[0].length == 1) ? '0' + parts[0] : parts[0];
        parts[1] = (parts[1].length == 1) ? '0' + parts[1] : parts[1];
        parts[2] = (parts[2].length == 1) ? '0' + parts[2] : parts[2];

        face.innerText = parts.join(':');

        requestAnimationFrame(tick);

    }
    nowdate();
    function nowdate() {
        var monthNames = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" ];
        var dayNames= ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
        var newDate = new Date();
        newDate.setDate(newDate.getDate());
        $('#Date').html(newDate.getFullYear() + "年" + monthNames[newDate.getMonth()] + '月' + newDate.getDate() + '日 ' + dayNames[newDate.getDay()]);

        setInterval( function() {
            var seconds = new Date().getSeconds();
            $("#sec").html(( seconds < 10 ? "0" : "" ) + seconds);

            var minutes = new Date().getMinutes();
            $("#min").html(( minutes < 10 ? "0" : "" ) + minutes);

            var hours = new Date().getHours();
            $("#hours").html(( hours < 10 ? "0" : "" ) + hours);
        },1000);
    }


    // 建立连接
    if (isNotEmpty(SOCKETIO_HOST)&&isNotEmpty(SOCKETIO_PORT)) {

        socket = io.connect('http://'+SOCKETIO_HOST+':'+SOCKETIO_PORT+'');
        socket.on('connect', function (data) {
            console.log("连接成功__");
        });
        socket.on('disconnect', function (data) {
            console.log("断开连接__");
        });



        socket.on('getback', function (data) {
            var mtssiddata=data.mtssid;
            if (isNotEmpty(mtssiddata)&&isNotEmpty(mtssid)&&mtssiddata==mtssid) {
                if (isNotEmpty(recorduser)){
                    for (var i = 0; i < recorduser.length; i++) {
                        var user = recorduser[i];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var starttime=data.starttime;
                            var asrstartime=data.asrstartime;
                            var recordrealshtml="";


                            //实时会议数据
                            if (usertype==1){
                                recordrealshtml='<div class="atalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrstartime+' </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }else if (usertype==2){
                                recordrealshtml='<div class="btalk" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>'+asrstartime+' 【'+username+'】 </p>\
                                                            <span onmousedown="copy_text(this,event)" >'+translatext+'</span> \
                                                      </div >';
                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").remove();
                            }
                            $("#recordreals").append(recordrealshtml);
                            var div = document.getElementById('recordreals');
                            div.scrollTop = div.scrollHeight;



                            //检测自动上墙是否开启
                            var record_switch_bool=$("#record_switch_bool").prop("checked");
                            if (record_switch_bool){
                                if (last_type==-1){
                                    if (usertype==1){
                                        qq+=translatext;
                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        datadata["q"]=qq;
                                        datadata["w"]=ww;
                                        setrecord_html();
                                    }
                                }else  if (last_type==1){//最后是问
                                    last_type=usertype;
                                    if (usertype==1){//最后是问，本次是问，判断本次问和最后一次问的时间是否一致，一致问刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_qq==starttime||laststarttime_qq==-1){
                                            qq="";//清空q
                                            qq+=translatext;
                                            laststarttime_qq=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='q']").text(qq);
                                        }else{

                                            //2.初始化问答
                                            laststarttime_qq=-1;
                                            laststarttime_ww=-1;
                                            last_type=-1;//1问题 2是答案
                                            qq="";
                                            ww="";
                                            www="";
                                            qq+=translatext;
                                            last_type=usertype;
                                            laststarttime_qq=starttime;
                                            datadata["q"]=qq;
                                            datadata["w"]=ww;
                                            setrecord_html();
                                        }
                                    }else if (usertype==2){//最后是问，本次是答，开始拼接答案
                                        ww+=translatext;
                                        laststarttime_ww=starttime;
                                        $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(ww);
                                    }
                                }else  if (last_type==2){//最后是答
                                    last_type=usertype;
                                    if (usertype==2){//最后是答，本次确实答，判断本次答和最后一次答的时间是否一致，一致刷新，不一致开始追加问答，并且初始化数据
                                        if (laststarttime_ww==starttime||laststarttime_ww==-1){
                                            ww="";
                                            ww+=translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(www+ww);
                                         }else{
                                            var labletext= $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text();
                                            www=labletext;
                                            ww=www+translatext;
                                            laststarttime_ww=starttime;
                                            $("#recorddetail tr[automaticbool='1'] td:first label[name='w']").text(ww);
                                         }

                                    }else if (usertype==1){//最后是答，本次为问

                                        //2.初始化问答
                                        laststarttime_qq=-1;
                                        laststarttime_ww=-1;
                                        last_type=-1;//1问题 2是答案
                                        qq="";
                                        ww="";
                                        www="";
                                        qq+=translatext;
                                        last_type=usertype;
                                        laststarttime_qq=starttime;
                                        datadata["q"]=qq;
                                        datadata["w"]=ww;
                                        setrecord_html();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }else{
        console.log("socket连接失败")
    }


    $(window).on('pagehide', function(event) {
        addRecord(1);
    });
    
    window.setInterval(function (args) {
        if (isNotEmpty(mtssid)) {
            getPolygraphdata();
        }
    },1000);


    layui.use(['layer','form'], function(){
        var $ = layui.$ //由于layer弹层依赖jQuery，所以可以直接得到
            ,form = layui.form;

        form.on('switch(automatic_record)', function(switchdata){
            var obj=switchdata.elem.checked;


            var con;
            var switch_bool;
            if (obj) {
                con="确定要开启自动甄别吗";
                switch_bool=1;
            }else{
                con="确定要关闭自动甄别吗";
                switch_bool=-1;
            }
            layer.open({
                content:con
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    switchdata.elem.checked=obj;
                    form.render();
                    if (switch_bool==1) {
                        layer.msg("自动甄别已开启")
                    }else{
                        layer.msg("自动甄别已关闭")
                    }
                    layer.close(index);
                }
                ,btn2: function(index, layero){
                    switchdata.elem.checked=!obj;

                    form.render();
                    layer.close(index);
                }
                ,cancel: function(){
                    //右上角关闭回调
                    switchdata.elem.checked=!obj;
                    form.render();
                }
            });
        });
    });

});

//自动上墙
function setrecord_html() {
    $("#recorddetail tr").attr("automaticbool","");
    var trtd_html='<tr automaticbool="1">\
        <td style="padding: 0;width: 90%;" class="onetd" >\
            <div class="table_td_tt font_red_color"><span>问：</span><label contenteditable="true" name="q" onkeydown="qw_keydown(event);" >'+datadata["q"]+'</label></div>\
              <div class="table_td_tt font_blue_color"><span>答：</span><label contenteditable="true" name="w" onkeydown="qw_keydown(event);" placeholder="">'+datadata["w"]+'</label></div>\
                </td>\
                <td style="float: right;">\
                    <div class="layui-btn-group">\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_up(this);"><i class="layui-icon layui-icon-up"></i></button>\
                    <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="tr_downn(this);"><i class="layui-icon layui-icon-down"></i></button>\
                    <a class="layui-btn layui-btn-danger layui-btn-xs" style="margin-right: 10px;" lay-event="del" onclick="tr_remove(this);"><i class="layui-icon layui-icon-delete"></i>删除</a>\
                    </div>\
                </td>\
                </tr>';
    $("#recorddetail").append(trtd_html);
    $("#recorddetail label").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
}

/*******************************图表区域*************************/
var myChart;
var date1 = [];
var data1 = [];

var init_br = 1;
var date_br = [];
var data_br = [];
function addData_br(shift,data) {
    init_br++;
    date_br.push(init_br);
    data_br.push(data);
    if (shift) {
        date_br.shift();
        data_br.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_br(false,0);
}

var init_bp = 1;
var date_bp = [];
var data_bp = [];
function addData_bp(shift,data) {
    init_bp++;
    date_bp.push(init_bp);
    data_bp.push(data);
    if (shift) {
        date_bp.shift();
        data_bp.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_bp(false,0);
}

var init_hr = 1;
var date_hr = [];
var data_hr = [];
function addData_hr(shift,data) {
    init_hr++;
    date_hr.push(init_hr);
    data_hr.push(data);
    if (shift) {
        date_hr.shift();
        data_hr.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hr(false,0);
}

date1=date_hr;
data1=data_hr;
var init_hrv = 1;
var date_hrv = [];
var data_hrv = [];
function addData_hrv(shift,data) {
    init_hrv++;
    date_hrv.push(init_hrv);
    data_hrv.push(data);
    if (shift) {
        date_hrv.shift();
        data_hrv.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_hrv(false,0);
}

var init_relax = 1;
var date_relax = [];
var data_relax = [];
function addData_relax(shift,data) {
    init_relax++;
    date_relax.push(init_relax);
    data_relax.push(data);
    if (shift) {
        date_relax.shift();
        data_relax.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_relax(false,0);
}

var init_spo2 = 1;
var date_spo2 = [];
var data_spo2 = [];
function addData_spo2(shift,data) {
    init_spo2++;
    date_spo2.push(init_spo2);
    data_spo2.push(data);
    if (shift) {
        date_spo2.shift();
        data_spo2.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_spo2(false,0);
}

var init_stress = 1;
var date_stress = [];
var data_stress = [];
function addData_stress(shift,data) {
    init_stress++;
    date_stress.push(init_stress);
    data_stress.push(data);
    if (shift) {
        date_stress.shift();
        data_stress.shift();
    }
}
for (var i = 1; i < 50; i++) {
    addData_stress(false,0);
}







function main1() {
    $("#main1").css( 'width',$(".layui-tab-title").width() );
    $(window).resize(function() {
        myChart.resize();
    });
    myChart = echarts.init(document.getElementById('main1'),'dark');
    var option = {
        title: {
            text: '心率',
        },
        tooltip: {
            trigger: 'axis',
            formatter: '{a}: {c}'
        },
        xAxis: {
            type: 'category',
            splitLine: {
                show: false
            },
            show: false,
            data: date1
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            },
            show: true
        },
        grid: {
            x:30,
            y:45,
            x2:30,
            y2:10,
        },
        series: [{
            name: '心率',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            itemStyle : {
                normal : {
                    color:'#00FF00',
                    lineStyle:{
                        color:'#00FF00'
                    }
                }
            },
            data: data1
        }]
    };
    myChart.setOption(option);
}



