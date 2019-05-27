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

                        var html='<tr>\
                                <td style="padding: 0;width: 90%;" class="onetd font_red_color" name="1">\
                                    <p contenteditable="true" name="q"  class="table_td_tt font_red_color">问：'+templateToProblem.problem+'</p>\
                                    <p contenteditable="true" name="w"  class="table_td_tt font_blue_color">答：'+templateToProblem.referanswer+'</p>\
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
                    $("#recorddetail p").focus(function(){
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
        <td style="padding: 0;width: 90%;" class="onetd font_red_color">\
            <p contenteditable="true" name="q"  class="table_td_tt font_red_color">问：'+text+'</p>\
            <p contenteditable="true" name="w"  class="table_td_tt font_blue_color">答：'+w+'</p>\
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
    $("#recorddetail p").focus(function(){
        td_lastindex["key"]=$(this).closest("tr").index();
        td_lastindex["value"]=$(this).attr("name");
    });
}

//tr移动删除事件
function tr_remove(obj) {
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
            startMC();
            $(obj).css("display","none");
            $(obj).siblings().css("display","block");
    }else{
       //暂停录音
      /*  $(obj).css("display","none");
        $(obj).siblings().css("display","block");*/
    }
}

//粘贴语音翻译文本
var copy_text_html="";
function copy_text(obj) {
    var text=$(obj).text();
    copy_text_html=text;

    $("#recorddetail p").each(function(){
        var lastindex=$(this).closest("tr").index();
        var value=$(this).attr("name");
        if (lastindex==td_lastindex["key"]&&value==td_lastindex["value"]) {
            $(this).append(copy_text_html);
            copy_text_html="";
        }
    });
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
        $("#recorddetail p").each(function(){
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
        $("#recorddetail p").each(function(){
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
        $("#recorddetail p").each(function(){
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
function random(lower, upper) {
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
}
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
                        <td style="padding: 0;width: 90%;" class="onetd font_red_color" name="1">\
                            <p contenteditable="true" name="q" class="table_td_tt font_red_color">问：'+problemtext+'</p>';
                        var answers=problem.answers;
                        if (isNotEmpty(answers)){
                            for (var j = 0; j < answers.length; j++) {
                                var answer = answers[j];
                                var answertext=answer.answer==null?"未知":answer.answer;
                                problemhtml+='<p contenteditable="true" name="w" class="table_td_tt font_blue_color">答：'+answertext+'</p>';
                            }
                        }else{
                            problemhtml+='<p contenteditable="true" name="w" class="table_td_tt font_blue_color">答：...</p>';
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
            if (isNotEmpty(caseAndUserInfo)){
                var occurrencetime_formatdata=caseAndUserInfo.occurrencetime_format;
                if (isNotEmpty(occurrencetime_formatdata)){
                    occurrencetime_format=occurrencetime_formatdata;
                }
            }


        }
    }else{
        layer.msg(data.message);
    }
}


//开始会议
var mtssid=null;//会议ssid
function startMC() {

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
                ,tdList:tdList,
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
            console.log("startMC返回结果_"+data);
            var asrnum=data.asrnum;
            var mtssiddata=data.mtssid;
            var polygraphnum=data.polygraphnum;
            var recordnum=data.recordnum;

            mtssid=mtssiddata;
            updateArraignment();
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
            var q=$(this).find("p[name='q']").text();
            q=q.replace(/\s/g,'');
            if (q.length>2){
                var str = q.substring(0,2);
                if (str=="问："||str=="问:") {
                    q = q.substring(2);
                    //经过筛选的q
                    var ws=$(this).find("p[name='w']");
                    if (isNotEmpty(q)){
                        if (null!=ws&&ws.length>0){
                            for (var j = 0; j < ws.length; j++) {
                                var w =ws.eq(j).text();
                                w=w.replace(/\s/g,'');
                                if (w.length>2){
                                    var str1 = w.substring(0,2);
                                    if (str1=="答："||str1=="答:") {
                                        w = w.substring(2);
                                        //经过筛选的w
                                        answers.push({
                                            answer:w
                                        });
                                    }
                                }
                            }
                        }
                        recordToProblems.push({
                            problem:q,
                            answers:answers
                        });
                    }
                }
            }
        })
        var data={
            token:INIT_CLIENTKEY,
            param:{
                recordssid: recordssid,
                recordbool:recordbool,
                recordToProblems:recordToProblems
            }
        };
        ajaxSubmitByJson(url, data, calladdRecord);
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
        overMC();//结束会议
        window.history.go(-1);
        layer.close(index);
    }, function(index){

        layer.close(index);
    });
}


//导出word
function exportWord(){
    var url=getActionURL(getactionid_manage().waitRecord_exportWord);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            recordssid: recordssid
        }
    };
    ajaxSubmitByJson(url, data, callbackexportWord);
}
function callbackexportWord(data){
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){

        }
    }else{
        layer.msg(data.message);
    }
}


