var templatessid=null;//模板ssid
var recordUserInfos;//询问人和被询问人数据



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
        layer.msg(data.message,{icon: 2});
    }
}

//导出全部题目
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

                    $("#recorddetail .font_blue_color").focus(function(){
                        td_lastindex=$(this).closest("tr").index();
                    });
                }




            }
        }
    }else{
        layer.msg(data.message,{icon: 2});
    }

}

var td_lastindex;//td的上一个光标位置  为0需要处理一下
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
   $("#recorddetail .font_blue_color").focus(function(){
       td_lastindex=$(this).closest("tr").index();
    });
}

//情绪分析
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

//录音按钮显示隐藏 type:1开始录音 2结束录音
var t;
function img_bool(obj,type){
        if (type==1){
            console.log("录音中__");
            $(obj).css("display","none");
            $(obj).siblings().css("display","block");

            //开始会议
            startMC();

            //实时会议数据
        /*    var recordtype=1;
            var username="未知";
            var translatext="未知";
            t = setInterval(function (args) {
                /!* $("#recordreals").html("");*!/
                if (recordtype==1){
                    recordrealclass="atalk";
                    username="检察官";
                    translatext="我是检察官，现在开始考察你";
                    recordtype=2;
                }else if (recordtype==2){
                    recordrealclass="btalk";
                    username="被询问人";
                    translatext="我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察我是被询问人，现在开始接受考察"
                    recordtype=1;
                }
                var recordrealshtml='<div class="'+recordrealclass+'" >\
                                                        <p>【'+username+'】 2019-4-14 02:24:55</p>\
                                                        <span ondblclick="copy_text(this)">'+translatext+'</span> \
                                                  </div >';

                $("#recordreals").append(recordrealshtml);
            },1000);*/
        }else{
            console.log("录音关闭__");
            $(obj).css("display","none");
            $(obj).siblings().css("display","block");
           // clearInterval(t);

            //结束会议
            overMC();
        }
}

//粘贴语音翻译文本
var copy_text_html="";
function copy_text(obj) {
    var text=$(obj).text();
    copy_text_html=text;

    $("#recorddetail .font_blue_color").each(function(){
            var lastindex=$(this).closest("tr").index();
            if (lastindex==td_lastindex) {
                $(this).append(copy_text_html);
                copy_text_html="";
            }
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
        $("#recorddetail .font_blue_color").each(function(){
                var lastindex=$(this).closest("tr").index();
                if (lastindex==td_lastindex) {
                    $(this).append(occurrencetime_format);
                }
        });
        btn(obj);
    }
}
function get_current_time(obj) {
    getTime();
    if (isNotEmpty(currenttime)){
        $("#recorddetail .font_blue_color").each(function(){
                var lastindex=$(this).closest("tr").index();
                if (lastindex==td_lastindex) {
                    $(this).append(currenttime);
                }
        });
        btn(obj);
    }
}
function get_yesterday_time(obj) {
    getTime();
    if (isNotEmpty(yesterdaytime)){
        $("#recorddetail .font_blue_color").each(function(){
            var lastindex=$(this).closest("tr").index();
            if (lastindex==td_lastindex) {
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
            var caseAndUserInfo=data.caseAndUserInfo;
            if (isNotEmpty(record)){
                var recordUserInfosdata=record.recordUserInfos;
                if (isNotEmpty(recordUserInfosdata)){
                    recordUserInfos=recordUserInfosdata;
                }
            }
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
function startMC() {
    if (isNotEmpty(recordUserInfos)){
        var tdList=[];
        var user1={
            username:recordUserInfos.username
            ,userssid:recordUserInfos.userssid
            ,grade:2 //1主麦，2副麦，有时需要一些特殊的处理(主麦只有一个)
            ,asrtype:"AVST"
        };
        var user2={
            username:recordUserInfos.adminname
            ,userssid:recordUserInfos.adminssid
            ,grade:1
            ,asrtype:"AVST"
        };
       /*   询问人二暂不参与
        var user3={
            username:recordUserInfos.otheradminname
            ,userssid:recordUserInfos.otheradminssid
            ,grade:2
        };*/
        tdList.push(user1);
        tdList.push(user2);
        // tdList.push(user3);

        var url=getActionURL(getactionid_manage().waitRecord_startRercord);
        var data={
            token:INIT_CLIENTKEY,
            param:{
                meetingtype: 2       //会议类型，1视频/2音频
                ,mcType:"AVST"       //会议采用版本,现阶段只有AVST
                ,modelbool:1         //是否需要会议模板，1需要/-1不需要
                ,mtmodelssid:'asgfjry521784h67' //会议模板ssid
                ,tdList:tdList
            }
        };
        ajaxSubmitByJson(url, data, callbackstartMC);
    }else {
        layer.msg("参数为空");
    }
}
var mtssid=null;
function callbackstartMC(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("startMC返回结果_"+data)
            mtssid=data;
            getMCAsrTxtBack();
        }
    }else{
        layer.msg(data.message);
    }
}

//结束会议
function overMC() {
    var url=getActionURL(getactionid_manage().waitRecord_overRercord);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            mtssid:mtssid
            ,mcType:"AVST"
        }
    };
    ajaxSubmitByJson(url, data, callbackoverMC);
}
function callbackoverMC(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log("overMC(返回结果_"+data)
        }
    }else{
        layer.msg(data.message);
    }
}

//获取会议返回
function getMCAsrTxtBack() {
    var url=getActionURL(getactionid_manage().waitRecord_getRercordAsrTxtBack);
    var data={
        token:INIT_CLIENTKEY,
        param:{
            mtssid:mtssid
            ,mcType:"AVST"
        }
    };
    ajaxSubmitByJson(url, data, callbackgetMCAsrTxtBack);
}
function callbackgetMCAsrTxtBack(data) {
    if(null!=data&&data.actioncode=='SUCCESS'){
        var data=data.data;
        if (isNotEmpty(data)){
            console.log(" getMCAsrTxtBack返回结果_"+data)
        }
    }else{
        layer.msg(data.message);
    }
}


//保存按钮
function addRecord() {
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
        layer.msg(data.message,{icon: 2});
    }
}

$(function () {
    $("#recorddetail .font_blue_color").focus(function(){
        td_lastindex=$(this).closest("tr").index();
    });

    $("#dl_dd dd").click(function () {
        var text=$(this).attr('lay-value');
        //文本
        $("#recorddetail .font_blue_color").each(function(){
                var lastindex=$(this).closest("tr").index();
                if (lastindex==td_lastindex) {
                    $(this).append(text);
                }
        });
        btn(this);
    })

    $("#recorddetail .font_blue_color").focus(function(){
        td_lastindex=$(this).closest("tr").index();
    });

});