//修改提讯数据（注入会议ssid）
function updateArraignment() {
    if (isNotEmpty(mtssid)){
        var url=getActionURL(getactionid_manage().waitRecord_updateArraignment);
        var data={
            token:INIT_CLIENTKEY,
            param:{
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
        if (isNotEmpty(datas)) {
            layer.close(loadindex);
            for (var i = 0; i < datas.length; i++) {
                var data=datas[i];
                if (isNotEmpty(recorduser)){
                    for (var j = 0; j < recorduser.length; j++) {
                        var user = recorduser[j];
                        var userssid=user.userssid;
                        if (data.userssid==userssid){
                            var username=user.username==null?"未知":user.username;//用户名称
                            var usertype=user.grade;//1、询问人2被询问人
                            var translatext=data.txt==null?"...":data.txt;//翻译文本
                            var asrtime=data.asrtime;//时间
                            var starttime=data.starttime;
                            //实时会议数据
                            if (usertype==1){
                                recordrealclass="atalk";

                            }else if (usertype==2){
                                recordrealclass="btalk";

                            }
                            var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                            if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                                $("#recordreals div[userssid="+userssid+"]:last").remove();
                            }
                            var recordrealshtml='<div class="'+recordrealclass+'" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrtime+'</p>\
                                                            <span ondblclick="copy_text(this)" >'+translatext+'</span> \
                                                      </div >';

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

$(function () {
    //回车加trtd
    var trtd_html='<tr>\
        <td style="padding: 0;width: 90%;" class="onetd font_red_color" name="1">\
            <p contenteditable="true" name="q"  class="table_td_tt font_red_color">问：</p>\
            <p contenteditable="true" name="w"  class="table_td_tt font_blue_color">答：</p>\
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
            $("#recorddetail p").focus(function(){
                td_lastindex["key"]=$(this).closest("tr").index();
                td_lastindex["value"]=$(this).attr("name");
            });
        }
    };


    $("#dl_dd dd").click(function () {
        var text=$(this).attr('lay-value');
        //文本
        $("#recorddetail p").each(function(){
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


    // 建立连接
    socket = io.connect('http://192.168.17.178:8888');

    socket.on('connect', function (data) {
        console.log("连接成功__");
    });
    socket.on('disconnect', function (data) {
        console.log("断开连接__");
    });
    socket.on('getback', function (data) {
        if (isNotEmpty(recorduser)){
            for (var i = 0; i < recorduser.length; i++) {
                var user = recorduser[i];
                var userssid=user.userssid;
                if (data.userssid==userssid){
                    var username=user.username==null?"未知":user.username;//用户名称
                    var usertype=user.grade;//1、询问人2被询问人
                    var translatext=data.txt==null?"...":data.txt;//翻译文本
                    var asrtime=data.asrtime;//时间
                    var starttime=data.starttime;
                    //实时会议数据
                    if (usertype==1){
                        recordrealclass="atalk";

                    }else if (usertype==2){
                        recordrealclass="btalk";

                    }

                    var laststarttime =$("#recordreals div[userssid="+userssid+"]:last").attr("starttime");
                    if (laststarttime==starttime&&isNotEmpty(laststarttime)){
                        $("#recordreals div[userssid="+userssid+"]:last").remove();
                    }
                    var recordrealshtml='<div class="'+recordrealclass+'" userssid='+userssid+' starttime='+starttime+'>\
                                                            <p>【'+username+'】 '+asrtime+'</p>\
                                                            <span ondblclick="copy_text(this)" >'+translatext+'</span> \
                                                      </div >';

                    $("#recordreals").append(recordrealshtml);
                    var div = document.getElementById('recordreals');
                    div.scrollTop = div.scrollHeight;
                }
            }
        }
    });

    $(window).on("unload", function(event){
        addRecord(1);
    });
    $(window).on('beforeunload', function(event) {
        addRecord(1);
    });
    $(window).on('pagehide', function(event) {
        addRecord(1);
    });
});



